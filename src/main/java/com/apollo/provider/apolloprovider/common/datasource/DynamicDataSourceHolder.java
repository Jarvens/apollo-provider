package com.apollo.provider.apolloprovider.common.datasource;

/**
 * TODO:
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/3 下午3:00
 */
public final class DynamicDataSourceHolder {

    /**
     * 动态数据源存储
     */

    private static ThreadLocal<DynamicDataSourceGlobal> DYNAMIC_DATASOURCE_GLOBAL = new ThreadLocal<>();


    /**
     * 创建私有构造器 防止通过外部初始化修改
     */
    private DynamicDataSourceHolder() {
    }


    /**
     * 存放数据源
     *
     * @param datasource
     */
    public static void putDataSource(DynamicDataSourceGlobal datasource) {
        DYNAMIC_DATASOURCE_GLOBAL.set(datasource);
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public static DynamicDataSourceGlobal getDataSource() {
        return DYNAMIC_DATASOURCE_GLOBAL.get();
    }

    /**
     * 清除数据源
     */
    public static void clearDataSource() {
        DYNAMIC_DATASOURCE_GLOBAL.remove();
    }
}
