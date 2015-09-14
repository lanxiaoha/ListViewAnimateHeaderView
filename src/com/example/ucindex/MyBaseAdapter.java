package com.example.ucindex;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyBaseAdapter extends BaseAdapter{
	
	private List<String> list;
	private Context context;
	public MyBaseAdapter(Context context ,List<String> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LinearLayout item =  (LinearLayout) View.inflate(context, R.layout.view_item, null);
		TextView textView = (TextView) item.findViewById(R.id.tv_item);
		textView.setText(list.get(position));
		
		return item;
	}
	
}
