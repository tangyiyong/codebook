$("#c").height(window.innerHeight - 50);

/**提示信息配置对象*/
toastr.options = {  
            closeButton: false,  
            debug: false,  
            progressBar: false,  
            positionClass: "toast-top-center",  
            onclick: null,  
            showDuration: "300",  
            hideDuration: "1000",  
            timeOut: "5000",  
            extendedTimeOut: "1000",  
            showEasing: "swing",  
            hideEasing: "linear",  
            showMethod: "fadeIn",  
            hideMethod: "fadeOut"  
        }; 

$(".getucode").click(function(){
	$(".hidecode").show();
});

$("#send").click(function(){
	var inu = $("#inu").val();
	var ine = $("#ine").val();
	
	if (inu == null || inu.length == 0 || ine == null || ine.length == 0){
		return;
	}
	
	var jgpattern =/^[a-zA-Z0-9_]{1,}$/;
	if(!jgpattern.test(inu)){
		toastr.warning("用户名只能是字母和数字和下划线组成");
		return;
	}
	
	var emailreg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
	if(!emailreg.test(ine)){
		toastr.warning("邮箱格式非法");
		return;
	}
	
	if (strlen(inu) < 6){
		toastr.warning("用户名太短");
	 	return;
	}
	
	if (strlen(inu) > 20){
		toastr.warning("用户名太长");
	 	return;
	}
	
	var l = layer.load(2);
	$.ajax({
        type: "post",
        url: basePath + '/mails/getucode',
        async: true, 
        data: {inu:inu,ine:ine},
        success: function(data) {
        	layer.close(l);
        	if (data == null || data.code != 200) {
        		layer.msg(data.msg);
        		return;
        	}
            $(".hidecode").hide();
            $("#u").val($("#inu").val());
            $("#uemail").val($("#ine").val());
            
            $("#inu").val("");
       	   	$("#ine").val("");
       	   	layer.msg("已发送,请注意查收邮件");
        }
    });
});

$("#check").click(function(){
	var u = $("#u").val();
	var p = $("#p").val();
	var code = $("#code").val();
	var uemail = $("#uemail").val();
	
	if (u == null || u.length == 0 || p == null || p.length == 0 || code == null || code.length == 0 ){
		return;
	}
	
	//var jgpattern =/^[A-Za-z0-9]+$/;
//	var jgpattern =/^[a-zA-Z0-9_]{1,}$/;
//	if(!jgpattern.test(u)){
//		toastr.warning("用户名只能是字母和数字和下划线组成");
//		return;
//	}

//	if (u.length > 15){
//		toastr.warning("用户名太长");
//		return;
//	}
//	if (strlen(u) < 6){
//		toastr.warning("用户名太短");
//	 	return;
//	}
	
//	if (strlen(u) > 20){
//		toastr.warning("用户名太长");
//	 	return;
//	}
	
//	if (p.length > 8 || p.length < 6) {
// 	   toastr.warning("密码长度最短为6最长为8");
// 	   return;
//	}
	
	var l = layer.load(2);
	$.ajax({
        type: "post",
        url: basePath + '/login/newpeople',
        async: true, 
        data: {
        		uname:u,
        		passcode:p,
        		ucode:code,
        		uemail:uemail
        		},
        success: function(data) {
        	layer.close(l);
           if (data == 'success'){
        	   toastr.success("创建成功,正在跳转登录");
        	   setTimeout(function(){
        		   window.location.href = basePath +"/login";
        	   },500);
           }else{
        	   toastr.warning(data);
           }
        }
    });
});

function strlen(str){
    var len = 0;
    for (var i=0; i<str.length; i++) { 
     var c = str.charCodeAt(i); 
    //单字节加1 
     if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) { 
       len++; 
     } 
     else { 
      len+=2; 
     } 
    } 
    return len;
}