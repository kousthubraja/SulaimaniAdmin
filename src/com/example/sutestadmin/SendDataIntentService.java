package com.example.sutestadmin;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendDataIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this

    private static final String TAG = "SendDataIntentService";
    private static final String APP_SERVER_URL = "http://sutest.comuv.com//addItem.php";


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SendDataIntentService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String itemName = extras.getString("itemName");
        String price = extras.getString("price");
        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(APP_SERVER_URL);

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("itemName", itemName));
                    nameValuePairs.add(new BasicNameValuePair("price", price));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }


}
