package com.example.ucindex.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class TouchListView extends ListView {

	public TouchListView(Context context) {
		super(context);
	}


	public TouchListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TouchListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		if(touchEventListener != null ){
			touchEventListener.onTouchEvent(ev);
		}
		
		return super.onTouchEvent(ev);
	}

	
	private OnTouchEventListener touchEventListener;
	
	public void setOnTouchEventListener(OnTouchEventListener touchEventListener){
		this.touchEventListener = touchEventListener;
	}
	
	public interface OnTouchEventListener{
		
		public void onTouchEvent(MotionEvent ev);
		
	}
	
}
