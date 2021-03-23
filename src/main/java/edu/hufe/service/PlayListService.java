package edu.hufe.service;

import edu.hufe.entity.MusicInfo;
import edu.hufe.entity.PlayList;

import java.util.List;

public interface PlayListService {
    // 查询所有数据源
    List<PlayList> queryAllPlayList();

    List<MusicInfo> queryDetailList(String sourceId, String id);

}
