package edu.hufe.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * 向音乐网站发起请求的工具类
 */
public class RequestUtil {

    /**
     * 链接指定url得到响应json
     * @param url
     * @param referer
     * @param data
     * @param method
     * @return
     */
    public static String connectToUrl(String url, String referer, Map<String,String> data, Enum method) throws IOException {
        Connection connection = Jsoup.connect(url);
        // 遍历map填充请求数据
        for(Map.Entry entry : data.entrySet()){
            connection.data(entry.getKey().toString(),entry.getValue().toString());
        }
        // 设置请求头
        if(referer != null && !referer.equals("")){
            connection.header("Referer",referer);
        }

        // 设置请求方式
        if(method == Method.POST){
            connection.method(Connection.Method.POST);
        }else if(method == Method.GET){
            connection.method(Connection.Method.GET);
        }
        connection.userAgent(UserAgent.randomAgent()).ignoreContentType(true).timeout(Const.TIME_OUT);
        // 发送请求
        return connection.execute().body();
    }
}
