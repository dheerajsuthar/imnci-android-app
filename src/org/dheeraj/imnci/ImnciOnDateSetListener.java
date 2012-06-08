package org.dheeraj.imnci;

import android.app.DatePickerDialog.OnDateSetListener;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

class ImnciOnDateSetListener implements OnDateSetListener {
	EditText dateEdit;
	Button dateButton;
	String day;
	String month;

	ImnciOnDateSetListener(Button buttonToEdit, EditText editTextToEdit) {
		dateButton = buttonToEdit;
		dateEdit = editTextToEdit;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Auto-generated method stub

		dateButton.setVisibility(View.GONE);
		dateEdit.setVisibility(View.VISIBLE);
		day = (dayOfMonth < 10) ? "0" + dayOfMonth : "" + dayOfMonth;
		month = (monthOfYear < 10) ? "0" + monthOfYear : "" + monthOfYear;
		dateEdit.setText("" + day + "/" + month + "/" + year);
		dateEdit.setEnabled(false);
	}
}