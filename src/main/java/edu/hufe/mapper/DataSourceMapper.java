package edu.hufe.mapper;

import edu.hufe.entity.DataSource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper // 表示该类是一个mapper接口文件,需要被springboot进行扫描
public interface DataSourceMapper {

    // 查询所有的数据源
    List<DataSource> findAllDataSource();

    // 根据id查询数据源
    DataSource findDataSourceById(String id);

    // 修改数据源信息
    boolean updateDataSource(DataSource dataSource);

    // 删除数据源信息
    boolean deleteDataSourceById(String id);

}
