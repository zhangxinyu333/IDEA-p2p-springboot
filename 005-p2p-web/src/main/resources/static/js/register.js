//错误提示
function showError(id, msg) {
    $("#" + id + "Ok").hide();
    $("#" + id + "Err").html("<i></i><p>" + msg + "</p>");
    $("#" + id + "Err").show();
    $("#" + id).addClass("input-red");
}

//错误隐藏
function hideError(id) {
    $("#" + id + "Err").hide();
    $("#" + id + "Ok").hide();
    $("#" + id + "Err").html("");
    $("#" + id).removeClass("input-red");
}

//显示成功
function showSuccess(id) {
    $("#" + id + "Err").hide();
    $("#" + id + "Err").html("");
    $("#" + id + "Ok").show();
    $("#" + id).removeClass("input-red");
}


//打开注册协议弹层
function alertBox(maskid, bosid) {
    $("#" + maskid).show();
    $("#" + bosid).show();
}

//关闭注册协议弹层
function closeBox(maskid, bosid) {
    $("#" + maskid).hide();
    $("#" + bosid).hide();
}

//注册协议确认
$(function () {
    $("#agree").click(function () {
        var ischeck = document.getElementById("agree").checked;
        if (ischeck) {
            $("#btnRegist").attr("disabled", false);
            $("#btnRegist").removeClass("fail");
        } else {
            $("#btnRegist").attr("disabled", "disabled");
            $("#btnRegist").addClass("fail");
        }
    });

    //验证手机号码
    $("#phone").on("blur", function () {
        var phone = $.trim($("#phone").val());

        if ("" == phone) {
            showError("phone", "请输入手机号码");
        } else if (!/^1[1-9]\d{9}$/.test(phone)) {
            showError("phone", "请输入正确的手机号码")
        } else {
            $.ajax({
                url: contextPath + "/loan/checkPhone",
                type: "get",
                data: "phone=" + phone,
                success: function (data) {
                    if (data.code == 1) {
                        showSuccess("phone");
                    } else {
                        showError("phone", data.message);
                    }
                },
                error: function () {
                    showError("phone", "系统繁忙请稍后重试");

                }
            });
        }
    });

    //验证密码
    $("#loginPassword").on("blur", function () {
        var loginPassword = $.trim($("#loginPassword").val());
        if ("" == loginPassword) {
            showError("loginPassword", "密码不能为空")
        } else if (!/^[0-9a-zA-Z]+$/.test(loginPassword)) {
            showError("loginPassword", "密码字符只可使用数字和大小写英文字母");
        } else if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)) {
            showError("loginPassword", "密码应同时包含英文和数字");
        } else if (loginPassword.length < 6 || loginPassword.length > 20) {
            showError("loginPassword", "密码长度为6~20位");
        } else {
            showSuccess("loginPassword");
        }
    })

    //注册
    $("#btnRegist").on("click", function () {
        //触发失去焦点事件
        $("#phone").blur();
        $("#loginPassword").blur();
        $("#messageCode").blur();

        /*var flag=true;
        //获取div标签id属性值以Err结尾的的div
        $("div[id$='Err']").each(function () {
            var errorTexts=$(this).text();
            if ("" != errorTexts) {
                flag=false;
                return;
            }
        });
        if (flag) {
            alert("发起注册请求");
        }*/
        var errorTexts = $("div[id$='Err']").text();
        var loginPassword = $.trim($("#loginPassword").val());
        var phone = $.trim($("#phone").val());
        var messageCode=$.trim($("#messageCode").val());
        if ("" == errorTexts) {
            $("#loginPassword").val($.md5(loginPassword));

            $.ajax({
                url: contextPath + "/loan/register",
                type: "post",
                data: {
                    phone: phone,
                    loginPassword: $.md5(loginPassword),
                    messageCode:messageCode
                },
                success: function (data) {
                    if (data.code == 1) {
                        window.location.href = contextPath + "/loan/page/realName";
                    } else {
                        showError("messageCode", data.message);
                        $("#loginPassword").val("");
                        $("#messageCode").val("");
                        hideError("loginPassword");
                    }
                },
                error: function () {
                    showError("messageCode", "系统繁忙，请稍后重试");
                }
            });
        }
    })

    $("#messageCodeBtn").on("click", function () {

        //判断是否倒计时
        if (!$("#messageCodeBtn").hasClass("on")) {
            //隐藏短信验证码错误提示
            hideError("messageCode");

            //判断其他元素是否通过验证
            //触发失去焦点的事件
            $("#phone").blur();
            $("#loginPassword").blur();

            var errorTexts=$("div[id$='Err']").text();
            var phone = $.trim($("#phone").val());
            if ("" == errorTexts){

                $.ajax({
                    url:contextPath+"/loan/messageCode",
                    type:"get",
                    data:"phone="+phone,
                    success:function (data) {
                        alert("您的短信验证码是:"+data.data);
                        if (data.code == 1) {
                            $.leftTime(60, function (d) {
                                //d.status,值true||false,倒计时是否结束;  true处在倒计时状态，false倒计时结束
                                //d.s,倒计时秒;


                                if (d.status) {
                                    $("#messageCodeBtn").addClass("on");
                                    $("#messageCodeBtn").html((d.s == "00" ? "60" : d.s) + "s后获取");
                                } else {
                                    $("#messageCodeBtn").removeClass("on");
                                    $("#messageCodeBtn").html("获取验证码");
                                }
                            });
                        }else {
                            showError("messageCode",data.message);
                        }
                    },
                    error:function () {
                        showError("messageCode","短信平台异常请稍后重试");
                    }
                });


            }

        }
    });
    $("#messageCode").on("blur",function () {
        var messageCode=$.trim($("#messageCode").val());
        if ("" == messageCode) {
            showError("messageCode","请输入短信验证码");
        }else {
            showSuccess("messageCode");
        }
    })
});
