package com.apollo.provider.apolloprovider.base.service;

import com.apollo.provider.apolloprovider.base.dao.CrudDao;
import com.apollo.provider.apolloprovider.base.entity.DataEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * TODO:Service 基类
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/3 下午4:05
 */
@Transactional(readOnly = true)
public abstract class CrudService<D extends CrudDao<T>, T extends DataEntity> {

    @Autowired
    private D dao;

    public D getDao() {
        return dao;
    }


    public T get(Integer id) {
        return dao.get(id);
    }

    public T get(T entity) {
        return dao.get(entity);
    }

    public List<T> findList(T entity) {
        return dao.findList(entity);
    }


    public List<T> queryList(Map<String, Object> queryMap) {
        return dao.queryList(queryMap);
    }


    public PageInfo<T> findPage(Integer pageNo, Integer pageSize, T entity) {
        PageHelper.startPage(pageNo, pageSize);
        List<T> list = dao.findList(entity);
        return new PageInfo<>(list);
    }


    public PageInfo<T> queryPage(Integer pageNo, Integer pageSize, Map<String, Object> queryMap) {
        PageHelper.startPage(pageNo, pageSize);
        List<T> list = dao.queryList(queryMap);
        return new PageInfo<>(list);
    }

    @Transactional(readOnly = false)
    public T save(T entity) {
        if (entity.getIsNewRecord()) {
            entity.preInsert();
            dao.insert(entity);
        } else {
            entity.preUpdate();
            dao.update(entity);
        }
        return entity;
    }


    @Transactional(readOnly = false)
    public void delete(T entity) {
        dao.delete(entity);
    }

    @Transactional(readOnly = false)
    public void deleteById(Integer id){
        dao.deleteById(id);
    }
}
