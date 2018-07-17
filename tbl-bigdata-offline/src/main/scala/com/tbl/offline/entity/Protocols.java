package com.tbl.offline.entity;

import java.util.List;

/**
 * author sunbin
 * date 2018/6/14 14:53
 * description
 */
public class Protocols {
    private int totalLen;
    private String productCommandVer;
    private String commandMode;
    private String packageNo;
    private int packageCount;
    private int curPackageNo;
    private String dataReturnWay;
    private String returnRequireFlag;
    private int singlePathLength;
    private String dataPathFlag;
    private int pathLen;
    private String pathData;
    private String command;
    private String excuteState;
    private int dataLen;
    private String data;
    private List<String> path;

    public int getTotalLen() {
        return totalLen;
    }

    public void setTotalLen(int totalLen) {
        this.totalLen = totalLen;
    }

    public String getProductCommandVer() {
        return productCommandVer;
    }

    public void setProductCommandVer(String productCommandVer) {
        this.productCommandVer = productCommandVer;
    }

    public String getCommandMode() {
        return commandMode;
    }

    public void setCommandMode(String commandMode) {
        this.commandMode = commandMode;
    }

    public String getPackageNo() {
        return packageNo;
    }

    public void setPackageNo(String packageNo) {
        this.packageNo = packageNo;
    }

    public int getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(int packageCount) {
        this.packageCount = packageCount;
    }

    public int getCurPackageNo() {
        return curPackageNo;
    }

    public void setCurPackageNo(int curPackageNo) {
        this.curPackageNo = curPackageNo;
    }

    public String getDataReturnWay() {
        return dataReturnWay;
    }

    public void setDataReturnWay(String dataReturnWay) {
        this.dataReturnWay = dataReturnWay;
    }

    public String getReturnRequireFlag() {
        return returnRequireFlag;
    }

    public void setReturnRequireFlag(String returnRequireFlag) {
        this.returnRequireFlag = returnRequireFlag;
    }

    public int getSinglePathLength() {
        return singlePathLength;
    }

    public void setSinglePathLength(int singlePathLength) {
        this.singlePathLength = singlePathLength;
    }

    public String getDataPathFlag() {
        return dataPathFlag;
    }

    public void setDataPathFlag(String dataPathFlag) {
        this.dataPathFlag = dataPathFlag;
    }

    public int getPathLen() {
        return pathLen;
    }

    public void setPathLen(int pathLen) {
        this.pathLen = pathLen;
    }

    public String getPathData() {
        return pathData;
    }

    public void setPathData(String pathData) {
        this.pathData = pathData;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getExcuteState() {
        return excuteState;
    }

    public void setExcuteState(String excuteState) {
        this.excuteState = excuteState;
    }

    public int getDataLen() {
        return dataLen;
    }

    public void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }
}

