package com.example.sutestadmin;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class Transactions extends Activity {
	
	private final String transURL = "http://sutest.comuv.com/showTransactions.php";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    if(!isConn()){
	    	Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_LONG).show();
	    	finish();
	    }
	    setContentView(R.layout.activity_transactions);
	    WebView wv = (WebView) findViewById(R.id.webView_trans);
	    wv.setInitialScale(95);
	    wv.loadUrl(transURL);
	    // TODO Auto-generated method stub
	}

	public boolean isConn() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }
}
