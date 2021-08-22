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
var num = 60;
var showTimeInterval;
//注册协议确认
$(function() {
	$("#agree").click(function(){
		agree()
	});

	$('#phone').blur(function () {
		checkPhone()
	})

	
	$('#loginPassword').blur(function () {
		checkPassword()
	})


	$('#messageCodeBtn').click(function () {
		var phone  = $('#phone').val();
		if (/^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/.test(phone)) {
			$.ajax({
				url:"/lcb/sendSmsCode",
				type:"get",
				data:{phone:phone},
				success:function (data) {
					if (data == "success"){
						num = 5;
						showTimeInterval = setInterval(display,1000)
					} else {
						alert(data)
					}
				}
			})
		}else if (phone == ""){
			showError("phone","手机号码不能为空")
		} else {
			showError("phone","请输入正确的手机号")
		}
	})

	$('#agreeBtn').click(function () {
		closeBox('mask','agreement')
		$('#agree').prop("checked","true")
		agree()
	})

	$('#disagreeBtn').click(function () {
		closeBox('mask','agreement')
	})

	$('#messageCode').blur(function () {
		checkMessageCode()
	})

	$('#btnRegist').click(function () {
		var phoneErr = $('#phoneErr').html()
		var loginPasswordErr = $('#loginPasswordErr').html()
		var messageCodeErr = $('#messageCodeErr').html()
		if (phoneErr != ""){
			checkPhone()
		} else if (loginPasswordErr != ""){
			checkPassword()
		}else if (messageCodeErr != ""){
			checkMessageCode()
		}else {
			var phone  = $('#phone').val();
			var loginPassword = $('#loginPassword').val();
			$.ajax({
				url:"/lcb/register",
				type:"post",
				data:{phone:phone,loginPassword:loginPassword},
				success:function (data) {
					if (data == true){
						alert("注册成功")
						window.location.href="/lcb/realName";
					} else {
						alert("系统繁忙，请稍后重试")
					}
				}
			})
		}
	})
});
function checkMessageCode() {
	var phone  = $('#phone').val();
	var code = $('#messageCode').val();
	if (code == ""){
		showError("messageCode","请输入验证码");
	} else {
		$.ajax({
			url:"/lcb/checkMessageCode",
			type:"post",
			async: false,
			data:{phone:phone,code:code},
			success:function (data) {
				if (data == false){
					showError("messageCode","验证码错误");
				}else {
					showSuccess("messageCode")
					hideError("messageCode")
				}
			}
		})
	}
}

function agree() {
	var ischeck = document.getElementById("agree").checked;
	if (ischeck) {
		$("#btnRegist").attr("disabled", false);
		$("#btnRegist").removeClass("fail");
	} else {
		$("#btnRegist").attr("disabled","disabled");
		$("#btnRegist").addClass("fail");
	}
}

function checkPassword() {
	var password = $('#loginPassword').val()
	if (/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/.test(password)){
		showSuccess("loginPassword")
		hideError("loginPassword")
	}else if(password == ""){
		showError("loginPassword","密码不能为空")
	}else{
		showError("loginPassword","密码长度为6-20位，必须包含英文和数字")
	}
}

function checkPhone() {
	var phone  = $('#phone').val();
	if (/^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/.test(phone)) {
		$.ajax({
			url:"/lcb/checkPhone",
			type:"post",
			data:{phone:phone},
			success:function (data) {
				if (data == true){
					showSuccess("phone")
					hideError("phone")
				} else {
					showError("phone","该手机号已注册")
				}
			}
		})
	}else if(phone == ""){
		showError("phone","手机号码不能为空")
	}else {
		showError("phone","请输入正确的手机号")
	}
}

function display() {
	num--;
	if (num > 0){
		$('#messageCodeBtn').html(num + "秒")
		$('#messageCodeBtn').prop("disabled","true")
		$('#messageCodeBtn').css("background-color","gray")
	}else {
		$('#messageCodeBtn').html("获取验证码")
		$('#messageCodeBtn')[0].removeAttribute("disabled")
		$('#messageCodeBtn').css("background-color","rgb(65,133,244)")
		clearInterval(showTimeInterval);
	}
}


