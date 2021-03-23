package edu.hufe.controller;

import edu.hufe.entity.DataSource;
import edu.hufe.entity.MusicInfo;
import edu.hufe.entity.PlayList;
import edu.hufe.service.DataSourceService;
import edu.hufe.service.PlayListService;
import edu.hufe.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MusicController {

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private PlayListService playListService;

    @Autowired
    private PlayerService playerService;

    /**
     * 获取系统所有数据源
     * @return
     */
    @RequestMapping("getDataSource")
    @ResponseBody
    public List<DataSource> getDataSource(){
        return dataSourceService.findDataSourceList();
    }

    /**
     * 歌曲搜索接口
     * @param source
     * @param count
     * @param page
     * @param keyword
     * @return
     */
    @RequestMapping("search")
    @ResponseBody
    public List<MusicInfo> searchMusic(String source, String count, String page, String keyword){
        System.out.println("搜素音乐-->" + source + "-->"+ count + "---->" + page + "---->" + keyword);
        return playerService.searchMusic(source, count, page, keyword);
    }


    @RequestMapping("getMusicInfo")
    @ResponseBody
    public MusicInfo getMusicInfo(String source, String id){
        System.out.println("加载音乐-->" + source + "-->"+ id);
        return playerService.getMusicInfoById(source, id);
    }

    /**
     * 获取歌单列表信息
     */
    @RequestMapping("playlist")
    @ResponseBody
    public List<PlayList> getPlayList(){
        return playListService.queryAllPlayList();
    }

    /**
     * 根据歌单id获取歌单下的所有歌曲
     */
    @RequestMapping("detailPlaylist")
    @ResponseBody
    public List<MusicInfo> getPlayListById(String source, String id){
        System.out.println("加载播放列表-->" + id + "--->" + source);
        return playListService.queryDetailList(source, id);
    }

    @RequestMapping("loadLyric")
    @ResponseBody
    public String getLyric(String source, String id){
        System.out.println("loadLyric--->"+ id + "--->" + source);
        return playerService.getLyricById(source, id);
    }
}
