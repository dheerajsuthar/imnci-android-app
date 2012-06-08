package org.dheeraj.imnci;

import android.view.View;

class Data {
	private String label;
	private View valueView;
	public View getValueView() {
		return valueView;
	}
	public void setValueView(View valueView) {
		this.valueView = valueView;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}



}