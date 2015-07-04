package com.example.sutestadmin;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class AddItem extends Activity{
	
	EditText item_name;
	EditText item_price;
	
	private static final String SERVER_URL = "http://sutest.comuv.com/";
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.add_item);

	        //getActionBar().setDisplayHomeAsUpEnabled(true);
	        
	        item_name = (EditText) findViewById(R.id.item_name);
       		item_price = (EditText) findViewById(R.id.item_price);
	    }

	 public void addItem(View v){
	    	new AsyncTask<Void, Void, Void>() {

		    	final String name = item_name.getText().toString();
		    	final String price = item_price.getText().toString();
		    	
	    		HttpEntity entity = null;
	    		String output = null;

				@Override
				protected Void doInBackground(Void... params) {
					HttpClient httpclient = new DefaultHttpClient();
					try{
						HttpPost httppost = new HttpPost(SERVER_URL + "addItem.php");
						List<NameValuePair> par = new ArrayList<NameValuePair>(2);
						par.add(new BasicNameValuePair("itemName", name));
						par.add(new BasicNameValuePair("price", price));
						httppost.setEntity(new UrlEncodedFormEntity(par, "UTF-8"));
						HttpResponse response = httpclient.execute(httppost);
						entity = response.getEntity();
						output = EntityUtils.toString(entity);
					}
					catch(Exception e){
						;
					}
					return null;
				}
				
				@Override
				protected void onPostExecute(Void result) {
					if(output != null ){
						String lines[] = output.split("\\r?\\n");
						output = lines[0];
						Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_SHORT).show();
						item_name.setText("");
						item_price.setText("");
					}
					else{
						Toast.makeText(getApplicationContext(), "Cannot add item, No internet connection.", Toast.LENGTH_LONG).show();
					}
					super.onPostExecute(result);
				}
			}.execute();

	    }

}
