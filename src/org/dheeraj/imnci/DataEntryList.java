package org.dheeraj.imnci;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DataEntryList extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String dataEntryMenu[] = { "Take Picture","Mother Registration", "ANC 02", "ANC 03",
				"ANC 04", "TT 1" , "TT 2","TT B","IFA","PNC","Abortion","PO"};
		setListAdapter(new ArrayAdapter<String>(this, R.layout.data_entry_item,
				dataEntryMenu));
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				
				String itemSelected = (String) ((TextView)view).getText();
				if(itemSelected.equals("Take Picture")){
					Intent intent = new Intent(DataEntryList.this, TakePicture.class);
					startActivity(intent);
				}
				if(itemSelected.equals("Mother Registration")){
					Intent intent = new Intent(DataEntryList.this, MotherRegistration.class);
					startActivity(intent);
				}
				if(itemSelected.equals("ANC 02")){
					Intent intent = new Intent(DataEntryList.this, ANC02.class);
					startActivity(intent);
				}
				if(itemSelected.equals("ANC 03")){
					Intent intent = new Intent(DataEntryList.this, ANC03.class);
					startActivity(intent);
				}
				if(itemSelected.equals("ANC 04")){
					Intent intent = new Intent(DataEntryList.this, ANC04.class);
					startActivity(intent);
				}
				if(itemSelected.equals("TT 1")){
					Intent intent = new Intent(DataEntryList.this, TT1.class);
					startActivity(intent);
				}
				if(itemSelected.equals("TT 2")){
					Intent intent = new Intent(DataEntryList.this, TT2.class);
					startActivity(intent);
				}
				if(itemSelected.equals("TT B")){
					Intent intent = new Intent(DataEntryList.this, TTB.class);
					startActivity(intent);
				}
				if(itemSelected.equals("IFA")){
					Intent intent = new Intent(DataEntryList.this, IFA.class);
					startActivity(intent);
				}
				if(itemSelected.equals("PNC")){
					Intent intent = new Intent(DataEntryList.this, PNC.class);
					startActivity(intent);
				}
				if(itemSelected.equals("Abortion")){
					Intent intent = new Intent(DataEntryList.this, Abortion.class);
					startActivity(intent);
				}
				if(itemSelected.equals("PO")){
					Intent intent = new Intent(DataEntryList.this, PO.class);
					startActivity(intent);
				}
			}
		});
	}
	
	

}
