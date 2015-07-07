package com.example.sutestadmin;

import java.net.URI;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Transactions extends Activity {
	
	private final String transURL = "http://sutest.comuv.com/showTransactions.php";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_transactions);
	    WebView wv = (WebView) findViewById(R.id.webView_trans);
	    wv.loadUrl(transURL);
	    // TODO Auto-generated method stub
	}

}
