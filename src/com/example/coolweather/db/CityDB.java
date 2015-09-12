package com.example.coolweather.db;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;

public class CityDB {
	
	public static void geFileFromAssets(Context context, String fileName){
		
		StringBuilder response = new StringBuilder("");
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName),"utf-8");
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
            	response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       	try{
			XmlPullParserFactory factory =XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(response.toString()));
			int eventType=xmlPullParser.getEventType();		
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
