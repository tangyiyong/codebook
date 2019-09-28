var testEditor;
var hrefType = "update";
var id = $("#id").val();

$(function() {

	//$.get('test.md', function(md) {});
	testEditor = editormd("test-editormd", {
		width : "100%",
		height : window.screen.availHeight,
		//autoHeight : true,
		path : basePath + '/static/plugin/editormd/lib/',
		theme : "default",//dark
		previewTheme : "default",//dark
		editorTheme : "default",//pastel-on-dark
		markdown : "",
		codeFold : true,
		//syncScrolling : false,
		saveHTMLToTextarea : true, // 保存 HTML 到 Textarea
		searchReplace : true,
		//watch : false,                // 关闭实时预览
		htmlDecode : "style,script,iframe|on*", // 开启 HTML 标签解析，为了安全性，默认不开启    
		//toolbar  : false,             //关闭工具栏
		//previewCodeHighlight : false, // 关闭预览 HTML 的代码块高亮，默认开启
		emoji : true,
		taskList : true,
		tocm : true, // Using [TOCM]
		tex : true, // 开启科学公式TeX语言支持，默认关闭
		flowChart : true, // 开启流程图支持，默认关闭
		sequenceDiagram : true, // 开启时序/序列图支持，默认关闭,
		//dialogLockScreen : false,   // 设置弹出层对话框不锁屏，全局通用，默认为true
		//dialogShowMask : false,     // 设置弹出层对话框显示透明遮罩层，全局通用，默认为true
		//dialogDraggable : false,    // 设置弹出层对话框不可拖动，全局通用，默认为true
		//dialogMaskOpacity : 0.4,    // 设置透明遮罩层的透明度，全局通用，默认值为0.1
		//dialogMaskBgColor : "#000", // 设置透明遮罩层的背景颜色，全局通用，默认为#fff
		imageUpload : false,//开启后则可以使用本地上传的方式
		imageFormats : [ "jpg", "jpeg", "gif", "png", "bmp", "webp" ],
		imageUploadURL : "",
		onload : function() {
			
			if (id != null && id.length > 0) {
				var loading = layer.load(2);
				$.ajax({
			        type: "get",
			        url: basePath + '/nodebook/content/' + id,
			        async: true, 
			        success: function(data) {
			        	if (data == null || data.code != 200){
			          	   window.location.href = basePath +"/";
			          	   return;
			             }
			           var nodebook = data.content;
			           testEditor.setMarkdown(nodebook.nodecontent);
			           $("#nodename").val(nodebook.nodeBookName);
			           layer.close(loading);
			           
			        }
			    });
			}
			
			//console.log('onload', this);
			//this.fullscreen();
			//this.unwatch();
			testEditor.setMarkdown("#JAVA");
			//this.watch().fullscreen();
			//this.width("100%");
			//this.height(480);
			//this.resize("100%", 640);
		}
	});

// var el = document.getElementById('nodename');
//     el.addEventListener('input',function () {
//     	var nodename = $("#nodename").val(); 
//     	if(nodename != null && nodename.length > 0) {
// 			document.title = nodename +"-新增笔记";
// 		}
//     });
//	$("#get-html-btn").bind('click', function() {
//		alert(testEditor.getHTML());
//	});
	// $("#get-md").bind('click', function() {
	// 	console.log(testEditor.getMarkdown());
	// });
	// $("#get-md-btn").bind('click', function() {
	// 	saveHtml(testEditor.getMarkdown());
	// });
	
//	$("#goto-line-btn").bind("click", function() {
//		testEditor.gotoLine(90);
//	});
//
//	$("#show-btn").bind('click', function() {
//		testEditor.show();
//	});
//
//	$("#hide-btn").bind('click', function() {
//		testEditor.hide();
//	});
//
//	$("#watch-btn").bind('click', function() {
//		testEditor.watch();
//	});
//
//	$("#unwatch-btn").bind('click', function() {
//		testEditor.unwatch();
//	});
//
//	$("#preview-btn").bind('click', function() {
//		testEditor.previewing();
//	});
//
//	$("#fullscreen-btn").bind('click', function() {
//		testEditor.fullscreen();
//	});
//
//	$("#show-toolbar-btn").bind('click', function() {
//		testEditor.showToolbar();
//	});
//
//	$("#close-toolbar-btn").bind('click', function() {
//		testEditor.hideToolbar();
//	});
//
//	$("#toc-menu-btn").click(function() {
//		testEditor.config({
//			tocDropdown : true,
//			tocTitle : "目录 Table of Contents",
//		});
//	});
//
//	$("#toc-default-btn").click(function() {
//		testEditor.config("tocDropdown", false);
//	});
});

var groupIndex = null;
$(".save").click(function(){
	$.ajax({
        type: "get",
        url: basePath + '/nodebook/mynodes/group',
        async: true, 
        success: function(data) {
           
           $("#group").html("");
           $("#group").append(`<option value="">选择分组</option>`);
           var len = data.content.length,groupData = data.content;
           for(var i =0; i<len; i ++) {
        	   $("#group").append(`<option value="`+groupData[i].id+`">`+groupData[i].nodeBookName+`</option>`);
           }
        }
    });
	
	groupIndex = layer.open({
		  type: 1,
		  title: false,
		  closeBtn: 0,
		  shadeClose: true,
		  area: ['40%', 'auto'], //宽高
		  content: $(".hidediv")
		});
});

$(".confirm").click(function(){
	var content = testEditor.getMarkdown();
	var nodename = $("#nodename").val();
	var group = $("#group").val();
	var tag = $("#tag").val();
	
	if (nodename == null || nodename.length == 0) {
		return;
	}
	if (group == null || group.length == 0) {
		return;
	}
	if (content == null || content.length == 0) {
		return;
	}
	
	$.ajax({
        type: "post",
        url: basePath + '/nodebook/insert',
        async: true, 
        data: {
        	id:id,
        	nodeBookName:nodename,
        	pId:group,
        	share:0,
        	parent:'false',
        	open:'false',
        	tag:tag,
        	nodecontent:content
        },
        success: function(data) {
        	if (data.code == 200){
        		layer.close(groupIndex);
        		layer.msg("已保存")
        		setTimeout(function(){
        			window.location.href = basePath +"/";
        		},1000);
        	}else{
        		console.log(data);
        	}
        }
    });
});
