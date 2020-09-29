package com.data.provider.controller;

import com.data.provider.entity.ServerInfo;
import com.data.provider.service.LinuxInfoAbstract;
import com.data.provider.service.ServerInfoAbstract;
import com.data.provider.service.WindowInfoAbstract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @Date: 2020/7/13 16:49
     * @Description: 获取当前服务器硬件信息
     */

    @CrossOrigin
    @RequestMapping("/server/info")
    public ServerInfo serverInfo(){
        // 获取操作系统类型
        String osName = System.getProperty("os.name").toLowerCase();
        // 根据当前操作系统获取相关系统参数
        ServerInfoAbstract serverInfoAbstract;
        serverInfoAbstract = osName.startsWith("windows") ? new WindowInfoAbstract() : new LinuxInfoAbstract();
        return serverInfoAbstract.getServerInfo();
    }
}
