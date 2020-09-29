package com.data.provider.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/*
 * @Author: tianyong
 * @Date: 2020/7/14 14:50
 * @Description: license请求参数
 */
public class LicenseInfo implements Serializable {


    // 证书名称
    private String subject;
    // 证书生效日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date notBefore;
    // 证书失效日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date notAfter;
    // 证书用户类型
    private String consumerType;
    // 证书用户数量
    private int consumerAmount = 1;
    // 证书描述信息
    private String info;
    // 证书扩展信息 (补充的服务器硬件校验信息)
    private CustomVerify extra;

    // 密钥别称
    private String privateAlias;
    // 密钥密码（需要妥善保管，不能让使用者知道）
    private String keyPass;
    // 访问秘钥库的密码
    private String storePass;
    // 证书生成路径
    private String licensePath;
    // 密钥库存储路径
    private String privateKeysStorePath;


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(Date notBefore) {
        this.notBefore = notBefore;
    }

    public Date getNotAfter() {
        return notAfter;
    }

    public void setNotAfter(Date notAfter) {
        this.notAfter = notAfter;
    }

    public String getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(String consumerType) {
        this.consumerType = consumerType;
    }

    public int getConsumerAmount() {
        return consumerAmount;
    }

    public void setConsumerAmount(int consumerAmount) {
        this.consumerAmount = consumerAmount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public CustomVerify getExtra() {
        return extra;
    }

    public void setExtra(CustomVerify extra) {
        this.extra = extra;
    }

    public String getPrivateAlias() {
        return privateAlias;
    }

    public void setPrivateAlias(String privateAlias) {
        this.privateAlias = privateAlias;
    }

    public String getKeyPass() {
        return keyPass;
    }

    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }

    public String getStorePass() {
        return storePass;
    }

    public void setStorePass(String storePass) {
        this.storePass = storePass;
    }

    public String getLicensePath() {
        return licensePath;
    }

    public void setLicensePath(String licensePath) {
        this.licensePath = licensePath;
    }

    public String getPrivateKeysStorePath() {
        return privateKeysStorePath;
    }

    public void setPrivateKeysStorePath(String privateKeysStorePath) {
        this.privateKeysStorePath = privateKeysStorePath;
    }
}
