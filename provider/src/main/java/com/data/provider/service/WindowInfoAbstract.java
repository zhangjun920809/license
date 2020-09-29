package com.data.provider.service;

import com.data.provider.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/*
 * @Author: tianyong
 * @Date: 2020/7/13 18:12
 * @Description: window平台参数信息
 */
public class WindowInfoAbstract extends ServerInfoAbstract{


    /* 变量 */
    private static Logger log = LoggerFactory.getLogger(WindowInfoAbstract.class);


    // 获取window平台IP
    @Override
    protected List<String> Ip() {
        List<String> result = null;
        List<InetAddress> inetAddresses = Utils.getLocalAllInetAddress();
        if(inetAddresses != null && inetAddresses.size() > 0){
            result = inetAddresses.stream().map(InetAddress::getHostAddress).distinct().map(String::toLowerCase).collect(Collectors.toList());
        }
        return result;
    }


    // 获取window平台MAC
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
        /*List<String> result = null;
        //1. 获取所有网络接口
        List<InetAddress> inetAddresses = Utils.getLocalAllInetAddress();
        if(inetAddresses != null && inetAddresses.size() > 0){
            //2. 获取所有网络接口的Mac地址
            result = inetAddresses.stream().map(n->Utils.getMacByInetAddress(n)).distinct().collect(Collectors.toList());
        }
        return result;*/
        return null;
    }


    // 使用WMIC获取window平台CPU序列号
    @Override
    protected String Cpu() {
        String serialNumber = "";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("wmic cpu get processorid");
            process.getOutputStream().close();
            Scanner scanner = new Scanner(process.getInputStream());
            if(scanner.hasNext()){
                scanner.next();
            }
            if(scanner.hasNext()){
                serialNumber = scanner.next().trim();
            }
            scanner.close();
        } catch (IOException e) {
            log.error("获取window平台CPU序列号失败!",e);
        }
        return serialNumber;
    }


    // 使用WMIC获取window平台MAINBOARD序列号
    @Override
    protected String MainBoard() {
        String serialNumber = "";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("wmic baseboard get serialnumber");
            process.getOutputStream().close();
            Scanner scanner = new Scanner(process.getInputStream());
            if(scanner.hasNext()){
                scanner.next();
            }
            if(scanner.hasNext()){
                serialNumber = scanner.next().trim();
            }
            scanner.close();
        } catch (IOException e) {
            log.error("获取window平台MAINBOARD序列号失败!",e);
        }
        return serialNumber;
    }
}
