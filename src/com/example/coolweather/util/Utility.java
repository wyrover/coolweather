package com.example.coolweather.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.preference.PreferenceManager;
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
	
//	public static void addModel(CoolWeatherDB coolWeatherDB,String jsonStr){
//		JSONArray array = new JSONArray(jsonStr);			
//		for (int i = 0; i < array.length(); i++) {
//			
//			JSONArray ja=array.getJSONArray(i);
//			
//			if("province".equals(type)){
//				Province province= new Province();
//				province.setprovinceCode(ja.getString(1));
//				province.setprovinceName(ja.getString(0));
//				coolWeatherDB.savePcovince(province);
//			}
//			else if("city".equals(type)){
//				City city = new City();
//				city.setcityCode(ja.getString(1));
//				city.setcityName(ja.getString(0));
//				coolWeatherDB.saveCity(city);
//			}
//			else if("county".equals(type)){
//				County county = new County();
//				county.setCountyCode(ja.getString(1));
//				county.setCountyName(ja.getString(0));
//				coolWeatherDB.saveCounty(county);
//			}
//	}
	
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,
			String jsonStr){
		
		boolean isover=false;
		if (TextUtils.isEmpty(jsonStr))
			return isover;
		jsonStr=toJson(jsonStr);
		try{

			JSONArray array = new JSONArray(jsonStr);			
			for (int i = 0; i < array.length(); i++) {
				
				JSONArray ja=array.getJSONArray(i);
				
				Province province= new Province();
				province.setprovinceCode(ja.getString(1));
				province.setprovinceName(ja.getString(0));
				coolWeatherDB.savePcovince(province);	
			}
			isover=true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return isover;
	}
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
			String jsonStr,int provinceId){
		
		boolean isover=false;
		if (TextUtils.isEmpty(jsonStr))
			return isover;
		jsonStr=toJson(jsonStr);
		try{

			JSONArray array = new JSONArray(jsonStr);			
			for (int i = 0; i < array.length(); i++) {
				
				JSONArray ja=array.getJSONArray(i);				
				City city = new City();
				city.setcityCode(ja.getString(1));
				city.setcityName(ja.getString(0));
				city.setprovinceId(provinceId);
				coolWeatherDB.saveCity(city);
			}
			isover=true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return isover;
	}
	
	public synchronized static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,
			String jsonStr,int cityId){
		
		boolean isover=false;
		if (TextUtils.isEmpty(jsonStr))
			return isover;
		jsonStr=toJson(jsonStr);
		try{

			JSONArray array = new JSONArray(jsonStr);			
			for (int i = 0; i < array.length(); i++) {
				
				JSONArray ja=array.getJSONArray(i);				
				County county = new County();
				county.setCountyCode(ja.getString(1));
				county.setCountyName(ja.getString(0));
				county.setcityId(cityId);
				coolWeatherDB.saveCounty(county);
			}
			isover=true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return isover;
	}
	/**
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地。
	 */
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONArray weatherArray = jsonObject.getJSONArray("weather");
			JSONObject weatherInfo=weatherArray.getJSONObject(0);
			String cityName = weatherInfo.getString("city_name");
			String weatherCode = weatherInfo.getString("city_id");
			JSONArray futureArray=weatherInfo.getJSONArray("future");
			JSONObject futureInfo=futureArray.getJSONObject(0);
			String temp1 = futureInfo.getString("low");
			String temp2 = futureInfo.getString("high");
			String weatherDesp = futureInfo.getString("text");
			String publishTime = futureInfo.getString("day");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,
					weatherDesp, publishTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
	 */
	public static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
//	public static String geFileFromAssets(Context context, String fileName) { 
//	    	
//		if (context == null || TextUtils.isEmpty(fileName)) {
//	            return null;
//	        }
//	
//	        StringBuilder s = new StringBuilder("");
//	        try {
//	            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
//	            BufferedReader br = new BufferedReader(in);
//	            String line;
//	            while ((line = br.readLine()) != null) {
//	                s.append(line);
//	            }
//	            return s.toString();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	            return null;
//	        }
//	}
}
