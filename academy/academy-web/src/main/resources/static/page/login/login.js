layui.use(['form','layer','jquery'],function(){

    if (top != window){
        top.location.href = window.location.href;

    }

    var $ = layui.jquery;

    $("#validateImg").attr("src",captcha.code);

    //监听提交
    var form = layui.form;
    form.on("submit(login)",function(data){
        var field = data.field;
        field.key = captcha.key;
        $.ajax({
            url: "/login/login",
            type: 'POST',
            cache: false,
            processData: false,
            dataType:"json",
            contentType: "application/json;charset=utf-8",
            data:JSON.stringify(field)
        }).done(function(result) {
            console.log(result);
            if(result.code == 0){
                var url = result.data.url;
                window.location.href=url;
            }else{
                parent.layer.msg(result.msg, {time: 3000}, function () {
                    //重新加载父页面
                    parent.location.reload();
                });
            }
        });
        return false;
    })

    //表单输入效果
    $(".loginBody .input-item").click(function(e){
        e.stopPropagation();
        $(this).addClass("layui-input-focus").find(".layui-input").focus();
    })
    $(".loginBody .layui-form-item .layui-input").focus(function(){
        $(this).parent().addClass("layui-input-focus");
    })
    $(".loginBody .layui-form-item .layui-input").blur(function(){
        $(this).parent().removeClass("layui-input-focus");
        if($(this).val() != ''){
            $(this).parent().addClass("layui-input-active");
        }else{
            $(this).parent().removeClass("layui-input-active");
        }
    })
})


