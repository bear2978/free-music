package edu.hufe.service;

import edu.hufe.entity.DataSource;
import java.util.List;

public interface DataSourceService {
    // 查询所有数据源
    List<DataSource> findDataSourceList();

}
