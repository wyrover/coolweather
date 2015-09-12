package com.example.coolweather.model;

public class Province extends Model {
	private int id;
	private String provinceName;
	private String provinceCode;
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	public String getprovinceName(){
		return provinceName;
	}
	public void setprovinceName(String provinceName){
		this.provinceName=provinceName;
	}
	public String getprovinceCode(){
		return provinceCode;
	}
	public void setprovinceCode(String provinceCode){
		this.provinceCode=provinceCode;
	}
}
