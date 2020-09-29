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

/*
 * @Author: tianyong
 * @Date: 2020/7/21 15:13
 * @Description: license验证
 */
public class LicenseVerify {


    /* 成员变量 */
    private static Logger log = LoggerFactory.getLogger(LicenseVerify.class);


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
