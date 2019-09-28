
var loading = layer.load(2);
var matterIndex = null;
$.ajax({
        type: "get",
        url: basePath + '/nodebook/content/' + $("#id").val(),
        async: true, 
        success: function(data) {
        	if (data == null || data.code != 200){
         	   window.location.href = basePath +"/";
         	   return;
            }
        	
           var nodebook = data.content;
           $("#nodebookname").html(nodebook.nodeBookName);
           //var upTime = new Date(nodebook.updateTime);
           //var date_ = upTime.getFullYear() +"/"+ (upTime.getMonth() +1 ) +"/"+ upTime.getDate();
           //var time_ = upTime.getHours() +":"+ upTime.getMinutes()
           //$("#updateTime").html(date_ +" "+ time_);
           $("#updateTime").html(nodebook.time);
           
           //文章内容
           var testEditormdView = editormd.markdownToHTML("nodecontent", {
            markdown        : nodebook.nodecontent ,//+ "\r\n" + $("#append-test").text(),
            //htmlDecode      : true,       // 开启 HTML 标签解析，为了安全性，默认不开启
            htmlDecode      : "style,script,iframe,sub,sup|on*",  // you can filter tags decode
            //toc             : false,
            tocm            : true,    // Using [TOCM]
            //tocContainer    : "#custom-toc-container", // 自定义 ToC 容器层
            //gfm             : false,
            //tocDropdown     : true,
            // markdownSourceCode : true, // 是否保留 Markdown 源码，即是否删除保存源码的 Textarea 标签
            emoji           : true,
            taskList        : true,
            tex             : true,  // 默认不解析
            flowChart       : true,  // 默认不解析
            sequenceDiagram : true,  // 默认不解析
           });
           
           $(".codebook").show();
           layer.close(loading);
           
           // ====相关笔记
           $(".codebook").mouseup(function(e){
        	   if(window.getSelection) { 
	        	   var textObj = document.getElementById("nodecontent"); 
	        	   var selectedText = window.getSelection().toString(); 
	        	   if(selectedText.length == 0){
	        		   return;
	        	   }
	        	   $.ajax({
	        	        type: "get",
	        	        url: basePath + '/nodebook/matter?data=' + selectedText,
	        	        async: true, 
	        	        success: function(data) {
	        	        	
	        	           $("#matter").html("");
	        	           matterIndex = layer.open({
			        	        		   type: 1,
			        	        		   shade: false,
			        	        		   title: "相关笔记链接", //不显示标题
			        	        		   offset: 'rb',
			        	        		   area: ['350px', '20%'], //宽高
			        	        		   content: $(".matter"), //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
			        	        		   cancel: function(){
			        	        		     //layer.msg('捕获就是从页面已经存在的元素上，包裹layer的结构', {time: 5000, icon:6});
			        	        		   }
			        	        		 });
	        	           var len = data.content.length,metadata = data.content;
	        	           if (len == 0){
	        	        	   return;
	        	           }
	        	           for (var i=0; i<len; i++){
	        	        	   var url_ = basePath+"/nodebook/view/"+metadata[i].id;
	        	        	   $("#matter").append("<li style='list-style-type: none;border-bottom: #999999 1px dashed'><a style='font-size: 16px;' href='"+url_+"' >"+metadata[i].nodeBookName+"</a></li>");
	        	           }
	        	           
	        	        }
	        	   });
        	   } 
        	});
           // ====相关笔记
           
           
        }
    });
