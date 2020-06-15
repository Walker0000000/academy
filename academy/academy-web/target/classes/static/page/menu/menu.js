layui.use(['jquery', 'table', 'layer', 'form'], function () {

    var table = layui.table;

    var $ = layui.jquery;

    var data = {};

    //第一个实例
    table.render({
        elem: '#menuList',
        height: "full-120",
        id: 'menuReload',
        url: '/menu/getMenuList', //数据接口
        method: 'GET',
        page: true, //开启分页,
        cols: [
            [ //表头
                {type: 'radio', field: 'id', title: 'ID', width: '5%', sort: true},
                {field: 'menuCode', title: '菜单编码', width: '10%'},
                {field: 'menuName', title: '菜单名称', width: '10%', sort: true},
                {field: 'parentName', title: '上级菜单', width: '10%'},
                {field: 'sort', title: '排序', width: '10%', sort: true},
                {field: 'icon', title: '图标', width: '5%'},
                {field: 'path', title: '路由地址', width: '15%'},
                {field: 'menuLevel', title: '菜单等级', width: '10%', sort: true},
                {field: 'createTime', title: '创建时间', width: '15%', sort: true}
            ]
        ]
    });

    var $ = layui.$, active = {
        reload: function () {
            var menuName = $('#menuName');
            //执行重载
            table.reload('menuReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    "menuName": menuName.val()
                }
            }, 'data');
        }
    };

    $('#menuTable').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    //监听行工具事件
    table.on('radio(menuList)', function (obj) {
        data = obj.data; //获得当前行数据
    });

    $("#add").on("click", function () {
        openAddUser();
    });

    //打开添加页面
    function openAddUser() {
        layer.open({
            type: 2,
            area: ['35%', '80%'],
            title: '编辑菜单',
            shadeClose: true,
            content: ['/menu/gotoMenuInsertPage'],
            btn: ['保存', '关闭'],//只是为了演示
            yes: function (index, addOpen) {
                layer.getChildFrame('body', index).find('button[lay-submit]').click()
                return false;
            },
            btn2: function () {
                layer.closeAll();
            }
        });
    }

    $("#edit").on("click", function () {
        if (data == null || JSON.stringify(data) === '{}') {
            layer.msg("请选择编辑菜单");
            return;
        }
        var id = data.id;
        editOpen(id);
    });

    function editOpen(id) {
        layer.open({
            type: 2,
            area: ['35%', '80%'],
            title: '编辑菜单',
            shadeClose: true,
            content: ['/menu/gotoMenuUpdatePage?id=' + id],
            btn: ['保存', '关闭'],//只是为了演示
            yes: function (index, addOpen) {
                layer.getChildFrame('body', index).find('button[lay-submit]').click()
                return false;
            },
            btn2: function () {
                layer.closeAll();
            }
        });
    }

    $("#del").on("click", function () {
        if (data == null || JSON.stringify(data) === '{}') {
            layer.msg("请选择删除菜单");
            return;
        }

        layer.confirm('确认删除该数据？', function (index) {
            var id = data.id;
            deleteMenu(id);
            layer.close(index);
        });

    });

    function deleteMenu(id) {
        var dataId = {"id": id};
        $.ajax({
            url: "/menu/deleteMenu",
            type: 'POST',
            cache: false,
            processData: false,
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(dataId)
        }).done(function (result) {
            if (result.code == 0) {
                parent.layer.msg("操作成功!", {time: 1000}, function () {
                    //重新加载父页面
                    location.reload();
                });
                return;
            } else {
                layer.alert(result.msg, function () {
                    layer.closeAll();//关闭所有弹框
                });
            }
        });
    }

});




