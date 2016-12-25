package com.hxp.paper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 * @author Administrator
 */
public class PreviewActivity extends Activity implements View.OnClickListener,
		OnPageChangeListener {
	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	private int currentItem;
	private View top,left,right,leftL,rightL;
	private Context context;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String message = msg.getData().getString("msg");
			String title = msg.getData().getString("title");
			new AlertDialog.Builder(context).setMessage(message).setTitle(title).setNegativeButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					finish();
				}
			}).show();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		currentItem = getIntent().getIntExtra("index",0);
		context = this;
		views = new ArrayList<View>();
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		for (int i = 0; i < WallPaperAdapter.paper.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setScaleType(ScaleType.CENTER_CROP);
			iv.setLayoutParams(mParams);
			iv.setOnClickListener(listener);
			if (i == currentItem && WallPaperAdapter.paper != null && WallPaperAdapter.paper.length>0){
				iv.setImageResource(WallPaperAdapter.paper[i]);
			}
			views.add(iv);
		}

		vp = (ViewPager) findViewById(R.id.viewpager_guide);
		vpAdapter = new ViewPagerAdapter(context,views);
		vp.setAdapter(vpAdapter);
		setCurView(currentItem);
		vp.setOnPageChangeListener(this);
		findViewById(R.id.set2desktop).setOnClickListener(this);
		top = findViewById(R.id.topPanel);
		left = findViewById(R.id.last_page);
		right = findViewById(R.id.next_page);
		leftL = findViewById(R.id.last_page1);
		rightL = findViewById(R.id.next_page1);
		left.setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.back1).setOnClickListener(this);
		right.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				showPop();
			}
		},1000);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				disPop();
			}
		},1500);
		super.onResume();
	}


	boolean isDestroy = false;

	@Override
	public void onDestroy() {
		System.gc();
		super.onDestroy();
		isDestroy = true;
	}

	View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if(top.getVisibility() == View.VISIBLE){
				disPop();
			}else{
				showPop();
			}
		}
	};

	private void setCurView(int position) {
		if (position < 0 || position >= WallPaperAdapter.paper.length) {
			return;
		}
		vp.setCurrentItem(position);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	private void redrawArraws(int arg0) {
		if ((WallPaperAdapter.paper.length - 1) == arg0) {
			right.setVisibility(View.GONE);
		}else{
			right.setVisibility(View.VISIBLE);
		}
		if (0 == arg0){
			left.setVisibility(View.GONE);
		}else{
			left.setVisibility(View.VISIBLE);
		}
		disPop();
	}

	boolean needSetLock = true;
	boolean needSetWall = true;
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.set2desktop:
				needSetLock = false;
				setWall2(false);
				break;
			case R.id.back:
				finish();
				break;
			case R.id.back1:
				finish();
				break;
			case R.id.last_page:
				setCurView(currentItem - 1);
				break;
			case R.id.next_page:
				setCurView(currentItem + 1);
				break;
//			case R.id.share:
//				Bundle bundle = new Bundle();
//				bundle.putString("title", context.getString(R.string.share_title));
//				bundle.putString("url", PreferencesUtils.getString("shareUrl", ""));
//				bundle.putString("des", context.getString(R.string.share_description_1) + PreferencesUtils.getString("userid", "") + context.getString(R.string.share_description_2));
////				bundle.putString("image", WallPaperAdapter.paper.get(currentItem).image);
////				bundle.putString("image", new File(DRFileSystem.getSDPath()+"DCIM/P60414-170352.jpg").getAbsolutePath());
//				bundle.putString("image", DRConst.ICON_URL);
//				new DialogShare(context,bundle,1).show();
//				break;
		}
	}

	private void showPop(){
		Animation animInTop = AnimationUtils.loadAnimation(context, R.anim.pop_in);
		top.setAnimation(animInTop);
		top.setVisibility(View.VISIBLE);
		Animation animInLeft = AnimationUtils.loadAnimation(context, R.anim.left_in);
		leftL.setAnimation(animInLeft);
		leftL.setVisibility(View.VISIBLE);
		Animation animInRight = AnimationUtils.loadAnimation(context, R.anim.right_in);
		rightL.setAnimation(animInRight);
		rightL.setVisibility(View.VISIBLE);
	}

	private void disPop(){
		Animation animOutTop = AnimationUtils.loadAnimation(context,
				R.anim.pop_out);
		top.setAnimation(animOutTop);
		top.setVisibility(View.GONE);
		Animation animInLeft = AnimationUtils.loadAnimation(context, R.anim.left_out);
		leftL.setAnimation(animInLeft);
		leftL.setVisibility(View.GONE);
		Animation animInRight = AnimationUtils.loadAnimation(context, R.anim.right_out);
		rightL.setAnimation(animInRight);
		rightL.setVisibility(View.GONE);
	}



	public void setWall2(final boolean isBoth) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int path = WallPaperAdapter.paper[currentItem];
				Bitmap bitmap = decodeSampledBitmapFromResource(getResources(),path,1080,1920);
				WallpaperManager wallpaperManager = WallpaperManager.getInstance(PreviewActivity.this);
				try {
					wallpaperManager.setBitmap(bitmap);
					sendMsg("","桌面壁纸设置成功！");
				} catch (IOException e1) {
					Toast.makeText(context,"设置失败，请重试",Toast.LENGTH_LONG).show();
					e1.printStackTrace();
				}
				// 新建一个URL对象,网络图片设为壁纸
//				URL url = null;
//				try {
//					url = new URL(path);
//					// 打开一个HttpURLConnection连接
//					HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//					// 设置连接超时时间
//					urlConn.setConnectTimeout(5 * 1000);
//					// 开始连接
//					urlConn.connect();
//					// 判断请求是否成功
//					if (urlConn.getResponseCode() == 200) {
//						// 获取返回的数据
//						InputStream inputStream = urlConn.getInputStream();
//						setWallpaper(inputStream);
//						if(isBoth){
//							if(!needSetLock){
//								sendMsg("1分钟后设置生效","锁屏壁纸和桌面壁纸设置成功！");
//							}
//						}else{
//							sendMsg("","桌面壁纸设置成功！");
//						}
//						needSetWall = false;
//					}
//				}catch(Exception e){
//					Toast.makeText(context,"设置失败，请重试",Toast.LENGTH_LONG).show();
//					e.printStackTrace();
//				}
			}
		}).start();
	}

	private void sendMsg(String mm, String title) {
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString("msg",mm);
		data.putString("title",title);
		msg.setData(data);
		handler.sendMessage(msg);
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
		// 先把inJustDecodeBounds设置为true 取得原始图片的属性
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// 然后算一下我们想要的最终的属性
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 在decode的时候 别忘记直接 把这个属性改为false 否则decode出来的是null
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 先从options 取原始图片的 宽高
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			//一直对这个图片进行宽高 缩放，每次都是缩放1倍，然后这么叠加，当发现叠加以后 也就是缩放以后的宽或者高小于我们想要的宽高
			//这个缩放就结束 跳出循环 然后就可以得到我们极限的inSampleSize值了。
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		redrawArraws(position);
	}

	@Override
	public void onPageSelected(int arg0) {
		currentItem = arg0;
		if(WallPaperAdapter.paper != null && WallPaperAdapter.paper.length>0){
			try {
				((ImageView)views.get(arg0)).setImageResource(WallPaperAdapter.paper[arg0]);
			} catch (OutOfMemoryError e) {
			}
		}
	}

}
