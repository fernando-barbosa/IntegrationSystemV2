package com.example.stoneintegration;

import java.util.ArrayList;
import java.util.List;

import br.com.stone.classes.StartPrint;
import br.com.stone.methods.CancellationResponse;
import br.com.stone.methods.TransactionResponse;
import br.com.stone.objects.PrintObject;
import br.com.stone.xml.ReturnOfCancellationXml;
import br.com.stone.xml.ReturnOfTransactionXml;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

	public Button transactionButton,
				  cancelationButton,
				  printerButton;
	
	public static String printAmount = null, 
						 printCa = null,
						 printArn = null,
						 printParcel = null,
						 printFlag = null,
						 printStatus = null,
						 printDate = null,
						 printAmountOfInstallments = null,
						 printDemandId = null,
						 printTransactionType = null;
	
	BluetoothAdapter mBluetoothAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		instanceView();
	}

	protected void onResume() {
		super.onResume();
		
		transactionResponse();
	}
	
	// Instancia todos os elementos da activity.
    private void instanceView() {
    	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	
    	transactionButton = (Button) findViewById(R.id.goToTransactionButton);
    	cancelationButton = (Button) findViewById(R.id.goToCancelationButton);
    	printerButton     = (Button) findViewById(R.id.goToPrinterButton);
    	
    	transactionButton.setOnClickListener(this);
    	cancelationButton.setOnClickListener(this);
    	printerButton.setOnClickListener(this);
    }

	public void onClick(View v) {
		
		Intent intent = null;

		switch (v.getId()) {
		case R.id.goToTransactionButton:
			enableBluetoothDialog(transactionButton);
			intent = new Intent(MainActivity.this, TransactionActivity.class);
			break;
			
		case R.id.goToCancelationButton:
			intent = new Intent(MainActivity.this, CancellationActivity.class);
			break;

		case R.id.goToPrinterButton:
			if (printCa != null) {
				Toast.makeText(getApplicationContext(), "Imprimindo...", Toast.LENGTH_SHORT).show();
				printerList();
			} else {
				Toast.makeText(getApplicationContext(), "É necessário fazer uma transação.", Toast.LENGTH_SHORT).show();
			}
			break;

		}
		
		if (intent != null) {
			startActivity(intent);
		}
	}
	
    // Pega os dados do aplicativo Stone através de um bundle e cria um log informativo para transação ou cancelamento. 
    void transactionResponse() {
    	Bundle backActivity = getIntent().getExtras();
    	
    	if (backActivity != null) {
    		String xmlTransaction = backActivity.getString("xmlTransaction");
    		String xmlCancellation = backActivity.getString("xmlCancellation");
    		
    		if(xmlTransaction != null && xmlTransaction.equals("") == false) {
    			ReturnOfTransactionXml mReturnOfTransactionXml = new ReturnOfTransactionXml();
				mReturnOfTransactionXml = TransactionResponse.getTransaction(this, xmlTransaction, backActivity);
				
				Log.i("sdk_stone",
    						"\n\n======== Dados recebidos SDK ========"
    						+ "\nValor               : " + mReturnOfTransactionXml.amount
    						+ "\nCA                  : " + mReturnOfTransactionXml.ca
    						+ "\nARN                 : " + mReturnOfTransactionXml.arn
    						+ "\nParcelas            : " + mReturnOfTransactionXml.parcel
    						+ "\nBandeira            : " + mReturnOfTransactionXml.flag
    						+ "\nStatus              : " + mReturnOfTransactionXml.status
    						+ "\nData                : " + mReturnOfTransactionXml.date
    						+ "\nAmountOfInst.       : " + mReturnOfTransactionXml.amountOfInstallments
    						+ "\nDemandId            : " + mReturnOfTransactionXml.demandId
    						+ "\nTipo da transação   : " + mReturnOfTransactionXml.transactionType);
				
				// Atribui os dados que serão impressos no método printerList() por meio de uma string.
				printAmount = "Valor : R$" + mReturnOfTransactionXml.amount;
				printCa = "CA : " + mReturnOfTransactionXml.ca;
				printArn = "ARN : " + mReturnOfTransactionXml.arn;
				printParcel = "Parcelas : " + mReturnOfTransactionXml.parcel;
				printFlag = "Bandeira : " + mReturnOfTransactionXml.flag;
				printStatus = "Status : " + mReturnOfTransactionXml.status;
				printDate = "Data : " + mReturnOfTransactionXml.date;
				printAmountOfInstallments = "AmountOfInst.: " + mReturnOfTransactionXml.amountOfInstallments;
				printDemandId = "DemandId : " + mReturnOfTransactionXml.demandId;
				printTransactionType = "Tipo da transação : " + mReturnOfTransactionXml.transactionType;
    		}
    		
    		if (xmlCancellation != null && xmlCancellation.equals("") == false) {
				ReturnOfCancellationXml mReturnOfCancellationXml = new ReturnOfCancellationXml();
				mReturnOfCancellationXml = CancellationResponse.getCancellation(this, xmlCancellation, backActivity);

				Log.i("sdk_stone", "\n\n======== Dados Recebidos SDK ========"
						+ "\nCA		: " + mReturnOfCancellationXml.ca 
						+ "\nARN	: " + mReturnOfCancellationXml.arn 
						+ "\nStatus	: " + mReturnOfCancellationXml.status);
			}
    		
    	} else {
    		Log.e("Bundle", "Bundle atual: null");
    	}
	}
    
    // Impressão dos dados da transação. Cria lista, popula a lista e envia comando para impressão.
    private void printerList() {
    	new Thread () {
    		public void run() {
    			// Formata o printAmount para que ele seja impresso com os dados de forma monetária.
    	    	String value = printAmount.substring(0, printAmount.length() - 2);
    	        String cents = printAmount.substring(value.length(), value.length() + 2);
    	        String amount = value + "," + cents;
    	    	
    	        // Cria uma lista e popula essa lista para ser impressa.
    			List<PrintObject> listPrintObjects = new ArrayList<PrintObject>();
    			listPrintObjects.add(new PrintObject("Dados Recebidos", PrintObject.BIG, PrintObject.CENTER));
    			listPrintObjects.add(new PrintObject(amount, PrintObject.MEDIUM, PrintObject.LEFT));
    			listPrintObjects.add(new PrintObject(printCa, PrintObject.MEDIUM, PrintObject.LEFT));
    			listPrintObjects.add(new PrintObject(printArn, PrintObject.MEDIUM, PrintObject.LEFT));
    			listPrintObjects.add(new PrintObject(printParcel, PrintObject.MEDIUM, PrintObject.LEFT));
    			listPrintObjects.add(new PrintObject(printFlag, PrintObject.MEDIUM, PrintObject.LEFT));
    			listPrintObjects.add(new PrintObject(printStatus, PrintObject.MEDIUM, PrintObject.LEFT));
    			listPrintObjects.add(new PrintObject(printDate, PrintObject.MEDIUM, PrintObject.LEFT));
    			listPrintObjects.add(new PrintObject(printAmountOfInstallments, PrintObject.MEDIUM, PrintObject.LEFT));
    			listPrintObjects.add(new PrintObject(printDemandId, PrintObject.MEDIUM, PrintObject.LEFT));
    			listPrintObjects.add(new PrintObject(printTransactionType, PrintObject.MEDIUM, PrintObject.LEFT));
    	    	StartPrint.sendPrint(MainActivity.this, listPrintObjects);    	    	  
    		}
    	}.start();
    	return;
    }
    
    // Exibe para o usuário, atráves de um dialog, que o Bluetooth está sendo ligado. 
 	private void enableBluetoothDialog(View view) {
 		final ProgressDialog ringProgressDialog = ProgressDialog.show(this, "Aguarde...", "Ligando Bluetooth.", true);
 		
 		ringProgressDialog.setCancelable(true);
 		
 		new Thread(new Runnable() {
 			public void run() {
 				try {
 					// Liga o adaptador Bluetooth.
 					mBluetoothAdapter.enable();
 					Thread.sleep(3 * 1000);
 				} catch (Exception e) {	}
 				ringProgressDialog.dismiss();
 			}
 		}).start();
 	}
}
