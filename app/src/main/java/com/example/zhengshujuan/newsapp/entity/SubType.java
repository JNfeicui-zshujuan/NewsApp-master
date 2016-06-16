package com.example.zhengshujuan.newsapp.entity;

public class SubType {
	
	private int subid;
	public String subgroup;
	
	public SubType(int subid, String subgroup) {
		this.subid = subid;
		this.subgroup = subgroup;
	}
	public int getSubid() {
		return subid;
	}
	public String getSubgroup() {
		return subgroup;
	}
	
}