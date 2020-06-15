layui.use(['jquery', 'table', 'layer', 'form'], function () {

    var table = layui.table;

    var form = layui.form;

    var $ = layui.jquery;

    var data = {};

    //第一个实例
    table.render({
        elem: '#courseList',
        height: "full-120",
        id: 'courseReload',
        url: '/course/getCourseList', //数据接口
        method: 'GET',
        page: true, //开启分页,
        cols: [
            [ //表头
                {type: 'radio', field: 'id', title: 'ID', width: '5%', sort: true},
                {field: 'projectName', title: '项目名称', width: '10%'},
                {field: 'brandName', title: '品牌名称', width: '10%', sort: true},
                {field: 'className', title: '分类', width: '10%'},
                {field: 'courseName', title: '课程名称', width: '10%', sort: true},
                {field: 'labelName', title: '课程标签', width: '10%', sort: true},
                {field: 'ages', title: '适学年龄', width: '10%', sort: true},
                {field: 'classHour', title: '课时', width: '5%'},
                {field: 'classTime', title: '课次', width: '5%'},
                {field: 'statusStr', title: '状态', width: '5%'},
                {field: 'createUserName', title: '创建人', width: '10%', sort: true},
                {field: 'createTime', title: '创建时间', width: '15%', sort: true},
                {fixed: 'right', title: '操作', toolbar: '#barDemo', width: '15%'}
            ]
        ]
    });

    $(function () {
        initCombobox();
    });

    var $ = layui.$, active = {
        reload: function () {
            let courseName = $('#courseName');
            let classCode = $('#classCode');
            let labelCode = $('#labelCode');
            let status = $('#status');
            //执行重载
            table.reload('courseReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    "courseName": courseName.val(),
                    "classCode": classCode.val(),
                    "labelCode": labelCode.val(),
                    "status": status.val()
                }
            }, 'data');
        }
    };


    $('#courseTable').on('click', function () {
        let type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    //监听行工具事件
    table.on('radio(courseList)', function (obj) {
        data = obj.data; //获得当前行数据
    });

    $("#add").on("click", function () {
        openAddCourse();
    });

    //监听行工具事件
    table.on('tool(courseList)', function (obj) {
        var data = obj.data;
        var id = data.id;
        if (obj.event === 'set') {
            setChapterOpen(id);
        } else if (obj.event === 'edit') {
            editOpen(id);
        }
    });

    //打开添加页面
    function openAddCourse() {
        layer.open({
            type: 2,
            area: ['35%', '80%'],
            title: '编辑课程',
            shadeClose: true,
            content: ['/course/gotoCourseInsertPage'],
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

    function editOpen(id) {
        layer.open({
            type: 2,
            area: ['35%', '80%'],
            title: '编辑课程',
            shadeClose: true,
            content: ['/course/gotoCourseUpdatePage?id=' + id],
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

    function setChapterOpen(id) {
        layer.open({
            type: 2,
            area: ['100%', '100%'],
            title: '章节设置',
            shadeClose: true,
            content: ['/chapter/gotoCourseChapterPage?id=' + id],
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

    //删除课程
    $("#del").on("click", function () {
        if (data == null || JSON.stringify(data) === '{}') {
            layer.msg("请选择要删除的课程");
            return;
        }

        layer.confirm('确认删除该数据？', function (index) {
            let id = data.id;
            updateCourseStatus(id, 0);
            layer.close(index);
        });

    });

    //下架课程
    $("#soldOut").on("click", function () {
        if (data == null || JSON.stringify(data) === '{}') {
            layer.msg("请选择要下架的课程");
            return;
        }

        layer.confirm('确认下架该课程？', function (index) {
            let id = data.id;
            updateCourseStatus(id, 6);
            layer.close(index);
        });

    });

    //提交审核
    $("#audit").on("click", function () {
        if (data == null || JSON.stringify(data) === '{}') {
            layer.msg("请选择要提交审核的课程");
            return;
        }

        layer.confirm('确认提交审核该课程？', function (index) {
            let id = data.id;
            updateCourseStatus(id, 2);
            layer.close(index);
        });

    });

    function updateCourseStatus(id, status) {
        let params = {"id": id, "status": status};
        $.ajax({
            url: "/course/updateCourseStatus",
            type: 'POST',
            cache: false,
            processData: false,
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(params)
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

    //初始化下拉框
    function initCombobox() {
        initClassCombobox();
        initLabelCombobox();
    }

    //初始化课程分类下拉框
    function initClassCombobox() {
        $.ajax({
            type: "GET",
            url: "/class/getClassCombobox",
            success: function (res) {
                $.each(res.data, function (index, item) {
                    $("#classCode").append(new Option(item.name, item.code));
                });
                layui.form.render("select");
            }, error: function () {
                alert("初始化课程分类下拉框失败")
            }
        })
    }

    //初始化课程标签下拉框
    function initLabelCombobox() {
        $.ajax({
            type: "GET",
            url: "/label/getLabelCombobox",
            success: function (res) {
                $.each(res.data, function (index, item) {
                    $("#labelCode").append(new Option(item.name, item.code));
                });
                layui.form.render("select");
            }, error: function () {
                alert("初始化课程标签下拉框失败")
            }
        })
    }

});




