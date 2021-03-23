package edu.hufe.mapper;

import edu.hufe.entity.PlayList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 歌单列表
 */
@Mapper
public interface PlayListMapper {
    // 查询所有的数据源
    List<PlayList> findAllPlayList();

}
