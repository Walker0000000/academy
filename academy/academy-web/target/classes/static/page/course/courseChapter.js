layui.extend({
    dtree: '../layui_ext/dtree/dtree'   // {/}的意思即代表采用自有路径，即不跟随 base 路径
}).use(['dtree', 'util', 'jquery', 'layer', 'form', 'layedit'], function () {

    var $ = layui.jquery;
    var dtree = layui.dtree
    var layedit = layui.layedit;
    var form = layui.form; //layui 的 form 模块是必须的

    layedit.build('chapterBody');
    layedit.build('teachingPlanBody');

    var chapterTree = dtree.render({
        elem: '#chapterTree',
        method: 'POST',
        toolbar: true,
        toolbarShow: ["add", "delete"],
        data: []
    });

    /*加载树*/
    loadContentDtree();

    dtree.on("node('chapterTree')", function (obj) {
        var data = obj.param;
        console.log(obj);
        level = data.level;
        var context = data.context;
        var nodeId = data.nodeId;
    });

    //监听提交
    form.on('submit(menuSubmit)', function (data) {

        var checkMenus = [];
        var menuData = chapterTree.GetAllCheckBox();
        menuData.forEach(function (value) {
            // console.log(value);
            //console.log(value.value+'--'+value.checked);
            if (value.checked) {
                checkMenus.push(value.value);
            }
        });
        console.log(checkMenus);

        var field = data.field;
        field.checkMenus = checkMenus;
        $.ajax({
            url: "/role/updateRole",
            type: 'POST',
            cache: false,
            processData: false,
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(field)
        }).done(function (result) {
            console.log(result);
            if (result.code == 0) {
                parent.layer.msg("操作成功!", {time: 1000}, function () {
                    //重新加载父页面
                    parent.location.reload();
                });
                return;
            } else {
                layer.alert(result.msg, function () {
                    layer.closeAll();//关闭所有弹框
                });
            }
        });
        return false;
    });

    /*加载检查内容树结构*/
    function loadContentDtree() {
        $.ajax({
            url: '/chapter/getChapterTree?id=' + 1,
            type: 'GET',
            cache: false,
            processData: false,
            dataType: "json",
            contentType: "application/json;charset=utf-8",

        }).done(function (result) {
            console.log(result)
            if (result.code == 0) {
                dtree.render({
                    elem: '#chapterTree',
                    method: 'GET',
                    toolbar: true,
                    data: result.data.chapterList
                });
            } else {

            }
        });
        return false;
    }

});





