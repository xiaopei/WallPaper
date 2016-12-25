package com.hxp.paper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 横向锁屏设置
 * @author Administrator
 *
 */
public class WallPaperAdapter extends BaseAdapter {
//	public static int[] strings = new int[]{R.mipmap.fengjing,R.mipmap.dongwu,R.mipmap.katongdongman,R.mipmap.qiche,R.mipmap.meinv,R.mipmap.katongdongma1,
//							R.mipmap.gaoxiao,R.mipmap.dongwu1,R.mipmap.mingxing,R.mipmap.youxi,R.mipmap.dongwu2,R.mipmap.gaoxiao1};

	private int pos = 0;
	public static int[] paper =  new int[]{R.mipmap.paper1,R.mipmap.paper2,R.mipmap.paper3};
	private Context mContext;

	public WallPaperAdapter(Context mContext){
		this.mContext = mContext;
	}
	
	public void setPos(int index){
		this.pos = index;
	}



	@Override
	public int getCount() {
		return paper.length;
	}

	@Override
	public Object getItem(int position) {
		return paper[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.item_wall_paper,null);
			holder = new ViewHolder();
			holder.btn_money = (ImageView) view.findViewById(R.id.btn_money);
			holder.btn_select = (ImageView) view.findViewById(R.id.btn_select);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)holder.btn_money.getLayoutParams();
		lp.width = (int) ((1080 - 30*3)/2);
		lp.height = lp.width* 1920/1080;
		holder.btn_money.setLayoutParams(lp);
		 holder.btn_money.setImageResource(paper[position]);
		return view;
	}

	class ViewHolder {
		ImageView btn_money,btn_select;
	}

}
