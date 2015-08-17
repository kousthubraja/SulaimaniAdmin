package com.example.sutestadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ViewFeedback extends Activity {
	
	// Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    
	ArrayList<HashMap<String, String>> feedbackList;

	 private static final String TAG_DATA = "data";
	 private static final String TAG_USERNAME = "username";
	 private static final String TAG_FEEDBACK = "feedback";
	 private static String url_all_feedback = "http://sutest.comuv.com/getfeedback.php";
	 
	// products JSONArray
	JSONArray feedback = null;
	 
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_feedback);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        feedbackList = new ArrayList<HashMap<String, String>>();
        
        new LoadAllFeedback().execute();
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
    
    class LoadAllFeedback extends AsyncTask<String, String, String> {

    	boolean failed = false;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {}

        /**
         * getting All feedback from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            

            try {
            	JSONObject json = jParser.makeHttpRequest(url_all_feedback, "GET", params);
            	// Getting Array of feedback
                feedback = json.getJSONArray(TAG_DATA);

                // looping through All feedback
                for (int i = 0; i < feedback.length(); i++) {
                    JSONObject c = feedback.getJSONObject(i);

                    // Storing each json item in variable
                    String username = c.getString(TAG_USERNAME);
                    String feedback = c.getString(TAG_FEEDBACK);

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_USERNAME, username);
                    map.put(TAG_FEEDBACK, feedback);

                    // adding HashList to ArrayList
                    feedbackList.add(map);
                }
                //               }
            } catch (Exception e) {
            	failed = true;
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
//            pDialog.dismiss();
            // updating UI from Background Thread
        	if(failed){
        		Toast.makeText(getApplicationContext(), "Cannot retrieve feedbacks, No internet connection.", Toast.LENGTH_LONG).show();
        		return;
        	}
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                	
                	ListView lv = (ListView) findViewById(R.id.feedbackListView);
                	
                    ListAdapter adapter = new SimpleAdapter(
                            getApplicationContext(), feedbackList,
                            R.layout.feedback_item, new String[]{TAG_USERNAME, TAG_FEEDBACK},
                            new int[]{R.id.username, R.id.feedback});
                    // updating listview
                    lv.setAdapter(adapter);
                }
            });

        }
    }

}

