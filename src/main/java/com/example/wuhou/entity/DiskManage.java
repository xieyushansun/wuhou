package com.example.wuhou.entity;

public class DiskManage {
    private String _id;
    //磁盘名称
    private String diskName;
    //总共空间
    private String totalSpace;
    //已用空间
    private String usedSpace;
    //剩余空间
    private String restSpace;
    //设置使用百分比(整数)
    private String usedPercent;
    //是否选中为当前存储盘
    private String isChoosed;
    //是否被选中过（是否存有数据）
    private String isUsed;

    public DiskManage(){
        _id = null;
        diskName = "";
        //总共空间
        totalSpace = "";
        //已用空间
        usedSpace = "";
        //剩余空间
        restSpace = "";
        //设置使用百分比(整数)
        usedPercent = "";
        //是否选中为当前存储盘
        isChoosed = "";
        //是否被选中过（是否存有数据）
        isUsed = "";
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(String totalSpace) {
        this.totalSpace = totalSpace;
    }

    public String getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(String usedSpace) {
        this.usedSpace = usedSpace;
    }

    public String getRestSpace() {
        return restSpace;
    }

    public void setRestSpace(String restSpace) {
        this.restSpace = restSpace;
    }

    public String getUsedPercent() {
        return usedPercent;
    }

    public void setUsedPercent(String usedPercent) {
        this.usedPercent = usedPercent;
    }

    public String getIsChoosed() {
        return isChoosed;
    }

    public void setIsChoosed(String isChoosed) {
        this.isChoosed = isChoosed;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }
}
