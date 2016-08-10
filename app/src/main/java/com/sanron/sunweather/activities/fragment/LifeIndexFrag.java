package com.sanron.sunweather.activities.fragment;

import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanron.sunweather.R;
import com.sanron.sunweather.activities.adapter.IndexPagerAdapater;
import com.sanron.sunweather.entity.Weather;
import com.viewpagerindicator.TitlePageIndicator;

/**
 * 生活指数
 * 
 * @author 三荣
 * 
 */
public class LifeIndexFrag extends BaseFragment {

	private View contentView;
	private ViewPager pager;
	private IndexPagerAdapater pageAdapter;
	private TextView tvCity;
	private TitlePageIndicator indicator;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initViews(inflater);
		return contentView;
	}

	public void initViews(LayoutInflater inflater){
		contentView = inflater.inflate(R.layout.fragment_life_index, null);
		tvCity = (TextView) contentView.findViewById(R.id.tv_city);

		pager = (ViewPager) contentView.findViewById(R.id.pager_life_index);
		pageAdapter = new IndexPagerAdapater(getActivity());
		pager.setAdapter(pageAdapter);
		
		indicator = (TitlePageIndicator) contentView.findViewById(R.id.pager_indicator);
		indicator.setViewPager(pager);
		
		updateViews();
	}
	

	@Override
	protected void updateViews() {
		tvCity.setText(weatherData.getCity()+" 生活指数");
		
		int start = weatherData.getWeatherIndexByDate(new Date());
		List<Weather> weathers = weatherData.getWeathers();
		pageAdapter.setData(
				weathers.subList(
						start,Math.min(start+3,weathers.size()-1)));
	}
	
}
