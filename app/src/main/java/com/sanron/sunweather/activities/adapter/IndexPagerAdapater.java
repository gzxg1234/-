package com.sanron.sunweather.activities.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;

import com.sanron.sunweather.R;
import com.sanron.sunweather.common.IconProvider;
import com.sanron.sunweather.entity.Weather;
import com.sanron.sunweather.entity.Weather.LifeIndex;

/**
 * 生活指数页面
 *
 * @author Sanron
 */
public class IndexPagerAdapater extends PagerAdapter {
    private Context context;
    private List<ListView> views;
    private List<LifeIndexAdapter> adapters;
    private List<Weather> data;
    public final String[] TITLES = new String[]{"今天", "明天", "后天"};

    public IndexPagerAdapater(Context context) {
        this.context = context;
        this.views = new ArrayList<ListView>();
        this.adapters = new ArrayList<LifeIndexAdapter>();

        for (int i = 0; i < 3; i++) {
            ListView listView = new ListView(context);
            listView.setClipToPadding(false);
            listView.setPadding(0,
                    context.getResources().getDimensionPixelSize(R.dimen.cloud_height),
                    0,
                    context.getResources().getDimensionPixelSize(R.dimen.cloud_height));
            listView.setVerticalScrollBarEnabled(false);
            listView.setDividerHeight(0);

            LifeIndexAdapter adapter = new LifeIndexAdapter();
            listView.setAdapter(adapter);

            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            listView.setLayoutParams(lp);

            adapters.add(adapter);
            views.add(listView);
        }
    }

    public void setData(List<Weather> data) {
        this.data = data;
        notifyDataSetChanged();
        for (int i = 0; i < adapters.size(); i++) {
            adapters.get(i).setData(data.get(i).getLifeIndexs());
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : Math.min(data.size(), 3);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    public class LifeIndexAdapter extends BaseAdapter {

        private List<LifeIndex> data;

        public void setData(List<LifeIndex> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LifeIndex indexItem = data.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.list_item_lifeindex, parent, false);
            }
            ImageView ivIndex = (ImageView) convertView
                    .findViewById(R.id.iv_index);
            TextView tvIndexName = (TextView) convertView
                    .findViewById(R.id.tv_index_name);
            TextView tvIndexValue = (TextView) convertView
                    .findViewById(R.id.tv_index_value);
            TextView tvDetail = (TextView) convertView
                    .findViewById(R.id.tv_index_detail);
            tvIndexName.setText(indexItem.getName() + " :");
            tvIndexValue.setText(" " + indexItem.getValue());
            tvDetail.setText(indexItem.getDetail());
            Integer icon = IconProvider.getLifeIndexIcon(indexItem.getName());
            if (icon != null) {
                ivIndex.setImageResource(icon);
            }
            return convertView;
        }
    }
}