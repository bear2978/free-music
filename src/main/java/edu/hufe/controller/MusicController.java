package edu.hufe.controller;

import edu.hufe.entity.DataSource;
import edu.hufe.entity.MusicInfo;
import edu.hufe.entity.PlayList;
import edu.hufe.service.DataSourceService;
import edu.hufe.service.PlayListService;
import edu.hufe.service.PlayerService;
import edu.hufe.utils.UserAgent;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

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
    public List<DataSource> getDataSource() {
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
    public List<MusicInfo> searchMusic(String source, String count, String page, String keyword) {
        System.out.println("搜素音乐-->" + source + "-->" + count + "---->" + page + "---->" + keyword);
        return playerService.searchMusic(source, count, page, keyword);
    }


    @RequestMapping("getMusicInfo")
    @ResponseBody
    public MusicInfo getMusicInfo(String source, String id) {
        System.out.println("加载音乐-->" + source + "-->" + id);
        return playerService.getMusicInfoById(source, id);
    }

    /**
     * 获取歌单列表信息
     */
    @RequestMapping("playlist")
    @ResponseBody
    public List<PlayList> getPlayList() {
        return playListService.queryAllPlayList();
    }

    /**
     * 根据歌单id获取歌单下的所有歌曲
     */
    @RequestMapping("detailPlaylist")
    @ResponseBody
    public List<MusicInfo> getPlayListById(String source, String id) {
        System.out.println("加载播放列表-->" + id + "--->" + source);
        return playListService.queryDetailList(source, id);
    }

    @RequestMapping("loadLyric")
    @ResponseBody
    public String getLyric(String source, String id) {
        System.out.println("loadLyric--->" + id + "--->" + source);
        return playerService.getLyricById(source, id);
    }

    @RequestMapping("userSyn")
    @ResponseBody
    public String userSyn(String uid) {
        System.out.println("uid--->" + uid);
        return playerService.getUserListById(uid);
    }

    @RequestMapping("downLoadMusic")
    public void downLoadMusic(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1.获取表单信息
        Map<String, String[]> map = request.getParameterMap();
        MusicInfo musicInfo = new MusicInfo();
        // 2.将数据封装进对象
        BeanUtils.populate(musicInfo, map);
        // System.out.println(musicInfo);
        System.out.println("下载音乐-->" + musicInfo.getId() + "--->" + musicInfo.getSource());
        String musicUrl = musicInfo.getMusicUrl();
        // url为空
        if (musicUrl == null || "".equals(musicUrl) || "null".equals(musicUrl)) {
            // 进行搜索
            MusicInfo newInfo = playerService.getMusicInfoById(musicInfo.getSource(), musicInfo.getId());
            musicUrl = newInfo.getMusicUrl();
        }
        // 确保url存在,在进行下载
        if (musicUrl != null && !"".equals(musicUrl)) {
            // 获取文件名
            String fileName = musicInfo.getName() + "-" + musicInfo.getArtist() + ".mp3";
            HttpURLConnection con = (HttpURLConnection) new URL(musicUrl).openConnection();
            // con.setRequestProperty("referrer","https://pixivic.net/");
            con.setRequestProperty("User-Agent", UserAgent.randomAgent());
            BufferedInputStream bs = new BufferedInputStream(con.getInputStream());
            ServletContext servletContext = request.getServletContext();
            String mimeType = servletContext.getMimeType(fileName);
            response.setHeader("content-type", mimeType);
            // response.setContentType("application/force-download");// 设置强制下载不打开
            // response.setContentType("application/octet-stream");
            // 对文件名进行编码
            fileName = URLEncoder.encode(fileName,"UTF-8");
            response.setHeader("Content-disposition", "attachment;fileName=" + fileName);
            System.out.println(mimeType);
            System.out.println(fileName);
            // 使用输出流写到客户端
            response.setCharacterEncoding("UTF-8");
            ServletOutputStream sos = response.getOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = bs.read(buff)) != -1) {
                sos.write(buff, 0, len);
            }
            // 关闭流资源
            sos.flush();
            bs.close();
            sos.close();
        }
    }
}
