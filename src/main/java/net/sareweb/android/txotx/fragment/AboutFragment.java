package net.sareweb.android.txotx.fragment;

import net.sareweb.android.txotx.R;
import net.sareweb.android.txotx.image.ImageLoader;
import net.sareweb.android.txotx.model.Oharra;
import net.sareweb.android.txotx.rest.OharraRESTClient;
import net.sareweb.android.txotx.rest.TxotxConnectionData;
import net.sareweb.android.txotx.util.ImageUtils;
import net.sareweb.android.txotx.util.TxotxPrefs_;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

@EFragment(R.layout.about)
public class AboutFragment extends SherlockFragment {

	private static final String TAG = "AboutFragment";
	
	@ViewById(R.id.txVersion)
	TextView txVersion;

	@Override
	public void onResume() {
		super.onResume();
		try {
			txVersion.setText("Txootx! " + getSherlockActivity().getPackageManager().getPackageInfo(getSherlockActivity().getPackageName(), 0).versionName);
			Log.d(TAG, "APK version code: " + getSherlockActivity().getPackageManager().getPackageInfo(getSherlockActivity().getPackageName(), 0).versionCode);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}		
	}

}
