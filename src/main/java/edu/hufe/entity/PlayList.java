package edu.hufe.entity;

import java.io.Serializable;

/**
 * 歌单实体
 */
public class PlayList implements Serializable {
    // id
    private String id;
    // 名字
    private String name;
    // imgUrl
    private String cover;
    // 数据源id
    private String sourceId;
    // 列表创建者名字(暂时没用到，可空)
    private String creatorName;
    // 列表创建者头像(暂时没用到，可空)
    private String creatorAvatar;
    // 是否启用
    private Integer enable;  // 0代表不启用,1代表启用
    // 创建时间
    private String creatTime;

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorAvatar() {
        return creatorAvatar;
    }

    public void setCreatorAvatar(String creatorAvatar) {
        this.creatorAvatar = creatorAvatar;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    @Override
    public String toString() {
        return "PlayList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", creatorAvatar='" + creatorAvatar + '\'' +
                ", enable=" + enable +
                ", creatTime='" + creatTime + '\'' +
                '}';
    }
}
