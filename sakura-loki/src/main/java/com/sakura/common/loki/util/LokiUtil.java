package com.sakura.common.loki.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.sakura.common.loki.exception.LokiException;
import com.sakura.common.loki.item.FilterInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LokiUtil {
	public static JSONObject get(String url) {
		try {
			OkHttpClient client = new OkHttpClient();
			client = new OkHttpClient.Builder().readTimeout(20000, TimeUnit.MINUTES)
					.connectTimeout(20, TimeUnit.SECONDS).build();
			Request request = new Request.Builder().url(url).get().build();
			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				String s = response.body().string();
				// JSONArray jsonArray = JSONArray.parseArray(s);
				JSONObject jsonObject = JSON.parseObject(s);
				// JSONArray data = jsonObject.getJSONArray("data");
				return jsonObject;
			} else {
				// return JSON.parseObject(response.body().string());
				throw new LokiException("接口返回错误信息:" + response.body().string());
				// log.error("接口返回错误信息:" + response.body().string());
				// System.out.println("sonarqube请求失败");
			}
		} catch (Exception e) {
			throw new LokiException("get请求失败-可能loki没启动");
		}

	}

	// String时间转Long
	public static Long string2Millis(String dateStr, String formatStr) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
			return simpleDateFormat.parse(dateStr).getTime();
		} catch (Exception e) {
			throw new LokiException("获取时间格式出错",e);
		}
	}

	// 对获取到的loki数据进行根据时间降序
	public static List<Object> lokiListSort(List<Object> list, FilterInfo filterInfo) {
		try {
			Collections.sort(list, new Comparator() {
				@Override
				public int compare(Object o1, Object o2) {
					JSONObject jsonObject1 = (JSONObject) o1;

					Long date1 = getDateLong(jsonObject1);
					JSONObject jsonObject2 = (JSONObject) o2;
					Long date2 = getDateLong(jsonObject2);
					int sort = 0;
					if ("asc".equalsIgnoreCase(filterInfo.getSortMethod())) {
						sort = (int) (date1 - date2);
					} else {
						sort = (int) (date2 - date1);
					}
					return sort;
				}

			});
			return list;
		} catch (Exception e) {
			throw new LokiException("数据排序出错",e);
		}

	}

	// 获取时间字段
	public static String getDateString(JSONObject jsonObject) {
		try {
			Object dateOrTime = jsonObject.get("date");
			if (dateOrTime == null) {
				dateOrTime = jsonObject.get("time");
			}
			if (dateOrTime == null) {
				dateOrTime = jsonObject.get("@timestamp");
			}
			return dateOrTime.toString();
		} catch (Exception e) {
			throw new LokiException("获取时间字段出错",e);
		}

	}

	// 获取时间字段 Long
	public static Long getDateLong(JSONObject jsonObject) {

		try {
			String formatStr = "yyyy-MM-dd HH:mm:ss.SSS";
			Object dateOrTime = jsonObject.get("date");
			if (dateOrTime == null) {
				dateOrTime = jsonObject.get("time");
				formatStr = "yyyy-MM-dd'T'HH:mm:ssXXX";
			}
			if (dateOrTime == null) {
				dateOrTime = jsonObject.get("@timestamp");
				formatStr = "yyyy-MM-dd HH:mm:ss.SSS";
			}

			return string2Millis(dateOrTime.toString(), formatStr);
		} catch (Exception e) {
			throw new LokiException("获取时间字段出错",e);
		}

	}
	
	public static Long getDateLong(String time) {
		try {
			Long localDateTimeSize=null;
			// 只接受long型范围查询
			if (!StringUtils.isEmpty(time)) {
				DateTimeFormatter dateTimeFormatters = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime localDateTime = LocalDateTime.parse(time,dateTimeFormatters);
				localDateTimeSize=localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				
			}
			return localDateTimeSize;
		} catch (Exception e) {
			throw new LokiException("时间转换出错",e);
		}
	}
	
	
	//处理loki查询出来的json对象
	public static List<Object> dealData(JSONObject jo,FilterInfo filterInfo){
		try {
			String wordFields=filterInfo.getWordFields();
			Long startTimeSize=getDateLong(filterInfo.getStart());
			Long endTimeSize=getDateLong(filterInfo.getEnd());
			List<Object> list=new ArrayList<Object>();
			JSONArray jsonArray = jo.getJSONObject("data").getJSONArray("result");
			  for (int i = 0; i < jsonArray.size(); i++) {
			        JSONObject source = (JSONObject)jsonArray.get(i);
			        JSONArray sourceArray = source.getJSONArray("values");
			        for (int j = 0; j < sourceArray.size(); j++) {
			        	JSONArray source1 = (JSONArray) sourceArray.get(j);
			        	for (int z = 0; z < source1.size(); z++) {
			        		if(z==1) {
			        			//包含某些字段直接输出
			        			if(!StringUtils.isEmpty(wordFields)) {
			        				if(wordFields.indexOf("info")!=-1||wordFields.indexOf("warn")!=-1||wordFields.indexOf("debug")!=-1||wordFields.indexOf("error")!=-1) {
				        				String v=source1.get(z).toString();
				        				list.add(v);
				        			}
			        			}
			        			else {
			                     //否则继续处理数据 并且过滤出对应时间（loki查询出来的数据不在时间范围问题）
			        			JSONObject value= JSON.parseObject(source1.get(z).toString());
			        			//获取时间
			        			Long date=LokiUtil.getDateLong(value);
			        			if (startTimeSize != null && endTimeSize != null) {
			        				if(date>=startTimeSize&&date<=endTimeSize) {
			            				list.add(value);
			            			}
			        			}else {
			        				list.add(value);
			        			}
			        			
			        			}
			        		}
			        	}
			        	
			        }
			    }
			  if(!StringUtils.isEmpty(wordFields)) {
				//排序
				list=lokiListSort(list,filterInfo);
			  }
			return list;
		} catch (Exception e) {
			throw new LokiException("loki数据处理失败",e);
		}
	}

}
