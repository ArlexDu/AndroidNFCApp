package edu.happy.tools;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.happy.mynfcapp.R;

public class MyListAdapter extends BaseAdapter {
	
	private ArrayList<ApplicationInfo> ListItems;
	
	private Context context;
	
	private LayoutInflater listcontainer;
	
	public MyListAdapter(Context con,ArrayList<ApplicationInfo> list) {
		// TODO Auto-generated constructor stub
		this.context = con;
		ListItems = list;
		listcontainer = LayoutInflater.from(context);
	}

	//计算一共有多少条目
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ListItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			convertView = listcontainer.inflate(R.layout.applist_layout, null);
			holder = new ViewHolder();
			holder.appIcon =(ImageView) convertView.findViewById(R.id.app_icon);
			holder.appName=(TextView) convertView.findViewById(R.id.app_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.appIcon.setBackground((ListItems.get(position).getAppIcon()));
		holder.appName.setText(ListItems.get(position).getAppName().toString());
		return convertView;
	}
	
	
	public class ViewHolder{
		public ImageView appIcon;
		public TextView appName;
	}

}
