package cn.bowseros.config;

import java.util.ArrayList;
import java.util.List;

import cn.bowseros.vo.NodesCount;
import codebook.commons.SimpleBloomFilter;

/**
 *
 * Created by bowser Sep 24, 2019/5:16:56 PM
 * 全局容器列表
 */
public class ListItem {

	public static SimpleBloomFilter toDayRegisterList = new SimpleBloomFilter();// 一小时内已注册IP列表
	public static SimpleBloomFilter notAccessList = new SimpleBloomFilter();// 拒绝访问列表
	public static SimpleBloomFilter accfilter = new SimpleBloomFilter();// 访问者过滤器
	public static List<NodesCount> nodesCountList = new ArrayList<>(10);//每小时用户笔记数量排名
	
}
