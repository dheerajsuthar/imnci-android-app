package org.dheeraj.imnci;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
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

public class MotherRegistration extends Activity {
	private TableLayout tableLayout;
	private TableRow tableRow;
	private ArrayList<Data> dataMap;
	private Button submitButton;
	private DatePickerDialog dobDialog;
	private EditText result;
	private TextView labelResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_entry_activity);

		dataMap = new ArrayList<Data>();
		String tableName = "mother_reg";
		String labels[] = { "mother", "husband", "date of birth", "age",
				"category", "phone no", "blood group", "LMP", "JSY Benefits",
				"abortions" };
		String categories[] = { "SC", "ST", "OBC", "GEN" };
		String bloodGroups[] = { "A pos", "B pos", "O pos", "AB pos", "A neg",
				"B neg", "O neg", "AB neg" };
		String JSYBenefits[] = { "Y", "N" };

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

			if (data.getLabel().equals("date of birth")
					|| data.getLabel().equals("LMP")) {
				showDateDialog(data);
			} else if (data.getLabel().equals("category")) {
				showSpinner(data, categories);
			} else if (data.getLabel().equals("blood group")) {
				showSpinner(data, bloodGroups);

			} else if (data.getLabel().equals("JSY Benefits")) {
				showSpinner(data, JSYBenefits);

			} else {
				showTextDataRows(data);
			}
			// add view to data

		}
		String urlString = "";
		tableRow = new TableRow(this);
		submitButton = new Button(this);
		//test//
		labelResult = new TextView(this);
		result = new EditText(this);
		result.setHint("Result form Net Will Come Here");
		//test//
		submitButton.setOnClickListener(new ImnciOnClickListener(dataMap, getApplicationContext(),
				urlString,tableName,result));
		submitButton.setText("Submit");
		tableRow.addView(submitButton);
		tableLayout.addView(tableRow);
		
		tableRow = new TableRow(this);
		labelResult.setText("Result: ");
		tableRow.addView(labelResult);
		tableRow.addView(result);
		tableLayout.addView(tableRow);
		

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
				dobDialog = new DatePickerDialog(MotherRegistration.this, l, c
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