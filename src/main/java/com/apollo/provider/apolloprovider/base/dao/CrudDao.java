package com.apollo.provider.apolloprovider.base.dao;

import java.util.List;
import java.util.Map;

/**
 * TODO:
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/3 下午3:38
 */
public interface CrudDao<T> extends BaseDao {

    /**
     * 获取单挑数据
     *
     * @param id
     * @return
     */
    T get(Integer id);


    /**
     * 获取单挑数据
     *
     * @param entity
     * @return
     */
    T get(T entity);


    /**
     * 查询数据列表
     *
     * @param entity
     * @return
     */
    List<T> findList(T entity);

    /**
     * 查询数据列表
     *
     * @param queryMap 查询条件
     * @return
     */
    List<T> queryList(Map<String, Object> queryMap);


    /**
     * 查询所有数据列表
     *
     * @return
     */
    List<T> findAllList();

    /**
     * 插入数据
     *
     * @param entity
     * @return
     */
    int insert(T entity);

    /**
     * 更新数据
     *
     * @param entity
     * @return
     */
    int update(T entity);

    /**
     * 删除数据
     *
     * @param entity
     * @return
     */
    int delete(T entity);

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    int deleteById(Integer id);
}
