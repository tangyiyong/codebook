var zTree;
var demoIframe;
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

var setting = {
	view: {
		dblClickExpand: false,
		showLine: false,
		selectedMulti: false
	},
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "pId",
			rootPId: ""
		}
	},
	edit: {
		enable: true,
		showRemoveBtn:false,
		showRenameBtn:false
	},
	callback: {
		beforeDrag: beforeDrag,
		beforeRemove: beforeRemove,
		beforeClick: function(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("tree");
			if (treeNode.parent == 'true') {
				zTree.expandNode(treeNode);
				return false;
			} else {
				window.location.href= basePath+"/nodebook/view/"+treeNode.id;
				return true;
			}
		}
	}
};

var klsetting = {
		data: {
			simpleData: {
				enable: true
			}
		},
		edit: {
			enable: false,
			showRemoveBtn:false,
			showRenameBtn:false
		},
		callback:{
			beforeDrag: beforeDrag
		}
	};

function iNode(){
	$.ajax({
        type: "get",
        url: basePath + '/i/node',
        async: true, 
        success: function(data) {
           if(data == null){
        	   toastr.warning("操作失败");
        	   return;
           }
           if (data.code != 200){
        	   toastr.warning(data.msg);
        	   return;
           }
          
           var mynodes = data.content.mynodes;
           if (mynodes.code == 200){
        	   var t = $("#tree");
           	   t = $.fn.zTree.init(t, setting, mynodes.content);
           	   fuzzySearch('tree','#keyword',null,true); //初始化模糊搜索方法
    		   t.setting.edit.showRemoveBtn = (mynodes.otherData.allowdelete == 1);//删除功能开关
           }
           
           var mylinks = data.content.mylinks;
           if (mylinks.code == 200){
        	   var keepLinkTree = $.fn.zTree.init($("#keepLinkTree"), klsetting, mylinks.content);
               keepLinkTree.setting.edit.showRemoveBtn = (mylinks.otherData.allowdelete == 1); //删除功能开关
           }
           
        }
    });
}

function expandAll(flg){
	var treeObj = $.fn.zTree.getZTreeObj("tree");
	treeObj.expandAll(flg);
}

//function loadNodeTree(){
//	$.ajax({
//        type: "get",
//        url: basePath + '/nodebook/mynodes',
//        async: true, 
//        success: function(data) {
//           if(data == null){
//        	   toastr.warning("操作失败");
//        	   return;
//           }
//           if (data.code != 200){
//        	   toastr.warning(data.msg);
//        	   return;
//           }
//           var t = $("#tree");
//       	   t = $.fn.zTree.init(t, setting, data.content);
//       	   fuzzySearch('tree','#keyword',null,true); //初始化模糊搜索方法
//       	   
//		   t.setting.edit.showRemoveBtn = (data.otherData.allowdelete == 1);//删除功能开关
//		   
//		   //$("#nodeGroup").html(data.otherData.nodeGroup);//笔记分组数量
//		   //$("#nodeCount").html(data.otherData.nodeCount);//笔记数量
//        }
//    });
//}
//function loadKeepLinkTree(){
//	$.ajax({
//        type: "get",
//        url: basePath + '/keeplink/mykeeplinks',
//        async: true, 
//        success: function(data) {
//           var keepLinkTree = $.fn.zTree.init($("#keepLinkTree"), klsetting, data.content);
//           //删除功能开关
//           keepLinkTree.setting.edit.showRemoveBtn = (data.otherData.allowdelete == 1);
//        }
//    });
//}

var groupIndex = null;
$(document).ready(function() {
	
	//设置高度
	$("#nodeTree").height('auto');//(window.innerHeight)
	$("#keepLink").height('auto');
	
	$("#addkl").click(function(){
		groupIndex = layer.open({
			  type: 1,
			  title: false,
			  closeBtn: 0,
			  shadeClose: true,
			  area: ['40%', 'auto'], //宽高
			  content: $(".kpl")
			});
	});
	
	$("#addNodeGroup").click(function(){
		groupIndex = layer.open({
			  type: 1,
			  title: false,
			  closeBtn: 0,
			  shadeClose: true,
			  area: ['40%', 'auto'], //宽高
			  content: $(".group")
			});
	});
	
	/**
	 * 保存收藏链接
	 */
	$(".savekl").click(function(){
		var kname = $("#kname").val();
		if (kname == null || kname.length == 0) {
			toastr.warning("未填写链接名称");
			return;
		}
		var kurl = $("#kurl").val();
		if (kurl == null || kurl.length == 0) {
			toastr.warning("未填写链接地址");
			return;
		}
		
		$.ajax({
            type: "post",
            url: basePath + '/keeplink/update',
            async: true, 
            data: {
            		id:"",
            		kname:kname,
            		kurl:kurl
            		},
            success: function(data) {
               if (data != null && data.code == 200){
            	   //loadKeepLinkTree();
            	   iNode();
            	   layer.close(groupIndex);
            	   toastr.success("添加成功");
               }else{
            	   toastr.warning((data.msg == null || data.msg.length == 0)?"操作失败":data.msg );
               }
            }
        });
	});
	
	/**
	 * 保存笔记分组
	 */
	$(".save").click(function(){
		var groupName = $("#groupName").val();
		if (groupName == null || groupName.length == 0) {
			toastr.warning("未填写节点名称");
			return;
		}
		$.ajax({
            type: "post",
            url: basePath + '/nodebook/insert/group',
            async: true, 
            data: {
            		nodeBookName:groupName,
            		parent:'true',
            		open:'false'
            		},
            success: function(data) {
            	if (data != null && data.code == 200){
            	   //loadNodeTree();
            		iNode();
            	   layer.close(groupIndex);
            	   toastr.success("添加成功");
               }else{
            	   toastr.warning((data.msg == null || data.msg.length == 0)?"操作失败":data.msg );
               }
            }
        });
	});
	
	$(".nodetree").click(function(){
		$("#keepLink").hide();
		$("#nodeTree").show();
		$(".nodetree").addClass('tabactive');
		$(".keeplink").removeClass('tabactive');
	  　$("#keyword").removeAttr("readonly");
	});
	$(".keeplink").click(function(){
		$("#nodeTree").hide();
		$("#keepLink").show();
		$(".keeplink").addClass('tabactive');
		$(".nodetree").removeClass('tabactive');
		$("#keyword").attr("readonly","readonly");
	});
	
//	$(".newsrefresh").click(function(){
//		page += 1;
//		get(page * 10);
//	});
	
	//var page = 0;
	//get(page);
	iNode();
});

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

function get(start) {
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

//function home(){
//	window.location.href= basePath + "/";
//}

//function settings(){
//	window.open(basePath + "/nodebook/mysetting","_blank");
//}

//function f(){
//	loadNodeTree();
//	loadKeepLinkTree();
//}

function leavemessage(){
	window.open(basePath + "/about","_blank");
}

function loadReady() {
	var bodyH = demoIframe.contents().find("body").get(0).scrollHeight,
		htmlH = demoIframe.contents().find("html").get(0).scrollHeight;
		maxH = Math.max(bodyH, htmlH),
		minH = Math.min(bodyH, htmlH),
		h = demoIframe.height() >= maxH ? minH : maxH;
	if (h < 530) h = 530;
	demoIframe.height(window.innerHeight);
}

function beforeRemove(treeId, treeNode){
	var msg = "";
	if (treeNode.parent == 'true'){
		msg = "确定删除笔记分组?";
	}else{
		msg = "确定删除笔记?";
	}
	
	var l_ = layer.confirm(msg, {
		  btn: ['确定','取消'] // 按钮
		}, function(){
			var delIndex = layer.load(2);
			$.ajax({
	            type: "post",
	            url: basePath + '/nodebook/delete',
	            async: true, 
	            data: {id:treeNode.id},
	            success: function(data) {
	            	if(data != null) {
		               if (data.code == 200){
		            	   //loadNodeTree();
		            	   iNode();
		            	   toastr.success("已删除");
		               }else{
		            	   toastr.warning(data.msg);
		               }
		               layer.close(delIndex);
	            	   layer.close(l_);
	            	}
	            }
	        });
			return true;
		}, function(){
		});
	return false;
}

function beforeDrag(treeId, treeNodes) {
	return false;
}