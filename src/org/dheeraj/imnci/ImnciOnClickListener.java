package org.dheeraj.imnci;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

class ImnciOnClickListener implements OnClickListener {
	ArrayList<Data> dataMap;
	Data data;
	Context context;
	String error;
	String url;
	EditText res;
	String tableName;
	DbHelper dbHelper;
	SQLiteDatabase dbWriter;
	SQLiteDatabase dbReader;
	public final String tag = "ImnciOnClickListener";
	boolean netAccess;

	public ImnciOnClickListener(ArrayList<Data> dataMap, Context context,
			String url, String tableName, EditText result) {
		// TODO Auto-generated constructor stub
		this.dataMap = dataMap;
		this.context = context;
		this.url = url;
		this.res = result;
		this.tableName = tableName;
		dbHelper = new DbHelper(context);
		netAccess = false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String s = "";
		String label;
		String value;

		error = "";
		if (check(dataMap)) {
			// show data
			for (Data data : dataMap) {
				label = data.getLabel();
				value = ((EditText) data.getValueView()).getText().toString();

				s += label + " : " + value + "\n";

			}
			// try to post data to net
			// netaccess = posttonet();
			//
			//url = geturl(tableName,dataMap);
			// fill Database
			fillDatabase(tableName, dataMap);
			
			//Log.d(tag, url);
		// new InternetDataTransaction(this.context,this.res).execute(url);
		}

		else {
			s = "Error! " + error + " Please fill form again.";
		}
		Toast.makeText(context, s, Toast.LENGTH_LONG).show();

	}

	private String geturl(String tableName, ArrayList<Data> dataMap) {
		// TODO Auto-generated method stub
		String address = "http://google.com",label,value;
		LinkedHashMap<String, String> labelValueMap;
		
		labelValueMap = new LinkedHashMap<String, String>();
		for(Data data: dataMap){
			label = data.getLabel();
			value = ((EditText) data.getValueView()).getText().toString();
			labelValueMap.put(label, value);
		}
		String labels[] = { "mother", "husband", "date of birth", "age",
				"category", "phone no", "blood group", "LMP", "JSY Benefits",
				"abortions" };
		if(tableName.equals("mother_reg")){
			address = "http://10.0.2.2/sms.php?MOB=919533131641"
					+"&req_msg=UNIT%20" +
					"-R" +
					"-M_NAME:"+labelValueMap.get(labels[0]) +"%20" +
					"-H_NAME:"+labelValueMap.get(labels[1]) +"%20" +
					"-DOB:"+labelValueMap.get(labels[2]) +"" +
					"-AGE:"+labelValueMap.get(labels[3]) +"" +
					"-Category:"+labelValueMap.get(labels[4]) +"" +
					"-Phone%20No:"+labelValueMap.get(labels[5]) +"" +
					"-Blood%20Grp:"+labelValueMap.get(labels[6]) +"" +
					"-LMP:"+labelValueMap.get(labels[7]) +"" +
					"-JSY%20Benfits:"+labelValueMap.get(labels[8]) +"" +
					"-Abortions:"+labelValueMap.get(labels[9]) +"" +
					"&Command=TRK&Parameter=A41713743";
		}
		return address;
	}

	private void fillDatabase(String tableName, ArrayList<Data> dataMap) {
		// TODO Auto-generated method stub
		Log.d(tag, "in fillDatabase");
		int mid = 1;

		dbReader = dbHelper.getReadableDatabase();
		dbWriter = dbHelper.getWritableDatabase();
		try {
			if (tableName == "mother_reg") {
				if (netAccess) {
					// mid = get mid from the net;

				} else {
					String columns[] = { "def_mid" };
					Cursor crsr = dbReader.query("default_mid", columns, null,
							null, null, null, "def_mid DESC", null);
					if (crsr.moveToFirst()) {
						mid = crsr.getInt(crsr.getColumnIndex("def_mid"));
						crsr.close();
						mid++;
					}
					ContentValues cv = new ContentValues();
					cv.put("exported",0); //exported fix 
					cv.put("def_mid", mid);
					dbWriter.insertOrThrow("default_mid", null, cv);

				}

			}
			if (tableName == "mother_reg") {

				Log.d(tag, "" + dbWriter.getVersion());
				ContentValues cv = new ContentValues();
				cv.put("mid", mid);
				cv.put("exported",0); //exported fix
				// convert labels as appropriate for the table column names
				for (Data data : dataMap) {
					data.setLabel(data.getLabel().replace(' ', '_'));
					cv.put(data.getLabel(), ((EditText) data.getValueView())
							.getText().toString());
				}
				dbWriter.insertOrThrow(tableName, null, cv);
			}

			if (tableName == "anc_02" || tableName == "anc_03"
					|| tableName == "anc_04" || tableName == "tt1"
					|| tableName == "tt2"||tableName == "ttb"
					|| tableName == "ifa" || tableName == "pnc"
					|| tableName == "abortions" ||tableName == "po") {

				Log.d(tag, "in table" + tableName);
				// convert labels as appropriate for the table column names
				ContentValues cv = new ContentValues();
				cv.put("exported",0); //exported fix
				for (Data data : dataMap) {
					data.setLabel(data.getLabel().replace(' ', '_'));
					cv.put(data.getLabel(), ((EditText) data.getValueView())
							.getText().toString());
				}
				try {
					dbWriter.insertOrThrow(tableName, null, cv);
				} catch (SQLException e) {

					Toast.makeText(context,
							"Entry of this mid is already complete",
							Toast.LENGTH_LONG).show();
				}
			}
			
			
		} finally {
			dbReader.close();
			dbWriter.close();
		}

	}

	public boolean check(ArrayList<Data> dataMap) {
		String label;
		String value;
		String regExp;
		Pattern pattern;
		Matcher matcher;
		for (Data data : dataMap) {
			label = data.getLabel();

			value = ((EditText) data.getValueView()).getText().toString();
			if (label.equals("mother") || label.equals("husband")) {
				Log.d("motherReg", label + ":" + value);
				regExp = "\\s*[a-zA-Z]+\\s*[a-zA-Z]+";
				pattern = Pattern.compile(regExp);
				matcher = pattern.matcher(value);
				if (!matcher.matches()) {
					error += "in field " + label;
					return false;
				}
			}
			if (label.equals("date of birth") || label.equals("LMP")) {
				Log.d("motherReg", label + ":" + value);
				regExp = "\\s*[0-3][0-9]/[0-1][0-9]/[1-2][0-9][0-9][0-9]";
				pattern = Pattern.compile(regExp);
				matcher = pattern.matcher(value);
				if (!matcher.matches()) {
					error += "in field " + label;
					return false;
				}
			}
			if (label.equals("age") || label.equals("abortions")) {
				Log.d("motherReg", label + ":" + value);
				regExp = "\\s*[0-9]*";
				pattern = Pattern.compile(regExp);
				matcher = pattern.matcher(value);
				if (!matcher.matches()) {
					error += "in field " + label;
					return false;
				}
			}
			if (label.equals("phone no")) {
				Log.d("motherReg", label + ":" + value);
				regExp = "\\s*([0-9]{10})*";
				pattern = Pattern.compile(regExp);
				matcher = pattern.matcher(value);
				if (!matcher.matches()) {
					error += "in field " + label;
					return false;
				}
			}
		}

		return true;
	}
}