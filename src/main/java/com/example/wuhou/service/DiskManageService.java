package com.example.wuhou.service;

import com.example.wuhou.Dao.DiskManageDao;
import com.example.wuhou.entity.DiskManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiskManageService {
    @Autowired
    DiskManageDao diskManageDao;
    public List<DiskManage> getAllDiskNameAndSpaceFromComputer() throws Exception {
        return diskManageDao.getAllDiskNameAndSpaceFromComputer();
    }
//    public void refreshDiskDatabase() throws Exception {
//        diskManageDao.refreshDiskDatabase();
//    }
    public DiskManage getDiskUsedSituation(String diskName) throws Exception {
        return diskManageDao.getDiskUsedSituation(diskName);
    }
    public DiskManage getCurrentDiskNameAndSpace() throws Exception {
        return diskManageDao.getCurrentDiskNameAndSpace();
    }
    public List<DiskManage> getUsedDiskNameAndSpace()throws Exception{
        return diskManageDao.getUsedDiskNameAndSpace();
    }
    public void setCurrentDisk(String diskName) throws Exception {
        diskManageDao.setCurrentDisk(diskName);
    }

}
