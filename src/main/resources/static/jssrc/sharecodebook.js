
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
            htmlDecode      : "style,script,iframe",  // you can filter tags decode
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
           
           //新浪meta
	   	   //<meta property="og:type" content="article" />
           let meta = document.createElement('meta');
           meta.name="weibo:type";
           meta.content="article";
           document.getElementsByTagName('head')[0].appendChild(meta);
	   	   //<meta property="og:url" content="文章的URL地址，请勿携带统计代码，分页网址请统一填写第一页链接" />
           let meta2 = document.createElement('meta');
           meta2.name="weibo:article:url";
           meta2.content=window.location.href;
           document.getElementsByTagName('head')[0].appendChild(meta2);
	   	   //<meta property="og:title" content="文章的显示名称标题，不建议携带网站名称等SEO信息" />
           let meta3 = document.createElement('meta');
           meta3.name="weibo:article:title";
           meta3.content=nodebook.nodeBookName;
           document.getElementsByTagName('head')[0].appendChild(meta3);
	   	   //<meta property="og:description" content="文章的文字描述，将直接显示在微博里，建议完整填写，不要重复填写标题" />
           let meta4 = document.createElement('meta');
           meta4.name="weibo:article:description";
           meta4.content="描述:"+nodebook.nodeBookName;
           document.getElementsByTagName('head')[0].appendChild(meta4);
	   		//<!--选填-->
	   		//<meta name="weibo: article:create_at" content="文章的创建时间" />
	   		//<meta name="weibo: article:update_at" content="文章的更新时间" />"
           
        }
    });
