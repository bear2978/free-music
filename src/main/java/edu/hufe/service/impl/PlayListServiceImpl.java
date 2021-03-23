package edu.hufe.service.impl;

import edu.hufe.entity.DataSource;
import edu.hufe.entity.MusicInfo;
import edu.hufe.entity.PlayList;
import edu.hufe.mapper.DataSourceMapper;
import edu.hufe.mapper.PlayListMapper;
import edu.hufe.service.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;

@Service()
@Transactional
public class PlayListServiceImpl implements PlayListService {

    @Autowired
    private PlayListMapper playListMapper;

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    // @Cacheable(cacheNames = "playlist")
    public List<PlayList> queryAllPlayList() {
        List<PlayList> lists = null;
        // 1.尝试从redis获取数据
        Object result = null;
        try {
            result = redisTemplate.opsForValue().get("playlist");
            System.out.println(result);
            if(result != null){
                lists = (List<PlayList>) result;
                return lists;
            }
        }catch (Exception e){
            // e.printStackTrace();
            System.err.println("未连接redis");
        }
        lists = playListMapper.findAllPlayList();
        // 将结果存入缓存
        // redisTemplate.opsForValue().set("playlist",lists);
        return lists;
    }

    @Override
    // @Cacheable(cacheNames = "")
    public List<MusicInfo> queryDetailList(String sourceId, String id) {
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
            Method method = clazz.getMethod("getPlayListById", String.class);
            // 关闭访问检查
            method.setAccessible(true);
            return (List<MusicInfo>)method.invoke(clazz, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
