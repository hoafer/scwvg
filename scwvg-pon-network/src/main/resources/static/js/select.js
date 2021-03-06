/**
 * @aothor: lul
 * @unit: 智谷园网络科技有限公司
 * @iphone:18482297774
 * @date：20192019/5/12
 * @Explain：下拉框公共类查询
 **/
layui.config({
    base: '/' //静态资源所在路径
}).extend({
    index: 'lib/index' //主入口模块
}).use(['layer', 'form', 'element'], function(){
    var layer = layui.layer
        ,form = layui.form
        ,element = layui.element

    function renderSelect() {
        //重新渲染 不然无法显示
        layui.form.render("select");
    }

var token=localStorage.getItem("token");
initSelectUserId(); //用户身份类型
initSelectSpec();
initSelectCity();
initSelectRole();
//父级菜单
initParentMenus();
//厂家查询
initVendors();

//指令添加下啦列表
initCardType();
initCmdVendor();
queryProtocol();
initCmdSpec();
initCmdCardType();


function initSelectUserId() {
    $.ajax({
        type:"get",
        url:"/selects/getUserTypeAll?token"+token,
        success:function (data) {
            var userID=$("#userid");
            $.each(data,function (i,item) {
                userID.append("<option value="+item.user_type_id+">"+item.user_type_name+"</option>");
            })
            renderSelect();
        },
    });
}
function initSelectSpec() {
    $.ajax({
        type:"get",
        url:"/selects/getUserSpec?token"+token,
        success:function (data) {
            var userID=$("#userspec");
            $.each(data,function (i,item) {
                if(item.spec_id !=1){
                    userID.append("<option value="+item.spec_id+">"+item.spec_name+"</option>");
                }
            })
            renderSelect();
        },
    });
}
function initSelectCity() {
    $.ajax({
        type:"get",
        url:"/selects/getUserCity?token"+token,
        success:function (data) {
            var userID=$("#usercity");
            $.each(data,function (i,item) {
                if(item.spec_id !=1){
                    userID.append("<option value="+item.city_id+">"+item.city_name+"</option>");
                }
            })
            renderSelect();
        },
    });
}

function initSelectRole(){
    $.ajax({
        type:"get",
        url:"/selects/getUserRole?token"+token,
        success:function (data) {
            var userID=$("#userrole");
            $.each(data,function (i,item) {
                if(item.spec_id !=1){
                    userID.append("<option value="+item.wvg_role_id+">"+item.wvg_role_description+"</option>");
                }
            })
            renderSelect();
        },
    });
}



function initParentMenus() {
    $.ajax({
        type:"get",
        url:"/selects/getParentMenus?token"+token,
        success:function (data) {
            var wvgparent=$("#menuParent");
            $.each(data,function (i,item) {
                if(item.wvg_menu_id !=1) {
                    wvgparent.append("<option value=" + item.wvg_menu_id + ">" + item.wvg_menu_name + "</option>");
                }
            })
            renderSelect();
        },
    });
}


    function initVendors() {
        $.ajax({
            type:"get",
            url:"/selects/getinitVendors?token"+token,
            success:function (data) {
                var vendorName=$("#vendorName");
                $.each(data,function (i,item) {
                        vendorName.append("<option value=" + item.res_parent_id + ">" + item.res_vendor_name + "</option>");
                })
                renderSelect();
            },
        });
    }

    //指令添加板卡类型
    function initCardType() {
        $.ajax({
            type:"get",
            url:"/selects/getCardType?token"+token,
            success:function (data) {
                var cmd_card_type=$("#cmd_card_type");
                $.each(data,function (i,item) {
                    cmd_card_type.append("<option value=" + item.card_type_id + ">" + item.card_type_name + "</option>");
                })
                renderSelect();
            },
        });
    }

    function initCmdVendor() {
        $.ajax({
            type:"get",
            url:"/selects/getinitVendors?token"+token,
            success:function (data) {
                var res_vendor_id=$("#res_vendor_id");
                $.each(data,function (i,item) {
                    res_vendor_id.append("<option value=" + item.res_parent_id + ">" + item.res_vendor_name + "</option>");
                })
                renderSelect();
            },
        });
    }

    function queryProtocol() {
        $.ajax({
            type:"get",
            url: "/selects/queryProtocol?token" + token,
            success: function (data) {
                var cmd_protocol_id = $("#cmd_protocol_id");
                $.each(data, function (i, item) {
                    cmd_protocol_id.append("<option value=" + item.cmd_type_id + ">" + item.cmd_type_name + "</option>");
                })
                renderSelect();
            },
        })
    }
    function initCmdSpec() {
        $.ajax({
            type:"get",
            url:"/selects/getUserSpec?token"+token,
            success:function (data) {
                var spec_id=$("#spec_id");
                $.each(data,function (i,item) {
                    if(item.spec_id !=1){
                        spec_id.append("<option value="+item.spec_id+">"+item.spec_name+"</option>");
                    }
                })
                renderSelect();
            },
        });
    }
    function initCmdCardType() {
        $.ajax({
            type:"get",
            url: "/selects/queryCardType?token" + token,
            success: function (data) {
                var cmd_card_type = $("#cmd_card_type");
                $.each(data, function (i, item) {
                    cmd_card_type.append("<option value=" + item.card_type_id + ">" + item.card_type_name + "</option>");
                })
                renderSelect();
            },
        })
    }
});

