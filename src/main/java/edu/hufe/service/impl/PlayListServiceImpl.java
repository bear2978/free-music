package edu.hufe.service.impl;

import edu.hufe.api.KuWoMusic;
import edu.hufe.api.WangYiMusic;
import edu.hufe.entity.MusicInfo;
import edu.hufe.entity.PlayList;
import edu.hufe.mapper.PlayListMapper;
import edu.hufe.service.PlayListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service()
@Transactional
public class PlayListServiceImpl implements PlayListService {

    @Autowired
    private PlayListMapper playListMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<PlayList> queryAllPlayList() {
        List<PlayList> list = null;
        // 1.尝试从redis获取数据
        Object result = redisTemplate.opsForValue().get("playlist");
        System.out.println(result);
        // 2.redis有数据
        if(result != null){
            list = (List<PlayList>) result;
        }else {
            System.out.println("从数据库查询");
            // 3.redis没有,从数据库查询
            list = playListMapper.findAllPlayList();
            // 4.将结果存储到redis
            // redisTemplate.set("playlist", list);
        }
        return list;
    }

    @Override
    // @Cacheable(cacheNames = "")
    public List<MusicInfo> queryDetailList(String sourceId, String id) {
        List<MusicInfo> list = new ArrayList<>();
        // 根据sourceId进行分流
        try {
            if("1".equals(sourceId)){
                list = WangYiMusic.getPlayListById(id);
            }else if("2".equals(sourceId)){
                System.out.println("QQ源");
            }else if("3".equals(sourceId)){
                list = KuWoMusic.getPlayListById(id);
            }else if("4".equals(sourceId)){
                System.out.println("酷狗源");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }
}
