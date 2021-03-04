package edu.hufe.entity;

/**
 * 歌曲实体
 */
public class MusicInfo {
    // id
    private String id;
    // 歌曲名称
    private String name;
    // 歌手
    private String artist;
    // 专辑名称
    private String album;
    // 数据源
    private String source;
    // 图片封面id
    private String picId;
    // 图片封面
    private String picUrl;
    // 音乐url
    private String musicUrl;
    // 歌词
    private String lyricId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getLyricId() {
        return lyricId;
    }

    public void setLyricId(String lyricId) {
        this.lyricId = lyricId;
    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", source='" + source + '\'' +
                ", picId='" + picId + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", musicUrl='" + musicUrl + '\'' +
                ", lyricId='" + lyricId + '\'' +
                '}';
    }
}
