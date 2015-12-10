package com.holcim.altimetrik.android.exception;

import com.holcim.hsea.R;
import com.holcim.altimetrik.android.utilities.OAuthUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Class to handle error messages, progress screen and others modal message screen in application
 * @author labreu
 *
 */
public class HolcimDialogHandler extends Dialog {

	private static Context mContext;
	private android.view.View.OnClickListener mLogoutClickListener;
	public android.view.View.OnClickListener mDismissClickListener;

	public int mSelectedMonth = 0;
	public int mSelectedYear = 0;


	private static final int PROGRESS_DIALOG 	= 0;
	private static final int ERROR_DIALOG 		= 1;
	private static final int CONFIRM_DIALOG 	= 2;
	private static final int SWIPER_DIALOG 		= 3;
	private static final int INFO_DIALOG        = 4;
	private static final int DATE_PICKER_DIALOG = 5;
	private static final int SHOW_QUESTION_MESSAGE = 6;

	public HolcimDialogHandler(final Context context) {
		super(context, R.style.CustomDialogTheme);
		mContext = context;
		mLogoutClickListener = new android.view.View.OnClickListener() {			
			public void onClick(View view) {
				OAuthUtil.Delete(mContext);
				//Intent i = new Intent(mContext, LensSplashActivity.class);
				//mContext.startActivity(i);
				//LensFragmentController.tryCleanFragmentManager((FragmentActivity)context);
			}
		};
		mDismissClickListener = new android.view.View.OnClickListener() {			
			public void onClick(View v) {
				HolcimDialogHandler.this.dismiss();
			}
		};
	}

	/**
	 * Set the layout based in parameters
	 */
	private void setTheme(int pType, String pMessage, String pButtonOKText, 
			String pButtonCancelText, android.view.View.OnClickListener pButtonOKOnclickListener, 
			android.view.View.OnClickListener pButtonCancelOnclickListener, int pMonth, int pYear)
	{
		LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.custom_dialog_view, null);

		Button btnOkSmall     = (Button)view.findViewById(R.id.confirmButton1);
		Button btnOk          = (Button)view.findViewById(R.id.btnOk);
		Button btnCancelSmall = (Button)view.findViewById(R.id.cancelButton1);

		LinearLayout contentLayout     = (LinearLayout)view.findViewById(R.id.content_layout);
		LinearLayout progressBarLayout = (LinearLayout)view.findViewById(R.id.progress);
		LinearLayout twoButtonsLayout  = (LinearLayout)view.findViewById(R.id.two_buttons_layout);
		LinearLayout oneButtonLayout   = (LinearLayout)view.findViewById(R.id.one_button_layout);

		TextView title = (TextView)view.findViewById(R.id.content_title);
		TextView message = (TextView)view.findViewById(R.id.content_text);

		switch (pType) {
		case PROGRESS_DIALOG:
		{
			contentLayout.setVisibility(View.GONE);
			progressBarLayout.setVisibility(View.VISIBLE);
			break;
		}
		case CONFIRM_DIALOG:
		{
			title.setText(mContext.getString(R.string.message_title_confirm));
			message.setText(pMessage);
			btnOkSmall.setText(pButtonOKText);
			btnCancelSmall.setText(pButtonCancelText);
			if (pButtonOKOnclickListener != null)
				btnOkSmall.setOnClickListener(pButtonOKOnclickListener);
			if (pButtonCancelOnclickListener != null)
				btnCancelSmall.setOnClickListener(pButtonCancelOnclickListener);
			twoButtonsLayout.setVisibility(View.VISIBLE);

			break;
		}
		case INFO_DIALOG:
		{
			title.setText(mContext.getString(R.string.message_title_info));
			message.setText(pMessage);
			btnOk.setText(pButtonOKText);
			if (pButtonOKOnclickListener != null)
				btnOk.setOnClickListener(pButtonOKOnclickListener);
			oneButtonLayout.setVisibility(View.VISIBLE);
			break;
		}
		case SHOW_QUESTION_MESSAGE:{
			title.setText(mContext.getString(R.string.you_are_about_to_close));
			message.setText(pMessage);
			btnOkSmall.setText(pButtonOKText);
			btnCancelSmall.setText(pButtonCancelText);
			if (pButtonOKOnclickListener != null)
				btnOkSmall.setOnClickListener(pButtonOKOnclickListener);
			if (pButtonCancelOnclickListener != null)
				btnCancelSmall.setOnClickListener(pButtonCancelOnclickListener);
			twoButtonsLayout.setVisibility(View.VISIBLE);
			break;
		}
		default:
		{
			title.setText(mContext.getString(R.string.message_title_error));
			message.setText(pMessage);
			btnOk.setText(pButtonOKText);
			if (pButtonOKOnclickListener != null)
				btnOk.setOnClickListener(pButtonOKOnclickListener);
			oneButtonLayout.setVisibility(View.VISIBLE);
			break;
		}
		}

		this.setContentView(view);

		WindowManager.LayoutParams lp = this.getWindow().getAttributes();  
		lp.dimAmount=0.6f; 
		this.getWindow().setAttributes(lp);  
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

	}

	public void showDatePicker(android.view.View.OnClickListener pButtonOnClickListener, int pMonth, int pYear)
	{		
		this.setTheme(DATE_PICKER_DIALOG, "", "", "", pButtonOnClickListener, mDismissClickListener, pMonth, pYear);		
		this.show();
	}

	public void showSwiperDialog(String pMessage, String pButtonText, android.view.View.OnClickListener pButtonOnClickListener)
	{
		this.setTheme(SWIPER_DIALOG, pMessage, pButtonText, "", pButtonOnClickListener, null, 0, 0);
		this.show();
	}

	// No internet connection with custom on click function
	public void showNoInternetRefreshDialog(android.view.View.OnClickListener pButtonOnClickListener) {
		this.setTheme(INFO_DIALOG, mContext.getString(R.string.message_no_internet_connection), mContext.getString(R.string.message_button_ok), "", pButtonOnClickListener, null, 0, 0);
		this.show();
	}

	// No internet connection modal with ok dismiss
	public void showNoInternetRefreshDialog() {
		this.setTheme(INFO_DIALOG, mContext.getString(R.string.message_no_internet_connection), mContext.getString(R.string.message_button_ok), "", mDismissClickListener, null, 0, 0);
		this.show();
	}

	// You cannot set folder name as NULL or empty
	public void showEditTextError() {
		this.setTheme(INFO_DIALOG, mContext.getString(R.string.error_edittext), mContext.getString(R.string.message_button_ok), "", mDismissClickListener, null, 0, 0);
		this.show();
	}

	public void hideNoInternetRefreshDialog() {
		this.hide();
	}

	/**
	 * Show message for session expiration
	 */
	public void showSessionExpiration()
	{
		this.showError(mContext.getString(R.string.message_session_expired), 
				mContext.getString(R.string.message_button_ok), mLogoutClickListener);
	}

	public void showSessionExpiration(android.view.View.OnClickListener pLogoutOnClickListener)
	{
		this.showError(mContext.getString(R.string.message_session_expired), 
				mContext.getString(R.string.message_button_ok), pLogoutOnClickListener);
	}

	/**
	 * Show message for no connectivity
	 */
	public void showNoConnectionError()
	{
		this.setTheme(ERROR_DIALOG, mContext.getString(R.string.message_no_internet_connection), 
				mContext.getString(R.string.message_button_close), "", mLogoutClickListener, null, 0, 0);
		this.setCancelable(false);
		this.show();
	}

	/**
	 * Show message for no connectivity
	 */
	public void showNoConnectionErrorLogin()
	{
		this.setTheme(ERROR_DIALOG, mContext.getString(R.string.message_no_internet_connection), 
				mContext.getString(R.string.message_button_close), "", mDismissClickListener, null, 0, 0);
		this.setCancelable(false);
		this.show();
	}

	public void showInformation(String pMessage){
		this.setTheme(INFO_DIALOG, pMessage, mContext.getString(R.string.message_button_ok), null, mDismissClickListener, null, 0, 0);
		this.setCancelable(false);
		this.show();
	}

	public void showInformation(String pMessage, android.view.View.OnClickListener pDismissClickListener){
		this.setTheme(INFO_DIALOG, pMessage, mContext.getString(R.string.message_button_ok), null, pDismissClickListener, null, 0, 0);
		this.setCancelable(false);
		this.show();
	}

	/**
	 * Show confirmation message with 2 buttons 
	 * @param pMessage
	 * @param pButtonOKText
	 * @param pButtonCancelText
	 * @param pButtonOKClickListener
	 * @param pButtonCancelClickListener
	 */
	public void showConfirmation(String pMessage, String pButtonOKText, 
			String pButtonCancelText, 
			android.view.View.OnClickListener pButtonOKClickListener,
			android.view.View.OnClickListener pButtonCancelClickListener)
	{
		this.setTheme(CONFIRM_DIALOG, pMessage, pButtonOKText, pButtonCancelText, pButtonOKClickListener, pButtonCancelClickListener, 0, 0);
		this.setCancelable(true);
		this.show();
	}

	/**
	 * Show a generic app error for internal exceptions management
	 */
	public void showGenericError()
	{
		this.showErrorButtonClose(mContext.getString(R.string.message_app_internal_error));
	}

	/**
	 * Show the progress screen
	 */
	public void showProgress()
	{
		this.setTheme(PROGRESS_DIALOG, "", "", "", null, null, 0, 0);
		this.setCancelable(false);
		this.show();
	}

	/**
	 * Show error message with one button
	 * @param pMessage
	 * @param pButtonText
	 * @param pButtonClickListener
	 */
	public void showError(String pMessage, String pButtonText, android.view.View.OnClickListener pButtonClickListener)
	{
		this.setTheme(ERROR_DIALOG, pMessage, pButtonText, "", pButtonClickListener, null, 0, 0);
		this.setCancelable(false);
		this.show();
	}

	/**
	 * Show error message with one button with text "Close" and close dialog action
	 * @param pMessage
	 * @param pButtonText
	 * @param pButtonClickListener
	 */
	public void showErrorButtonClose(String pMessage)
	{
		this.setTheme(ERROR_DIALOG, pMessage, mContext.getString(R.string.message_button_close), "", mDismissClickListener, null, 0, 0);
		this.setCancelable(false);
		this.show();
	}

	/**
	 * Show error message with one button with text "OK" and close dialog action
	 * @param pMessage
	 * @param pButtonText
	 * @param pButtonClickListener
	 */
	public void showErrorButtonOK(String pMessage)
	{
		this.setTheme(ERROR_DIALOG, pMessage, mContext.getString(R.string.message_button_ok), "", mDismissClickListener, null, 0, 0);
		this.setCancelable(false);
		this.show();
	}

	/**
	 * Show error message with one button with text "OK" and close dialog action
	 * @param pMessage
	 * @param pButtonText
	 * @param pButtonClickListener
	 */
	public void showQuestionMessage(String pMessage, View.OnClickListener okButtonOnClickListener)
	{
		this.setTheme(SHOW_QUESTION_MESSAGE, pMessage, mContext.getString(R.string.message_title_confirm),mContext.getString(R.string.cancel) ,okButtonOnClickListener, mDismissClickListener, 0, 0);
		this.setCancelable(false);
		this.show();
	}

	/**
	 * Show confirmation dialog to logout
	 */
	public void showConfirmLogout()
	{
		this.setTheme(CONFIRM_DIALOG, mContext.getString(R.string.message_logout_confirmation), 
				mContext.getString(R.string.message_button_yes), 
				mContext.getString(R.string.message_button_no), mLogoutClickListener, mDismissClickListener, 0, 0);
		this.setCancelable(false);
		this.show();
	}

	public void showConfirmLogout(android.view.View.OnClickListener pLogoutOnClickListener)
	{
		this.setTheme(CONFIRM_DIALOG, mContext.getString(R.string.message_logout_confirmation), 
				mContext.getString(R.string.message_button_yes), 
				mContext.getString(R.string.message_button_no), pLogoutOnClickListener, mDismissClickListener, 0, 0);
		this.setCancelable(false);
		this.show();
	}


}

