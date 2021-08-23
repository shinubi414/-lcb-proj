
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

	$('#phone').blur(function () {
		checkPhone()
	})

	$('#realName').blur(function () {
		checkName()
	})

	$('#idCard').blur(function () {
		checkIdCard()
	})

	$('#captcha').blur(function () {
		checkCode()
	})

	$('#btnRegist').click(function () {
		if (checkPhone() && checkName() && checkIdCard() && checkCode()){
			var phone = $('#phone').val();
			var realName = $('#realName').val();
			var idCard = $('#idCard').val();
			var captcha = $('#captcha').val()
			$.ajax({
				url:"/lcb/doRealName",
				type:"post",
				data:{phone:phone,realName:realName,idCard:idCard,captcha:captcha},
				success:function (data) {
					if (data != "ok"){
						alert(data)
					} else {
						window.location.href="/lcb/loan/page/login"
					}
				}
			})
		}
	})
	
});
function checkPhone() {
	var phone = $('#phone').val()
	if (phone == ""  || /^[ ]*$/.test(phone)) {
		showError("phone","手机号不能为空")
		return false;
	}else if(!/^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/.test(phone)){
		showError("phone","请输入正确的手机号")
		return false;
	}else {
		hideError("phone")
		return true;
	}

}

function checkName() {
	var realName = $('#realName').val()
	if (realName == "" || /^[ ]*$/.test(realName)){
		showError("realName","姓名不能为空")
		return false;
	}else if(!/[\u4e00-\u9fa5]/.test(realName)){
		showError("realName","姓名仅支持中文")
		return false;
	}else {
		hideError("realName")
		return true;
	}
}

function checkIdCard() {
	var idCard = $('#idCard').val()
	if (idCard == "" || /^[ ]*$/.test(idCard)){
		showError("idCard","身份证号不能为空")
		return false;
	}else if (!/(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}[0-9Xx]$)/.test(idCard)){
		showError("idCard","身份证格式不合法")
		return false;
	}else {
		hideError("idCard")
		return true;
	}
}
function checkCode() {
	var captcha = $('#captcha').val()
	if (captcha == "" ||  /^[ ]*$/.test(captcha)){
		showError("captcha","验证码不能为空")
		return false;
	}else {
		hideError("captcha")
		return true;
	}
}


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