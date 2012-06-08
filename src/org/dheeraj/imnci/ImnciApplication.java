package org.dheeraj.imnci;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class ImnciApplication extends Application implements
		OnSharedPreferenceChangeListener {
	private AppAtrribs appAttribs;
	private SharedPreferences prefs;
	private String[] tableArray = { "abortions", "anc_02", "anc_03", "anc_04", "ifa",
			"mother_reg", "pnc", "pnc", "tt1", "tt2", "ttb" };
	private String xmlFile = "db.xml";
	private static final String tag = "Imnci";
	

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);

		Log.d(tag, "On created");
	}

	public synchronized AppAtrribs getAppAttribs() {
		if (this.appAttribs == null) {
			this.appAttribs = new AppAtrribs();

			this.appAttribs
					.setNetAvailable(this.prefs.getBoolean("internet", false));
			this.appAttribs.setUrl(this.prefs.getString("url",
					"http://172.20.0.19/cgi-bin/squealitor.py"));
			this.appAttribs.setSmsAvailable(this.prefs.getBoolean("sms", false));
			this.appAttribs.setSmsNo(this.prefs.getString("smsno", "3325"));
			this.appAttribs.setTableArray(this.tableArray);
			this.appAttribs.setXmlFile(this.xmlFile);
			this.appAttribs.setLocUrl(this.prefs.getString("locurl", "http://172.20.0.19/cgi-bin/updatemob.cgi"));
			this.appAttribs.setGSMAvailable(this.prefs.getBoolean("gsm", true));
			this.appAttribs.setUpdatesUrl(this.prefs.getString("updatesUrl", "http://172.20.0.19/cgi-bin/imnciupdates.py"));

		}
		return this.appAttribs;
	}

	@Override
	public synchronized void onSharedPreferenceChanged(
			SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub
		this.appAttribs = null;
	}

}

class AppAtrribs {
	private String url;
	private String smsNo;
	private boolean NetAvailable;
	private boolean SmsAvailable;
	private boolean GSMAvailable;
	private String tableArray[];
	private String xmlFile;
	private String locUrl;
	private String updatesUrl;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSmsNo() {
		return smsNo;
	}

	public void setSmsNo(String smsNo) {
		this.smsNo = smsNo;
	}

	public boolean isNetAvailable() {
		return NetAvailable;
	}

	public void setNetAvailable(boolean netAvailable) {
		NetAvailable = netAvailable;
	}

	public boolean isSmsAvailable() {
		return SmsAvailable;
	}

	public void setSmsAvailable(boolean smsAvailable) {
		SmsAvailable = smsAvailable;
	}

	public String[] getTableArray() {
		return tableArray;
	}

	public void setTableArray(String tableArray[]) {
		this.tableArray = tableArray;
	}

	public String getXmlFile() {
		return xmlFile;
	}

	public void setXmlFile(String xmlFile) {
		this.xmlFile = xmlFile;
	}

	public String getLocUrl() {
		return locUrl;
	}

	public void setLocUrl(String locUrl) {
		this.locUrl = locUrl;
	}

	public boolean isGSMAvailable() {
		return GSMAvailable;
	}

	public void setGSMAvailable(boolean gSMAvailable) {
		GSMAvailable = gSMAvailable;
	}

	public String getUpdatesUrl() {
		return updatesUrl;
	}

	public void setUpdatesUrl(String updatesUrl) {
		this.updatesUrl = updatesUrl;
	}
}