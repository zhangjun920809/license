package com.data.provider.core;

import com.data.provider.entity.CustomVerify;
import com.data.provider.entity.ServerInfo;
import com.data.provider.service.LinuxInfoAbstract;
import com.data.provider.service.WindowInfoAbstract;
import com.data.provider.utils.Utils;
import de.schlichtherle.license.*;
import de.schlichtherle.xml.GenericCertificate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import static de.schlichtherle.xml.XMLConstants.DEFAULT_BUFSIZE;
import static de.schlichtherle.xml.XMLConstants.XML_CHARSET;

/*
 * @Author: tianyong
 * @Date: 2020/7/13 16:33
 * @Description: 自定义lecense管理器 (单例设计模式)
 */
public class CustomLicenseManager extends LicenseManager {


    // 定义变量
    private static volatile CustomLicenseManager instance;
    private static Logger log = LoggerFactory.getLogger(CustomLicenseManager.class);

    // 有参数构造
    public CustomLicenseManager(LicenseParam param) {super(param);}


    // 单例设计模式生成对象
    public static CustomLicenseManager getInstance(LicenseParam param){
        if(instance == null){
            synchronized (CustomLicenseManager.class){
                if(instance == null){
                    instance = new CustomLicenseManager(param);
                }
            }
        }
        return instance;
    }


    // 重写LicenseManager的create方法
    @Override
    protected synchronized byte[] create(LicenseContent content,LicenseNotary notary) throws Exception {
        this.initialize(content);
        // 验证自定义变量
        Utils.validateCreate(content);
        final GenericCertificate certificate = notary.sign(content);
        return getPrivacyGuard().cert2key(certificate);
    }


    // 重写LicenseManager的verify方法
    @Override
    protected synchronized LicenseContent verify(final LicenseNotary notary) throws Exception {
        final byte[] key = getLicenseKey();
        if (null == key){
            throw new NoLicenseInstalledException("抱歉！无法找到与之匹配的数字校验证书 : " + getLicenseParam().getSubject());
        }
        GenericCertificate certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent)this.load(certificate.getEncoded());
        // 校验额外license参数
        this.validate(content);
        setCertificate(certificate);
        return content;
    }


    // 重写XMLDecoder解析XML
    protected Object load(String encoded){
        BufferedInputStream inputStream = null;
        XMLDecoder decoder = null;
        try {
            inputStream = new BufferedInputStream(new ByteArrayInputStream(encoded.getBytes(XML_CHARSET)));
            decoder = new XMLDecoder(new BufferedInputStream(inputStream, DEFAULT_BUFSIZE),null,null);
            return decoder.readObject();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            try {
                if(decoder != null){
                    decoder.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (Exception e) {
                log.error("XMLDecoder解析XML失败",e);
            }
        }
        return null;
    }


    // 重写validate方法，增加ip地址、mac地址、cpu序列号等其他信息的校验
    @Override
    protected synchronized void validate(final LicenseContent content) throws LicenseContentException {
        super.validate(content);
        CustomVerify expectedCheck = (CustomVerify) content.getExtra();
        String os = System.getProperty("os.name").toLowerCase();
        ServerInfo serverInfo = os.startsWith("windows") ? new WindowInfoAbstract().getServerInfo() : new LinuxInfoAbstract().getServerInfo();
        if(expectedCheck != null && serverInfo != null){
            if(expectedCheck.isIpCheck() && !checkIpAddress(expectedCheck.getIps(),serverInfo.getIps())){
                log.error("证书无效，当前服务器的IP没在授权范围内");
                throw new LicenseContentException("证书无效，当前服务器的IP没在授权范围内");
            }
            if(expectedCheck.isIpCheck() && !checkIpAddress(expectedCheck.getMacs(),serverInfo.getMacs())){
                log.error("证书无效，当前服务器的Mac地址没在授权范围内");
                throw new LicenseContentException("证书无效，当前服务器的Mac地址没在授权范围内");
            }
            if(expectedCheck.isIpCheck() && !checkSerial(expectedCheck.getBoards(),serverInfo.getBoards())){
                log.error("证书无效，当前服务器的主板序列号没在授权范围内");
                throw new LicenseContentException("证书无效，当前服务器的主板序列号没在授权范围内");
            }
            if(expectedCheck.isIpCheck() && !checkSerial(expectedCheck.getCpus(),serverInfo.getCpus())){
                log.error("证书无效，当前服务器的CPU序列号没在授权范围内");
                throw new LicenseContentException("证书无效，当前服务器的CPU序列号没在授权范围内");
            }
        }else{
            log.error("不能获取服务器硬件信息");
            throw new LicenseContentException("不能获取服务器硬件信息");
        }
    }


    // 校验当前服务器的IP/Mac地址是否在可被允许的IP范围内
    private boolean checkIpAddress(List<String> expectedList, List<String> serverList){
        if(expectedList != null && expectedList.size() > 0){
            if(serverList != null && serverList.size() > 0){
                for(String expected : expectedList){
                    if(serverList.contains(expected.trim())){
                        return true;
                    }
                }
            }
            return false;
        }else {
            return true;
        }
    }

    // 校验当前服务器硬件（主板、CPU等）序列号是否在可允许范围内
    private boolean checkSerial(String expectedSerial,String serverSerial){
        if(StringUtils.isNotBlank(expectedSerial)){
            if(StringUtils.isNotBlank(serverSerial)){
                return expectedSerial.equals(serverSerial);
            }
            return false;
        }else{
            return true;
        }
    }

}
