package cn.bowseros.service;

import java.util.List;
import java.util.Map;

import cn.bowseros.entity.NodebookItem;
import cn.bowseros.vo.NodebookVo;
import cn.bowseros.vo.NodesCount;
import codebook.commons.R;

/**
 *
 * Created by bowser Sep 7, 2019/2:59:35 PM
 * 
 */
public interface NodebookItemService {

	/**
	 * 新增笔记分组
	 * @param nodebookItem
	 * @return
	 */
	R<String> insertGroup(NodebookItem nodebookItem);

	/**
	 * 查询我的笔记节点
	 * @return
	 */
	R<List<NodebookItem>> mynodes();
	
	/**
	 * 查询我的笔记节点数量和背景图
	 * @return
	 */
	Map<String,String> mynodesNum();

	/**
	 * 保存新笔记
	 * @param nodebookContent
	 * @return
	 */
	R<String> insert(NodebookVo nodebookVo);

	/**
	 * 查询我的笔记分组
	 * @return
	 */
	R<List<Map<String, Object>>> mynodesGroup();

	/**
	 * 查询我的笔记内容
	 * @param nodebookid
	 * @return
	 */
	R<NodebookVo> mynodesContent(String nodebookid);

	/**
	 * 查询相关笔记
	 * @param selectedText
	 * @return
	 */
	R<List<NodebookVo>> matter(String selectedText);

	/**
	 * 删除笔记
	 * @param nodebookVo
	 * @return
	 */
	R<String> delete(NodebookVo nodebookVo);

	/**
	 * 获取共享笔记
	 * @return
	 */
	Map<String,Object> getOpenshare(long current,long size);

	/**
	 * 笔记数量排名
	 * @return
	 */
	List<NodesCount> nodebookCount();
	
	/**
	 * 开发者笔记总条数
	 * @return
	 */
	Integer nodeCount();
	
	/**
	 * 不同的笔记节点类型查询
	 * @param currentpage
	 * @param i
	 * @return
	 */
	Map<String, Object> getOpenshareByType(String nodetype, int currentpage, int i);

}
