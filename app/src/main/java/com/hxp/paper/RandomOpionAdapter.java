package com.hxp.paper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * 微信提现记录适配器
 * @author Administrator
 *
 */
public class RandomOpionAdapter extends BaseAdapter {
	private ArrayList<RandomOptionBean> randomTypes;
	private Context mContext;

	public RandomOpionAdapter(Context mContext, ArrayList<RandomOptionBean> moneys){
		this.randomTypes = moneys;
		this.mContext = mContext;
	}
	

	@Override
	public int getCount() {
		return randomTypes.size();
	}

	@Override
	public Object getItem(int position) {
		return randomTypes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.item_money,null);
			holder = new ViewHolder();
			holder.btn_money = (TextView) view.findViewById(R.id.btn_money);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.btn_money.getLayoutParams();
		lp.width = (int) ((1080 - 80*3)/3);
		holder.btn_money.setLayoutParams(lp);
		if("1".equals(randomTypes.get(position).selected)){
			holder.btn_money.setBackgroundResource(R.drawable.selected_option);
		}else{
			holder.btn_money.setBackgroundResource(R.drawable.bg_white_round);
		}
		holder.btn_money.setText(randomTypes.get(position).typeName);
		return view;
	}

	class ViewHolder {
		TextView btn_money;
	}
}
