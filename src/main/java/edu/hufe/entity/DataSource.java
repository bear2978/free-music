package edu.hufe.entity;

import java.io.Serializable;

/**
 * 音乐数据源实体
 */
public class DataSource implements Serializable {

    // 自增长主键id
    private Integer id;

    // 数据源名称
    private String name;

    // 类路径
    private String classpath;

    // 别称
    private String alias;

    // 是否启用
    private Integer enable;  // 0代表不启用,1代表启用

    // 创建时间
    private String creatTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClasspath() {
        return classpath;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
        return "DataSource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", classpath='" + classpath + '\'' +
                ", alias='" + alias + '\'' +
                ", enable=" + enable +
                ", creatTime='" + creatTime + '\'' +
                '}';
    }

}

