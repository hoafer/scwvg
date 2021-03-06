package com.scwvg.service;

import com.github.pagehelper.Page;
import com.scwvg.entitys.AlarmCounts;
import com.scwvg.entitys.BandUserCounts;
import com.scwvg.entitys.FluxCounts;
import com.scwvg.entitys.Msg;
import com.scwvg.entitys.scwvgponnetwork.WvgSpecType;
import com.scwvg.entitys.scwvgponnetwork.WvgVendor;

import java.util.List;
import java.util.Map;

/**
 * @aothor: lul
 * @unit: 智谷园网络科技有限公司
 * @iphone:18482297774
 * @date：20192019/5/21
 * @Explain：公告查询类接口
 **/
public interface WvgPublicService {
    /**
     * 专业查询
     * @return
     */
    Page<WvgSpecType> querySpecAll(Map<String,Object> params,Page<WvgSpecType> page);

    /**
     * 专业新增
     * @param specType
     * @return
     */
    Msg saveSpec(WvgSpecType specType);

    /**
     * 专业修改
     * @param specType
     * @return
     */
    Msg editSpec(WvgSpecType specType);

    /**
     * 专业删除
     * @param spec_id
     * @return
     */
    Msg delSpec(Long spec_id);

    /**
     * 厂家管理
     * @param params
     * @param page
     * @return
     */
    Page<WvgVendor> queryVendorAll(Map<String,Object> params, Page<WvgSpecType> page);

    /**
     * 厂家新增
     * @param vendor
     * @return
     */
    Msg saveVendor(WvgVendor vendor);

    /**
     * 厂家修改
     * @param vendor
     * @return
     */
    Msg editVendor(WvgVendor vendor);

    /**
     * 厂家删除
     * @param res_vendor_id
     * @return
     */
    Msg delVendor(Long res_vendor_id);

    /**
     * 首页厂家查询
     * @return
     */
    List<WvgVendor> queryMainVendors();

    /**
     * 首页【今日用户增减情况】
     * @return
     * @param page
     */
    Page<BandUserCounts> queryBandUsers(Page<BandUserCounts> page);

    /**
     * 首页【今日告警量】
     * @return
     * @param page
     */
    Page<AlarmCounts> queryAlarms(Page<AlarmCounts> page);

    /*首页【今日流量拥塞量】*/
    Page<FluxCounts> queryFluxs(Page<FluxCounts> page);
}
