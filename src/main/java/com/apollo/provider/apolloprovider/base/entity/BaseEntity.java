package com.apollo.provider.apolloprovider.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * TODO:Entity 基类
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/3 下午4:10
 */
public abstract class BaseEntity implements Serializable {


    /**
     * 实体类编号 主键
     */
    private Integer id;


    /**
     * 是否是新记录 调用setIsNewRecord() 设置新记录,使用自定义ID
     * 设置为true后 强制执行插入语句，ID 不会自动生成，需手动传入
     */
    private boolean isNewRecord = false;

    public BaseEntity() {

    }


    public BaseEntity(Integer id) {
        this();
        this.id = id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 在插入数据之前执行方法  子类实现
     */
    public abstract void preInsert();


    /**
     * 在更新之前执行方法  子类实现
     */
    public abstract void preUpdate();

    @JsonIgnore
    public boolean getIsNewRecord() {
        return isNewRecord || StringUtils.isEmpty(getId());
    }


    public void setIsNewRecord(boolean isNewRecord){
        this.isNewRecord = isNewRecord;
    }

    //TODO  toString

}
