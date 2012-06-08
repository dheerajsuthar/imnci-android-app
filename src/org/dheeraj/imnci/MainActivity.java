package org.dheeraj.imnci;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ListView updateListView;
	private ArrayList<Updates> updatesList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		String updateTitles[] = {"You currently have no updaets"};
		String updateMessages[] = {"Kindly update yourself by clicking Menu->Get Updates"};

		updatesList = new ArrayList<Updates>();
		for (int i = 0; i < updateTitles.length; i++) {
			updatesList.add(new Updates(0, "" + i, 0, updateTitles[i],
					updateMessages[i]));
		}

		UpdatesAdapter upAdapter = new UpdatesAdapter(this,
				R.layout.update_item, updatesList);
		updateListView = (ListView) findViewById(R.id.updatesList);
		updateListView.setAdapter(upAdapter);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		MenuInflater mf = getMenuInflater();
		mf.inflate(R.menu.menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.DataEntry:
			Intent dataEntryIntent = new Intent(this, DataEntryList.class);
			startActivity(dataEntryIntent);
			return true;

		case R.id.ShowData:
			Intent showDataIntent = new Intent(this, ShowData.class);
			startActivity(showDataIntent);
			return true;

		case R.id.Preference:
			Intent prefsDataIntent = new Intent(this, SharedPrefsActivity.class);
			startActivity(prefsDataIntent);
			return true;

		case R.id.geoLocator:
			if (!((ImnciApplication) getApplication()).getAppAttribs()
					.isNetAvailable()) {
				Toast.makeText(this,
						"You have disabled net\nKindly change preferences",
						Toast.LENGTH_LONG).show();
				return true;
			}
			Intent geoLocIntent = new Intent(this, GeoLocator.class);
			startService(geoLocIntent);
			return true;

		case R.id.updaterService:
			if (!((ImnciApplication) getApplication()).getAppAttribs()
					.isNetAvailable()) {
				Toast.makeText(this,
						"You have disabled net\nKindly change preferences",
						Toast.LENGTH_LONG).show();
				return true;
			}
			Uploader uploader = new Uploader(this,
					(ImnciApplication) getApplication());
			String xmlString = getXmlFromDB();
			String url = ((ImnciApplication) getApplication()).getAppAttribs()
					.getUrl();
			uploader.execute(xmlString, url);
			return true;

		case R.id.fetchUpdates:
			if (!((ImnciApplication) getApplication()).getAppAttribs()
					.isNetAvailable()) {
				Toast.makeText(this,
						"You have disabled net\nKindly change preferences",
						Toast.LENGTH_LONG).show();
				return true;
			}
			View listView = findViewById(R.id.updatesList);
			GetUpdates gup = new GetUpdates(this,listView);
			
			String updatesUrl = ((ImnciApplication) getApplication()).getAppAttribs()
					.getUpdatesUrl();
			gup.execute(updatesUrl);
			return true;

		}

		return true;
	}

	private String getXmlFromDB() {
		// TODO Auto-generated method stub
		String columnName;
		String columnValue;
		Cursor cursor;
		String xmlString;
		String tableArray[];
		ImnciApplication imnciApp;
		DbHelper dbh;
		SQLiteDatabase dbReader;

		imnciApp = (ImnciApplication) getApplication();
		tableArray = imnciApp.getAppAttribs().getTableArray();
		dbh = new DbHelper(this);
		xmlString = "";
		dbReader = dbh.getReadableDatabase();

		File f = new File(Environment.getExternalStorageDirectory(), imnciApp
				.getAppAttribs().getXmlFile());

		xmlString += "<database>";
		try {
			for (String table : tableArray) {
				cursor = dbReader.query(table, null, "exported = 0", null,
						null, null, null);
				if (cursor.moveToFirst()) {
					xmlString += "<table name=\"" + table + "\" >";
					do {
						xmlString += "<row>";
						for (int i = 0; i < cursor.getColumnCount(); i++) {

							columnName = cursor.getColumnName(i);
							if (!columnName.equals("exported")) {
								xmlString += "<column";

								xmlString += " name=\"" + columnName + "\" ";
								if (columnName.equals("mid")
										|| columnName.equals("ID"))
									columnValue = "" + cursor.getInt(i);
								else
									columnValue = cursor.getString(i);
								xmlString += " value=\"" + columnValue + "\" ";
								xmlString += "></column>";
							}
						}
						xmlString += "</row>";
					} while (cursor.moveToNext());
					cursor.close();
					xmlString += "</table>";
				}

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "Error writing log xml", Toast.LENGTH_LONG);
			e.printStackTrace();
		}

		return xmlString;
	}
}