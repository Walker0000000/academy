layui.config({
    base: "/js/"
}).extend({
    treeSelect: "treeSelect"
});
layui.use(['treeSelect','jquery','layer','form'], function(){

    var $=layui.jquery;

    var treeSelect= layui.treeSelect;

    treeSelect.render({
        // 选择器
        elem: '#roleType',
        // 数据
        data: '/tree/getDictionaryTreeList?tableName=sc_sys_user&fieldName=user_type',
        // 异步加载方式：get/post，默认get
        type: 'GET',
        // 占位符
        placeholder: '请选择',
        // 是否开启搜索功能：true/false，默认false
        search: true,
        // 一些可定制的样式
        style: {
            folder: {
                enable: true
            },
            line: {
                enable: true
            }
        },
        // 点击回调
        click: function(d){
            console.log(d);
            $("#roleType").val(d.current.value);
        },
        // 加载完成后的回调函数
        success: function (d) {
            // 刷新树结构
            treeSelect.refresh('roleType');
        }
    });
    //下拉树结束

    //监听提交
    var form = layui.form;
    form.on('submit(insertSubmit)', function(data){

        var field = data.field;

        $.ajax({
            url: "/role/insertRole",
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




