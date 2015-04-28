package com.example.stoneintegration;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MonetaryMask implements TextWatcher {
	
	EditText amountEditText;
	private String current = "";

	public MonetaryMask(EditText amountEditText) {
		this.amountEditText = amountEditText;
	}	
	
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		if (!s.toString().equals(current)) {
			amountEditText.removeTextChangedListener(this);
			double parseValue;
			
			String string = s.toString().replaceAll("[R$ ,.]", "");			

			try {
				parseValue = Double.parseDouble(string);
			} catch (Exception e) {
				parseValue = Double.parseDouble("0");
			};

			Locale.setDefault(Locale.ENGLISH);

			NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

			DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();

			decimalFormatSymbols.setCurrencySymbol("");
			decimalFormatSymbols.setGroupingSeparator('.');
			decimalFormatSymbols.setMonetaryDecimalSeparator(',');

			((DecimalFormat) numberFormat).setDecimalFormatSymbols(decimalFormatSymbols);
			String formated = numberFormat.format(parseValue / 100);

			current = formated;
			
			amountEditText.setText(formated);
			amountEditText.setSelection(formated.length());
			amountEditText.addTextChangedListener(this);
		}
	}
	
	public void beforeTextChanged(CharSequence s, int start, int count, int after){}

	public void afterTextChanged(Editable s) {}
}
