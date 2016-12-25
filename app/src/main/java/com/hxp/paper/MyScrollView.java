package com.hxp.paper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 
 * @author Darcy
 *
 */
public class MyScrollView extends ScrollView {
	private long downTime;

	private OnScrollChangedListener mScrollListener;

	public MyScrollView(Context context) {
		super(context);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnScrollChangedListener(OnScrollChangedListener l){
		this.mScrollListener = l;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(mScrollListener != null)
			mScrollListener.onScrollChanged(l, t, oldl, oldt);
	}

	public interface OnScrollChangedListener{
		void onScrollChanged(int l, int t, int oldl, int oldt);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				downTime = System.currentTimeMillis();
				break;
			case MotionEvent.ACTION_UP:
				if(System.currentTimeMillis() - downTime < 500){
					return false;
				}
				break;
		}
		return super.onTouchEvent(ev);
	}
}
