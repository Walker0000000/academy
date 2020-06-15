layui.config({
    base: "/js/"
});
layui.use(['jquery', 'layer', 'form'], function () {

    var $ = layui.jquery;

    $(function () {
        initCombobox();
    });

    loadFrom();

    //监听提交
    var form = layui.form;
    form.on('submit(courseEditSubmit)', function (data) {
        var field = data.field;
        $.ajax({
            url: "/course/updateCourse",
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
                $("#classCode").val(courseData.classCode);
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
                $("#labelCode").val(courseData.labelCode);
                layui.form.render("select");
            }, error: function () {
                alert("初始化课程标签下拉框失败")
            }
        })
    }

});

function loadFrom() {
    //表单赋值
    layui.form.val("dataFrm", courseData);
}




