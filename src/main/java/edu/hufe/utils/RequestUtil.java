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
     * @param header
     * @param data
     * @param method
     * @return
     */
    public static String connectToUrl(String url, Map<String,String> header, Map<String,String> data, Connection.Method method) throws IOException {
        Connection connection = Jsoup.connect(url);
        // 设置请求头
        if(header != null){
            for(Map.Entry entry : header.entrySet()){
                connection.header(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        // 遍历map填充请求数据
        if(data != null) {
            for(Map.Entry entry : data.entrySet()){
                connection.data(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        // 设置请求方式,默认GET请求
        if(method != null){
            connection.method(method);
        }
        // 设置请求头,忽略响应类型
        connection.userAgent(UserAgent.randomAgent()).ignoreContentType(true).timeout(Const.TIME_OUT);
        // 发送请求
        return connection.execute().body();
    }
}
