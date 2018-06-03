package com.apollo.provider.apolloprovider.common.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO:动态数据源实现读写分离
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/3 下午2:50
 */
public class DynamicDataSource extends AbstractRoutingDataSource {


    /**
     * 写数据源
     */
    private Object writeDataSource;


    /**
     * 读数据源
     */
    private Object readDataSource;


    @Override
    public void afterPropertiesSet() {

        if (StringUtils.isEmpty(writeDataSource)) {
            throw new IllegalArgumentException("Property  writeDataSource  is required !");
        }

        setDefaultTargetDataSource(writeDataSource);
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DynamicDataSourceGlobal.WRITE.name(), writeDataSource);
        if (!StringUtils.isEmpty(readDataSource)) {
            targetDataSource.put(DynamicDataSourceGlobal.READ.name(), readDataSource);
        }
        setTargetDataSources(targetDataSource);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        DynamicDataSourceGlobal dynamicDataSourceGlobal = DynamicDataSourceHolder.getDataSource();
        if (StringUtils.isEmpty(dynamicDataSourceGlobal) || dynamicDataSourceGlobal == DynamicDataSourceGlobal.WRITE) {
            return DynamicDataSourceGlobal.WRITE.name();
        }
        return DynamicDataSourceGlobal.READ.name();
    }


    public Object getWriteDataSource() {
        return writeDataSource;
    }

    public void setWriteDataSource(Object writeDataSource) {
        this.writeDataSource = writeDataSource;
    }

    public Object getReadDataSource() {
        return readDataSource;
    }

    public void setReadDataSource(Object readDataSource) {
        this.readDataSource = readDataSource;
    }
}
