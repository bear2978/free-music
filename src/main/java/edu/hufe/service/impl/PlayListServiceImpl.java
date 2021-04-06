package edu.hufe.service.impl;

import edu.hufe.api.KuWoMusic;
import edu.hufe.api.WangYiMusic;
import edu.hufe.entity.MusicInfo;
import edu.hufe.entity.PlayList;
import edu.hufe.mapper.PlayListMapper;
import edu.hufe.service.PlayListService;
import edu.hufe.utils.Const;
import edu.hufe.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service()
@Transactional
public class PlayListServiceImpl implements PlayListService {

    @Autowired
    private PlayListMapper playListMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<PlayList> queryAllPlayList() {
        // 1.尝试从redis获取数据
        List<PlayList> list = redisUtil.getList("playlist", PlayList.class);
        // 2.redis没有数据
        if (list == null) {
            System.out.println("从数据库查询");
            // 3.redis没有,从数据库查询
            list = playListMapper.findAllPlayList();
            // 4.将结果存储到redis
            redisUtil.set("playlist", list);
        }
        return list;
    }

    @Override
    public List<MusicInfo> queryDetailList(String sourceId, String id) {
        /**
         * 如何存储每日榜单？   歌单id + 日期
         */
        // 获取当前日期
        String date = new SimpleDateFormat(Const.DATE_FORMAT).format(System.currentTimeMillis());
        String key = "musicList" + date + "-" + id;
        // 1.尝试从redis获取数据
        List<MusicInfo> list = redisUtil.getList(key, MusicInfo.class);
        if(list == null){
            // 根据sourceId进行分流
            try {
                if ("1".equals(sourceId)) {
                    list = WangYiMusic.getPlayListById(id);
                } else if ("2".equals(sourceId)) {
                    System.out.println("QQ源");
                } else if ("3".equals(sourceId)) {
                    list = KuWoMusic.getPlayListById(id);
                } else if ("4".equals(sourceId)) {
                    System.out.println("酷狗源");
                }
                // 将爬虫数据结果存储到redis, 默认存储一天
                redisUtil.set(key, list, Const.REDIS_DEFAULT_EXPIRE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
