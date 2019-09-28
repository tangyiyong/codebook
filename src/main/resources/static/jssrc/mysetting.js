/**提示信息配置对象*/
toastr.options = {  
            closeButton: false,  
            debug: false,  
            progressBar: false,  
            positionClass: "toast-top-center",//"toast-bottom-right",  
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

var loading = layer.load(2);
$.ajax({
        type: "get",
        url: basePath + '/nodebook/mysetting/get',
        async: true,
        success: function(data) {
           if(data != null){
        	   var pep = data.content;
        	    $("#myemail").val(pep.myemail);
   				$("#myhome").val(pep.myhome);
   				$("#mygitee").val(pep.mygitee);
   				$("#mygithub").val(pep.mygithub);
   				$("#openshare").val(pep.openshare);
   				$("#allowdelete").val(pep.allowdelete);
   				$("#loginReminder").val(pep.loginReminder);
   				var ubackground = pep.ubackground;
   				if (ubackground != null){
   					$("body").css("background","url('/static/bk/"+ubackground+"')");
   				}
           }
           savestting();
           layer.close(loading);
        }
   });

function savestting(){
$(".savestting").click(function(){
	var pep = {
			myemail:$("#myemail").val(),
			myhome:$("#myhome").val(),
			mygitee:$("#mygitee").val(),
			mygithub:$("#mygithub").val(),
	};
	
	if (pep.myemail != null && pep.myemail.length > 0) {
	var emailreg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
		if(!emailreg.test(pep.myemail)){
			toastr.warning("邮箱格式非法");
			return;
		}
	}
	
	if (pep.myhome != null && pep.myhome.length > 0) {
		var urlreg=/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;
		if (!urlreg.test(pep.myhome)){
			toastr.warning("个人网址格式不正确");
			return;
		}
	}
	
	if (pep.mygitee != null && pep.mygitee.length > 0) {
		var urlreg=/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;
		if (!urlreg.test(pep.mygitee)){
			toastr.warning("gitee网址格式不正确");
			return;
		}
	}
	
	if (pep.mygithub != null && pep.mygithub.length > 0) {
		var urlreg=/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;
		if (!urlreg.test(pep.mygithub)){
			toastr.warning("github网址格式不正确");
			return;
		}
	}
	
	loading = layer.load(2);
	$.ajax({
	        type: "post",
	        url: basePath + '/nodebook/mysetting/update',
	        async: true,
	        data:pep,
	        success: function(data) {
	        	if (data == null){
	        		toastr.warning("操作失败");
	        		return;
	        	}else{
	        		if(data.code == 200){
	        			toastr.success("个人信息已更新");
	        		}else{
	        			toastr.warning(data.msg);
	        		}
	        	}
	           layer.close(loading);
	        }
	   });
});
}

$(".updatepasscode").click(function(){
	var pep = {
			oldpasscode:$("#oldpasscode").val(),
			newpasscode:$("#newpasscode").val(),
	};
	if (pep.oldpasscode == null || pep.oldpasscode.length == 0 || pep.newpasscode == null || pep.newpasscode.length == 0){
		return;
	}
	
	loading = layer.load(2);
	$.ajax({
        type: "post",
        url: basePath + '/nodebook/mysetting/passcode/update',
        async: true,
        data:pep,
        success: function(data) {
           if(data != null){
        	   if (data.code == 200) {
        		   $("#oldpasscode").val("");
        		   $("#newpasscode").val("");
        		   toastr.success("密码已修改,下次登入时生效。");
        	   }else{
        		   toastr.warning(data.msg);
        	   }
           }else{
        	    toastr.warning("操作失败");
       			return;
           }
           layer.close(loading);
        }
   });
});

$(".savesystemstting").click(function(){
	var pep = {
			openshare:$("#openshare").val(),
			allowdelete:$("#allowdelete").val(),
			loginReminder:$("#loginReminder").val()
	};
	$.ajax({
        type: "post",
        url: basePath + '/nodebook/setting/update',
        async: true,
        data:pep,
        success: function(data) {
           if(data != null){
        	   if (data.code == 200) {
        		   toastr.success("系统设置已更新");
        	   }else{
        		   toastr.warning(data.msg);
        	   }
           }else{
	       	    toastr.warning("操作失败");
	   			return;
           }
           layer.close(loading);
        }
   });
});

$(".updatebackground").click(function(){
	var pep = {
			ubackground:bg
	};
	$.ajax({
        type: "post",
        url: basePath + '/nodebook/mysetting/background/update',
        async: true,
        data:pep,
        success: function(data) {
           if(data != null){
        	   if (data.code == 200) {
        		   toastr.success("背景图已更新");
        	   }else{
        		   toastr.warning(data.msg);
        	   }
           }else{
	       	    toastr.warning("操作失败");
	   			return;
           }
           layer.close(loading);
        }
   });
});

var bg = "";
function selectedubackground(bgt){
	bg = bgt;
	console.log(bg);
	$("body").css("background","url('/static/bk/"+bg+"')");
}

//var mailIndex = null;
//function mailvalidation(){
//	loading = layer.load(2);
//	$.ajax({
//        type: "post",
//        url: basePath + '/nodebook/mysetting/mail/validation',
//        async: true,
//        success: function(data) {
//           console.log(data);
//           if(data != null && data.code == 200){
//        	   mailIndex = layer.open({
//     			  type: 1,
//     			  title: false,
//     			  closeBtn: 0,
//     			  shadeClose: true,
//     			  content: $(".mailView")
//     			});
//           }
//           layer.close(loading);
//        }
//   });
//}
