var referrer = "";//登录后返回页面
referrer = document.referrer;
if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
			referrer = window.opener.location.href;
		}  
	} catch (e) {
	}
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


$(function () {

	//按键盘Enter键即可登录
	$(document).keyup(function(event){
		if(event.keyCode == 13){
			login();
		}
	});


	$('#login').click(function () {
		var phone = $('#phone').val()
		var password = $('#loginPassword').val()
		var captcha = $('#captcha').val()

		if (/^[ ]*$/.test(phone)) {
			showError("phone","请输入手机号")
			return;
		}else if (!/^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/.test(phone)) {
			showError("phone","请输入正确手机号")
			return;
		}
		hideError("phone")


		if (password == ""){
			showError("password","请输入密码")
			return;
		}
		hideError("password")

		if (/^[ ]*$/.test(captcha)){
			showError("captcha","请输入验证码")
			return;
		}
		hideError("captcha")

		$.ajax({
			url:"/lcb/loan/page/doLogin",
			type:"post",
			data:{phone:phone,password:password,captcha:captcha},
			success:function (data) {
				if (data != ""){
					$('#loginMsg').html(data)
				} else {
					window.location.href="/lcb/index"
				}
			}
		})
	})

	$('#phone').blur(function () {
		checkPhone()
	})

	$('#loginPassword').blur(function () {
		checkPassword()
	})

	$('#captcha').blur(function () {
		checkCaptcha()
	})
})

function checkPhone() {
	var phone = $('#phone').val()
	if (/^[ ]*$/.test(phone)) {
		showError("phone","请输入手机号")
	}else if (!/^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/.test(phone)) {
		showError("phone","请输入正确手机号")
	}else {
		hideError("phone")
	}
}

function checkPassword() {
	var password = $('#loginPassword').val()
	if (password == ""){
		showError("password","请输入密码")
	}else {
		hideError("password")
	}
}

function checkCaptcha() {
	var captcha = $('#captcha').val()
	if (/^[ ]*$/.test(captcha)){
		showError("captcha","请输入验证码")
	}else {
		hideError("captcha")
	}

}


