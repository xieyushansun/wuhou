package com.example.wuhou.entity;

public class DiskManageForDataBase {
    String _id;
    //磁盘名称
    String diskName;
    //是否选中为当前存储盘
    String isChoosed;

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIsChoosed() {
        return isChoosed;
    }

    public void setIsChoosed(String isChoosed) {
        this.isChoosed = isChoosed;
    }

}
