layui.use(['tree', 'util','jquery','layer','form'], function(){

    var $=layui.jquery;

    var form = layui.form; //layui 的 form 模块是必须的

    //1、最基础的用法 - 直接绑定json
    var roleTree = new layuiXtree({
        elem: 'roleTree',   //(必填) 放置xtree的容器，样式参照 .xtree_contianer
        form: form,     //(必填) layui 的 from
        data: UserRoleData.userRoleList,    //(必填) json数据
        isopen: true,   //加载完毕后的展开状态，默认值：true
        ckall: true,    //启用全选功能，默认值：false
        ckallback: function () { }, //全选框状态改变后执行的回调函数
        icon: {        //三种图标样式，更改几个都可以，用的是layui的图标
            open: "&#xe62a;",       //节点打开的图标
            close: "&#xe62a;",     //节点关闭的图标
            end: "&#xe600;"      //末尾节点的图标
        },
        color: {       //三种图标颜色，独立配色，更改几个都可以
            open: "#EE9A00",        //节点图标打开的颜色
            close: "#EEC591",     //节点图标关闭的颜色
            end: "#828282"       //末级节点图标的颜色
        },
        click: function (data) {  //节点选中状态改变事件监听，全选框有自己的监听事件
            // console.log(data.elem); //得到checkbox原始DOM对象
            console.log(data.elem.checked); //开关是否开启，true或者false
            console.log(data.value); //开关value值，也可以通过data.elem.value得到
            // console.log(data.othis); //得到美化后的DOM对象
        }
    });


    loadFrom();

    //监听提交
    form.on('submit(userSubmit)', function(data){

        var checkRoles = [];
        var menuData = roleTree.GetAllCheckBox();
        menuData.forEach(function(value){
            // console.log(value);
            //console.log(value.value+'--'+value.checked);
            if(value.checked){
                checkRoles.push(value.value);
            }
        });
        console.log(checkRoles);

        var field = data.field;
        field.checkRoles=checkRoles;
        $.ajax({
            url: "/user/userAuthorize",
            type: 'POST',
            cache: false,
            processData: false,
            dataType:"json",
            contentType: "application/json;charset=utf-8",
            data:JSON.stringify(field)
        }).done(function(result) {
            console.log(result);
            if(result.code == 0){
                parent.layer.msg("操作成功!", {time: 1000}, function () {
                    //重新加载父页面
                    parent.location.reload();
                });
                return;
            }else{
                layer.alert(result.msg,function(){
                    layer.closeAll();//关闭所有弹框
                });
            }
        });
        return false;
    });

});

function loadFrom() {
    //表单赋值
    layui.form.val("dataFrm", UserRoleData);
}




