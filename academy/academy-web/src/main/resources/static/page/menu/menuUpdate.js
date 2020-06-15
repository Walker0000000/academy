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
        elem: '#parentCode',
        // 数据
        data: '/menu/getSysMenuTreeList',
        // 异步加载方式：get/post，默认get
        type: 'get',
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
            $("#parentCode").val(d.current.menu_code);
        },
        // 加载完成后的回调函数
        success: function (d) {
            // console.log(menuData);
            //选中节点，根据id筛选
            treeSelect.checkNode('parentCode', menuData.parentId);
            //获取zTree对象，可以调用zTree方法
            var treeObj = treeSelect.zTree('parentCode');
            //刷新树结构
            treeSelect.refresh('parentCode');
        }
    });

    loadFrom();

    //监听提交
    var form = layui.form;
    form.on('submit(menuEditSubmit)', function(data){
        var field = data.field;
        $.ajax({
            url: "/menu/updateMenu",
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
    layui.form.val("dataFrm", menuData);
}




