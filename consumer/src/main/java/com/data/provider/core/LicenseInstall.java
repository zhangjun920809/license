package com.data.provider.core;


import com.data.provider.entity.SubjectVerify;
import com.data.provider.utils.Utils;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

/*
 * @Author: tianyong
 * @Date: 2020/7/15 17:19
 * @Description: license安装
 */
@Component
@Order(value = 1)
public class LicenseInstall {

    /* 成员变量 */
    private static Logger log = LoggerFactory.getLogger(LicenseInstall.class);

    // 证书subject
    /*@Value("${license.subject}")
    private String subject;

    // 公钥别称
    @Value("${license.publicAlias}")
    private String publicAlias ;

    // 访问公钥库的密码
    @Value("${license.storePass}")
    private String storePass ;

    // 证书生成路径
    @Value("${license.licensePath}")
    private String licensePath ;

    // 密钥库存储路径
    @Value("${license.publicKeysStorePath}")
    private String publicKeysStorePath ;*/

    private String publicKeysStorePath ;
    private String licensePath ;
    private String storePass = "hongyi2020";
    private String publicAlias = "publiccert";
    private String subject = "InDass";

    public LicenseInstall() {
        String  pro = System.getProperty("carbon.home");
        if (pro != null ) {
            if (pro.endsWith("Stream")) {
                int i = pro.lastIndexOf("/");
                String substring = pro.substring(0, i);
                this.publicKeysStorePath = substring + "/license/publicCerts.store";
                this.licensePath = substring + "/license/license.lic";
            } else {
                this.publicKeysStorePath = pro + "/license/publicCerts.store";
                this.licensePath = pro + "/license/license.lic";
            }
        }
    }


    // 主要函数
    @PostConstruct
    public void init(){
        System.out.println("++++++++++++安装证书开始+++++++++++++");
        SubjectVerify subjectVerify = getSubjectVerify();
        //安装证书
        this.install(subjectVerify);
        System.out.println("++++++++++++安装证书结束+++++++++++++");
    }


    // 获取license参数
    public SubjectVerify getSubjectVerify(){
        SubjectVerify param = new SubjectVerify();
        param.setSubject(subject);
        param.setPublicAlias(publicAlias);
        param.setStorePass(storePass);
        param.setLicensePath(licensePath);
        param.setPublicKeysStorePath(publicKeysStorePath);
        return param;
    }


    // 安装证书操作
    public synchronized LicenseContent install(SubjectVerify param){
        LicenseContent result = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            LicenseManager licenseManager =CustomLicenseManager.getInstance(Utils.initLicenseParam(param));
            licenseManager.uninstall();
            result = licenseManager.install(new File(param.getLicensePath()));
            log.info(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}",format.format(result.getNotBefore()),format.format(result.getNotAfter())));
        }catch (Exception e){
            log.error("证书安装失败！",e);
            System.exit(0);
        }
        return result;
    }



}
