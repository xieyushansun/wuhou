package com.example.wuhou.Dao;

import com.example.wuhou.entity.DiskManage;
import com.example.wuhou.entity.DiskManageForDataBase;
import com.example.wuhou.util.PageUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DiskManageDao {
    @Autowired
    MongoTemplate mongoTemplate;
    public List<DiskManage> getAllDiskNameAndSpaceFromComputer() throws Exception {
//        List<DiskManage> diskManageList = mongoTemplate.findAll(DiskManage.class);
//        if (diskManageList.size() == 0){
//            refreshDiskDatabase();
//            return mongoTemplate.findAll(DiskManage.class);
//        }else {
//            return diskManageList;
//        }
        File[] roots = File.listRoots();
        if (roots.length == 0){
            throw new Exception("此电脑上没有磁盘！");
        }
        List<DiskManage> diskManageList = new ArrayList<>();
        for (int i = 0; i < roots.length; i++) {
            DiskManage diskManage = new DiskManage();
            String diskName = roots[i].getPath().split(":")[0];
            double totalSpace = (double) roots[i].getTotalSpace()/1024/1024/1024.;
            double restSpace = (double) roots[i].getFreeSpace()/1024/1024/1024.;
            double usedSpace = totalSpace - restSpace;
            //总磁盘空间大小
            diskManage.setTotalSpace(String.format("%.2f", totalSpace));
            //剩余磁盘空间大小
            diskManage.setRestSpace(String.format("%.2f", restSpace));
            //已用磁盘空间大小
            diskManage.setUsedSpace(String.format("%.2f", usedSpace));
            //磁盘名
            diskManage.setDiskName(diskName);
            //设置已使用百分比
            diskManage.setUsedPercent(String.format("%.0f", (restSpace/totalSpace)*100));
            //磁盘是否设置当前存储盘和是否已经选中过
            Query query = new Query();
            query.addCriteria(Criteria.where("diskName").is(diskName));
            DiskManageForDataBase diskManageForDataBase = mongoTemplate.findOne(query, DiskManageForDataBase.class);

            if (diskManageForDataBase != null){
                diskManage.setIsChoosed(diskManageForDataBase.getIsChoosed());
                diskManage.setIsUsed("1");
            }else {
                diskManage.setIsChoosed("0");
                diskManage.setIsUsed("0");
            }

            diskManageList.add(diskManage);
        }
        return diskManageList;
    }
    public DiskManage getDiskUsedSituation(String diskName) throws Exception {
        File[] roots = File.listRoots();
        DiskManage diskManage = new DiskManage();
        for (int i = 0; i < roots.length; i++) {
            if (diskName.equals(roots[i].getPath().split(":")[0])){
                double totalSpace = (double) roots[i].getTotalSpace()/1024/1024/1024.;
                double restSpace = (double) roots[i].getFreeSpace()/1024/1024/1024.;
                double usedSpace = totalSpace - restSpace;
                //总磁盘空间大小
                diskManage.setTotalSpace(String.format("%.2f", totalSpace));
                //剩余磁盘空间大小
                diskManage.setRestSpace(String.format("%.2f", restSpace));
                //已用磁盘空间大小
                diskManage.setUsedSpace(String.format("%.2f", usedSpace));
                //磁盘名
                diskManage.setDiskName(diskName);
                //设置已使用百分比
                diskManage.setUsedPercent(String.format("%.0f", (restSpace/totalSpace)*100));
                return diskManage;
            }
        }
        throw new Exception("没有该磁盘！");
    }
    // 更新数据库中磁盘内容
//    public void refreshDiskDatabase() throws Exception {
//        DiskManage currentDiskManage = getCurrentDiskNameAndSpace();
//        String currentDiskName = "";
//        if (currentDiskManage != null){
//            currentDiskName = currentDiskManage.getDiskName();
//        }
//        File[] roots = File.listRoots();
//        for (int i = 0; i < roots.length; i++) {
//            DiskManage diskManage = new DiskManage();
//            double totalSpace = (double) roots[i].getTotalSpace()/1024/1024/1024.;
//            double restSpace = (double) roots[i].getFreeSpace()/1024/1024/1024.;
//            double usedSpace = totalSpace - restSpace;
//
//            //总磁盘空间大小
//            diskManage.setTotalSpace(String.format("%.2f", totalSpace));
//            //剩余磁盘空间大小
//            diskManage.setRestSpace(String.format("%.2f", restSpace));
//            //已用磁盘空间大小
//            diskManage.setUsedSpace(String.format("%.2f", usedSpace));
//            //磁盘名
//            diskManage.setDiskName(roots[i].getPath().split(":")[0]);
//            //设置已使用百分比
//            diskManage.setUsedPercent(String.format("%.0f", (restSpace/totalSpace)*100));
//            //磁盘是否设置当前存储盘
//            if (currentDiskManage == null){
//                diskManage.setIsChoosed("0");
//            }else {
//                if (currentDiskName.equals(diskManage.getDiskName())){
//                    diskManage.setIsChoosed("1");
//                }else {
//                    diskManage.setIsChoosed("0");
//                }
//            }
//            mongoTemplate.insert(diskManage);
//        }
//    }
    public DiskManage getCurrentDiskNameAndSpace() throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("isChoosed").is("1"));
        DiskManageForDataBase diskManageForDataBase =  mongoTemplate.findOne(query, DiskManageForDataBase.class);
        if (diskManageForDataBase == null){
            return null;
        }
        DiskManage diskManage = new DiskManage();

        DiskManage d = getDiskUsedSituation(diskManageForDataBase.getDiskName());

        diskManage.setDiskName(diskManageForDataBase.getDiskName());
        diskManage.setIsUsed("1");
        diskManage.setIsChoosed("1");
        diskManage.setRestSpace(d.getRestSpace());
        diskManage.setTotalSpace(d.getTotalSpace());
        diskManage.setUsedSpace(d.getUsedSpace());
        diskManage.setUsedPercent(d.getUsedPercent());

        return diskManage;
    }
    //获取之前使用过的磁盘的信息
    public List<DiskManage> getUsedDiskNameAndSpace() throws Exception {
        //数据库已有磁盘
        List<DiskManageForDataBase> diskManageForDataBaseList = mongoTemplate.findAll(DiskManageForDataBase.class);
        //返回给前端的磁盘信息
        List<DiskManage> returnDiskManageList = new ArrayList<>();
        for (int i = 0; i < diskManageForDataBaseList.size(); i++){
            DiskManageForDataBase diskManageForDataBase =diskManageForDataBaseList.get(i);
            DiskManage diskManage = new DiskManage();

            diskManage.setDiskName(diskManageForDataBase.getDiskName());
            diskManage.setIsUsed("1");
            diskManage.setIsChoosed(diskManageForDataBase.getIsChoosed());

            DiskManage d = getDiskUsedSituation(diskManageForDataBase.getDiskName());
            diskManage.setUsedPercent(d.getUsedPercent());
            diskManage.setTotalSpace(d.getTotalSpace());
            diskManage.setUsedPercent(d.getUsedPercent());
            diskManage.setRestSpace(d.getRestSpace());

            returnDiskManageList.add(diskManage);
        }
        return returnDiskManageList;
    }
    public void setCurrentDisk(String diskName) throws Exception {
        if (!checkIsDiskNameIllegal(diskName)){
            throw new Exception("磁盘名不合法");
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("diskName").is(diskName));
        DiskManageForDataBase diskManageForDataBase = mongoTemplate.findOne(query, DiskManageForDataBase.class);
        Update update1 = new Update();
        update1.set("isChoosed", "0");
        //将所有是否选择置为0
        Query query1 = new Query();
        mongoTemplate.updateMulti(query1, update1, DiskManageForDataBase.class);
        //说明还没有磁盘使用过或选中过
        if (diskManageForDataBase == null){
            diskManageForDataBase = new DiskManageForDataBase();
            diskManageForDataBase.setDiskName(diskName);
            diskManageForDataBase.setIsChoosed("1");
            mongoTemplate.insert(diskManageForDataBase);
        }else {

            Update update2 = new Update();
            update2.set("isChoosed", 1);
            //将新选择的置为1
            mongoTemplate.updateFirst(query, update2, DiskManageForDataBase.class);
        }
    }
    public boolean checkIsDiskNameIllegal(String name){
        boolean b = false;
        File[] roots = File.listRoots();
        if (roots != null){
            for (File root : roots) {
                String diskName = root.getPath().split(":")[0];
                if (diskName.equals(name)){
                    b = true;
                }
            }
        }
        return b;
    }
}
