package com.data.provider.service;

import com.data.provider.entity.ServerInfo;

import java.net.SocketException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @Author: tianyong
 * @Date: 2020/7/13 17:02
 * @Description: 服务器信息抽象父类 (模板方法设计模式)
 */
public abstract class ServerInfoAbstract {

    /* 变量 */
    private static Logger log = LoggerFactory.getLogger(ServerInfoAbstract.class);
    // IP
    protected abstract List<String> Ip();
    // MAC
    protected abstract List<String> Mac() throws SocketException;
    // CPU
    protected abstract String Cpu();
    // 主板
    protected abstract String MainBoard();


    // 获取服务器硬件信息
    public ServerInfo getServerInfo(){
        ServerInfo licenseParam = new ServerInfo();
        try {
            licenseParam.setIps(this.Ip());
            licenseParam.setMacs(this.Mac());
            licenseParam.setCpus(this.Cpu());
            licenseParam.setBoards(this.MainBoard());
        }catch (Exception e){
            log.error("获取服务器硬件信息失败!",e);
        }
        return licenseParam;
    }

}
