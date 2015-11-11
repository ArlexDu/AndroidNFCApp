package edu.happy.mynfcapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.happy.tools.ApplicationInfo;
import edu.happy.tools.MyListAdapter;

public class IntstalledApplicationListActivity extends Activity implements OnItemClickListener{

	private ArrayList<ApplicationInfo> list = new ArrayList<ApplicationInfo>();
	private ListView listview ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_app_list);
		listview = (ListView)findViewById(R.id.applist);
		PackageManager packageManager = getPackageManager();
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(packageManager.GET_ACTIVITIES);
		for(PackageInfo packageInfo:packageInfos){
			ApplicationInfo info = new ApplicationInfo();
			info.setAppIcon(packageInfo.applicationInfo.loadIcon(getPackageManager()));
			info.setAppName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
			info.setPackageName(packageInfo.packageName);
			list.add(info);
		}
		MyListAdapter adapter = new MyListAdapter(this, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("package_name", list.get(position).getPackageName());
		setResult(0,intent);
		finish();
	}
	
	//÷ÿ–¥∑µªÿº¸£¨±‹√‚∑µªÿø’÷∏’Î¥ÌŒÛ
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
