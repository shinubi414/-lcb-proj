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
		var phone  = $('#phone').val();
		if (/^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/.test(phone)) {
			showSuccess("phone")
		}else {
			showError("phone","手机号码为11位数字")
		}
	})
	
	$('#loginPassword').blur(function () {
		var password = $('#loginPassword').val()
		if (/[\\da-zA-Z]{6,20}/.test(password)){
			showError("loginPassword","\"密码必须由6-20位英文和数字组成\"")
		}
	})


	$('#messageCodeBtn').click(function () {
		num = 5;
		showTimeInterval = setInterval(display,1000)
	})

});
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


