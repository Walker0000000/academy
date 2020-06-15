layui.extend({
    dtree: '../layui_ext/dtree/dtree'   // {/}的意思即代表采用自有路径，即不跟随 base 路径
}).use(['dtree', 'util', 'jquery', 'layer', 'form', 'layedit', 'upload'], function () {

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
        toolbarShow: ["delete"],
        data: []
    });

    /*加载树*/
    loadContentDtree();

    dtree.on("node('chapterTree')", function (obj) {
        var data = obj.param;
        console.log(obj);
        level = data.level;
        //点击父节点，清空右侧信息，进行新增
        if (level == 1) {
            $("#id").val("");

            //点击子节点，查询章节信息，进行修改
        } else if (level == 2) {

        }
    });

    var upload = layui.upload;

    //普通图片上传
    var uploadInst = upload.render({
        elem: '#uploadImage',
        dataType: 'jsonp',  // 处理Ajax跨域问题
        url: '/fileResource/upload?folder=a3', //改成您自己的上传接口
        size: 1024 * 2,//限制上传图片大小 单位KB
        before: function (obj) {
            //预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                $('#picturesContent').append('<div style="float: left;text-align: center;height: 100px;"><img src="' + result + '" alt="' + file.name + '" class="layui-upload-img" style="width: 80px; margin:10px;cursor:pointer;"><p>' + file.name + '</p><a class="layui-btn layui-btn-xs" onclick=' + reloadImageUrls(this) + '>删除</a></div>')
            });
        },
        done: function (res) {
            // //如果上传失败
            if (res.result == 200) {
                $("#imageUrls").val($("#imageUrls").val() + res.allPath + ";");
                console.log($("#imageUrls").val());
            } else {
                return layer.msg('上传失败');
            }
            //上传成功
        },
        error: function () {
            //演示失败状态，并实现重传
            var demoText = $('#demoText');
            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
            demoText.find('.demo-reload').on('click', function () {
                uploadInst.upload();
            });
        }
    });

    //视频上传
    var uploadVadio = upload.render({
        elem: '#uploadVadio',
        dataType: 'jsonp',  // 处理Ajax跨域问题
        url: '/fileResource/upload?folder=a3', //改成您自己的上传接口
        accept: 'video',//视频
        before: function (obj) {
            //预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                $('#vadioContent').append('<div style="float: left;text-align: center;"><img src="../../images/userface1.jpg" alt="' + file.name + '" class="layui-upload-img" style="width: 80px; margin:10px;cursor:pointer;"><p>' + file.name + '</p></div>')
            });
        },

        done: function (res) {
            // //如果上传失败
            if (res.result == 200) {
                $("#vadioUrls").val($("#vadioUrls").val() + res.allPath + ";");
            } else {
                return layer.msg('上传失败');
            }
            //上传成功
        },
        error: function () {
            //演示失败状态，并实现重传
            var demoText = $('#demoText');
            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
            demoText.find('.demo-reload').on('click', function () {
                uploadVadio.upload();
            });
        }
    });

    //音频上传
    var uploadAudio = upload.render({
        elem: '#uploadAudio',
        dataType: 'jsonp',  // 处理Ajax跨域问题
        url: '/fileResource/upload?folder=a3', //改成您自己的上传接口
        accept: 'audio',//视频
        before: function (obj) {
            //预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                $('#audioContent').append('<div style="float: left;text-align: center;"><img src="../../images/userface1.jpg" alt="' + file.name + '" class="layui-upload-img" style="width: 80px; margin:10px;cursor:pointer;"><p>' + file.name + '</p></div>')
            });
        },

        done: function (res) {
            // //如果上传失败
            if (res.result == 200) {
                $("#audioUrls").val($("#audioUrls").val() + res.allPath + ";");
            } else {
                return layer.msg('上传失败');
            }
            //上传成功
        },
        error: function () {
            //演示失败状态，并实现重传
            var demoText = $('#demoText');
            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
            demoText.find('.demo-reload').on('click', function () {
                uploadAudio.upload();
            });
        }
    });

    //文件上传
    var uploadFile = upload.render({
        elem: '#uploadFile',
        dataType: 'jsonp',  // 处理Ajax跨域问题
        url: '/fileResource/upload?folder=a3', //改成您自己的上传接口
        accept: 'file',//视频
        before: function (obj) {
            //预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                $('#fileContent').append('<div style="float: left;text-align: center;"><img src="../../images/userface1.jpg" alt="' + file.name + '" class="layui-upload-img" style="width: 80px; margin:10px;cursor:pointer;"><p>' + file.name + '</p></div>')
            });
        },

        done: function (res) {
            // //如果上传失败
            if (res.result == 200) {
                $("#fileUrls").val($("#fileUrls").val() + res.allPath + ";");
            } else {
                return layer.msg('上传失败');
            }
            //上传成功
        },
        error: function () {
            //演示失败状态，并实现重传
            var demoText = $('#demoText');
            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
            demoText.find('.demo-reload').on('click', function () {
                uploaFile.upload();
            });
        }
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

    function reloadImageUrls(imageUrl) {
        var imageUrls = $("#imageUrls").val();
        imageUrls = imageUrls.replace(imageUrl + ";", "");
        $("#imageUrls").val(imageUrls);
    }

});





