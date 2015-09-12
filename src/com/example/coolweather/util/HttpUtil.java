package com.example.coolweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;

public class HttpUtil {
	
	public static void sendHttpRequest(final Context context,final String address,final HttpCallbackListener listener){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//HttpURLConnection connection=null;
				try{
					
					StringBuilder response = new StringBuilder("hello");
					
//			     
//			        InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open("city.txt"),"utf-8");
//			        BufferedReader br = new BufferedReader(in);
//			        String line="";
//			        while ((line = br.readLine()) != null) {
//			            	response.append(line);
//			            }
					
			        if(listener!=null){
						listener.onFinsh(response.toString());
					}
				}
				catch(Exception e){
					if(listener!=null){
						listener.onError(e);
					}
				}
			}
		}).start();
	}
}
