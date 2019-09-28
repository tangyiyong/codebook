package cn.bowseros.vo;

/**
 *
 * Created by bowser Sep 20, 2019/9:05:15 PM
 * 
 */
public class NodesCount implements Comparable<NodesCount> {

	private String nameStyleStart;// 名称样式头
	private String nameStylEnd;// 名称样式尾
	private String topStyle;// 排名样式
	private String name;// 用户名
	private Integer num;// 笔记时数量

	
	public String getTopStyle() {
		return topStyle;
	}

	public void setTopStyle(String topStyle) {
		this.topStyle = topStyle;
	}

	public String getNameStyleStart() {
		return nameStyleStart;
	}

	public void setNameStyleStart(String nameStyleStart) {
		this.nameStyleStart = nameStyleStart;
	}

	public String getNameStylEnd() {
		return nameStylEnd;
	}

	public void setNameStylEnd(String nameStylEnd) {
		this.nameStylEnd = nameStylEnd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	@Override
	public int compareTo(NodesCount o) {
		return o.getNum().compareTo(this.getNum());
	}

}
