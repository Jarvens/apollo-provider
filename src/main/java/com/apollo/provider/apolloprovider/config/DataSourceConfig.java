package com.apollo.provider.apolloprovider.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.apollo.provider.apolloprovider.base.exception.SystemException;
import com.apollo.provider.apolloprovider.common.datasource.DynamicDataSource;
import com.apollo.provider.apolloprovider.common.datasource.DynamicDataSourceTransactionManager;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * TODO:
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/3 下午2:44
 */
@Configuration
public class DataSourceConfig {

    /**
     * 写数据库
     *
     * @return
     */
    @Primary
    @Bean(name = "writeDataSource")
    @ConfigurationProperties("spring.datasource.write")
    public DruidDataSource writeDataSource() {
        return new DruidDataSource();
    }


    /**
     * 读数据库
     *
     * @return
     */
    @Bean(name = "readDataSource")
    @ConfigurationProperties("spring.datasource.read")
    public DruidDataSource readeDataSource() {
        return new DruidDataSource();
    }


    /**
     * 动态数据源
     *
     * @return
     */
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setWriteDataSource(writeDataSource());
        dynamicDataSource.setReadDataSource(readeDataSource());
        return dynamicDataSource;
    }


    /**
     * 动态事务管理器
     *
     * @param dynamicDataSource
     * @return
     */
    @Bean(name = "dynamicTransactionManager")
    public DataSourceTransactionManager dynamicTransactionManager(
        @Qualifier("dynamicDataSource") DataSource dynamicDataSource) {

        return new DynamicDataSourceTransactionManager((dynamicDataSource));
    }


    @Bean
    @ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
    public SqlSessionFactory dynamicSqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dynamicDataSource,
        MybatisProperties properties) {

        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dynamicDataSource);
        sessionFactory.setConfigLocation(new DefaultResourceLoader().getResource(properties.getConfigLocation()));
        sessionFactory.setMapperLocations(properties.resolveMapperLocations());
        try {
            return sessionFactory.getObject();
        } catch (Exception ex) {
            throw new SystemException(ex);
        }
    }
}
