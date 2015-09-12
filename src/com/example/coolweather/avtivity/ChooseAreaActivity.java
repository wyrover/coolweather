package com.example.coolweather.avtivity;

import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.*;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;
import com.example.coolweather.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity{
	public static final int LEVEL_PROVINCE =0;
	public static final int LEVEL_CITY =1;
	public static final int LEVEL_COUNTY =2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList =new ArrayList<String>();
	
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	
	private Province selectedProvince;
	private City selectedCity;
	private County selectedCounty;
	
	private int currentLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView=(ListView)findViewById(R.id.list_view);
		titleText=(TextView)findViewById(R.id.title_text);
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter);
		coolWeatherDB= CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(currentLevel==LEVEL_PROVINCE){
					selectedProvince=provinceList.get(position);
//					queryCities();
				}
				else if(currentLevel==LEVEL_CITY){
					selectedCity=cityList.get(position);
//					queryCounties();
				}
			}		
		});
		queryProvinces();
	}

	/**
	 * ��ѯȫ�����е�ʡ�����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	 */
	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getprovinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}

	/**
	 * ��ѯѡ��ʡ�����е��У����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	 */
//	private void queryCities() {
//		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
//		if (cityList.size() > 0) {
//			dataList.clear();
//			for (City city : cityList) {
//				dataList.add(city.getcityName());
//			}
//			adapter.notifyDataSetChanged();
//			listView.setSelection(0);
//			titleText.setText(selectedProvince.getprovinceName());
//			currentLevel = LEVEL_CITY;
//		} else {
//			queryFromServer(selectedProvince.getprovinceCode(), "city");
//		}
//	}
//	
	/**
	 * ��ѯѡ���������е��أ����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	 */
//	private void queryCounties() {
//		countyList = coolWeatherDB.loadCounties(selectedCity.getId());
//		if (countyList.size() > 0) {
//			dataList.clear();
//			for (County county : countyList) {
//				dataList.add(county.getCountyName());
//			}
//			adapter.notifyDataSetChanged();
//			listView.setSelection(0);
//			titleText.setText(selectedCity.getcityName());
//			currentLevel = LEVEL_COUNTY;
//		} else {
//			queryFromServer(selectedCity.getcityCode(), "county");
//		}
//	}
	
	/**
	 * ���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯʡ�������ݡ�
	 */
	private void queryFromServer(final String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(this,address,new HttpCallbackListener() {
			@Override
			public void onFinsh(String response) {
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.addModel(ChooseAreaActivity.this,coolWeatherDB,type);
				}
//				else if ("city".equals(type)) {
//					result = Utility.handleCitiesResponse(coolWeatherDB,
//							response, selectedProvince.getId());
//				} else if ("county".equals(type)) {
//					result = Utility.handleCountiesResponse(coolWeatherDB,
//							response, selectedCity.getId());
//				}
				if (result) {
					// ͨ��runOnUiThread()�����ص����̴߳����߼�
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
////							} else if ("city".equals(type)) {
////								queryCities();
////							} else if ("county".equals(type)) {
////								queryCounties();
							}
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// ͨ��runOnUiThread()�����ص����̴߳����߼�
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this,
										"����ʧ��", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	/**
	 * ��ʾ���ȶԻ���
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	/**
	 * �رս��ȶԻ���
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			
		}
	}
	
	/**
	 * ����Back���������ݵ�ǰ�ļ������жϣ���ʱӦ�÷������б�ʡ�б�����ֱ���˳���
	 */
	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
//			queryCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			finish();
		}
	}

	
}
