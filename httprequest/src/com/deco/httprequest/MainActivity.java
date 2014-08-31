package com.deco.httprequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		BeingWatched observed = new BeingWatched(); 
		Watcher observing = new Watcher(); 
		observed.addObserver(observing); 
		observed.Request("http://footballchallenger.net/service.php?nav=match&info=live&zip=0"); 		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class Watcher implements Observer { 
		public void update(Observable obj, Object arg) {
			String aa = (String)arg;
			TextView _mytext = (TextView)findViewById(R.id.mytext);
			_mytext.setText(aa);
		} 
	}	
}

class BeingWatched extends Observable { 
	void Request(String URL){
		 new MyRequestTask().execute(URL);
	}
	
	class MyRequestTask extends AsyncTask<String, String, String>{
	    @Override
	    protected String doInBackground(String... uri) {
	        HttpClient httpclient = new DefaultHttpClient();
	        HttpResponse response;
	        String responseString = null;
	        try {
	            response = httpclient.execute(new HttpGet(uri[0]));
	            StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            } else{
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
	        } catch (ClientProtocolException e) {
	        } 
	        catch (IOException e) {
	        }
	        
	        return responseString;
	    }
	    
	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
			setChanged();
			notifyObservers(result);
	    }	    
	}
}
