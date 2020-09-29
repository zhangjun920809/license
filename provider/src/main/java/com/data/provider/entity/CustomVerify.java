package com.data.provider.entity;

import java.io.Serializable;
import java.util.List;

/*
 * @Author: tianyong
 * @Date: 2020/7/14 15:07
 * @Description: 补充验证信息
 */
public class CustomVerify implements Serializable {
    // 允许的IP
    private List<String> ips;
    // 允许的MAC
    private List<String> macs;
    // 允许的CPU
    private String cpus;
    // 允许的主板
    private String boards;
    // 是否认证IP
    private boolean ipCheck;
    // 是否认证MAC
    private boolean macCheck;
    // 是否认证CPU
    private boolean cpuCheck;
    // 是否认证主板
    private boolean boardCheck;

    public List<String> getIps() {
        return ips;
    }

    public void setIps(List<String> ips) {
        this.ips = ips;
    }

    public List<String> getMacs() {
        return macs;
    }

    public void setMacs(List<String> macs) {
        this.macs = macs;
    }

    public String getCpus() {
        return cpus;
    }

    public void setCpus(String cpus) {
        this.cpus = cpus;
    }

    public String getBoards() {
        return boards;
    }

    public void setBoards(String boards) {
        this.boards = boards;
    }

    public boolean isIpCheck() {
        return ipCheck;
    }

    public void setIpCheck(boolean ipCheck) {
        this.ipCheck = ipCheck;
    }

    public boolean isMacCheck() {
        return macCheck;
    }

    public void setMacCheck(boolean macCheck) {
        this.macCheck = macCheck;
    }

    public boolean isCpuCheck() {
        return cpuCheck;
    }

    public void setCpuCheck(boolean cpuCheck) {
        this.cpuCheck = cpuCheck;
    }

    public boolean isBoardCheck() {
        return boardCheck;
    }

    public void setBoardCheck(boolean boardCheck) {
        this.boardCheck = boardCheck;
    }


}
