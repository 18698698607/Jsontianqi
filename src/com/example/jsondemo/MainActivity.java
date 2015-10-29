package com.example.jsondemo;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static final String GET_URL = "http://op.juhe.cn/onebox/weather/query?cityname=%E5%A4%A7%E8%BF%9E&key=03b4b890ab2200e9db7000aa921e2437";
	private Button btn;
	private EditText city;
	private TextView name, weather1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn = (Button) findViewById(R.id.getWeather);
		city = (EditText) findViewById(R.id.city);
		name = (TextView) findViewById(R.id.city_name);
		weather1 = (TextView) findViewById(R.id.city_weather);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)  {

				new AsyncTask<Void, Void, String>() {

					@Override
					protected String doInBackground(Void... arg0) {

						String txt = "";
						try {
							txt = doget();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						return txt;
					}

					@Override
					protected void onPostExecute(String result1) {
						// TODO Auto-generated method stub
						super.onPostExecute(result1);
						JSONObject json;
//						String city_name=null;
//						JSONObject weather=null;
//						String temperature=null;
						try {
							json = new JSONObject(result1);
							String reason = json.getString("reason");
							Log.e("reason", "================================" + reason);
							JSONObject result = json.getJSONObject("result");
							Log.e("result", "================================" + result);
							JSONObject data = result.getJSONObject("data");
							Log.e("data", "================================" + data);
							JSONObject realtime = data.getJSONObject("realtime");
							Log.e("realtime", "================================" + realtime);
							String city_name = realtime.getString("city_name");
							Log.e("city_name", "================================" + city_name);
							JSONObject weather=realtime.getJSONObject("weather");
							String temperature=weather.getString("temperature");
							
							// for (int i = 0; i < resultArray.length(); i++) {
							// JSONObject result = resultArray.getJSONObject(i);
							// String date = weajson.getString("date");
							// String week = weajson.getString("week");
							// Log.e("", "date=======" + date);
							// Log.e("", "week=======" + week);

							name.setText(city_name);
							weather1.setText("温度"+temperature+"°");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

					}
				}.execute();
			}
		});
	}

	private String doget() throws Exception {
		String cit=city.getEditableText().toString();
		//提取文本转换格式
		cit = URLEncoder.encode(cit, "utf-8");
		Log.e("", "==city" + cit);
		//回转
//		String t = URLDecoder.decode(cit, "utf-8");
//		Log.e("", "==city" + t);
		String txt = "";
		// 创建url
		URL url = new URL("http://op.juhe.cn/onebox/weather/query?cityname="+cit+"&key=03b4b890ab2200e9db7000aa921e2437");
		// 通过HttpURLConnection创建连接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置连接超时等时间
		conn.setConnectTimeout(500);
		// 对响应吗进行判断
		if (conn.getResponseCode() == 200) {

			// 获取数据用流
			InputStream is = conn.getInputStream();
			// Bitmap bmp = BitmapFactory.decodeStream(is);

			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] data = new byte[1024];
			int len = 0;
			StringBuffer buff = new StringBuffer();
			while ((len = bis.read(data)) != -1) {
				String str = new String(data, 0, len);
				buff.append(str);
			}
			txt = buff.toString();

			// 关闭流
			bis.close();

		}
		// // 关闭连接
		// conn.disconnect();
		
		return txt;

	}
}
