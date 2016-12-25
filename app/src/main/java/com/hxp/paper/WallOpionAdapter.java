package com.hxp.paper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * 横向锁屏设置
 * @author Administrator
 *
 */
public class WallOpionAdapter extends BaseAdapter {
	private ArrayList<RandomOptionBean> strings;
	private int pos = 0;
	private Context mContext;
	private int type;
	private final int HOPION = 0 ;

	public WallOpionAdapter(Context mContext, ArrayList<RandomOptionBean> moneys, int type){
		this.strings = moneys;
		this.mContext = mContext;
		this.type = type;
	}
	
	public void setPos(int index){
		this.pos = index;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return strings.size();
	}

	@Override
	public Object getItem(int position) {
		return strings.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if(type == HOPION){
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.item_money,null);
			holder = new ViewHolder();
			holder.btn_money = (TextView) view.findViewById(R.id.btn_money);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.btn_money.getLayoutParams();
			lp.width = 1080/5;
			lp.height = (int) (40*3);
			if(position == pos){
				holder.btn_money.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
			}else{
				holder.btn_money.setTextColor(mContext.getResources().getColor(R.color.textcolor_gray));
			}
			holder.btn_money.setLayoutParams(lp);
		}else {
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(R.layout.item_option,null);
				holder = new ViewHolder();
				holder.btn_money = (TextView) view.findViewById(R.id.btn_money);
				holder.btn_select = (TextView) view.findViewById(R.id.btn_select);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.btn_money.getLayoutParams();
			lp.width = (int) ((1080 - 50 * 3) / 3);
			lp.height = (int) (40 * 3);
			holder.btn_money.setLayoutParams(lp);
			holder.btn_select.setLayoutParams(lp);
			if(position == pos){
				holder.btn_select.setVisibility(View.VISIBLE);
			}else{
				holder.btn_select.setVisibility(View.GONE);
			}
		}
		holder.btn_money.setText(strings.get(position).typeName);
		return view;
	}

	class ViewHolder {
		TextView btn_money,btn_select;
	}

}
