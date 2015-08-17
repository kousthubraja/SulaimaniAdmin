package com.example.sutestadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	private GoogleCloudMessaging gcm;
	private static final String PROJECT_NUMBER = "273844770174";
	private static final String APP_SERVER_URL = "http://sutest.comuv.com//gcm_registration.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sharedPreferences.getString("SENT_TOKEN_TO_SERVER", "");
        if(token.isEmpty()) 
        	getRegId();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void add_item(View view) {
    	Intent intent = new Intent(this, AddItem.class);
    	startActivity(intent);
    	
    }
    
    public void delete_item(View view) {
    	Intent intent = new Intent(this, DelItem.class);
    	startActivity(intent);
    	
    }
    
    public void view_orders(View view) {
    	Intent intent = new Intent(this, Orders.class);
    	startActivity(intent);
    	
    }
    
    public void view_feedback(View view) {
    	Intent intent = new Intent(this, ViewFeedback.class);
    	startActivity(intent);
    	
    }
    
    public void view_transactions(View view) {
    	Intent intent = new Intent(this, Transactions.class);
    	startActivity(intent);
    	
    }
    
    public void exit(View v){
		finish();
	}
    
    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    String token = gcm.register(PROJECT_NUMBER);
                    Log.i("GCM",  token);
                    
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(APP_SERVER_URL);
                    
                	try{
	                    // Add your data
	                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	                    nameValuePairs.add(new BasicNameValuePair("regId", token));
	                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
	
	                    // Execute HTTP Post Request
	                    HttpResponse response = httpclient.execute(httppost);
	                } catch (ClientProtocolException e) {
	                }
		            

                } catch (IOException ex) {

                }
                return null;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }
}
