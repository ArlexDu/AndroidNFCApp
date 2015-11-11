package edu.happy.tools;

public class ReadInformation {
	
	private String data;
	private String textType;
	private String maxsize;
	public String getData() {
		return "数据内容是："+data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getTextType() {
		return "数据类型是："+textType;
	}
	public void setTextType(String textType) {
		this.textType = textType;
	}
	public String getMaxsize() {
		return "nfc标签大小是："+maxsize;
	}
	public void setMaxsize(String maxsize) {
		this.maxsize = maxsize;
	}
	

}
