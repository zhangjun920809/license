package com.data.provider.controller;

import com.data.provider.core.LicenseCreator;
import com.data.provider.entity.LicenseInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

/*
 * @Author: tianyong
 * @Date: 2020/7/13 16:47
 * @Description: 生成license接口
 */
@RestController
@RequestMapping("/service/v1/license")
public class LicenseController {


    // 证书生成路径 (读取配置文件)
    @Value("${license.licensePath}")
    private String licensePath;


    /*
     * @Author: tianyong
     * @Date: 2020/7/14 14:29
     * @Description: 生成license证书
     */
    @CrossOrigin
    @RequestMapping("/generate/license")
    public String generateLicense(@RequestBody LicenseInfo licenseInfo) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取证书生成路径
        if(StringUtils.isBlank(licenseInfo.getLicensePath())){
            licenseInfo.setLicensePath(licensePath);
        }
        // 生成证书
        LicenseCreator licenseCreator = new LicenseCreator(licenseInfo);
        boolean flag = licenseCreator.generateLicense();
        // 返回结果
        if(flag){
            return MessageFormat.format("证书生成成功，证书有效期：{0} - {1}",format.format(licenseInfo.getNotBefore()),format.format(licenseInfo.getNotAfter()));
        }else{
            return "证书生成失败!";
        }
    }


}
