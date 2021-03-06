package com.scwvg.entitys;

import lombok.Data;

/**
 * @aothor: lul
 * @unit: 智谷园网络科技有限公司
 * @iphone:18482297774
 * @date：20192019/5/23
 * @Explain：
 **/
@Data
public class FluxCounts extends Console {
    /*private Long flux_up_mubers;      //上行端口拥塞数
    private Long flux_up_affect_muber; //影响用户数
    private Long flux_down_mubers;//下线端口拥塞数
    private Long flux_down_affect_mubers;//影响用户数
    private String flux_device_ids;//设备ID集合*/

    private Long flux_olt_mubers; //流量拥塞OLT数量
    private Long flux_pon_mubers;//PON口数量
    private Long affect_mubers;//影响用户数
}
