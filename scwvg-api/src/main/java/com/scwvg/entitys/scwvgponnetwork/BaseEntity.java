package com.scwvg.entitys.scwvgponnetwork;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @aothor: lul
 * @unit: 智谷园网络科技有限公司
 * @iphone:18482297774
 * @date：20192019/5/1
 * @Explain：此处因为很多表都需要用到UUID和创建时间，修改时间，所以进行单独定义
 **/
@Data
public abstract class BaseEntity<ID extends Serializable> implements Serializable
{
    private static final long serialVersionUID = 2054813493011812469L;
    //生成UUID
    private ID id; //TokenID

    private Date createTime = new Date();
    private Date updateTime = new Date();
    private Long wvg_user_id;  //到处都需要有这个新增人的字段
    //转换数据库里int转String的问题
    private String changeStr;
    //转换数据库里int转String的问题
    private String changeStr1;
    //转换数据库里int转String的问题
    private String changeStr2;
    //转换数据库里int转String的问题
    private String changeStr4;
    //转换数据库里int转String的问题
    private String changeStr3;
    //转换数据库里int转String的问题
    private String changeStr5;

    public ID getId() {
        return id;
    }
}
