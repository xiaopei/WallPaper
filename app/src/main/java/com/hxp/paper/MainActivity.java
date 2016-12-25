package com.hxp.paper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * 壁纸设置
 * Created by Administrator on 2016/4/22.
 */
public class MainActivity extends Activity implements MyScrollView.OnScrollChangedListener,AdapterView.OnItemClickListener{
    private ArrayList<RandomOptionBean> randomTypes;
    private LinearLayout bottomLin;
    private GridView gv;
    private MyGridView gvPaper;
    private MyScrollView sc;
    private HorizontalListView horizontalListView;
    private WallPaperAdapter adapter;
    private WallOpionAdapter gOpionAdapter;
    private WallOpionAdapter hOpionAdapter;
    private int currentPosition;
    private int pageNum = 1;
    private String typeId = "";
    private String paperId = "";
    private Intent intent;
    private Context context;
    String typeString = "{\"types\":[{\"typeId\":10,\"typeName\":\"\\u98ce\\u666f\",\"selected\":1},{\"typeId\":2,\"typeName\":\"\\u660e\\u661f\",\"selected\":1},{\"typeId\":16,\"typeName\":\"\\u52a8\\u7269\",\"selected\":1},{\"typeId\":1,\"typeName\":\"\\u7f8e\\u5973\",\"selected\":1},{\"typeId\":8,\"typeName\":\"\\u6c7d\\u8f66\",\"selected\":0},{\"typeId\":17,\"typeName\":\"\\u521b\\u610f\",\"selected\":1},{\"typeId\":4,\"typeName\":\"\\u52a8\\u6f2b\",\"selected\":0},{\"typeId\":6,\"typeName\":\"\\u6e38\\u620f\",\"selected\":1},{\"typeId\":18,\"typeName\":\"\\u5176\\u5b83\",\"selected\":0}]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        ((TextView) findViewById(R.id.tv_header)).setText("壁纸设置");
        randomTypes = ((RandomOptionVo)new Gson().fromJson(typeString,RandomOptionVo.class)).types;
        ImageView setting = (ImageView)findViewById(R.id.im_webrefrence);
        setting.setImageResource(R.mipmap.paper_setting);
        setting.setVisibility(View.VISIBLE);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRandom();
            }
        });
        initView();
        intent = getIntent();
    }

    private boolean isIn = false;

    @Override
    protected void onNewIntent(Intent intent) {
        this.intent = intent;
        super.onNewIntent(intent);
    }


    private void initView() {
        gv = (GridView) findViewById(R.id.gv_hide);
        gvPaper = (MyGridView) findViewById(R.id.gv_paper);
        bottomLin = (LinearLayout) findViewById(R.id.lin_bottom);
        sc = (MyScrollView) findViewById(R.id.sc);
        sc.setOnScrollChangedListener(this);
        horizontalListView = (HorizontalListView) findViewById(R.id.horizon_listview);
        gvPaper.setAdapter(adapter = new WallPaperAdapter(this));
        horizontalListView.setOnItemClickListener(this);
        gv.setOnItemClickListener(this);
        gvPaper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                        gotoPreview(i);
            }
        });
        RandomOptionBean beanAll = new RandomOptionBean("","全部");
        randomTypes.add(0,beanAll);
        for(int i = 9;i<randomTypes.size();i++){
            randomTypes.remove(i);
        }
        gOpionAdapter = new WallOpionAdapter(context, randomTypes,1);
        hOpionAdapter = new WallOpionAdapter(context, randomTypes,0);
        gv.setAdapter(gOpionAdapter);
        horizontalListView.setAdapter(hOpionAdapter);
        if(!TextUtils.isEmpty(typeId)){
            for(int i = 1 ;i<randomTypes.size();i++){
                if(typeId.equals(randomTypes.get(i).typeId)){
                    currentPosition = i ;
                    gOpionAdapter.setPos(i);
                    hOpionAdapter.setPos(i);
                }
            }
        }
    }

    private void gotoPreview(int i) {
        Intent intent = new Intent(MainActivity.this,PreviewActivity.class);
        intent.putExtra("index",i);
        startActivity(intent);
    }

    int gvBottom;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e("hh", "onWindowFocusChanged==" + hasFocus);
        if (hasFocus) {
            gvBottom = gv.getBottom();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int upY = sc.getScrollY();
                    if (upY >= gvBottom && bottomLin.getVisibility() != View.VISIBLE) {
                        Animation animInTop = AnimationUtils.loadAnimation(context, R.anim.pop_in);
                        bottomLin.setAnimation(animInTop);
                        bottomLin.setVisibility(View.VISIBLE);
                        horizontalListView.setSelection(currentPosition);
                    } else if(upY <= gvBottom && bottomLin.getVisibility() == View.VISIBLE){
                        Animation animOutTop = AnimationUtils.loadAnimation(context,
                                R.anim.pop_out);
                        bottomLin.setAnimation(animOutTop);
                        bottomLin.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    };

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        selectType(position);
    }

    private void selectType(int position) {
        if(sc.getScrollY() > gvBottom+5){
            sc.scrollTo(0,gvBottom+5);
        }
        pageNum = 1;
        currentPosition = position;
        typeId = randomTypes.get(currentPosition).typeId;
        gOpionAdapter.setPos(currentPosition);
        hOpionAdapter.setPos(currentPosition);
    }


    private void setRandom() {
        new WallPaperDialog(context,randomTypes).setYesButton(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pageNum > 1){
                    pageNum = 1;
                }
            }
        }).show();
    }
}
