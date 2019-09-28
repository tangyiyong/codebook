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

$(document).ready(function(){
	
	$("#weiboLogin").click(function(){
		window.location.href = basePath + "/oauth/weibo";
	});
	
	$("#check").click(function(){
		var u = $("#u").val();
		var p = $("#p").val();
		if ( u == null || u.length == 0 || p == null || p.length == 0 || p.length < 6 ){
			return;
		}
		
		var l = layer.load(2);
		$.ajax({
	        type: "post",
	        url: basePath + '/login/check',
	        async: true, 
	        data: {
	        		uname:u,
	        		passcode:p
	        		},
	        success: function(data) {
	        	console.log(data);
	           layer.close(l);
	           if (data == 'success'){
	        	   window.location.href = basePath + "/";
	           }else{
	        	   if (data.msg){
	        		   toastr.error(data.msg);
	        	   }else{
	        		   toastr.error(data);
	        	   }
	           }
	        }
	    });
	});

});

