package net.sareweb.android.txotx.fragment.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class VersionDialogFragment extends DialogFragment {
	
	boolean mandatory;
	private static String TAG = "VersionDialogFragment";
	
	public VersionDialogFragment(boolean mandatory) {
		this.mandatory=mandatory;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		String title = "Txootx!-en eguneraketa berri bat eskuragarri dago.";
		if (mandatory)
			title = "Txootx!-en bertsioa eguneratu behar duzu erabiltzen jarraitu ahal izateko.";

		builder.setMessage(title)
				.setPositiveButton("Eguneratu",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Log.d(TAG,"Eguneratu.");
								mListener.onDialogPositiveClick(VersionDialogFragment.this);
							}
						})
				.setNegativeButton("Orain ez!",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Log.d(TAG,"Ez eguneratu.");
								mListener.onDialogNegativeClick(VersionDialogFragment.this);
							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}
	
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        // Verify that the host activity implements the callback interface
	        try {
	            // Instantiate the NoticeDialogListener so we can send events to the host
	            mListener = (VersionDialogListener) activity;
	        } catch (ClassCastException e) {
	            // The activity doesn't implement the interface, throw exception
	            throw new ClassCastException(activity.toString()
	                    + " must implement NoticeDialogListener");
	        }
	    }

	public interface VersionDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	VersionDialogListener mListener;

}
