package com.example.coolweather.model;

public class County extends Model{
	private int id;
	private String CountyName;
	private String CountyCode;
	private int cityId;
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	public String getCountyName(){
		return CountyName;
	}
	public void setCountyName(String CountyName){
		this.CountyName=CountyName;
	}
	public String getCountyCode(){
		return CountyCode;
	}
	public void setCountyCode(String CountyCode){
		this.CountyCode=CountyCode;
	}
	public int getcityId(){
		return cityId;
	}
	public void setcityId(int cityId){
		this.cityId=cityId;
	}
}
