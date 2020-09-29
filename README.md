# licenses

#### 项目介绍
项目中使用 `TrueLicense `生成和验证`License证书`（服务器许可）的示例代码

#### 技术依赖：
* `Spring Boot`：项目基础架构
* `TrueLicense `：基于`Java`实现的生成和验证服务器许可的简单框架

#### 环境依赖：
* `JDK8+`

#### 项目整体思路：
1. 生成公钥，私钥 <br/>
    1.0. 注意：<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.0.1 进入java/bin目录执行keytool目录<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.0.2 storepass/keypass密码复杂度至少要满足：数字+字母<br/>
    1.0. 参数概要<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# alias: 别名<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# validity: 3650表示10年有效<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# keystore: 指定私钥库文件的名称(生成在当前目录)<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# storepass：指定私钥库的密码(获取keystore信息所需的密码)<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;# keypass：指定别名条目的密码(私钥的密码)<br/>
    1.1. 首先要用KeyTool工具来生成私匙库： <br/>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;keytool -genkey -alias privatekey -keystore privateKeys.store -keysize 1024 -validity 2920 -storepass "hongyi2020" -keypass 
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"zhimakaimen2020" -dname "CN=BigData, OU=HongYi, O=InDass, L=BJ, ST=BJ, C=CN" <br/>
          
    1.2. 然后把私匙库内的公匙导出到一个文件当中： <br/>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;keytool -export -alias privatekey -file license.cer -keystore privateKeys.store -storepass "hongyi2020" <br/>
          
    1.3. 然后再把这个证书文件导入到公匙库： <br/>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;keytool -import -alias publiccert -file license.cer -keystore publicCerts.store -storepass "hongyi2020" <br/>
          
    1.4. 最后生成文件privateKeys.store、publicCerts.store拷贝出来备用 <br/>
    
#### 生成证书请求参数示例如下所示：
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请求时需要在Header中添加一个 **Content-Type = application/json;charset=UTF-8**
```json
{
	"subject": "license_demo",
	"privateAlias": "privateKey",
	"keyPass": "private_password1234",
	"storePass": "public_password1234",
	"licensePath": "C:/Users/zifangsky/Desktop/license_demo/license.lic",
	"privateKeysStorePath": "C:/Users/zifangsky/Desktop/license_demo/privateKeys.keystore",
	"issuedTime": "2018-07-10 00:00:01",
	"expiryTime": "2019-12-31 23:59:59",
	"consumerType": "User",
	"consumerAmount": 1,
	"description": "这是证书描述信息",
	"licenseCheckModel": {
		"ipAddress": ["192.168.245.1", "10.0.5.22"],
		"macAddress": ["00-50-56-C0-00-01", "50-7B-9D-F9-18-41"],
		"cpuSerial": "BFEBFBFF000406E3",
		"mainBoardSerial": "L1HF65E00X9"
	}
}
```

#### 注意事项
customKeyStore：可以让项目独立，文件独立，细品!!!