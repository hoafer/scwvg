package com.scwvg.mappers;

import com.scwvg.entitys.scwvgponnetwork.WvgToken;
import org.apache.ibatis.annotations.*;

/**
 * @aothor: lul
 * @unit: 智谷园网络科技有限公司
 * @iphone:18482297774
 * @date：20192019/5/2
 * @Explain：TokenMapper
 **/
@Mapper
public interface WvgTokenMapper {
    @Insert("insert into wvg_token(wvg_token_id," +
            "wvg_user_id," +
            "wvg_token_val," +
            "wvg_token_expireTime," +
            "wvg_token_addTime, wvg_token_updateTime) " +
            "values (#{id},#{wvg_user_id}, #{wvg_token_val}, " +
            "#{wvg_token_expireTime}, #{createTime}," +
            "#{updateTime})")
    int save(WvgToken wvgToken);

    @Select("select * from wvg_token t where t.wvg_token_id = #{wvg_token_id}")
    WvgToken getByTokenId(String wvg_token_id);

    @Update("update wvg_token t set wvg_user_id=#{wvg_user_id}," +
            "t.wvg_token_expireTime = #{wvg_token_expireTime}, " +
            "wvg_token_updateTime=#{updateTime}, " +
            "wvg_token_val=#{wvg_token_val} " +
            " where t.wvg_token_id = #{id}")
    int update(WvgToken model);

    @Delete("delete from wvg_token where wvg_token_id = #{wvg_token_id}")
    int delete(String id);

    /*通过用户ID删除token信息*/
    @Delete("delete from wvg_token where wvg_user_id=#{wvg_user_id}")
    public int deleteUserId(Long wvg_user_id);
    /*查询是否存在用户*/
    @Select("select count(1) from wvg_token where wvg_user_id=#{wvg_user_id}")
    public int queryCountByUserid(Long wvg_user_id);
}
