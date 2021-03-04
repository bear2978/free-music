package edu.hufe.entity;

/**
 * 歌单实体
 */
public class PlayList {
    // id
    private Long id;
    // 名字
    private String name;
    // imgUrl
    private String cover;
    // 列表创建者名字(暂时没用到，可空)
    private String creatorName;
    // 列表创建者头像(暂时没用到，可空)
    private String creatorAvatar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "PlayList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", creatorAvatar='" + creatorAvatar + '\'' +
                '}';
    }
}
