package edu.happy.mynfcapp;

import java.nio.charset.Charset;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private TextView info;//用于承载信息的控件
	private Button write;//按钮，点击进入写入模式
	private int WRITE_CODE = 1;//activity之间通讯的区分值
	private String mText;//暂时存储写入信息的全局变量
    private AlertDialog mDialog;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		去掉标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mian_layout);
		info = (TextView)findViewById(R.id.info_show);
		write = (Button)findViewById(R.id.write);
		write.setOnClickListener(this);
		 // mTagContent = (LinearLayout) findViewById(R.id.list);
//        resolveIntent(getIntent());
       
        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        //没有nfc硬件服务
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            return;
        }
        /*
         * PendingIntent 的理解:
         * 
         * pendingIntent是一种特殊的Intent。主要的区别在于Intent的执行立刻的，
         * 而pendingIntent的执行不是立刻的。pendingIntent执行的操作实质上是参
         * 数传进来的Intent的操作，但是使用pendingIntent的目的在于它所包含的
         * Intent的操作的执行是需要满足某些条件的。
         */
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
       
	}
	
	   //  展示没有nfc的窗口
    private void showMessage(int title, int message) {
        mDialog.setTitle(title);
        mDialog.setMessage(getText(message));
        mDialog.show();
    }
	
// 点击事件的处理
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		点击写入则跳入写入界面
        switch(v.getId()){
        case R.id.write:
        	Intent intent = new Intent(this, WriteActivity.class);
			startActivityForResult(intent, WRITE_CODE);
			break;
		}
	}
	
//	activity之间通讯数据的处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == WRITE_CODE){
			mText = data.getStringExtra("input_data");
			info.setText(mText);
		}
	}
    //当窗口的创建模式是singleTop或singleTask时调用，用于取代onCreate方法
    //当NFC标签靠近手机，建立连接后调用
    @Override
    public void onNewIntent(Intent intent) {
        //如果未设置要写入的文本，则读取标签上的文本数据
    	mText = "我差点就信啦！";
        if (mText == null) {
//            Intent myIntent = new Intent(this, ShowNFCTagContentActivity.class);
            //将intent传入另一个窗口，显示界面窗口 
//            myIntent.putExtras(intent);
            //需要指定这个Action，传递Intent对象时，Action不会传递
//            myIntent.setAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
//            startActivity(myIntent);
        }
        //将指定的文本写入NFC标签
        else {
            //获取Tag对象
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            //创建NdefMessage对象和NdefRecord对象
            NdefMessage ndefMessage = new NdefMessage(
                    new NdefRecord[] {createTextRecord(mText)});
 
            //开始向标签写入文本
            if (writeTag(ndefMessage, tag)) {
                //如果成功写入文本，将mtext设为null
                mText = null;
                //将主窗口显示的要写入的文本清空，文本只能写入一次
                //如要继续写入，需要再次指定新的文本，否则只会读取标签中的文本
                info.setText("");
            }
 
        }
 
    }
 
    //创建一个封装要写入的文本的NdefRecord对象
    public NdefRecord createTextRecord(String text) {
        //生成语言编码的字节数组，中文编码
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(
                Charset.forName("US-ASCII"));
        //将要写入的文本以UTF_8格式进行编码
        Charset utfEncoding = Charset.forName("UTF-8");
        //由于已经确定文本的格式编码为UTF_8，所以直接将payload的第1个字节的第7位设为0
        byte[] textBytes = text.getBytes(utfEncoding);
        int utfBit = 0;
        //定义和初始化状态字节
        char status = (char) (utfBit + langBytes.length);
        //创建存储payload的字节数组
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        //设置状态字节
        data[0] = (byte) status;
        //设置语言编码
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        //设置实际要写入的文本
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
                textBytes.length);
        //根据前面设置的payload创建NdefRecord对象
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return record;
    }
 
    //将NdefMessage对象写入标签，成功写入返回ture，否则返回false
    boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
 
        try {
            //获取Ndef对象
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                //允许对标签进行IO操作
                ndef.connect();
 
                if (!ndef.isWritable()) {
                    Toast.makeText(this, "NFC Tag是只读的！", Toast.LENGTH_LONG)
                            .show();
                    return false;
 
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(this, "NFC Tag的空间不足！", Toast.LENGTH_LONG)
                            .show();
                    return false;
                }
 
                //向标签写入数据
                ndef.writeNdefMessage(message);
                Toast.makeText(this, "已成功写入数据！", Toast.LENGTH_LONG).show();
                return true;
 
            } else {
                //获取可以格式化和向标签写入数据NdefFormatable对象
                NdefFormatable format = NdefFormatable.get(tag);
                //向非NDEF格式或未格式化的标签写入NDEF格式数据
                if (format != null) {
                    try {
                        //允许对标签进行IO操作
                        format.connect();
                        format.format(message);
                        Toast.makeText(this, "已成功写入数据！", Toast.LENGTH_LONG)
                                .show();
                        return true;
 
                    } catch (Exception e) {
                        Toast.makeText(this, "写入NDEF格式数据失败！", Toast.LENGTH_LONG)
                                .show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "NFC标签不支持NDEF格式！", Toast.LENGTH_LONG)
                            .show();
                    return false;
 
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
 
    }

}
