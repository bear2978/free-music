package edu.hufe.service.impl;

import edu.hufe.entity.DataSource;
import edu.hufe.mapper.DataSourceMapper;
import edu.hufe.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service()
@Transactional
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Override
    // @Cacheable(cacheNames = "dataSource")
    public List<DataSource> findDataSourceList() {
        return dataSourceMapper.findAllDataSource();
    }
}
