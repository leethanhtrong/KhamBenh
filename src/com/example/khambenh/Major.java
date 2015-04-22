package com.example.khambenh;

public class Major {
	String MaCK;
	String TenCK;

	public Major() {
	}

	public Major(String ma, String ten) {
		this.MaCK = ma;
		this.TenCK = ten;
	}

	public void setMaCK(String ma) {
		this.MaCK = ma;
	}

	public void setTenCK(String ten) {
		this.TenCK = ten;
	}

	public String getMaCK() {
		return MaCK;
	}

	public String getTenCK() {
		return TenCK;
	}
}