package edu.hufe.api;

import edu.hufe.entity.MusicInfo;
import edu.hufe.utils.DataUtil;
import edu.hufe.utils.RequestUtil;
import edu.hufe.utils.UserAgent;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WangYiMusic {

    /**
     * 根据关键字获取歌曲信息
     * @param keyword
     */
    public static List<MusicInfo> searchMusic(String count, String page, String keyword) throws IOException {
        List<MusicInfo> list = new ArrayList<>();
        String searchUrl = "http://music.163.com/api/cloudsearch/pc?s=" + keyword + "&type=1";
        String result = connectToUrl(searchUrl);
        JSONObject rs = JSONObject.fromObject(result);
        // 获取响应码
        String code = rs.getString("code");
        if ("200".equals(code)) {
            JSONArray songs = rs.getJSONObject("result").getJSONArray("songs");
            // 遍历所有歌曲
            for (int i = 0; i < songs.size(); i++) {
                JSONObject temp = songs.getJSONObject(i);
                list.add(parseJson(temp));
            }
        }
        return list;
    }

    /**
     * 根据歌单id获取该歌单所有歌曲
     * @param listId 歌单id
     */
    public static List<MusicInfo> getPlayListById(String listId) throws IOException {
        List<MusicInfo> list = new ArrayList<>();
        // https://music.163.com/#/playlist?id=3778679
        String url = "http://music.163.com/api/v3/playlist/detail";
        String result = Jsoup.connect(url)
                .data("id",listId)
                .method(Connection.Method.POST)
                .userAgent(UserAgent.randomAgent())
                .ignoreContentType(true).timeout(10000).execute().body();
        JSONObject rs = JSONObject.fromObject(result);
        // System.out.println(rs);
        String code = rs.getString("code");
        if("200".equals(code)){
            // 获取歌单下所有歌曲,并根据id获取歌曲详细信息
            JSONArray trackIds = rs.getJSONObject("playlist").getJSONArray("trackIds");
            for (int i = 0; i < trackIds.size(); i++) {
                JSONObject temp = trackIds.getJSONObject(i);
                String id = temp.getString("id");
                list.add(getMusicInfoById(id));
            }
        }
        return list;
    }

    /**
     * 根据歌曲id获取歌曲信息
     * @param id
     * @return
     * @throws IOException
     */
    public static MusicInfo getMusicInfoById(String id) throws IOException {
        MusicInfo musicInfo = null;
        String url = "http://music.163.com/api/v3/song/detail/";
        String result = Jsoup.connect(url)
                .data("c","[{\"id\":"+ id +",\"v\":0}]")
                .method(Connection.Method.POST)
                .userAgent(UserAgent.randomAgent())
                .ignoreContentType(true).timeout(10000).execute().body();
        JSONObject rs = JSONObject.fromObject(result);
        String code = rs.getString("code");
        if("200".equals(code)){
            JSONObject song = rs.getJSONArray("songs").getJSONObject(0);
            musicInfo = parseJson(song);
            // System.out.println(musicInfo);
        }
        return musicInfo;
    }

    /**
     * 获取歌曲歌词
     * @param id
     * @return
     * @throws IOException
     */
    public static String getLyricById(String id) throws IOException {
        String url = "http://music.163.com/api/song/lyric";
        // 创建请求数据map
        Map<String,String> data = new HashMap<>();
        data.put("id", id);
        data.put("os", "linux");
        data.put("lv", "-1");
        data.put("kv", "-1");
        data.put("tv", "-1");
        String result = RequestUtil.connectToUrl(url,null, data, Connection.Method.POST);
        // System.out.println(result);
        JSONObject rs = JSONObject.fromObject(result);
        String code = rs.getString("code");
        String lyric = "";
        if("200".equals(code)){
            lyric = rs.getString("lrc");
        }
        return lyric;
    }

    /**
     * 将响应的json数据解析成对象返回
     * @param jo
     */
    private static MusicInfo parseJson(JSONObject jo){
        // 获取相关歌曲信息
        String id = jo.getString("id");
        String name = jo.getString("name");
        String artist = jo.getJSONArray("ar").getJSONObject(0).getString("name");
        String album = jo.getJSONObject("al").getString("name");
        String picId = jo.getJSONObject("al").getString("pic");
        String picUrl = jo.getJSONObject("al").getString("picUrl");
        // 拼接歌曲外链
        String songUrl = "http://music.163.com/song/media/outer/url?id=" + id;
        return DataUtil.fillData(id,name,artist,album,"1",picId,picUrl,songUrl,id);
    }

    /**
     * 连接到指定Url
     * @param url
     * @return
     * @throws IOException
     */
    private static String connectToUrl(String url) throws IOException {
        Connection.Response resp = Jsoup.connect(url)
                .header("referer", "https://music.163.com/")
                .userAgent(UserAgent.randomAgent())
                .ignoreContentType(true).timeout(10000).execute();
        return resp.body();
    }

    public static void main(String[] args) {
        try {
            // getPlayListById("3779629");
            // getMusicInfoById("1815105886");
            // getLyricById("1815105886");
            getLyricById("1830718509");
            // searchMusic("饭思思");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}