package org.dheeraj.imnci;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GetUpdates extends AsyncTask<String, String, String> {

	Context context;
	public static final String tag = "getupdates";
	ListView updateListView;
	ProgressDialog pdlg;
	private ArrayList<Updates> updatesList;

	public GetUpdates(Context context, View listView) {
		// TODO Auto-generated constructor stub

		this.context = context;
		this.updateListView = (ListView) listView;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		this.pdlg = ProgressDialog.show(context, "Getting updates", "Please Wait");
		this.pdlg.setCancelable(true);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		this.pdlg.dismiss();
		if(result.equals("error connecting the server")|| result.equals("malformed url")){
			Toast.makeText(context, result, Toast.LENGTH_LONG).show();
			return;
		}
		updatesList = new ArrayList<Updates>();
		String updates[] = result.split("-");
		for (String update : updates) {
			Log.d(tag,update);
			String[] mesgParts = update.split(";");
			for(String mesgPart:mesgParts){
				Log.d(tag,mesgPart);
			}
			Log.d(tag,mesgParts[0]+":"+mesgParts[1]+":"+mesgParts[2]);
			updatesList.add(new Updates(0, "", Integer.parseInt(mesgParts[2]),
				mesgParts[0], mesgParts[1]));
		}

		UpdatesAdapter upAdapter = new UpdatesAdapter(context,
				R.layout.update_item, updatesList);
		this.updateListView.setAdapter(upAdapter);
		this.updateListView.setOnItemClickListener(new OnItemClickListener() {

			
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				Updates update=new Updates();
				update = updatesList.get(pos);
				Toast.makeText(context, update.getMessage(), Toast.LENGTH_LONG).show();
			}
		});
		Log.d(tag, result);
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String updateUrl = params[0];
		String response;
		Log.d(tag, updateUrl);
		try {
			URLConnection httpConn = new URL(updateUrl).openConnection();
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
