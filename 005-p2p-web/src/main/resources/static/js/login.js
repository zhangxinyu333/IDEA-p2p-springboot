$(function () {
	$.ajax({
		url:contextPath+"/loan/loadStat",
		 type:"get",
		success:function (data) {
			$(".historyAverageRate").html(data.historyAverageRate);
			$("#allUserCount").html(data.allUserCount);
			$("#allBidMoney").html(data.allBidMoney);
		}
	});

	$("#dateBtn1").on("click",function () {

		var phone = $.trim($("#phone").val());
		if ("" == phone) {
			$("#showId").html("请输入手机号码");
			return;
		} else if (!/^1[1-9]\d{9}$/.test(phone)) {
			$("#showId").html("请输入正确的手机号码")
			return;
		}else {
			$("#showId").html("");
		}

		var loginPassword=$.trim($("#loginPassword").val());
		if ("" == loginPassword) {
			$("#showId").html("请输入登录密码");
			return;
		}else {
			$("#showId").html("");
		}

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
							$("#dateBtn1").addClass("on");
							$("#dateBtn1").html((d.s == "00" ? "60" : d.s) + "s后获取");
						} else {
							$("#dateBtn1").removeClass("on");
							$("#dateBtn1").html("获取验证码");
						}
					});
				}else {
					$("#showId").html(data.message);
				}
			},
			error:function () {
				$("#showId").html("短信平台异常请稍后重试");
			}
		});

	});

	$("#loginBtn").on("click",function () {
		var phone = $.trim($("#phone").val());
		if ("" == phone) {
			$("#showId").html("请输入手机号码");
			return;
		} else if (!/^1[1-9]\d{9}$/.test(phone)) {
			$("#showId").html("请输入正确的手机号码")
			return;
		}else {
			$("#showId").html("");
		}

		var loginPassword=$.trim($("#loginPassword").val());
		if ("" == loginPassword) {
			$("#showId").html("请输入登录密码");
			return;
		}else {
			$("#showId").html("");
		}

		var messageCode=$.trim($("#messageCode").val());
		if ("" == messageCode) {
			$("#showId").html("请输入短信验证码");
			return;
		}else {
			$("#showId").html("");
		}


		$.ajax({
			url:contextPath+"/loan/login",
			type:"post",
			data:{
				phone:phone,
				loginPassword:$.md5(loginPassword),
				messageCode:messageCode
			},
			success:function (data) {
				if (data.code == 1) {
					var redirectUrl=$("#redirectUrl").val();
					window.location.href=redirectUrl;
				}else {
					$("#showId").html(data.message);
				}
			},
			error:function () {
				$("#showId").html("平台异常请稍后重试");
			}
		});
	})
});