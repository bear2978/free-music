package edu.hufe.api;

import edu.hufe.entity.MusicInfo;
import edu.hufe.utils.DataUtil;
import edu.hufe.utils.UserAgent;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KuGouMusic {

    /**
     * 根据关键字获取歌曲信息(网页端接口)
     * @param keyword 歌手名或歌曲名
     */
    public static List<MusicInfo> getMusicInfo(String keyword) throws IOException {
        List<MusicInfo> list = new ArrayList<>();
        String searchUrl = "http://songsearch.kugou.com/song_search_v2?callback=jQuery191034642999175022426_1489023388639&keyword="+
                keyword +"&page=1&pagesize=30&userid=-1&clientver=&platform=WebFilter&filter=2&iscorrection=1&privilege_filter=0&_=1489023388641%27";
        // 连接到url
        String body = connectToUrl(searchUrl);
        // 截取正确的json格式
        body = body.substring(body.indexOf("{"), body.lastIndexOf("}") + 1);
        return list;
    }

    /**
     * 根据关键字获取歌曲信息(客户端接口)
     * @param page 页码
     * @param num 查询记录条数
     * @param keyword 查询关键字
     * @return
     * @throws IOException
     */
    public static List<MusicInfo> searchMusic(String page, String num, String keyword) throws IOException {
        List<MusicInfo> list = new ArrayList<>();
        // 拼接请求数据url
        String url = "http://mobilecdn.kugou.com/api/v3/search/song";
        String result = Jsoup.connect(url)
                .data("pagesize", num)
                .data("page", page)
                .data("keyword", keyword)
                .userAgent(UserAgent.randomAgent())
                .timeout(10000).ignoreContentType(true).execute().body();
        // System.out.println(result);
        JSONObject rs = JSONObject.fromObject(result);
        // 获取响应状态
        String status = rs.getString("status");
        if("1".equals(status)){
            JSONArray songs = rs.getJSONObject("data").getJSONArray("info");
            // 遍历所有歌曲
            // for (int i = 0; i < songs.size(); i++) {
            for (int i = 0; i < 5; i++) {
                JSONObject item = songs.getJSONObject(i);
                String hash = item.getString("hash");
                String name = item.getString("songname_original");
                String artist = item.getString("singername");
                String albumID = item.getString("album_id");
                String album = item.getString("album_name");
                // 使用酷狗音乐的hash值及专辑id拼接id
                String id = hash + "-" + albumID;
                getMusicInfoById(id);
                // System.out.println(DataUtil.fillData(id,name,artist,album,"kugou",id,"","",id));
                list.add(DataUtil.fillData(id,name,artist,album,"kugou",id,"","",id));
            }
        }
        return list;
    }

    /**
     * 根据歌曲id获取歌曲信息
     * 主要响应歌曲的专辑图片和歌曲链接
     * @param id
     * @return
     * @throws IOException
     */
    public static MusicInfo getMusicInfoById(String id) throws IOException {
        MusicInfo musicInfo = new MusicInfo();
        // 获取响应的结果数据
        JSONObject rs = getMusicInfoJson(id);
        // System.out.println(rs);
        // String picUrl = rs.getString("img");
        String songUrl = "";
        try {
            songUrl = rs.getString("play_url");
            System.out.println(songUrl);
            if(songUrl == null || songUrl.equals("")){
                songUrl = rs.getString("play_backup_url");
            }
        }catch (Exception e){}
        // musicInfo.setPicUrl(picUrl);
        musicInfo.setMusicUrl(songUrl);
        // System.out.println(musicInfo);
        return musicInfo;
    }

    /**
     * 获取歌曲歌词
     * @param id
     * @return
     * @throws IOException
     */
    public static String getLyricById(String id) throws IOException {
        // 获取响应的结果数据
        JSONObject rs = getMusicInfoJson(id);
        // lyrics
        String lyric = rs.getString("lyrics");
        JSONObject json = new JSONObject();
        json.put("lyric", lyric);
        // System.out.println(json);
        return json.toString();
    }

    /**
     * 根据id值获取歌曲信息
     * @param id
     * @return
     * @throws IOException
     */
    private static JSONObject getMusicInfoJson(String id) throws IOException {
        // 分割id成hash值和专辑id
        String [] key = id.split("-");
        String hash = key[0];
        String albumId = "";
        if(key.length == 2){
            albumId = key[1];
        }
        String url = "https://wwwapi.kugou.com/yy/index.php?r=play/getdata&callback=jQuery1910634624435185092" +
                "_1593605532602&hash="+ hash +"&album_id="+ albumId +"&mid=f8433e44fd7f42656761dcfbafa7cf79";
        String result = connectToUrl(url);
        // 转变成正确格式的json
        result = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);
        JSONObject rs = JSONObject.fromObject(result);
        // System.out.println(rs);
        // 获取响应码
        String code = rs.getString("status");
        if("1".equals(code)){
            return rs.getJSONObject("data");
        }
        return rs;
    }

    /**
     * 连接到指定Url
     * @param url
     * @return
     * @throws IOException
     */
    private static String connectToUrl(String url) throws IOException {
        Connection.Response resp = Jsoup.connect(url)
                .header("Referer", "https://www.kugou.com/yy/html/search.html")
                .userAgent(UserAgent.randomAgent())
                .timeout(10000).ignoreContentType(true).execute();
        return resp.body();
    }

    public static void main(String[] args) {
        try {
            searchMusic("1","30","清空");
            // getMusicInfoById("0d66293f5c2d76ba2bd517d22277b46f","");
            // getMusicInfoById("004LMEOv19smN2");
            // getLyricById("004LMEOv19smN2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
