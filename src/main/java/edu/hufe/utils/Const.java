package edu.hufe.utils;

public interface Const {
    // 设置请求超时时间
    int TIME_OUT = 5000;
    // redis默认缓存时间,默认过期时长（一天）,单位：秒
    long REDIS_DEFAULT_EXPIRE = 60 * 60 * 24;
    // redis不设置过期时长
    long REDIS_NOT_EXPIRE = -1;
    // 时间格式化格式
    String DATE_FORMAT = "yyyyMMdd";
}
