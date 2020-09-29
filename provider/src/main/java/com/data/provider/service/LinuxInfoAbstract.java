package com.data.provider.service;

import com.data.provider.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * @Author: tianyong
 * @Date: 2020/7/13 18:11
 * @Description: linux平台参数信息
 */
public class LinuxInfoAbstract extends ServerInfoAbstract {

    /* 变量 */
    private static Logger log = LogManager.getLogger(LinuxInfoAbstract.class);

    // 获取linux平台IP
    @Override
    protected List<String> Ip() {
        List<String> result = null;
        List<InetAddress> inetAddresses = Utils.getLocalAllInetAddress();
        if(inetAddresses != null && inetAddresses.size() > 0){
            result = inetAddresses.stream().map(InetAddress::getHostAddress).distinct().map(String::toLowerCase).collect(Collectors.toList());
        }
        return result;
    }


    // 获取linux平台MAC
    @Override
    protected List<String> Mac() {

        try {
            java.util.Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            StringBuilder sb = new StringBuilder();
            ArrayList<String> tmpMacList=new ArrayList<>();
            while(en.hasMoreElements()){
                NetworkInterface iface = en.nextElement();
                List<InterfaceAddress> addrs = iface.getInterfaceAddresses();
                for(InterfaceAddress addr : addrs) {
                    InetAddress ip = addr.getAddress();
                    NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                    if(network==null){continue;}
                    byte[] mac = network.getHardwareAddress();
                    if(mac==null){continue;}
                    sb.delete( 0, sb.length() );
                    for (int i = 0; i < mac.length; i++) {sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));}
                    tmpMacList.add(sb.toString());
                }        }
            if(tmpMacList.size()<=0){return tmpMacList;}
            /***去重，别忘了同一个网卡的ipv4,ipv6得到的mac都是一样的，肯定有重复，下面这段代码是。。流式处理***/
            List<String> unique = tmpMacList.stream().distinct().collect(Collectors.toList());
            return unique;
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
        /*List<String> result = null;
        // 1. 获取所有网络接口
        List<InetAddress> inetAddresses = Utils.getLocalAllInetAddress();
        if(inetAddresses != null && inetAddresses.size() > 0){
            // 2. 获取所有网络接口的Mac地址
            result = inetAddresses.stream().map(n->Utils.getMacByInetAddress(n)).distinct().collect(Collectors.toList());
        }
        return result;*/
    }


    // 使用dmidecode命令获取linux平台CPU序列号
    @Override
    protected String Cpu() {
        String serialNumber = "";
        BufferedReader reader = null;
        String[] shell = {"/bin/bash","-c","dmidecode -t processor | grep 'ID' | awk -F ':' '{print $2}' | head -n 1"};
        try {
            Process process = Runtime.getRuntime().exec(shell);
            process.getOutputStream().close();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine().trim();
            if(StringUtils.isNotBlank(line)){
                serialNumber = line;
            }
            reader.close();
        } catch (Exception e) {
            log.error("获取linux平台CPU序列号失败!",e);
        }
        return serialNumber;
    }


    // 使用dmidecode命令获取linux平台MAINBOARD序列号
    @Override
    protected String MainBoard() {
        String serialNumber = "";
        Process process = null;
        String[] shell = {"/bin/bash","-c","dmidecode | grep 'Serial Number' | awk -F ':' '{print $2}' | head -n 1"};
        try {
            process = Runtime.getRuntime().exec(shell);
            process.getOutputStream().close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine().trim();
            if(StringUtils.isNotBlank(line)){
                serialNumber = line;
            }
            reader.close();
        } catch (IOException e) {
            log.error("获取linux平台MAINBOARD序列号!",e);
        }
        return serialNumber;
    }
}
