package com.wireless.home;

import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyListActivity extends ListActivity
{
    private WirelessAtHomeService mService;
    private ArrayAdapter<String> mAdapter;
    private List<String> mMACIDList;
    private boolean mIsBound = false;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_mylist);
    	
    	mMACIDList = new ArrayList<String>();
    	mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mMACIDList);
    	setListAdapter(mAdapter);
    	
    	doBindService();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    // Handle item selection
	    switch (item.getItemId()) 
	    {
	        case R.id.menu_quit:
	        	
				Toast.makeText(this, "Stopping Wireless@Home Service", Toast.LENGTH_SHORT).show();
				
				// Unbinding from the service and stopping it
				doUnbindService();
				stopService(new Intent(MyListActivity.this, WirelessAtHomeService.class));
				
				// Destroying activity
				finish();
				
	            return true;
	            
	        default:
	            return false;
	    }
	}
    
    private ServiceConnection mConnection = new ServiceConnection() 
    {
    	public void onServiceConnected(ComponentName className, IBinder binder)
    	{
    		mService = ((WirelessAtHomeService.MyBinder) binder).getService();
    		
    		Toast.makeText(MyListActivity.this, "Connected to the service", Toast.LENGTH_SHORT).show();
    	}
    	
    	public void onServiceDisconnected(ComponentName className) 
    	{
    		mService = null;
    		Toast.makeText(MyListActivity.this, "Disconnected from the service", Toast.LENGTH_SHORT).show();
    	}
    };
    
    void doBindService() 
    {
    	mIsBound = true;
    	bindService(new Intent(this, WirelessAtHomeService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    public void showServiceData() 
    {
      if (mService != null) 
      {
        Toast.makeText(this, "Number of elements = " + mService.getMACIDList().size(), Toast.LENGTH_SHORT).show();
        
        mMACIDList.clear();
        mMACIDList.addAll(mService.getMACIDList());
        mAdapter.notifyDataSetChanged();
      }
    }
  
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.mylist_button_start:
			
			Toast.makeText(this, "Starting Wireless@Home Service", Toast.LENGTH_SHORT).show();
			
		    startService(new Intent(MyListActivity.this, WirelessAtHomeService.class));
		    
	        break;
	        
		case R.id.mylist_button_stop:
			
			Toast.makeText(this, "Stopping Wireless@Home Service", Toast.LENGTH_SHORT).show();
			
			doUnbindService();
			stopService(new Intent(MyListActivity.this, WirelessAtHomeService.class));
			
			break;
			
		case R.id.mylist_button_results:
			
			showServiceData();
			break;
		}
	}
	
	void doUnbindService() 
	{
        if (mIsBound) 
        {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
            
            Toast.makeText(this, "Unbinding Wireless@Home Service", Toast.LENGTH_SHORT).show();
        }
    }
	
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        try 
        {
            doUnbindService();
        } 
        catch (Throwable t) { }
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {
    	// Starting ListItem Activity
    	Intent intent = new Intent(MyListActivity.this, ListItemActivity.class);
    	intent.putExtra("macid", ((TextView)v).getText().toString());
    	startActivity(intent);

      super.onListItemClick(l, v, position, id);
    }
    
} 