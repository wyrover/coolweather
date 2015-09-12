package com.example.coolweather.util;

public interface HttpCallbackListener {
	void onFinsh(String response);
	void onError(Exception e);
}
