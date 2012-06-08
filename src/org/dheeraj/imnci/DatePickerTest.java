package org.dheeraj.imnci;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

public class DatePickerTest extends Activity {
	TableRow tr;
	EditText et1;
	EditText et2;
	Button b1;
	Button b2;
	TableLayout tableLayout;
	private DatePickerDialog FirstDatePickerDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_entry_activity);
		tableLayout = (TableLayout) findViewById(R.id.dataTable);
		tableLayout.setColumnStretchable(0, true);
		tableLayout.setColumnStretchable(1, true);

		et1 = new EditText(this);
		et2 = new EditText(this);
		b1 = new Button(this);
		b2 = new Button(this);
		tr = new TableRow(this);
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FirstDatePickerDialog = new DatePickerDialog(
						DatePickerTest.this, new FirstDateSetListener(), 2000,
						11, 11);
				FirstDatePickerDialog.show();
			}
		});

		b2.setOnClickListener(new OnClickListener() {

			private DatePickerDialog SecondDatePickerDialog;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SecondDatePickerDialog = new DatePickerDialog(
						DatePickerTest.this, new FirstDateSetListener(), 2000,
						11, 11);
				SecondDatePickerDialog.show();
			}
		});
		tr.addView(et1);
		tr.addView(b1);
		tableLayout.addView(tr);
		tr = new TableRow(this);
		tr.addView(et2);
		tr.addView(b2);
		tableLayout.addView(tr);
	}
}

class FirstDateSetListener implements OnDateSetListener {

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub
		Log.d("dpDIalog", view.toString());
	
	}
}