package edu.happy.tools;


import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.content.SyncAdapterType;
import android.net.Uri;
import android.nfc.NdefRecord;

public class ReadUriRecord {
	
	//nfc  uri格式第一个字节用于确定当前的uri的头部标志，其他的部分就是uri的主要内容
    private static final Map<Byte, String> URI_PREFIX_MAP = new HashMap <Byte, String>();
    
    static{
    	URI_PREFIX_MAP.put((byte) 0x00, "");
    	URI_PREFIX_MAP.put((byte) 0x01, "http://www.");
    	URI_PREFIX_MAP.put((byte) 0x02, "https://www.");
    	URI_PREFIX_MAP.put((byte) 0x03, "http://");
    	URI_PREFIX_MAP.put((byte) 0x04, "https://");
    	URI_PREFIX_MAP.put((byte) 0x05, "tel:");
    	URI_PREFIX_MAP.put((byte) 0x06, "mailto:");
    	URI_PREFIX_MAP.put((byte) 0x07, "ftp://anonymous:anonymous@");
    	URI_PREFIX_MAP.put((byte) 0x08, "ftp://ftp.");
    	URI_PREFIX_MAP.put((byte) 0x09, "ftps://");
    	URI_PREFIX_MAP.put((byte) 0x0A, "sftp://");
    	URI_PREFIX_MAP.put((byte) 0x0B, "smb://");
    	URI_PREFIX_MAP.put((byte) 0x0C, "nfs://");
    	URI_PREFIX_MAP.put((byte) 0x0D, "ftp://");
    	URI_PREFIX_MAP.put((byte) 0x0E, "dav://");
    	URI_PREFIX_MAP.put((byte) 0x0F, "news:");
    	URI_PREFIX_MAP.put((byte) 0x10, "telnet://");
    	URI_PREFIX_MAP.put((byte) 0x11, "imap:");
    	URI_PREFIX_MAP.put((byte) 0x12, "rtsp://");
    	URI_PREFIX_MAP.put((byte) 0x13, "urn:");
    	URI_PREFIX_MAP.put((byte) 0x14, "pop:");
    	URI_PREFIX_MAP.put((byte) 0x15, "sip:");
    	URI_PREFIX_MAP.put((byte) 0x16, "sips:");
    	URI_PREFIX_MAP.put((byte) 0x17, "tftp:");
    	URI_PREFIX_MAP.put((byte) 0x18, "btspp://");
    	URI_PREFIX_MAP.put((byte) 0x19, "btl2cap://");
    	URI_PREFIX_MAP.put((byte) 0x1A, "btgoep://");
    	URI_PREFIX_MAP.put((byte) 0x1B, "tcpobex://");
    	URI_PREFIX_MAP.put((byte) 0x1C, "irdaobex://");
    	URI_PREFIX_MAP.put((byte) 0x1D, "file://");
    	URI_PREFIX_MAP.put((byte) 0x1E, "urn:epc:id:");
    	URI_PREFIX_MAP.put((byte) 0x1F, "urn:epc:tag:");
        URI_PREFIX_MAP.put((byte) 0x20, "urn:epc:pat:");
        URI_PREFIX_MAP.put((byte) 0x21, "urn:epc:raw:");
        URI_PREFIX_MAP.put((byte) 0x22, "urn:epc:");
        URI_PREFIX_MAP.put((byte) 0x23, "urn:nfc:");
    }
    
    public ReadUriRecord() {
		// TODO Auto-generated constructor stub
    	
	}
    
    public String ParseAbsoluteUri(NdefRecord ndefRecord){
    	byte[] payload = ndefRecord.getPayload();
    	String uri = new String(payload,Charset.forName("UTF-8"));
    	return uri;
    	
    }
    
    public String ParseWellKnowUri(NdefRecord ndefRecord){
    	byte[] payload = ndefRecord.getPayload();
    	String prefix = URI_PREFIX_MAP.get(payload[0]);
//    	获得前缀的长度
    	byte[] prefixbytes = prefix.getBytes(Charset.forName("UTF-8"));
//    	计算整个uri的长度
    	byte[] fullUri = new byte[prefixbytes.length+payload.length-1];
    	System.arraycopy(prefixbytes, 0, fullUri, 0, prefixbytes.length);
    	System.arraycopy(payload, 1, fullUri, prefixbytes.length, payload.length-1);
    	String uri = new String(fullUri,Charset.forName("UTF-8"));
    	return uri;
    }

}
