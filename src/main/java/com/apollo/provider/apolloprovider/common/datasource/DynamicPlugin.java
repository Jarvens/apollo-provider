package com.apollo.provider.apolloprovider.common.datasource;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import sun.plugin2.main.server.ResultHandler;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * TODO:动态数据源Mybatis拦截插件
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/3 下午3:14
 */
@Intercepts({@Signature(type = Executors.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executors.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class DynamicPlugin implements Interceptor {

    /**
     * Logger
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(DynamicPlugin.class);

    /**
     * 拦截SQL 表达式
     */
    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    /**
     * 动态数据源缓存
     */
    private static final Map<String, DynamicDataSourceGlobal> CACHE_MAP = new ConcurrentHashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        //如果没有事务
        if (!synchronizationActive) {
            Object[] objects = invocation.getArgs();
            MappedStatement mappedStatement = (MappedStatement) objects[0];
            DynamicDataSourceGlobal dynamicDataSourceGlobal;
            if (StringUtils.isEmpty((dynamicDataSourceGlobal = CACHE_MAP.get(mappedStatement.getId())))) {
                dynamicDataSourceGlobal = getDynamicDatasource(mappedStatement, objects[1]);
                LOGGER.warn("设置方法[{}] use [{}] Strategy,SqlCommandType [{}]..", mappedStatement.getId(),
                    dynamicDataSourceGlobal.name(), CACHE_MAP.put(mappedStatement.getId(), dynamicDataSourceGlobal));
            }
            DynamicDataSourceHolder.putDataSource(dynamicDataSourceGlobal);
        }
        return invocation.proceed();
    }



    /**
     * 获得动态数据源
     *
     * @param ms
     * @param parameterObject
     * @return
     */
    private DynamicDataSourceGlobal getDynamicDatasource(MappedStatement ms, Object parameterObject) {
        DynamicDataSourceGlobal dynamicDataSourceGlobal;

        //读
        if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
            //!selectKey 为自增id查询主键(SELECT LAST_INSERT_ID() )方法，使用主库
            if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                dynamicDataSourceGlobal = DynamicDataSourceGlobal.WRITE;
            } else {
                BoundSql boundSql = ms.getSqlSource().getBoundSql(parameterObject);
                String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                if (sql.matches(REGEX)) {
                    dynamicDataSourceGlobal = DynamicDataSourceGlobal.WRITE;
                } else {
                    dynamicDataSourceGlobal = DynamicDataSourceGlobal.READ;
                }
            }
        } else {
            dynamicDataSourceGlobal = DynamicDataSourceGlobal.WRITE;
        }
        return dynamicDataSourceGlobal;
    }

    @Override
    public Object plugin(Object o) {
        if (o instanceof Executor) {
            return Plugin.wrap(o, this);
        } else {
            return o;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
