package com.apollo.provider.apolloprovider.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * TODO:
 *
 * @author wangbinbin
 * @version 1.0.0
 * @date 2018/6/3 下午4:17
 */
public class DataEntity extends BaseEntity {

    /**
     * 删除标记 0  正常
     */
    public static final String DEL_FLAG_NORMAL = "0";

    /**
     * 删除标记 1  删除
     */
    public static final String DEL_FLAG_DELETE = "1";


    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 更新日期
     */
    private Date updateDate;

    /**
     * 删除标记
     */
    private String delFlag;


    public DataEntity(Integer id) {
        super(id);
    }

    @Override
    public void preInsert() {

        if (this.getIsNewRecord()) {
            //设置ID
        }
        this.updateDate = new Date();
        this.createDate = new Date();
    }

    @Override
    public void preUpdate() {
        this.updateDate = new Date();
    }

    public Date getCreateDate() {
        return createDate == null ? null : (Date) createDate.clone();
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate == null ? null : (Date) createDate.clone();
    }

    public Date getUpdateDate() {
        return updateDate == null ? null : (Date) updateDate.clone();
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate == null ? null : (Date) updateDate.clone();
    }

    @JsonIgnore
    @Length(min = 1,max = 1)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
