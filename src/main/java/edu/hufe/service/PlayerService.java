package edu.hufe.service;

import edu.hufe.entity.MusicInfo;

import java.util.List;

public interface PlayerService {

    List<MusicInfo> searchMusic(String sourceId, String count, String page, String keyword);

    MusicInfo getMusicInfoById(String sourceId, String id);

    String getLyricById(String sourceId, String id);

    String getUserListById(String uid);

}
