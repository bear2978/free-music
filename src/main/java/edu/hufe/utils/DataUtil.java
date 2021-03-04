package edu.hufe.utils;

import edu.hufe.entity.MusicInfo;

public class DataUtil {

    public static MusicInfo fillData(String id,String name,String artist,String album,String source,String picId,
                               String picUrl, String musicUrl, String lyricId){
        MusicInfo musicInfo = new MusicInfo();
        musicInfo.setId(id);
        musicInfo.setName(name);
        musicInfo.setArtist(artist);
        musicInfo.setAlbum(album);
        musicInfo.setSource(source);
        musicInfo.setPicId(picId);
        musicInfo.setPicUrl(picUrl);
        musicInfo.setMusicUrl(musicUrl);
        musicInfo.setLyricId(lyricId);
        return musicInfo;
    }

}