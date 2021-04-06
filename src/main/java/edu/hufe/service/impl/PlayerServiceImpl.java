package edu.hufe.service.impl;

import edu.hufe.api.KuGouMusic;
import edu.hufe.api.KuWoMusic;
import edu.hufe.api.QQMusic;
import edu.hufe.api.WangYiMusic;
import edu.hufe.entity.MusicInfo;
import edu.hufe.service.PlayerService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service()
public class PlayerServiceImpl implements PlayerService  {

    @Override
    public List<MusicInfo> searchMusic(String sourceId, String count, String page, String keyword) {
        List<MusicInfo> list = new ArrayList<>();
        // 根据sourceId进行分流
        try {
            if("1".equals(sourceId)){
                list = WangYiMusic.searchMusic(count, page, keyword);
            }else if("2".equals(sourceId)){
                list = QQMusic.searchMusic(count, page, keyword);
            }else if("3".equals(sourceId)){
                list = KuWoMusic.searchMusic(count, page, keyword);
            }else if("4".equals(sourceId)){
                list = KuGouMusic.searchMusic(count, page, keyword);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public MusicInfo getMusicInfoById(String sourceId, String id) {
        MusicInfo musicInfo = null;
        try {
            if("1".equals(sourceId)){
                musicInfo = WangYiMusic.getMusicInfoById(id);
            }else if("2".equals(sourceId)){
                musicInfo = QQMusic.getMusicInfoById(id);
            }else if("3".equals(sourceId)){
                musicInfo = KuWoMusic.getMusicInfoById(id);
            }else if("4".equals(sourceId)){
                musicInfo = KuGouMusic.getMusicInfoById(id);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return musicInfo;
    }

    @Override
    public String getLyricById(String sourceId, String id) {
        String lyric = null;
        try {
            if("1".equals(sourceId)){
                lyric = WangYiMusic.getLyricById(id);
            }else if("2".equals(sourceId)){
                lyric = QQMusic.getLyricById(id);
            }else if("3".equals(sourceId)){
                lyric = KuWoMusic.getLyricById(id);
            }else if("4".equals(sourceId)){
                lyric = KuGouMusic.getLyricById(id);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return lyric;
    }

    @Override
    public String getUserListById(String uid) {
        String result = null;
        try {
            result = WangYiMusic.getUserListById(uid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}