package com.example.coolweather.model;

public class City extends Model {
	private int id;
	private String cityName;
	private String cityCode;
	private int provinceId;
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	public String getcityName(){
		return cityName;
	}
	public void setcityName(String cityName){
		this.cityName=cityName;
	}
	public String getcityCode(){
		return cityCode;
	}
	public void setcityCode(String cityCode){
		this.cityCode=cityCode;
	}
	public int getprovinceId(){
		return provinceId;
	}
	public void setprovinceId(int provinceId){
		this.provinceId=provinceId;
	}
}
