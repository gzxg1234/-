package com.sanron.sunweather.activities.fragment;

import java.lang.reflect.Field;
import java.util.Date;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sanron.sunweather.R;
import com.sanron.sunweather.entity.AirEnvironment;
import com.sanron.sunweather.utils.ChineseCalendar;
import com.sanron.sunweather.utils.DateUtils;

/**
 * 显示其他天气信息（空气环境，日起日落..）
 * @author 三荣
 *
 */
public class ExtrasFrag extends BaseFragment{

	private View contentView;
	private TextView tvDate;
	private TextView tvLunarDate;
	private TextView tvWeekday;
	private RelativeLayout rlAqi; 
	private TextView tvAqiText;
	private TextView tvPM25;
	private TextView tvPM10;
	private TextView tvSO2;
	private TextView tvNO2;
	private SeekBar aqiSeek;
	private TextView tvNoAqi;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initView(inflater);
		return contentView;
	}

	private void initView(LayoutInflater inflater){
		contentView = inflater.inflate(R.layout.fragment_extras,null);
		tvDate = (TextView) contentView.findViewById(R.id.tv_date);
		tvLunarDate = (TextView) contentView.findViewById(R.id.tv_lunar_date);
		tvWeekday = (TextView) contentView.findViewById(R.id.tv_weekday);
		tvNoAqi = (TextView) contentView.findViewById(R.id.tv_noaqi);
		
		rlAqi = (RelativeLayout) contentView.findViewById(R.id.rl_aqi);
		aqiSeek = (SeekBar) rlAqi.findViewById(R.id.seek_aqi);
		setSeekBarUserSeekable(aqiSeek);
		tvAqiText = (TextView) rlAqi.findViewById(R.id.tv_aqi_text);
		tvPM25 = (TextView) rlAqi.findViewById(R.id.tv_pm25);
		tvPM10 = (TextView) rlAqi.findViewById(R.id.tv_pm10);
		tvSO2 = (TextView) rlAqi.findViewById(R.id.tv_so2);
		tvNO2 = (TextView) rlAqi.findViewById(R.id.tv_no2);
		
		updateViews();
	}
	
	//设置用户不可拖动seekbar
	private void setSeekBarUserSeekable(SeekBar seekBar){
		try {
			Field field = SeekBar.class.getSuperclass().getDeclaredField("mIsUserSeekable");
			field.setAccessible(true);
			field.set(seekBar, false);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void updateViews() {
		
		Date date = new Date();
		ChineseCalendar chineseCalendar = new ChineseCalendar(date);
		tvDate.setText(DateUtils.format(date, "yyyy年MM月dd号"));
		tvWeekday.setText(DateUtils.format(date, "E"));
		tvLunarDate.setText(
				chineseCalendar.getChinese(ChineseCalendar.CHINESE_HEAVENLY_STEM)+
				chineseCalendar.getChinese(ChineseCalendar.CHINESE_EARTHLY_BRANCH)+
				chineseCalendar.getChinese(ChineseCalendar.CHINESE_ZODIAC)+"年 "+
				chineseCalendar.getChinese(ChineseCalendar.CHINESE_MONTH)+
				chineseCalendar.getChinese(ChineseCalendar.CHINESE_DATE)
				);

		AirEnvironment airEnvironment = weatherData.getAirEnvironment();
		if(airEnvironment==null){
			tvNoAqi.setVisibility(View.VISIBLE);
			rlAqi.setVisibility(View.GONE);
		}else{
			tvNoAqi.setVisibility(View.GONE);
			rlAqi.setVisibility(View.VISIBLE);
			
			aqiSeek.setProgress(airEnvironment.getAqi());
			tvAqiText.setText("空气质量 : "+airEnvironment.getQuality()+
					"("+airEnvironment.getAqi()+")");
			tvPM25.setText(airEnvironment.getPm25()+"");
			tvPM10.setText(airEnvironment.getPm10()+"");
			tvSO2.setText(airEnvironment.getSo2()+"");
			tvNO2.setText(airEnvironment.getNo2()+"");
		}
	}
}



