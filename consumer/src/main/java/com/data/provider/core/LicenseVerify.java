package com.data.provider.core;

import com.data.provider.entity.SubjectVerify;
import com.data.provider.utils.Utils;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * @Author: tianyong
 * @Date: 2020/7/21 15:13
 * @Description: license验证
 */
public class LicenseVerify {


    /* 成员变量 */
    private static Logger log = LoggerFactory.getLogger(LicenseVerify.class);

    private String publicKeysStorePath ;
    private String licensePath ;
    private String storePass = "hongyi2020";
    private String publicAlias = "publiccert";
    private String subject = "InDass";

    //获取配置文件路径
    public LicenseVerify () {
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


    public void getSubjectVerify(){
        SubjectVerify sv = new SubjectVerify();
        sv.setSubject(subject);
        sv.setPublicAlias(publicAlias);
        sv.setStorePass(storePass);
        sv.setLicensePath(licensePath);
        sv.setPublicKeysStorePath(publicKeysStorePath);

        //校验证书
        ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(2);
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(new Date());
                try{
                    boolean flag = verify(sv);
                    if(!flag) {
                        System.exit(0);
                    }
                }catch (Exception e){
                    log.error("证书巡检失败！",e);
                }finally {
                    System.out.println("check: " + date);
                }
            }
        }, 0, 1, TimeUnit.HOURS);
    }

    // 校验License证书
    public boolean verify(SubjectVerify param){
        LicenseManager licenseManager = new CustomLicenseManager(Utils.initLicenseParam(param));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            LicenseContent licenseContent = licenseManager.verify();
            log.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}",format.format(licenseContent.getNotBefore()),format.format(licenseContent.getNotAfter())));
            return true;
        }catch (Exception e){
            log.error("证书校验失败！",e);
            return false;
        }
    }

}
