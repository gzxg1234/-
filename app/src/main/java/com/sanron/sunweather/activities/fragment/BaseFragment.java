package com.sanron.sunweather.activities.fragment;

import com.sanron.sunweather.entity.WeatherData;

import de.greenrobot.event.EventBus;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment extends Fragment{
	
	protected EventBus eventBus;
	protected WeatherData weatherData;
	protected Handler handler = new Handler(Looper.getMainLooper());
	protected boolean dataChanged = true;//数据是否发生改变

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		eventBus = EventBus.getDefault();
	}
	
	public void setWeatherData(WeatherData weatherData){
		if(this.weatherData != weatherData ){
			dataChanged = true;
		}
		this.weatherData = weatherData;
	}
	
	public void refreshViews(){
		if(dataChanged){
			handler.post(new Runnable() {
				@Override
				public void run() {
					updateViews();
					dataChanged = false;
				}
			});
		}
	}
	protected abstract void updateViews();
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
