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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

public class DelItem extends Activity {

	private static final String SERVER_URL = "http://sutest.comuv.com/";
	private SharedPreferences sharedPref;
	Context context;
	
	FoodAdapter foodAdapter;
	String orderJSON;
	double totalPrice = 0;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	  //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    
	    setContentView(R.layout.activity_order);
	    
	    context = getApplicationContext();
	    sharedPref = getSharedPreferences("userAuth",Context.MODE_PRIVATE);
	    
	  
	    fetchItems(null);

	}
	
	//Get all the orders the user have placed and display it
	public void fetchItems(View v){
		Toast.makeText(context,"Retrieving items...", Toast.LENGTH_SHORT).show();;
		
		new AsyncTask<Void, Void, Void>() {

    		HttpEntity entity = null;
    		String output = null;

			@Override
			protected Void doInBackground(Void... params) {
				HttpClient httpclient = new DefaultHttpClient();
				try{
					HttpPost httppost = new HttpPost(SERVER_URL + "listitems.php");
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
					try {
						String lines[] = output.split("\\r?\\n");
						output = lines[0];
						
						JSONArray reader = new JSONArray(output);
						
						ArrayList<FoodItem> itemList = new ArrayList<FoodItem>();
						
						for(int i=0; i< reader.length(); i++){
							JSONObject obj = reader.getJSONObject(i);
							//Toast.makeText(context, obj.getString("itemName"), Toast.LENGTH_SHORT).show();
							FoodItem item = new FoodItem(obj.getInt("id"), obj.getString("itemName"), obj.getDouble("price"));
							itemList.add(item);
						}
						foodAdapter = new FoodAdapter(context, R.layout.list_food_item, itemList);
						ListView listView1 = (ListView) findViewById(R.id.lstOrders);
						foodAdapter.notifyDataSetChanged();
						listView1.setAdapter(foodAdapter);
						
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					
				}
				else{
					Toast.makeText(getApplicationContext(), "Cannot retrieve items, No internet connection.", Toast.LENGTH_LONG).show();
					finish();
				}
				
				super.onPostExecute(result);
			}
    		
		}.execute();
		
		

    }
	
	public void sendDelete(){
		List<FoodItem> itemList = foodAdapter.foodList;
		JSONObject order = new JSONObject();
		JSONArray items = new JSONArray();
		totalPrice = 0;
		
		try {
			order.put("userName",sharedPref.getString("userName", ""));
			for(FoodItem temp:itemList){
				if(temp.quantity>0){
					JSONObject tmp = new JSONObject();
					tmp.put(String.valueOf(temp.id), temp.quantity);
					items.put(tmp);
					totalPrice += temp.price*temp.quantity;

				}
			}
			
			if(totalPrice == 0){
				Toast.makeText(context, "No item selected", Toast.LENGTH_SHORT).show();
				return;
			}

			order.put("items", items);
			
			orderJSON= order.toString();
			
			Toast.makeText(context, orderJSON
					, Toast.LENGTH_SHORT).show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void pay(View v){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	//sendOrder(orderJSON, totalPrice);
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(DelItem.this);
		builder.setMessage("Are you sure to pay Rs." + String.valueOf(totalPrice) + " from your card?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
		
		
	}
	
	public void confirmPay(View v){
		Toast.makeText(context, "Payment done", Toast.LENGTH_LONG).show();
		
		
		finish();
		
	}
	
	public void deleteItems(View v){
		List<FoodItem> itemList = foodAdapter.foodList;
		JSONObject order = new JSONObject();
		JSONArray items = new JSONArray();
		totalPrice = 0;
		
		try {
			order.put("userName",sharedPref.getString("userName", ""));
			for(FoodItem temp:itemList){
				if(temp.quantity>0){
					JSONObject tmp = new JSONObject();
					tmp.put(String.valueOf(temp.id), temp.quantity);
					items.put(tmp);
					totalPrice += temp.price*temp.quantity;

				}
			}
			
			if(totalPrice == 0){
				Toast.makeText(context, "No item selected", Toast.LENGTH_SHORT).show();
				return;
			}

			order.put("items", items);
			
			orderJSON= items.toString();
			
			Toast.makeText(context, orderJSON, Toast.LENGTH_SHORT).show();
			sendOrder(orderJSON);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		setContentView(R.layout.screen_choosedate);
		
	}
	
	
	
	void sendOrder(final String orderJSON){
		new AsyncTask<String, Void, Void>() {

    		String output = null;
    		
			@Override
			protected Void doInBackground(String... orderJSON) {
				HttpClient httpclient = new DefaultHttpClient();
				HttpEntity entity = null;
				
				try{
					
					HttpPost httppost = new HttpPost(SERVER_URL + "deleteItem.php");
					
					//Sets the POST request parameters
					List<NameValuePair> par = new ArrayList<NameValuePair>(2);
					par.add(new BasicNameValuePair("orderJSON", orderJSON[0]));
					
//					par.add(new BasicNameValuePair("date", getDateTime()));
					httppost.setEntity(new UrlEncodedFormEntity(par, "UTF-8"));
					
					//Send the order JSON and other details
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
					
					Toast.makeText(context, "result", Toast.LENGTH_LONG).show();
					finish();
				}
				else{
					Toast.makeText(context, "Cannot place order, No internet connection.", Toast.LENGTH_LONG).show();
				}
				
				super.onPostExecute(result);
			}
    		
		}.execute(orderJSON);
	}

	
	
}



