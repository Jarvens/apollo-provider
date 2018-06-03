package com.apollo.provider.apolloprovider.common.datasource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.sql.DataSource;

/**
 * TODO:动态数据源事务管理
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/3 下午3:06
 */
public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {


    public DynamicDataSourceTransactionManager(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        //设置数据源
        boolean readOnly = definition.isReadOnly();
        //只读事务 到 读库  读写事务到写库
        if (readOnly) {
            DynamicDataSourceHolder.putDataSource(DynamicDataSourceGlobal.READ);
        } else {
            DynamicDataSourceHolder.putDataSource(DynamicDataSourceGlobal.WRITE);
        }
        super.doBegin(transaction, definition);
    }


    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        //清理本地线程的数据源
        DynamicDataSourceHolder.clearDataSource();
    }
}
