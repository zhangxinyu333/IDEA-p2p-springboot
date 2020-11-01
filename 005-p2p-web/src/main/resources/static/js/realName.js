
//同意实名认证协议
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});

	$("#realName").on("blur",function () {
		var realName=$.trim($("#realName").val());
		if ("" == realName) {
			showError("realName","请输入真实姓名");
		}else if (!/^[\u4e00-\u9fa5]{0,}$/.test(realName)) {
			showError("realName","真实姓名只支持中文");
		}else {
			showSuccess("realName");
		}
	});

	$("#idCard").on("blur",function () {
        var idCard = $.trim($("#idCard").val());
        if ("" == idCard) {
            showError("idCard","请输入身份证号码");
        }else if (!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard)) {
            showError("idCard","请输入正确的身份证号码");
        }else {
            showSuccess("idCard");
        }
    });


    $("#messageCodeBtn").on("click", function () {

        //判断是否倒计时
        if (!$("#messageCodeBtn").hasClass("on")) {
            //隐藏短信验证码错误提示
            hideError("messageCode");

            //判断其他元素是否通过验证
            //触发失去焦点的事件
            $("#realName").blur();
            $("#idCard").blur();

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
    
    $("#btnRegist").on("click",function () {
        $("#realName").blur();
        $("#idCard").blur();
        $("#messageCode").blur();

        var errorTexts=$("div[id$='Err']").text();
        if ("" == errorTexts) {

            var realName=$.trim($("#realName").val());
            var idCard =$.trim($("#idCard").val());
            var messageCode=$.trim($("#messageCode").val());

            $.ajax({
                url:contextPath+"/loan/realName",
                type:"post",
                data:{
                    realName:realName,
                    idCard:idCard,
                    messageCode:messageCode
                },
                success:function (data) {
                    if (data.code == 1) {
                        window.location.href = contextPath+"/loan/myCenter";
                    }else {
                        showError("messageCode",data.message);
                    }
                },
                error:function () {
                    showError("messageCode","认证平台异常，请稍后重试");
                }
            });
        }

    })
});
//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}

//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}