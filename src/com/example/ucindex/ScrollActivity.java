package com.example.ucindex;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ucindex.widget.TouchListView;
import com.example.ucindex.widget.TouchListView.OnTouchEventListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class ScrollActivity extends Activity {

	private LinearLayout headerLinearLayout;
	/** 继承原始ListView */
	private TouchListView listView;
	private MyBaseAdapter adapter;
	/** headLinearLayout的高度 */
	private int initHeadLinearLayout;
	/** 第一個可見的Item位置 */
	int firstVisibleItem = 0;
	private Button btOk;

	private static String TAG = ScrollActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scroll);
		initView();
		initData();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		super.onWindowFocusChanged(hasFocus);
		// 获得LinearLayout的高度
		initHeadLinearLayout = headerLinearLayout.getMeasuredHeight();
	}

	private void initView() {
		headerLinearLayout = (LinearLayout) findViewById(R.id.contain);
		btOk = (Button) findViewById(R.id.bt_Ok);
		listView = (TouchListView) findViewById(R.id.listView);

		btOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(ScrollActivity.this, "哈哈", 1000).show();
			}
		});
	}

	private void initData() {

		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 30; i++) {
			list.add("项目：" + i);
		}

		adapter = new MyBaseAdapter(this, list);
		listView.setAdapter(adapter);
		// 设置Scroll监听器
		MyOnScrollListener listener = new MyOnScrollListener();
		listView.setOnScrollListener(listener);
		// 设置OnTouch监听器
		MyOnTouchEventListener touchEventListener = new MyOnTouchEventListener();
		listView.setOnTouchEventListener(touchEventListener);
	}

	
	/** 保存down时的y值 */
	private float down_y = 0;
	/** 保存move时的y值 */
	private float move_y = 0;
	/** 判断是否touch */
	private boolean isOnTouch = false;

	class MyOnTouchEventListener implements OnTouchEventListener {

		@Override
		public void onTouchEvent(MotionEvent ev) {

			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 按下
				ScrollActivity.this.isOnTouch = true;
				down_y = ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:

				float y = ev.getY();
				if (Math.abs(y - down_y) > 8) {
					move_y = y;
					slipe(down_y, move_y);
				}

				break;
			case MotionEvent.ACTION_UP:
				down_y = 0;
				move_y = 0;
				ScrollActivity.this.isOnTouch = false;
				animation();
				break;

			default:
				break;
			}

		}

	}

	/**
	 * 设置LinearLayout的透明度
	 * 
	 * @param down_y
	 * @param move_y
	 */
	private void slipe(float down_y, float move_y) {

		if ((down_y - move_y) > 0 && this.firstVisibleItem == 0) {//向上滑动且ListView的第一个可见Item是第1个。

			LayoutParams params = (LayoutParams) headerLinearLayout.getLayoutParams();
			//重新计算LinearLayout高度
			params.height = params.height - 15;
			if (params.height <= 0) {
				params.height = 0;
			}
			headerLinearLayout.setLayoutParams(params);

		} else if ((down_y - move_y) < 0 && this.firstVisibleItem == 0) {

			LayoutParams params = (LayoutParams) headerLinearLayout
					.getLayoutParams();
			// 这边的计算就应该要用“加减”不能用“乘除”，否则会很卡，因为“乘除”计算很耗时间
			params.height = params.height + 15;
			if (params.height >= initHeadLinearLayout) {
				params.height = initHeadLinearLayout;
			}
			headerLinearLayout.setLayoutParams(params);

		}

	}
	/**
	 * 在touch时：ACTION_UP时，把LinearLayout剩余的高度用动画来重绘。
	 */
	private void animation() {

		LayoutParams params = (LayoutParams) headerLinearLayout.getLayoutParams();
		if (params.height < (initHeadLinearLayout / 2)) {
			// 缩短动画
			ObjectAnimator anim = ObjectAnimator.ofInt(headerLinearLayout,"height", params.height, 0);
			anim.setDuration(200);
			anim.setInterpolator(new AccelerateDecelerateInterpolator());
			anim.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator valueAnim) {
					LayoutParams params = (LayoutParams) headerLinearLayout
							.getLayoutParams();
					params.height = (Integer) valueAnim.getAnimatedValue();
					headerLinearLayout.setLayoutParams(params);
				}
			});
			anim.start();

		} else {
			// 伸长动画

			ObjectAnimator anim = ObjectAnimator.ofInt(headerLinearLayout,
					"height", params.height, initHeadLinearLayout);
			anim.setDuration(200);
			anim.setInterpolator(new AccelerateDecelerateInterpolator());
			anim.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator valueAnim) {
					LayoutParams params = (LayoutParams) headerLinearLayout
							.getLayoutParams();
					params.height = (Integer) valueAnim.getAnimatedValue();
					headerLinearLayout.setLayoutParams(params);
				}
			});
			anim.start();
		}
	}
	
	

	class MyOnScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

			if (ScrollActivity.this.isOnTouch == false) {
				// 如果没有touch，则获得是否第一个显示的Item位置
				ScrollActivity.this.firstVisibleItem = firstVisibleItem;
			}

		}

	}

}
