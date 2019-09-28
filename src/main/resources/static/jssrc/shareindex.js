
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

var groupIndex = null;
$(document).ready(function() {
	$(".newsrefresh").click(function(){
		page += 1;
		getNews(page * 10);
	});
	
	var page = 0;
	getNews(page);
	
	var currentpage = $("#currentpage").val();
	//getOpenshare(currentpage);
	//nodebookCount();
});

/**
 * nodes cout top 10
 * @returns
 */
function nodebookCount(){
	$.ajax({
		type: "get",
		url: basePath + '/share/nodebook/count',
		async: true,
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function(data) {
			var len = data.length;
			if(len == 0){
				$("#nodebooksort").html('<div class="cell">暂无排名记录</div>');
				return;
			}
			$("#nodebooksort").html('');
			for(var i=0; i<len; i++){
				var i_ = data[i];
				var topitem = '<span style="padding-left: 15px;"/>';
				if (i_.topStyle != null && i_.topStyle.length > 0){
					topitem = '<img src="'+i_.topStyle+'"/>';
				}
				$("#nodebooksort").append('<div class="cell-s"><span class="dl">'+topitem+i_.name+'</span><span class="dr"><span class="badge">'+i_.num+'</span></span></div>');
			}
		} 
	});
}

/**
 * get share nodes
 * @param current
 * @returns
 */
function getOpenshare(current){
	if (current == null || current.length == 0) {
		current = 1;
	}
	
	$.ajax({
		type: "get",
		url: basePath + '/share/get?current=' + current,
		async: true,
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function(data) {
			var len = data.result.length;
			var totalPage = data.pages;
			if(len == 0){
				$("#nodebooks").html('<div class="cell-s">暂无共享笔记 <a href="'+basePath+'/">返回</a></div>');
				return;
			}
			
			$("#nodebooks").html('');
			var nextPage = '';
			var sPage = '';
			
			if(parseInt(current) < totalPage) {
				var np = (parseInt(current) +1);
				nextPage = '<strong><a href="'+basePath+'/share/nodes/'+(np < 1 ? 1 : np)+'"><i class="fa fa-caret-right" aria-hidden="true"></i>下一页</a></strong>';
			}
			if (parseInt(current) > 1) {
				var sp = (parseInt(current) -1);
				sPage = '<strong><a href="'+basePath+'/share/nodes/'+(sp < 1 ? 1 : sp)+'"><i class="fa fa-caret-left" aria-hidden="true"></i>上一页</a></strong>';
			}
			$("#nodebooks").append('<div class="cell-s">'+sPage+'  '+nextPage+' 共'+totalPage+'页</div>');
			
			for(var i=0; i<len; i++){
				var i_ = data.result[i];
				$("#nodebooks").append('<div class="cell-s"><span class="numitem">'+(i +1)+' </span><a target="_blank" href="'+basePath+'/share/view/'+i_.id+'">'+i_.time +" "+ i_.nodeBookName+'</a></div>');
			}
			
			$("#nodebooks").append('<div class="cell-s">'+sPage+'  '+nextPage+' 共'+totalPage+'页</div>');
		} 
	});
}

/**
 * get news
 * @param start
 * @returns
 */
function getNews(start) {
	$("#newscontent").html("请稍等...");
	$.ajax({
		type: "get",
		url: basePath + '/news/get/'+start+'/科技',
		async: true,
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success: function(data) {
			$("#newscontent").html("");
			for(var i=0; i<data.length; i++){
				var i_ = data[i];
				var html_ = '<tr>'+
				'<td align="right" width="30%">'+
				'<img width="120px;" contenteditable="true" height="110px;" src="'+i_.pic+'">'+
				'</td>'+
				'<td align="left" width="60%">'+
				'<strong><a target="_blank" href="'+i_.weburl+'">'+i_.title+'</a></strong>'+
				'<p>'+i_.time+'</p>'+
				'<p>'+i_.src+'</p>'+
				'</td>'+
				'</tr>';
				$("#newscontent").append(html_);
			}
		} 
	});
}

