package edu.hufe.api;

import edu.hufe.entity.MusicInfo;
import edu.hufe.utils.UserAgent;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WangYiMusic {

    /**
     * 根据关键字获取歌曲信息
     * @param keyword
     */
    public static List<MusicInfo> searchMusic(String keyword) throws IOException {
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
        String code = rs.getString("code");
        if("200".equals(code)){
            // 获取歌单下所有歌曲
//            JSONArray tracks = rs.getJSONObject("playlist").getJSONArray("tracks");
//            // 前三首歌曲有详细信息
//            for (int i = 0; i < tracks.size(); i++) {
//                JSONObject temp = tracks.getJSONObject(i);
//                list.add(parseJson(temp));
//                // System.out.println(parseJson(temp));
//            }
            // 根据id获取其余歌曲信息
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
        }
        return musicInfo;
    }

    public static String getLyricById(String id) throws IOException {
        String url = "http://music.163.com/api/song/lyric";
        String result = Jsoup.connect(url)
                .data("id",id)
                .data("os","linux")
                .data("lv","-1")
                .data("kv","-1")
                .data("tv","-1")
                .method(Connection.Method.POST)
                .userAgent(UserAgent.randomAgent())
                .ignoreContentType(true).timeout(10000).execute().body();
        String lyric = JSONObject.fromObject(result).getJSONObject("lrc").toString();
        System.out.println(lyric);
        return lyric;
    }


    /**
     * 将响应的json数据解析成对象返回
     * @param jo
     */
    private static MusicInfo parseJson(JSONObject jo){
        // 获取相关歌曲信息
        Long id = jo.getLong("id");
        String songName = jo.getString("name");
        String singer = jo.getJSONArray("ar").getJSONObject(0).getString("name");
        String album = jo.getJSONObject("al").getString("name");
        // 拼接歌曲外链
        String picId = jo.getJSONObject("al").getString("pic");
        String picUrl = jo.getJSONObject("al").getString("picUrl");
        String songUrl = "http://music.163.com/song/media/outer/url?id=" + id;
        String lyricId = jo.getString("id");
        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setId(id);
        musicInfo.setName(songName);
        musicInfo.setArtist(singer);
        musicInfo.setAlbum(album);
        musicInfo.setSource("netease");
        musicInfo.setPicId(picId);
        musicInfo.setPicUrl(picUrl);
        musicInfo.setMusicUrl(songUrl);
        musicInfo.setLyricId(lyricId);
        return musicInfo;
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
            getLyricById("1815105886");
            // searchMusic("饭思思");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}