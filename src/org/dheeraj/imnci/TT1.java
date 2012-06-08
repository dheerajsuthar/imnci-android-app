package org.dheeraj.imnci;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class TT1 extends Activity {
	private TableLayout tableLayout;
	private TableRow tableRow;
	private ArrayList<Data> dataMap;
	private Button submitButton;
	private DatePickerDialog dobDialog;
	private EditText result;
	private TextView labelResult;
	private DbHelper dbHelper;
	private ArrayList<String> midArrayList;
	private String[] midArray;
	private SQLiteDatabase dbWriter;
	private SQLiteDatabase dbReader;
	private Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_entry_activity);
		midArray = null;
		midArray = getMidArray();
		if (midArray == null) {
			Toast.makeText(this, "No id has been added yet", Toast.LENGTH_LONG)
					.show();
			return;
		}
		dataMap = new ArrayList<Data>();
		String tableName = "tt1";
		String labels[] = { "ID", "tt date", "anaemia", "complication",
				"rti sti" };
		String[] anaemia = new String[10];
		String[] complication = new String[10];
		for (int i = 0; i < 10; i++) {
			anaemia[i] = "" + i;
			complication[i] = "" + i;
		}
		String rti_sti[] = { "Y", "N" };

		for (String label : labels) {
			Data data = new Data();
			data.setLabel(label);
			dataMap.add(data);

		}
		tableLayout = (TableLayout) findViewById(R.id.dataTable);
		tableLayout.setColumnStretchable(0, true);
		tableLayout.setColumnStretchable(1, true);

		// create form
		for (Data data : dataMap) {

			if (data.getLabel().equals("tt date")) {
				showDateDialog(data);
			} else if (data.getLabel().equals("anaemia")) {
				showSpinner(data, anaemia);
			} else if (data.getLabel().equals("complication")) {
				showSpinner(data, complication);

			} else if (data.getLabel().equals("rti sti")) {
				showSpinner(data, rti_sti);

			} else if (data.getLabel().equals("ID")) {
				showSpinner(data, midArray);

			} else {
				showTextDataRows(data);
			}
			// add view to data

		}
		String urlString = "http://10.0.2.2/sms.php?MOB=919533131641&req_msg=UNIT%20-R-M_NAME:manhdbf%20-H_NAME:dgnvfsbj%20-DOB:22/04/1987-AGE:25-Category:GEN-Phone%20No:9533131641-Blood%20Grp:Apos-LMP:11/11/2011-JSY%20Benfits:y-Abortions:0&Command=TRK&Parameter=A41713743";
		tableRow = new TableRow(this);
		submitButton = new Button(this);
		// test//
		labelResult = new TextView(this);
		result = new EditText(this);
		result.setHint("Result form Net Will Come Here");
		// test//
		submitButton.setOnClickListener(new ImnciOnClickListener(dataMap,
				getApplicationContext(), urlString, tableName, result));
		submitButton.setText("Submit");
		tableRow.addView(submitButton);
		tableLayout.addView(tableRow);

		tableRow = new TableRow(this);
		labelResult.setText("Result: ");
		tableRow.addView(labelResult);
		tableRow.addView(result);
		tableLayout.addView(tableRow);

	}

	private String[] getMidArray() {
		// TODO Auto-generated method stub
		dbHelper = new DbHelper(this);
		dbReader = dbHelper.getReadableDatabase();
		try{
		midArrayList = new ArrayList<String>();
		String column[] = { "mid" };
		cursor = dbReader.query("mother_reg", column, null, null, null, null,
				"mid desc");
		if (cursor.moveToFirst()) {
			do {
				midArrayList.add(""
						+ (cursor.getInt(cursor.getColumnIndex("mid"))));
			} while (cursor.moveToNext());
			cursor.close();
			String mids[] = new String[midArrayList.size()];
			midArrayList.toArray(mids);
			return mids;
		} else
			return null;
		}
		finally{
			if(dbWriter != null)
			dbWriter.close();
			if(dbReader != null)
				dbReader.close();
		}
	}

	private void showSpinner(Data data, String[] valueArray) {
		// TODO Auto-generated method stub
		final EditText valueEdit = new EditText(this);
		TableRow tableRow = new TableRow(this);
		TextView labelText = new TextView(this);
		labelText.setText(data.getLabel());
		valueEdit.setVisibility(View.GONE);
		Spinner spinner = new Spinner(this);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, valueArray);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				valueEdit.setText(parent.getItemAtPosition(pos).toString());

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		tableRow.addView(labelText);
		tableRow.addView(spinner);
		tableRow.addView(valueEdit);
		data.setValueView(valueEdit);
		tableLayout.addView(tableRow);

	}

	private void showTextDataRows(Data data) {
		// TODO Auto-generated method stub
		TableRow tableRow = new TableRow(this);
		TextView labelText = new TextView(this);
		labelText.setText(data.getLabel());
		EditText valueEdit = new EditText(this);
		tableRow.addView(labelText);
		tableRow.addView(valueEdit);
		data.setValueView(valueEdit);
		tableLayout.addView(tableRow);
	}

	void showDateDialog(Data data) {
		final Button dateButton = new Button(this);
		final EditText valueEdit = new EditText(this);

		TableRow tableRow = new TableRow(this);
		TextView labelText = new TextView(this);
		labelText.setText(data.getLabel());

		dateButton.setText("Click");
		valueEdit.setVisibility(View.GONE);
		dateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				ImnciOnDateSetListener l = new ImnciOnDateSetListener(
						dateButton, valueEdit);
				dobDialog = new DatePickerDialog(TT1.this, l, c
						.get(Calendar.YEAR), c.get(Calendar.MONTH), c
						.get(Calendar.DAY_OF_MONTH));
				dobDialog.show();
			}
		});
		tableRow.addView(labelText);
		tableRow.addView(valueEdit);
		tableRow.addView(dateButton);
		data.setValueView(valueEdit);
		tableLayout.addView(tableRow);
	}
}