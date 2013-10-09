package net.sareweb.android.txotx.drawerToggle;

import net.sareweb.android.txotx.activity.TxotxActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class DrawerToggle extends ActionBarDrawerToggle {

	SherlockFragmentActivity activity;
	DrawerLayout drawerLayout;
	private static final String TAG ="DrawerToggle";
	
	public DrawerToggle(SherlockFragmentActivity activity, DrawerLayout drawerLayout,
			int drawerImageRes, int openDrawerContentDescRes,
			int closeDrawerContentDescRes) {
		super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes,
				closeDrawerContentDescRes);
		
		this.activity = activity;
		this.drawerLayout = drawerLayout;
		
	}
	
	 /** Called when a drawer has settled in a completely closed state. */
    public void onDrawerClosed(View view) {
    	activity.getSupportActionBar().setTitle("Txootx!");
    	activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
    }

    /** Called when a drawer has settled in a completely open state. */
    public void onDrawerOpened(View drawerView) {
    	activity.getSupportActionBar().setTitle("Menua");
    	activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
    }
    
    @Override
    public void onDrawerStateChanged(int newState) {
    	if(newState==2 && ((TxotxActivity)activity).drawerWasOpenedByBackButton()){
    		activity.finish();
    	}
    	else{
    		super.onDrawerStateChanged(newState);
    	}
    }
    
    
    
    

}
