package edu.hufe.service.impl;

import edu.hufe.entity.DataSource;
import edu.hufe.entity.MusicInfo;
import edu.hufe.mapper.DataSourceMapper;
import edu.hufe.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service()
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Override
    public List<MusicInfo> searchMusic(String sourceId, String count, String page, String keyword) {
        // 通过sourceId获取类路径名
        DataSource dataSource = dataSourceMapper.findDataSourceById(sourceId);
        if(dataSource == null){
            return null;
        }
        // 获取类路径
        String classpath = dataSource.getClasspath();
        // 通过反射获取字节码
        try {
            Class clazz = Class.forName(classpath);
            // 获取方法
            Method method = clazz.getMethod("searchMusic", String.class,String.class,String.class);
            // 关闭访问检查
            method.setAccessible(true);
            return (List<MusicInfo>)method.invoke(clazz, count, page, keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MusicInfo getMusicInfoById(String sourceId, String id) {
        DataSource dataSource = dataSourceMapper.findDataSourceById(sourceId);
        if(dataSource == null){
            return null;
        }
        // 获取类路径
        String classpath = dataSource.getClasspath();
        // 通过反射获取字节码
        try {
            Class clazz = Class.forName(classpath);
            // 获取方法
            Method method = clazz.getMethod("getMusicInfoById", String.class);
            // 关闭访问检查
            method.setAccessible(true);
            return (MusicInfo)method.invoke(clazz, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getLyricById(String sourceId, String id) {
        DataSource dataSource = dataSourceMapper.findDataSourceById(sourceId);
        if(dataSource == null){
            return null;
        }
        // 获取类路径
        String classpath = dataSource.getClasspath();
        // 通过反射获取字节码
        try {
            Class clazz = Class.forName(classpath);
            // 获取方法
            Method method = clazz.getMethod("getLyricById", String.class);
            // 关闭访问检查
            method.setAccessible(true);
            return (String)method.invoke(clazz, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}