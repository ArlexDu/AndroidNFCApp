package edu.happy.mynfcapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.Keyboard.Key;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class WriteActivity extends Activity {

	private EditText input_text;
	private Button ok;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write);
		input_text = (EditText) findViewById(R.id.input_text);
		ok = (Button) findViewById(R.id.done);
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch(v.getId()){
	        case R.id.done:
	        	Intent intent = new Intent();
	        	String text = input_text.getText().toString();
	        	if(IsUri(text)){
	        		intent.putExtra("Uri", text);
					setResult(0,intent);
					finish();
					break;	
	        	}else{
	        		intent.putExtra("text", text);
					setResult(1,intent);
					finish();
					break;	
	        	}
			}
      }
	//用于区分当前输入内容是否是url格式
	private boolean IsUri(String s){	
		 Pattern p = Pattern.compile("^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)"
		 		+ "(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)"
		 		+ "*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$",
		 		Pattern.CASE_INSENSITIVE );   
		 Matcher matcher =p.matcher(s);
		 boolean is = matcher.matches();
		 return  is;
	}
	
	//重写返回键，避免返回空指针错误
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent();
        	String text = "";
			intent.putExtra("text", text);
			setResult(1,intent);
			finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);	
		}
	}
}
