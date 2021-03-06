package com.scwvg.mappers;

import com.github.pagehelper.Page;
import com.scwvg.entitys.scwvgponnetwork.WvgMenu;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @aothor: lul
 * @unit: 智谷园网络科技有限公司
 * @iphone:18482297774
 * @date：20192019/5/2
 * @Explain：菜单操作
 **/
@Mapper
public interface WvgMenuMapper {
    /*查询所有菜单：通过父节点排序*/
    @Select("select * from wvg_menu x order by x.wvg_parent_id")
    public List<WvgMenu> queryAll();
    /*通过用户ID查找所属菜单*/
    @Select("SELECT x.* FROM wvg_menu x INNER JOIN wvg_role_menu y ON x.wvg_menu_id = y.wvg_menu_id " +
            "INNER JOIN wvg_role_user ru on ru.wvg_role_id= y.wvg_role_id WHERE ru.wvg_user_id=#{wvg_user_id} " +
            "ORDER BY wvg_menu_id")
    public List<WvgMenu> queryMenuByUserId(Long wvg_user_id);

    /*通过用户ID查找菜单*/
    @Select("SELECT\n" +
            "\tx.*\n" +
            "FROM\n" +
            "\twvg_menu x\n" +
            "INNER JOIN wvg_role_menu y ON x.wvg_menu_id = y.wvg_menu_id\n" +
            "INNER JOIN wvg_role r ON y.wvg_role_id = r.wvg_role_id\n" +
            "where r.wvg_role_id=#{wvg_role_id} ORDER BY wvg_menu_id")
    public List<WvgMenu> queryMenuByRoleId(long wvg_role_id);
    /*通过菜单ID查找菜单*/
    @Select("select * from wvg_menu where wvg_menu_id=#{wvg_menu_id}")
    public WvgMenu queryMenuById(Long wvg_menu_id);
    /*新增菜单*/
    /*通过ID修改*/
    @Insert("insert into wvg_menu(wvg_menu_id,\n" +
            "wvg_parent_id,\n" +
            "wvg_menu_name,\n" +
            "wvg_menu_url,\n" +
            "wvg_menu_type,\n" +
            "wvg_menu_type_name,\n" +
            "wvg_authority,\n" +
            "wvg_add_time,\n" +
            "wvg_menu_css,\n" +
            "wvg_updata_time,\n" +
            "wvg_menu_icon,\n" +
            "wvg_menu_state,\n" +
            "wvg_menu_explain,\n" +
            "wvg_user_id)" +
            "values(#{wvg_menu_id},\n" +
            "#{wvg_parent_id},\n" +
            "#{wvg_menu_name},\n" +
            "#{wvg_menu_url},\n" +
            "#{wvg_menu_type},\n" +
            "#{wvg_menu_type_name},\n" +
            "#{wvg_authority},\n" +
            "now(),\n" +
            "#{wvg_menu_css},\n" +
            "now(),\n" +
            "#{wvg_menu_icon},\n" +
            "#{wvg_menu_state},\n" +
            "#{wvg_menu_explain},\n" +
            "#{wvg_user_id})")
    public int insertMenu(WvgMenu wvgMenu);
    /*修改菜单*/
    @Update("update wvg_menu set" +
            " wvg_parent_id=#{wvg_parent_id},\n" +
            " wvg_menu_name=#{wvg_menu_name},\n" +
            " wvg_menu_url=#{wvg_menu_url},\n" +
            " wvg_menu_type=#{wvg_menu_type},\n" +
            " wvg_menu_type_name=#{wvg_menu_type_name},\n" +
            " wvg_authority=#{wvg_authority},\n" +
            " wvg_updata_time=now(),\n" +
            " wvg_menu_css=#{wvg_menu_css},\n" +
            " wvg_menu_icon=#{wvg_menu_icon},\n" +
            " wvg_menu_state=#{wvg_menu_state},\n" +
            " wvg_menu_explain=#{wvg_menu_explain},\n" +
            " wvg_user_id=#{wvg_user_id}" +
            " where wvg_menu_id=#{wvg_menu_id}")
    public int updateMenu(WvgMenu wvgMenu);

    /*删除角色表菜单ID*/
    @Delete(" delete from wvg_role_menu where wvg_menu_id=#{wvg_menu_id}")
    public int deleteRoleMenu(Long wvg_menu_id);
    /*根据ID删除菜单*/
    @Delete("delete from wvg_menu where wvg_menu_id=#{wvg_menu_id}")
    public int deleteMenu(Long wvg_menu_id);


    /*查询最大的ID*/
    @Select("SELECT MAX(wvg_menu_id) from wvg_menu")
    public int queryMaxMenuID();

   public Page<WvgMenu> queryMenuAllByPage(Map<String,Object> params);
   /*获取新增人*/
    @Select("select wvg_real_name from wvg_user where wvg_user_id=#{wvg_user_id} ")
   String getWvgUserName(Long wvg_user_id);

}
