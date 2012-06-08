package org.dheeraj.imnci;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class InternetDataTransaction extends
		AsyncTask<String, String, String> {
Context context;
EditText resultView;
String result;
	InternetDataTransaction(Context context, EditText resultView){
		this.context  = context;
		this.resultView = resultView;
	}
	@Override
	protected String doInBackground(String... urls) {
		// TODO Auto-generated method stub
		try {
			return getResult(urls[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "Unable to access the server.Please try later";
		}
		
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		Log.d("net-connect",result);
		//Toast.makeText(this.context, result, Toast.LENGTH_LONG);
		resultView.setText(result);
	}

	private String getResult(String urlString) throws IOException {
		// TODO Auto-generated method stub
		String result;
		int length = 500; //buffer length;
		InputStream is = null;
		try{
		URL url  = new URL(urlString);
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.setReadTimeout(10000);
		httpConnection.setConnectTimeout(15000);
		httpConnection.setRequestMethod("GET");
		httpConnection.setDoInput(true);
		
		httpConnection.connect();
		int responseCode = httpConnection.getResponseCode();
		Log.d("net-connect", "response from net: "+responseCode);
		is = httpConnection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		char[] buffer = new char[length];
		isr.read(buffer);
		result = new String(buffer);
		return result;
		}
		finally{
			if(is!= null)
				is.close();
		}
	}

	
}
