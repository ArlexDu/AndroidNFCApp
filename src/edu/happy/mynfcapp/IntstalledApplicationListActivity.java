package edu.happy.mynfcapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import edu.happy.tools.ApplicationInfo;
import edu.happy.tools.MyListAdapter;

public class IntstalledApplicationListActivity extends Activity implements OnItemClickListener{

	private ArrayList<ApplicationInfo> list = new ArrayList<ApplicationInfo>();
	private ListView listview ;
	private ProgressBar bar;
	Handler myHander = new Handler(){
		
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				init();
			}
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		System.out.println("list show");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_app_list);
		listview = (ListView)findViewById(R.id.applist);
		bar = (ProgressBar)findViewById(R.id.list_bar);
		new Thread(new Runnable() {
			public void run() {
				getdata();
				Message msg = Message.obtain();
				msg.what = 0;
				// 发送这个消息到消息队列中
				myHander.sendMessage(msg);
			}
		}).start();
	}
	private void init(){
		bar.setVisibility(View.INVISIBLE);
		MyListAdapter adapter = new MyListAdapter(this, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}
	private void getdata(){
		PackageManager packageManager = getPackageManager();
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(packageManager.GET_ACTIVITIES);
		for(PackageInfo packageInfo:packageInfos){
			ApplicationInfo info = new ApplicationInfo();
			info.setAppIcon(packageInfo.applicationInfo.loadIcon(getPackageManager()));
			info.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
			info.setPackageName(packageInfo.packageName);
			list.add(info);
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("package_name", list.get(position).getPackageName());
		intent.putExtra("appname", list.get(position).getAppName());
		setResult(0,intent);
		finish();
	}
	
	//重写返回键，避免返回空指针错误
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if(keyCode == KeyEvent.KEYCODE_BACK){
				Intent intent = new Intent();
				intent.putExtra("package_name", "");
				setResult(0,intent);
				finish();
				return true;
			}else{
				return super.onKeyDown(keyCode, event);	
			}
		}

}
