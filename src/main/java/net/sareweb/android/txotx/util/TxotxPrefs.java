package net.sareweb.android.txotx.util;

import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref.Scope;

@SharedPref(value=Scope.UNIQUE)
public interface TxotxPrefs {
	/*String email();
	String user();
	String pass();
	long userId();
	String registrationId();*/
	int rejectedVersion(); //Codigo de la ultima versión que se ha desestimado actualizar
}