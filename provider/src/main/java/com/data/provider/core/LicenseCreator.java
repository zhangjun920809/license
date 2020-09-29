package com.data.provider.core;

import com.data.provider.entity.CustomKeyStore;
import com.data.provider.entity.LicenseInfo;
import de.schlichtherle.license.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.text.MessageFormat;
import java.util.prefs.Preferences;

/*
 * @Author: tianyong
 * @Date: 2020/7/14 15:43
 * @Description: 生成license
 */
public class LicenseCreator {


    /* 变量 */
    private static Logger log = LogManager.getLogger(LicenseCreator.class);
    // 证书的发行者和主体字段信息
    private final static X500Principal DEFAULT_HOLDER_AND_ISSUER = new X500Principal("CN=BigData, OU=HongYi, O=InDass, L=BJ, ST=BJ, C=CN");
    //构造函数赋值
    private LicenseInfo licenseInfo;
    public LicenseCreator(LicenseInfo licenseInfo) {
        this.licenseInfo = licenseInfo;
    }


    // 生成license证书
    public boolean generateLicense(){
        try {
            LicenseManager licenseManager = CustomLicenseManager.getInstance(initLicenseParam());
            LicenseContent licenseContent = initLicenseContent();
            licenseManager.store(licenseContent,new File(licenseInfo.getLicensePath()));
            return true;
        }catch (Exception e){
            log.error(MessageFormat.format("证书生成失败：{0}",licenseInfo),e);
            return false;
        }
    }


    // 初始化证书生成参数
    private LicenseParam initLicenseParam(){
        Preferences preferences = Preferences.userNodeForPackage(LicenseCreator.class);
        //设置对证书内容加密的秘钥
        CipherParam cipherParam = new DefaultCipherParam(licenseInfo.getStorePass());
        KeyStoreParam privateStoreParam = new CustomKeyStore(LicenseCreator.class
                ,licenseInfo.getPrivateKeysStorePath()
                ,licenseInfo.getPrivateAlias()
                ,licenseInfo.getStorePass()
                ,licenseInfo.getKeyPass());
        return new DefaultLicenseParam(licenseInfo.getSubject()
                ,preferences
                ,privateStoreParam
                ,cipherParam);
    }


    // 初始化证书内容信息对象
    private LicenseContent initLicenseContent(){
        LicenseContent licenseContent = new LicenseContent();
        licenseContent.setHolder(DEFAULT_HOLDER_AND_ISSUER);
        licenseContent.setIssuer(DEFAULT_HOLDER_AND_ISSUER);
        BeanUtils.copyProperties(licenseInfo, licenseContent);
        return licenseContent;
    }

}
