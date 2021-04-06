package edu.hufe.service.impl;

import edu.hufe.entity.DataSource;
import edu.hufe.mapper.DataSourceMapper;
import edu.hufe.service.DataSourceService;
import edu.hufe.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service()
@Transactional
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<DataSource> findDataSourceList() {
        // 先去缓存查找数据,没有数据则去数据库查询
        List<DataSource> sources = redisUtil.getList("dataSources", DataSource.class);
        if(sources == null){
            // 从数据库查询
            System.out.println("从数据库查询数据源列表");
            sources = dataSourceMapper.findAllDataSource();
            // 将查询的结果保存到redis
            redisUtil.set("dataSources", sources);
        }
        return sources;
    }
}
