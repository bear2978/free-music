package edu.hufe.controller;

import edu.hufe.api.WangYiMusic;
import edu.hufe.entity.MusicInfo;
import edu.hufe.entity.PlayList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MusicController {

    /**
     * 歌曲搜索接口
     * @param count
     * @param source
     * @param page
     * @param keyword
     * @return
     */
    @RequestMapping("search")
    @ResponseBody
    public List<MusicInfo> searchMusic(int count, String source, int page, String keyword){
        System.out.println(count + "-->" + source + "-->" + page + "-->" +  keyword);
        // 根据数据源进行分流处理
        List<MusicInfo> list = null;
        try {
            switch (source){
                case "netease":
                    list = WangYiMusic.searchMusic(keyword);
                    break;
                case "":

                default:

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @RequestMapping("getSong")
    @ResponseBody
    public MusicInfo searchMusic(String id, String source){
        System.out.println(id + "-->" + source);
        // 根据数据源进行分流处理
        MusicInfo musicInfo = null;
        try {
            switch (source){
                case "netease":
                    musicInfo = WangYiMusic.getMusicInfoById(id);
                    break;
                case "":

                default:

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return musicInfo;
    }

    /**
     * 获取歌单列表信息
     */
    @RequestMapping("playlist")
    @ResponseBody
    public List<PlayList> getPlayList(){
        List<PlayList> playlist = new ArrayList<>();
        PlayList one = new PlayList();
        one.setId((long) 3778678);
        one.setName("云音乐热歌榜");
        // 播放列表封面图像
        one.setCover("http://p4.music.126.net/GhhuF6Ep5Tq9IEvLsyCN7w==/18708190348409091.jpg?param=200y200");
        PlayList two = new PlayList();
        two.setId((long) 3779629);
        two.setName("云音乐新歌榜");
        // 播放列表封面图像
        two.setCover("http://p4.music.126.net/2klOtThpDQ0CMhOy5AOzSg==/18878614648932971.jpg?param=200y200");
        playlist.add(one);
        playlist.add(two);
        return playlist;
    }

    /**
     * 根据歌单id获取歌单下的所有歌曲
     */
    @RequestMapping("detailPlaylist")
    @ResponseBody
    public List<MusicInfo> getPlayListById(String id){
        List<MusicInfo> list = null;
        try {
            list = WangYiMusic.getPlayListById(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @RequestMapping("loadLyric")
    @ResponseBody
    public String getLyric(String id, String source){
        System.out.println(id + "--->" + source);
        String lyric = null;
        try {
            switch (source){
                case "netease":
                    lyric = WangYiMusic.getLyricById(id);
                    break;
                case "":

                default:

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lyric;
    }
}
