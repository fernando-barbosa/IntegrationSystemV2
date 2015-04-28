package com.example.stoneintegration;

import br.com.stone.classes.StartCancellation;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CancellationActivity extends ActionBarActivity implements View.OnClickListener {

	public TextView titleTextView;
	public EditText arnEditText;
	public EditText caEditText;
	public Button cancelButton;
	
	Bundle backActivity = null;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation);
        
        instanceView();
	}
	
	protected void onResume() {
    	super.onResume();
    	
    	//responseCancellation();
	}
	
	// Instancia todos os elementos desta activity.
	private void instanceView() {
		titleTextView = (TextView) findViewById(R.id.cancellationLabel);
		arnEditText = (EditText) findViewById(R.id.arnEditText);
		caEditText  = (EditText) findViewById(R.id.caEditText);
		cancelButton = (Button) findViewById(R.id.cancellationButton);
		
		cancelButton.setOnClickListener(this);
	}

	// Ao clicar no botão os dados para cancelamento são enviados para o app da Stone.
	public void onClick(View v) {
		cancelTransaction();
	}
	
	// Inicia o cancelamento da transação com o app Stone.
	private void cancelTransaction() {
		String arn = arnEditText.getText().toString();
		String ca  = caEditText.getText().toString();
		
		if (arnEditText.getText().equals("") || caEditText.getText().equals("") == false) {
			StartCancellation.sendCancellationToStoneApplication(getApplicationContext(), arn, ca);
			
			//responseCancellation();
		} else {
			Toast.makeText(getApplicationContext(), "Valores precisam estar preenchidos.", Toast.LENGTH_SHORT).show();
		}
	}
}
