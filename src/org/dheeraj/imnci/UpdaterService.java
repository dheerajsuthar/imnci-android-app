package org.dheeraj.imnci;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {
	static final String tag = "Updater Service";
	private Updater up;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(tag, "In create");
		up = new Updater(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(tag, "in Destroy");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.d(tag, "in start");
		up.start();
	}

}

class Updater extends Thread {
	Context context;
	private DbHelper dbHelper;
	private SQLiteDatabase dbReader;
	String[] tableArray = { "abortions", "anc_02", "anc_03", "anc_04", "ifa",
			"mother_reg", "pnc", "pnc", "tt1", "tt2", "ttb" };
	String xmlString;
	String url;

	Updater(Context context) {
		this.context = context;
		dbHelper = new DbHelper(context);
		dbReader = dbHelper.getReadableDatabase();
		
		xmlString = "";
		url = "http://172.20.0.19/cgi-bin/fetch_data.cgi";
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		String columnName;
		String columnValue;
		Cursor cursor;
		
		File f = new File(Environment.getExternalStorageDirectory(), "data.xml");
		
		xmlString += "<database>";
		try {
		for (String table : tableArray) {
			
				xmlString += "<table name=\"" + table + "\" >";
				cursor = dbReader.query(table, null, null, null, null, null,
						null);
				if (cursor.moveToFirst()) {
					do {
						xmlString += "<row>";
						for (int i = 0; i < cursor.getColumnCount(); i++) {
							xmlString += "<column";

							columnName = cursor.getColumnName(i);
							xmlString += " name=\"" + columnName + "\" ";
							if (columnName.equals("mid")
									|| columnName.equals("ID"))
								columnValue = "" + cursor.getInt(i);
							else
								columnValue = cursor.getString(i);
							xmlString += " value=\"" + columnValue + "\" ";
							xmlString += "></column>";
						}
						xmlString += "</row>";
					} while (cursor.moveToNext());
					cursor.close();
					
				}
				xmlString += "</table>";
			
			}
		} finally {
			if (dbReader != null)
				dbReader.close();
		}
		xmlString += "</database>";
		Log.d("xml output", xmlString);
		try {
			FileOutputStream fop = new FileOutputStream(f);
			fop.write(xmlString.getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			String result = sendToNetwork(xmlString,url);
			Log.d("result",result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String sendToNetwork(String xmlString, String urlString) throws IOException {
		// TODO Auto-generated method stub
		int len = 500;
		String contentAsString = "";
	    InputStream is = null;
		try {
			urlString += "?xml=\""+xmlString+"\"";
	        URL url = new URL(urlString);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("POST");
	        conn.setDoInput(true);
	        // Starts the query
	        conn.connect();
	        int response = conn.getResponseCode();
	        Log.d("after conn", "The response is: " + response);
	        is = conn.getInputStream();

	        // Convert the InputStream into a string
	        contentAsString = readIt(is, len);
	        return contentAsString;
	        
	    // Makes sure that the InputStream is closed after the app is
	    // finished using it.
	    } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        if (is != null) {
	    
					is.close();
			
	        } 
	    }
		return contentAsString;

	}
	
	public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	    Reader reader = null;
	    reader = new InputStreamReader(stream, "UTF-8");        
	    char[] buffer = new char[len];
	    reader.read(buffer);
	    return new String(buffer);
	    
	}

}