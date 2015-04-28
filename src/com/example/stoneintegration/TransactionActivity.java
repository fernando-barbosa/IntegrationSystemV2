package com.example.stoneintegration;

import br.com.stone.classes.StartTransaction;
import br.com.stone.objects.Transaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

public class TransactionActivity extends ActionBarActivity implements OnCheckedChangeListener, View.OnClickListener {
	
	public EditText valueEditText;
	
	public Spinner spinner;
	
	public RadioGroup tipoCompraRadioGroup; 
	public RadioGroup parcelaRadioGroup;
	
	public RadioButton debitoRadioButton;
	public RadioButton creditoRadioButton;
	public RadioButton aVistaRadioButton;
	public RadioButton parceladoRadioButton;
	
	public Button button;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        
    	instanceView();
    }
    
    // Instancia todos os elementos dessa activity.
    private void instanceView() {
    	valueEditText = (EditText)findViewById(R.id.valueEditText);
        valueEditText.addTextChangedListener(new MonetaryMask(valueEditText));
        
        spinner = (Spinner) findViewById(R.id.parcelamentoSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.pacelamento_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        
        tipoCompraRadioGroup = (RadioGroup) findViewById(R.id.tipoCompraRadioGroup);
        tipoCompraRadioGroup.setOnCheckedChangeListener(this);
        parcelaRadioGroup = (RadioGroup) findViewById(R.id.parcelaRadioGroup);     
        parcelaRadioGroup.setOnCheckedChangeListener(this);
        
        debitoRadioButton = (RadioButton) findViewById(R.id.debitoRadioButton);
    	creditoRadioButton = (RadioButton) findViewById(R.id.creditoRadioButton);
    	aVistaRadioButton = (RadioButton) findViewById(R.id.aVistaRadioButton);
    	parceladoRadioButton = (RadioButton) findViewById(R.id.parceladoRadioButton);
    	
    	button = (Button) findViewById(R.id.resultButton);
    	button.setOnClickListener(this);
    }

    protected void onResume() {
    	super.onResume();
    	
    }

	// Cria um objeto transação e o popula com os dados preenchidos no app de integração para enviar ao app da Stone. 
    private void startTransaction() {
    	Transaction mTransaction = new Transaction();
		mTransaction.setAmount(valueEditText.getText().toString());
		mTransaction.setTypeOfPurchase(debitoRadioButton.isChecked() ? 1 : 2);
		mTransaction.setTypeOfInstalment(aVistaRadioButton.isChecked() ? 0 : 1);
		mTransaction.setNumberOfInstalments(Integer.valueOf(spinner.getSelectedItem().toString()));
		mTransaction.setDemandId(0);
		
		StartTransaction.startNewTransaction(this, mTransaction, null, null);
	}

    // Ao clicar no botão os dados são enviados para o app da Stone.
	public void onClick(View v) {
    	if (valueEditText.getText().toString().equals("") == false) {
			startTransaction();
		} else {
			Toast.makeText(getApplicationContext(), "Valores precisam estar preenchidos.", Toast.LENGTH_SHORT).show();
		}
	}
    
	public void onCheckedChanged(RadioGroup group, int checkedId) {}
}
