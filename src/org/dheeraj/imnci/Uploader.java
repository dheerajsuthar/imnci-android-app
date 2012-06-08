package org.dheeraj.imnci;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Uploader extends AsyncTask<String, String, String> {
	Context context;
	ProgressDialog pdlg;
	ImnciApplication imnciApp;
	String tableArray[];
	public static final String tag = "uploader";

	Uploader(Context context, ImnciApplication imnciApplication) {
		this.context = context;
		this.imnciApp = imnciApplication;
		this.tableArray = this.imnciApp.getAppAttribs().getTableArray();
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		SQLiteDatabase dbWriter;
		DbHelper dbh;

		this.pdlg.dismiss();

		dbh = new DbHelper(context);

		if (result.equals("0")) {
			result += "\nSuccessful";
			dbWriter = dbh.getWritableDatabase();
			try {
				for (String table : tableArray) {
					ContentValues cv = new ContentValues();
					cv.put("exported", 1);
					dbWriter.update(table, cv, null, null);
				}
			} finally {
				if (dbWriter != null)
					dbWriter.close();
			}
		} else
			result += "\nFailure";
		Toast.makeText(context, "process complete\nResult: " + result,
				Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		this.pdlg = ProgressDialog.show(context, "Uploading...", "Please Wait");
		this.pdlg.setCancelable(true);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub

		super.onProgressUpdate(values);
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String xmlString = params[0];
		String url = params[1];
		String response;
		Log.d(tag, url);
		try {
			URLConnection httpConn = new URL(url).openConnection();
			writeToServer(httpConn, xmlString);
			response = readFromServer(httpConn);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return "malformed url";
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return "error connecting the server";
		}
		return response;
	}

	private static void writeToServer(URLConnection httpConn, String xmlString)
			throws IOException {
		// TODO Auto-generated method stub
		httpConn.setDoOutput(true);
		OutputStreamWriter writer = new OutputStreamWriter(
				httpConn.getOutputStream());
		writer.write("xml=" + xmlString);
		writer.close();
	}

	private static String readFromServer(URLConnection httpConn)
			throws IOException {
		// TODO Auto-generated method stub
		String response = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(
				httpConn.getInputStream()));
		String temp;
		while ((temp = br.readLine()) != null)
			response += temp;
		br.close();
		return response;
	}
	
	

}
