package com.hxp.paper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 提示需要绑定手机的提示框
 * @author Administrator
 *
 */
public class WallPaperDialog extends Dialog implements DialogInterface {
	private Button btn_ok,btn_no;
	private GridView gridView;
	private View selectAll;
	ArrayList<RandomOptionBean> randomTypes;
	RandomOpionAdapter adapter;
	String selectedTypeString;
	String selectedNames;
	int selectedNum;
	private Context context;

	public WallPaperDialog(final Context context, ArrayList<RandomOptionBean> randomTypes1) {
		super(context,R.style.Dialog_Fullscreen);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		setContentView(R.layout.dialog_wall_paper);
		this.context = context;
//		WindowManager.LayoutParams lp = getWindow().getAttributes();
//		lp.width = (int) (DRConst.SCREEN_WIDTH - 50*DRConst.SCREEN_DPI);
//		lp.height = (int)(lp.width*0.8);
//		getWindow().setAttributes(lp);
		gridView = (GridView) findViewById(R.id.gridview);
		this.randomTypes = randomTypes1;
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				randomTypes.get(i).selected = "1".equals(randomTypes.get(i).selected)?"0":"1";
				adapter.notifyDataSetChanged();
			}
		});
		gridView.setAdapter(adapter = new RandomOpionAdapter(context,randomTypes));
		selectAll = findViewById(R.id.select_all);
		selectAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			for (RandomOptionBean bean : randomTypes) {
				bean.selected = "1";
				adapter.notifyDataSetChanged();
			}
			}
		});
		btn_ok = (Button) findViewById(R.id.btn_default_ok);
		btn_no = (Button) findViewById(R.id.btn_default_no);
		btn_no.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WallPaperDialog.this.dismiss();
			}
		});
	}

	public WallPaperDialog setEnsure(CharSequence data) {
		btn_ok.setText(data);
		return this;
	}

	public WallPaperDialog setCancel(CharSequence data) {
		btn_no.setText(data);
		return this;
	}

	public WallPaperDialog setNoButton(final View.OnClickListener onClickListener) {
		if (btn_no != null) {
			btn_no.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onClickListener.onClick(v);
					dismiss();
				}
			});
		}
		return this;
	}

	public WallPaperDialog setYesButton(final View.OnClickListener onClickListener) {
		if (btn_ok != null) {
			btn_ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					if(null != randomTypes) {
						StringBuilder selectedType = new StringBuilder();
						StringBuilder selectedTypeNames = new StringBuilder();
						for (RandomOptionBean bean : randomTypes) {
							if ("1".equals(bean.selected)) {
								selectedType.append(bean.typeId).append(",");
								selectedTypeNames.append(bean.typeName).append("、");
								selectedNum++;
							}
						}
						if (selectedType.length() > 0) {
							selectedTypeString = selectedType.substring(0, selectedType.length() - 1);
							selectedNames = selectedTypeNames.substring(0, selectedTypeNames.length() - 1);

						} else {
							Toast.makeText(context,"请至少选择一种壁纸类型",Toast.LENGTH_LONG).show();
						}
					}
				}

			});
		}
		return this;
	}


	@Override
	public void show() {
		try {
			super.show();
		} catch (WindowManager.BadTokenException e) {
		}
	}
}