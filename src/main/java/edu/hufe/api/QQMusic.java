package edu.hufe.api;

import edu.hufe.entity.MusicInfo;
import edu.hufe.utils.Base64Decoder;
import edu.hufe.utils.DataUtil;
import edu.hufe.utils.UserAgent;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 爬取QQ音乐接口
 */
public class QQMusic {

    /**
     * 根据关键字获取歌曲信息
     * @param page 页码
     * @param num 查询记录条数
     * @param keyword 查询关键字
     * @return
     * @throws IOException
     */
    public static List<MusicInfo> searchMusic(String page, String num, String keyword) throws IOException {
        List<MusicInfo> list = new ArrayList<>();
        // 拼接请求数据URL
        String url = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?p="+ page +"&n=" + num + "&w=" + keyword;
        String result = connectToUrl(url);
        // 截取正确的json格式,并转化成json对象
        result = result.substring(result.indexOf("{"), result.length() - 1);
        JSONObject rs = JSONObject.fromObject(result);
        // 获取响应码,防止后面程序出错
        String code = rs.getString("code");
        // System.out.println(result);
        if("0".equals(code)){
            // 获取歌曲列表的JSON数据
            JSONArray songs = rs.getJSONObject("data").getJSONObject("song").getJSONArray("list");
            for (int i = 0; i < songs.size(); i++) {
            // for (int i = 0; i < 5; i++) {
                // 获取每一条数据
                JSONObject item = songs.getJSONObject(i);
                String id = item.getString("songmid");
                String name = item.getString("songname");
                String artist = item.getJSONArray("singer").getJSONObject(0).getString("name");
                String album = item.getString("albumname");
                // 获取专辑id,然后拼接专辑图片
                // https://y.gtimg.cn/music/photo_new/T002R300x300M000000HJKHt4gHLtA.jpg?max_age=2592000
                String albumId = item.getString("albummid");
                String picUrl = "https://y.gtimg.cn/music/photo_new/T002R300x300M000"+ albumId +".jpg?max_age=2592000";
                // System.out.println(picUrl);
                // 暂时不获取音乐的url
                list.add(DataUtil.fillData(id,name,artist,album,"tencent",id,picUrl,"",id));
            }
        }
        return list;
    }

    /**
     * 根据歌曲id获取歌曲Url信息
     * @param mid
     * @return
     * @throws IOException
     */
    public static MusicInfo getMusicInfoById(String mid) throws IOException {
        // String url = "https://c.y.qq.com/v8/fcg-bin/fcg_play_single_song.fcg?songmid=" + mid + "&platform=yqq&format=json";
        // String result = connectToUrl(url);
        // https://ws.stream.qqmusic.qq.com/C1000039MnYb0qxYhV.m4a?fromtag=38
        // System.out.println(result);
        MusicInfo musicInfo = new MusicInfo();
        // 拼接歌曲外链
        String url = "https://u.y.qq.com/cgi-bin/musicu.fcg?data={\"req\":{\"module\":\"CDN.SrfCdnDispatchServer\",\"method\":\"GetCdnDispatch\",\"param\":{\"guid\":\"703417739\",\"calltype\":0,\"userip\":\"\"}},\"req_0\":{\"module\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\",\"param\":{\"guid\":\"703417739\",\"songmid\":[\""+ mid+ "\"],\"songtype\":[0],\"uin\":\"\",\"loginflag\":1,\"platform\":\"20\"}},\"comm\":{\"uin\":\"\",\"format\":\"json\",\"ct\":24,\"cv\":0}}";
        JSONObject rs = JSONObject.fromObject(connectToUrl(url));
        // System.out.println(rs);
        String musicUrl = rs.getJSONObject("req_0").getJSONObject("data").getJSONArray("midurlinfo").getJSONObject(0).getString("purl");
        // 得到下载歌曲的URL
        // http://ws.stream.qqmusic.qq.com/
        // http://dl.stream.qqmusic.qq.com/
        String songUrl = "";
        if(musicUrl != null && !musicUrl.equals("")){
            // 过滤掉拼接失败的URL
            songUrl = "http://dl.stream.qqmusic.qq.com/" + musicUrl;
        }
        musicInfo.setMusicUrl(songUrl);
        System.out.println(musicInfo);
        return musicInfo;
    }

    /**
     * 获取歌曲歌词
     * @param id
     * @return
     * @throws IOException
     */
    public static String getLyricById(String id) throws IOException {
        // https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new.fcg
        String url = "https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new.fcg?songmid=" + id;
        String result = connectToUrl(url);
        // 截取正确的json数据格式
        result = result.substring(result.indexOf("{"), result.length() - 1);
        // System.out.println(result);
        JSONObject rs = JSONObject.fromObject(result);
        // 获取响应码
        String retCode = rs.getString("retcode");
        String lyric = "";
        if("0".equals(retCode)){
            lyric = Base64Decoder.decode(rs.getString("lyric"));
        }
        // 重新产生解密后的歌词json
        JSONObject json = new JSONObject();
        json.put("lyric", lyric);
        // System.out.println(json);
        return json.toString();
    }

    /**
     * 连接到指定Url
     * @param url
     * @return
     * @throws IOException
     */
    public static String connectToUrl(String url) throws IOException {
        Connection.Response response = Jsoup.connect(url)
                .header("Origin", "https://y.qq.com")
                .header("Referer", "https://y.qq.com/portal/search.html")
                .header("Sec-Fetch-Mode", "cors")
                .userAgent(UserAgent.randomAgent())
                .timeout(10000).ignoreContentType(true).execute();
        return response.body();
    }

    public static void main(String[] args) {
        try {
            // searchMusic("1","30","清空");
            getMusicInfoById("004LZwE13k5hzj");
            // getMusicInfoById("004LMEOv19smN2");
            // getLyricById("004LMEOv19smN2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
