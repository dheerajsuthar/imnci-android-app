package org.dheeraj.imnci;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ShowData extends Activity {
	private Spinner spinnerId;
	private DbHelper dbHelper;
	private SQLiteDatabase dbReader;
	private Cursor cursor;
	private ArrayList<String> idList;
	private ArrayAdapter<String> adapter;
	private String id;
	private TableLayout dataTable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_data);
		dbHelper = new DbHelper(this);
		spinnerId = (Spinner) findViewById(R.id.spinnerData);
		dataTable = (TableLayout) findViewById(R.id.tableShowData);
		fillSpinner();

	}

	private void fillData(String id) {
		String tableNames[] = { "mother_reg", "anc_02", "anc_03", "anc_04",
				"tt1", "tt2", "ttb", "abortions", "po", "pnc", "ifa" };
		for (String table : tableNames) {
			getDataFromTable(table, id);
		}
	}

	private void getDataFromTable(String table, String id) {
		// TODO Auto-generated method stub

		getTableTitle(table);
		getTableColumns(table, id);
	}

	private void getTableColumns(String table, String id) {
		// TODO Auto-generated method stub
		SQLiteDatabase dbReader;
		Cursor cursor;
		TableRow colRow;
		TextView labelView;
		TextView valueView;
		String label;
		String value;

		dbReader = dbHelper.getReadableDatabase();
		try {
			if (table.equals("mother_reg"))
				cursor = dbReader.query(table, null, "mid=" + id, null, null,
						null, null, null);
			else
				cursor = dbReader.query(table, null, "ID=" + id, null, null,
						null, null, null);

			Log.d("getTableColumns", table);
			if (cursor.moveToFirst()) {
				do {
					Log.d("in cursor", "" + cursor.getColumnCount());
					for (int i = 0; i < cursor.getColumnCount(); i++) {
						label = cursor.getColumnName(i);
						value = cursor.getString(i);

						labelView = new TextView(this);
						valueView = new TextView(this);
						colRow = new TableRow(this);

						labelView.setText(label);
						valueView.setText(value);
						colRow.addView(labelView);
						colRow.addView(valueView);
						dataTable.addView(colRow);
					}

				} while (cursor.moveToNext());

			}
			cursor.close();
		} finally {
			if (dbReader != null)
				dbReader.close();
		}
	}

	private void getTableTitle(String table) {
		// TODO Auto-generated method stub
		TableRow tabRow;
		TextView tv;
		String title = table.replace('_', ' ').toUpperCase();
		tabRow = new TableRow(this);
		tv = new TextView(this);
		tv.setText(title);
		tv.setTextSize(20);
		tabRow.addView(tv);
		dataTable.addView(tabRow);
	}

	private void fillImage(String id) {
		// TODO Auto-generated method stub
		TableRow imgRow;
		ImageView imageView;
		boolean picFound;
		
		SQLiteDatabase dbReader;
		Cursor cursor;
		Bitmap bmpImage;
		Bitmap scaledBmp;
		
		scaledBmp = null;
		bmpImage = null;

		imgRow = new TableRow(this);
		imageView = new ImageView(this);

		picFound = false;
		dbReader = dbHelper.getReadableDatabase();
		try {
			String tableName = "pictures";
			String[] columns = { "mid", "uri" };
			Log.d("id value", "" + id);
			cursor = dbReader.query(tableName, columns, "mid=" + id, null,
					null, null, null, null);
			Log.d("gotCursor", "foundcursor");
			if (cursor.moveToFirst()) {
				picFound = true;
				Log.d("in cursor", "" + cursor.getColumnCount());
				String path = cursor.getString(cursor.getColumnIndex("uri"));
				Log.d("show_data:imagepath", path);
				// imgUri = Uri.parse(new File(path).toString());
				bmpImage = BitmapFactory.decodeFile(path);
				
				
				
				scaledBmp = Bitmap.createScaledBitmap(bmpImage, 100, 150, true);
				bmpImage.recycle();
				bmpImage = null;

			}
			cursor.close();
		} finally {
			if (dbReader != null)
				dbReader.close();
		}

		if (!picFound)
			imageView.setImageResource(R.drawable.default_user);
		else {
			// Log.d("inimageview", imgUri.toString());
			imageView.setImageBitmap(scaledBmp);

		}
		// LayoutParams lv = new LayoutParams(200, 300);
		// imgRow.setLayoutParams(lv);

		imgRow.addView(imageView);

		dataTable.addView(imgRow);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindDrawables(findViewById(R.id.showDataViewRoot));
		System.gc();
	}

	private void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindDrawables(findViewById(R.id.showDataViewRoot));
		System.gc();
	}

	void fillSpinner() {
		dbReader = dbHelper.getReadableDatabase();
		String columns[] = { "mid" };

		try {
			cursor = dbReader.query("mother_reg", columns, null, null, null,
					null, "mid DESC");
			if (cursor.moveToFirst()) {
				idList = new ArrayList<String>();
				do {
					idList.add(cursor.getString(cursor.getColumnIndex("mid")));
				} while (cursor.moveToNext());
				cursor.close();

				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, idList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerId.setAdapter(adapter);
				spinnerId
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								id = arg0.getItemAtPosition(arg2).toString();
								// dataTable.removeAllViews();
								unbindDrawables(dataTable);
								fillImage(id);
								fillData(id);
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});
			}
		} finally {
			if (dbReader != null)
				dbReader.close();
		}
	}
}
