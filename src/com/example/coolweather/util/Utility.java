package com.example.coolweather.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import org.json.JSONArray;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.coolweather.avtivity.ChooseAreaActivity;
import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Model;
import com.example.coolweather.model.Province;


public class Utility {
	
//	public synchronized static boolean handleProvincesResponse(Context context,CoolWeatherDB coolWeatherDB,
//			String response){
//		boolean isover=false;
//		if(!TextUtils.isEmpty(response)){	
//			try{
//				
//				
//				InputStream inputStream =context.getResources().getAssets().open("province.txt");
//				
//				XmlPullParserFactory factory =XmlPullParserFactory.newInstance();
//				XmlPullParser xmlPullParser = factory.newPullParser();
//			//	InputStream is =new ByteArrayInputStream(response.getBytes());
//				xmlPullParser.setInput(inputStream,"utf-8");
//				int eventType=xmlPullParser.getEventType();		
//				while(eventType!=XmlPullParser.END_DOCUMENT){
//					String nodename=xmlPullParser.getName();
//					switch(eventType){
//						case XmlPullParser.START_TAG:{
//							if("area".equals(nodename)){
//								if(xmlPullParser.getAttributeValue(0).length()>2){
//									break;
//								}
//								Province province= new Province();
//								province.setprovinceCode(xmlPullParser.getAttributeValue(0));
//								province.setprovinceName(xmlPullParser.getAttributeValue(1));
//								coolWeatherDB.savePcovince(province);
//							}
//							else if("city".equals(nodename)){
//								City city = new City();
//								city.setcityCode(xmlPullParser.getAttributeValue(0));
//								city.setcityName(xmlPullParser.getAttributeValue(1));
//								coolWeatherDB.saveCity(city);
//							}
//							break;
//						}
//						default: break;
//					}
//					eventType=xmlPullParser.next();
//				}
//				if(eventType==XmlPullParser.END_DOCUMENT)
//					isover=true;
//			}
//			catch(Exception e){
//				e.printStackTrace();
//				return false;
//			}
//		}
//		return isover;
//	}
	
	public static String toJson(String jsonStr){
    	
    	return jsonStr.substring(jsonStr.lastIndexOf("(")+1, jsonStr.lastIndexOf(")"));
    }
	
	public static boolean addModel(Context context,CoolWeatherDB coolWeatherDB,String type){
		
		boolean isover=false;
		String jsonStr="";
		if (type.equals("province")) {
			jsonStr=geFileFromAssets(context, "citylist.txt");
		}
		if (TextUtils.isEmpty(jsonStr))
			return isover;
		jsonStr=toJson(jsonStr);
		try{
			JSONArray array = new JSONArray(jsonStr);
			
			for (int i = 0; i < array.length(); i++) {
				
				JSONArray ja=array.getJSONArray(i);
				
				if("province".equals(type)){
					Province province= new Province();
					province.setprovinceCode(ja.getString(1));
					province.setprovinceName(ja.getString(0));
					coolWeatherDB.savePcovince(province);
				}
				else if("city".equals(type)){
					City city = new City();
					city.setcityCode(ja.getString(1));
					city.setcityName(ja.getString(0));
					coolWeatherDB.saveCity(city);
				}
				else if("county".equals(type)){
					County county = new County();
					county.setCountyCode(ja.getString(1));
					county.setCountyName(ja.getString(0));
					coolWeatherDB.saveCounty(county);
				}
				
			}
			isover=true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return isover;
	}
	public static String geFileFromAssets(Context context, String fileName) { 
	    	
		if (context == null || TextUtils.isEmpty(fileName)) {
	            return null;
	        }
	
	        StringBuilder s = new StringBuilder("");
	        try {
	            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
	            BufferedReader br = new BufferedReader(in);
	            String line;
	            while ((line = br.readLine()) != null) {
	                s.append(line);
	            }
	            return s.toString();
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	}
}