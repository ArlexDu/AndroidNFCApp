package edu.happy.tools;

import android.graphics.drawable.Drawable;

public class ApplicationInfo {
	
   private String appName;
   private String packageName;
   private Drawable appIcon;
public String getAppName() {
	return appName;
}
public void setAppName(String appName) {
	this.appName = appName;
}
public String getPackageName() {
	return packageName;
}
public void setPackageName(String packageName) {
	this.packageName = packageName;
}
public Drawable getAppIcon() {
	return appIcon;
}
public void setAppIcon(Drawable appIcon) {
	this.appIcon = appIcon;
}

   
}
