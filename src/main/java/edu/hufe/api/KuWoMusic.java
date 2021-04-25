package edu.hufe.api;

import edu.hufe.entity.MusicInfo;
import edu.hufe.utils.DataUtil;
import edu.hufe.utils.RequestUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 酷我音乐接口
 */
public class KuWoMusic {

    /**
     * 根据关键字获取歌曲信息
     * @param num 结果数
     * @param page 页码 1..n
     * @param keyword 关键字
     */
    public static List<MusicInfo> searchMusic(String num, String page, String keyword) throws IOException {
        List<MusicInfo> list = new ArrayList<>();
        String url = "http://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key="+ keyword
                +"&pn="+ page +"&rn="+ num +"&httpsStatus=1&reqId=" + UUID.randomUUID();
        String referer = "http://www.kuwo.cn/search/list?key=" + keyword;
        // 设置请求头
        Map<String,String> header = new HashMap<>();
        String csrf = getCsrf();
        header.put("Referer", referer);
        header.put("csrf", csrf);
        header.put("Cookie", "kw_token=" + csrf);
        String result = RequestUtil.connectToUrl(url, header, null, Connection.Method.GET);
        // System.out.println(result);
        JSONObject rs = JSONObject.fromObject(result);
        // 获取响应码
        String code = rs.getString("code");
        if ("200".equals(code)) {
            JSONArray songs = rs.getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < songs.size(); i++) {
                JSONObject item = songs.getJSONObject(i);
                list.add(parseJson(item));
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
        // http://www.kuwo.cn/api/www/bang/bang/musicList?bangId=17&pn=1&rn=30&httpsStatus=1&reqId=d6739aa0-8b7f-11eb-9226-579cef584e97
        String url = "http://www.kuwo.cn/api/www/bang/bang/musicList?bangId="+ listId +"&pn=1&rn=30&httpsStatus=1&reqId="+ UUID.randomUUID();
        Map<String,String> header = new HashMap<>();
        String csrf = getCsrf();
        header.put("Cookie", "kw_token=" + csrf);
        header.put("csrf", csrf);
        String result = RequestUtil.connectToUrl(url, header, null, Connection.Method.GET);
        // System.out.println(result);
        JSONObject rs = JSONObject.fromObject(result);
        String code = rs.getString("code");
        // 判断是否正确响应
        if ("200".equals(code)) {
            JSONArray musicList = rs.getJSONObject("data").getJSONArray("musicList");
            // 遍历列表
            for (int i = 0; i < musicList.size(); i++) {
                JSONObject item = musicList.getJSONObject(i);
                list.add(parseJson(item));
            }
        }
        return list;
    }

    /**
     * 根据歌曲id获取歌曲Url信息
     * @param rid
     * @return
     * @throws IOException
     */
    public static MusicInfo getMusicInfoById(String rid) throws IOException {
        MusicInfo musicInfo = new MusicInfo();
        // 拼接歌曲的资源URL
        String url ="http://www.kuwo.cn/url?format=mp3&rid=" + rid + "&response=url&type=convert_url3"+
                "&br=128kmp3&from=web&t=1573199898861&reqId=" + UUID.randomUUID();
        String result = RequestUtil.connectToUrl(url,null,null, Connection.Method.GET);
        // System.out.println(result);
        JSONObject rs = JSONObject.fromObject(result);
        // 获取响应码
        String code = rs.getString("code");
        String musicUrl = "";
        if ("200".equals(code)) {
            musicUrl = rs.getString("url");
        }
        musicInfo.setMusicUrl(musicUrl);
        return musicInfo;
    }

    /**
     * 获取歌曲歌词
     * @param rid
     * @return
     * @throws IOException
     */
    public static String getLyricById(String rid) throws IOException {
        JSONObject lyric = new JSONObject();
        String url = "http://m.kuwo.cn/newh5/singles/songinfoandlrc?musicId="+ rid +
                "&httpsStatus=1&reqId=" + UUID.randomUUID();
        String result = RequestUtil.connectToUrl(url,null,null, Connection.Method.GET);
        // System.out.println(result);
        JSONObject rs = JSONObject.fromObject(result);
        String status = rs.getString("status");
        if("200".equals(status)) {
            JSONObject data = rs.getJSONObject("data");
            if(data.containsKey("lrclist") && !"null".equals(data.getString("lrclist"))){
                // 获取歌词的json
                JSONArray lrcList = data.getJSONArray("lrclist");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < lrcList.size(); i++){
                    JSONObject item = lrcList.getJSONObject(i);
                    String content = item.getString("lineLyric");
                    String time = item.getString("time");
                    sb.append("["+ formatTime(time) +"] " + content + "\n");
                }
                lyric.put("lyric", sb.toString());
            }
        }
        // System.out.println(lyric.toString());
        return lyric.toString();
    }

    /**
     * 获取进入首页Cookie中的csrf
     * @return
     */
    private static String getCsrf(){
        String url = "http://www.kuwo.cn/";
        try {
            HttpURLConnection con  = (HttpURLConnection)new URL(url).openConnection();
            // 获得响应头
            String sessionValue = con.getHeaderField("Set-Cookie");
            String[] sessionId = sessionValue.split(";");
            // 从响应头中得到token值
            return sessionId[0].substring(sessionId[0].indexOf("=") + 1);
        } catch (IOException e) {
            e.printStackTrace();
            return "qazwsxedcr";
        }
    }

    /**
     * 将响应的json数据解析成对象返回
     * @param jo
     */
    private static MusicInfo parseJson(JSONObject jo){
        // 获取相关歌曲信息
        String id = jo.getString("rid");
        String name = jo.getString("name");
        String artist = jo.getString("artist");
        String album = jo.getString("album");
        // 有些歌曲没有图片
        String picUrl = "";
        if(jo.containsKey("pic")){
            picUrl = jo.getString("pic");
        }
        return DataUtil.fillData(id, name, artist, album,"3",id,picUrl,"",id);
    }

    /**
     * 格式化时间
     * @param time
     */
    private static String formatTime(String time){
        String result = "";
        // 1.将秒数与毫秒值分割开
        String [] arr = time.split("\\.");
        String second = arr[0];
        String mSec = "000";
        if(arr.length >= 2){
            mSec = arr[1];
        }
        // 2.格式化秒数
        int sec = Integer.parseInt(second);
        if(sec < 60){
            result = "00:" + sec +"." + mSec;
        }else {
            int minute = sec / 60;
            sec = sec % 60;
            result = minute + ":" + sec + "." + mSec;
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            // searchMusic(1,"清空");
            // searchMusic("1","30","白月光与朱砂痣");
            // getPlayListById("93");
            getLyricById("173029318");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
