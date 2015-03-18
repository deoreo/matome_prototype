package com.jds.webapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;


public class AlertDialogManager {
	/**
	 * Displays common alert dialog based on given parameter (with no response
	 * on OK clicked)
	 * 
	 * @param context
	 *            Context of activity which show the alert dialog
	 * @param title
	 *            Title of alert dialog
	 * @param message
	 *            Message of alert dialog
	 * @param status
	 *            true if success, false if failed
	 */
	public void showAlertDialog(
                    final Context context,
                    String title,
			        String message, Boolean status) {
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		
		Resources res = context.getResources();
		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

        // Setting Dialog not to be dismissed outside the box
        alertDialog.setCanceledOnTouchOutside(false);

		if (status != null)
			// Setting alert dialog icon
			alertDialog
					.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
				"OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
					}
				});

	}

    /**
     * Displays common alert dialog based on given parameter
     *
     * @param context
     *            Context of activity which show the alert dialog
     * @param title
     *            Title of alert dialog
     * @param message
     *            Message of alert dialog
     * @param listener
     *            Listener of the button
     * @param status
     *            true if success, false if failed
     */
    public void showAlertDialog(
                        final Context context,
                        String title,
                        String message,
                        DialogInterface.OnClickListener listener,
                        Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        Resources res = context.getResources();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        if (status != null){
            alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        }

        // Setting Dialog not to be dismissed outside the box
        alertDialog.setCanceledOnTouchOutside(false);

        // Setting OK Button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                "OK",
                listener);

    }

	/**
	 * Displays Internet alert dialog (with OK go to HOME)
	 * 
	 * @param context
	 * @param status
	 */
	public void showInternetAlertDialog(final Context context, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		Resources res = context.getResources();
		String message = "Could not connect to internet";
		String title = "Internet Trouble";

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

        // Setting Dialog not to be dismissed outside the box
        alertDialog.setCanceledOnTouchOutside(false);

		if (status != null)
			alertDialog
					.setIcon((status) ? R.drawable.success : R.drawable.fail);

		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
				"OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

}
