package com.example.khambenh;

public class Doctor {
	String MaCK;
	String MaBS;
	String HoTen;

	public Doctor() {
	}

	public Doctor(String MaCK, String MaBS, String HoTen) {
		this.MaCK = MaCK;
		this.MaBS = MaBS;
		this.HoTen = HoTen;
	}

	public void setMaCK(String ma) {
		this.MaCK = ma;
	}

	public void setHoTen(String ten) {
		this.HoTen = ten;
	}

	public String getMaCK() {
		return MaCK;
	}

	public String getHoTen() {
		return HoTen;
	}

	public String getMaBS() {
		return this.MaBS;
	}

	public void setMaBS(String mabs) {
		this.MaBS = mabs;
	}
}