package com.holcim.altimetrik.android.utilities;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holcim.hsea.R;

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

	private boolean isHidden;

	private static final int PROGRESS_DIALOG 	= 0;
	private static final int ERROR_DIALOG 		= 1;
	private static final int CONFIRM_DIALOG 	= 2;
	private static final int SWIPER_DIALOG 		= 3;
	private static final int INFO_DIALOG        = 4;
	private static final int DATE_PICKER_DIALOG = 5;
	private static final int SHOW_QUESTION_MESSAGE = 6;
	private static final int NOTIFICATION_DIALOG = 7;
	private static final int WARNING_DIALOG = 8;
	private static final int DATEPICKER_DIALOG = 9;

	//Constructor
	public HolcimDialogHandler(Context context) {
		super(context,R.style.CustomDialogTheme);

		mContext = context;
		mLogoutClickListener = new android.view.View.OnClickListener() {			
			public void onClick(View view) {
				OAuthUtil.Delete(mContext);
				//TODO:uncomment
				//Intent i = new Intent(mContext, SplashScreenActivity.class);
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

	public boolean isHidden(){
		return this.isHidden;
	}

	public void isHidden(boolean isHidden){
		this.isHidden = isHidden;
	}

	/**
	 * Hide HolcimDialogHandler to show pin modal
	 * @return void
	 */
	public void hideDialog() {
		this.hide();
		this.isHidden = true;
	}

	/**
	 * Show HolcimDialogHandler after show pin modal admitted
	 * @return void
	 */
	public void showDialog() {
		this.show();
		this.isHidden = false;
	}

	/**
	 * Show the progress screen
	 */
	public void showProgress()
	{
		this.setTheme(PROGRESS_DIALOG, "", "", "", null, null, 0, 0, null);
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
		this.setTheme(ERROR_DIALOG, pMessage, pButtonText, "", pButtonClickListener, null, 0, 0, null);
		this.setCancelable(false);
		this.show();
	}
	
	/**
	 * Show notification message with one button
	 * @param pMessage
	 * @param pButtonText
	 * @param pButtonClickListener
	 */
	public void showNotification(String pMessage, String pButtonText, android.view.View.OnClickListener pButtonClickListener)
	{
		this.setTheme(NOTIFICATION_DIALOG, pMessage, pButtonText, "", pButtonClickListener, null, 0, 0, null);
		this.setCancelable(false);
		this.show();
	}
	
	/**
	 * Show warning message with two button
	 * @param pMessage
	 * @param pButtonText
	 * @param pButtonClickListener
	 */
	public void showWarning(String pMessage, String pButtonTextOk, String pButtonCancelText, android.view.View.OnClickListener pButtonOKOnclickListener, android.view.View.OnClickListener pButtonCancelOnclickListener)
	{
		this.setTheme(WARNING_DIALOG, pMessage, pButtonTextOk, pButtonCancelText, pButtonOKOnclickListener, pButtonCancelOnclickListener, 0, 0, null);
		this.setCancelable(false);
		this.show();
	}

	/**
	 * Show warning message with two button
	 * @param pMessage
	 * @param pButtonText
	 * @param pButtonClickListener
	 */
	public void showDatePickerModal(String date, String pButtonTextOk, String pButtonCancelText, android.view.View.OnClickListener pButtonOKOnclickListener, android.view.View.OnClickListener pButtonCancelOnclickListener)
	{
		this.setTheme(DATEPICKER_DIALOG, date, pButtonTextOk, pButtonCancelText, pButtonOKOnclickListener, pButtonCancelOnclickListener, 0, 0, null);
		this.setCancelable(false);
		this.show();
	}	
	
	/**
	 * Set the layout based in parameters
	 */
	private void setTheme(int pType, String pMessage, String pButtonOKText, 
			String pButtonCancelText, android.view.View.OnClickListener pButtonOKOnclickListener, 
			android.view.View.OnClickListener pButtonCancelOnclickListener, int pMonth, int pYear, String titleName){
		
		LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.dialog, null);

		Button btnOkSmall     = (Button)view.findViewById(R.id.confirmButton1);
		Button btnOk          = (Button)view.findViewById(R.id.btnOk);
		Button btnCancelSmall = (Button)view.findViewById(R.id.cancelButton1);

		LinearLayout contentLayout     = (LinearLayout)view.findViewById(R.id.content_layout);
		LinearLayout progressBarLayout = (LinearLayout)view.findViewById(R.id.progress);
		LinearLayout twoButtonsLayout  = (LinearLayout)view.findViewById(R.id.two_buttons_layout);
		RelativeLayout oneButtonLayout = (RelativeLayout)view.findViewById(R.id.one_button_layout);

		DatePicker datePickerLayout  = (DatePicker)view.findViewById(R.id.datePicker_birthdate_modal);		
		
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
			title.setText(titleName);
			message.setText(pMessage);
			btnOkSmall.setText(pButtonOKText);
			btnCancelSmall.setText(pButtonCancelText);
			if (pButtonOKOnclickListener != null)
				btnOkSmall.setOnClickListener(pButtonOKOnclickListener);
			if (pButtonCancelOnclickListener != null)
				btnCancelSmall.setOnClickListener(pButtonCancelOnclickListener);
			twoButtonsLayout.setVisibility(View.VISIBLE);
			break;
		}case NOTIFICATION_DIALOG:{
			title.setText(mContext.getString(R.string.notification));
			message.setText(pMessage);
			btnOk.setText(pButtonOKText);
			if (pButtonOKOnclickListener != null)
				btnOk.setOnClickListener(pButtonOKOnclickListener);
			oneButtonLayout.setVisibility(View.VISIBLE);
			break;
		}case WARNING_DIALOG:{
			title.setText(mContext.getString(R.string.warning));
			message.setText(pMessage);
			btnOkSmall.setText(pButtonOKText);
			btnCancelSmall.setText(pButtonCancelText);
			if (pButtonOKOnclickListener != null)
				btnOkSmall.setOnClickListener(pButtonOKOnclickListener);
			if (pButtonCancelOnclickListener != null)
				btnCancelSmall.setOnClickListener(pButtonCancelOnclickListener);
			twoButtonsLayout.setVisibility(View.VISIBLE);

			break;
		}case DATEPICKER_DIALOG:{
			title.setText(mContext.getString(R.string.pick_date));
			datePickerLayout.setVisibility(View.VISIBLE);
			twoButtonsLayout.setVisibility(View.VISIBLE);
			message.setVisibility(View.GONE);
			btnOkSmall.setText(pButtonOKText);
			btnCancelSmall.setText(pButtonCancelText);
			if(pMessage != null){
				String[] separated = pMessage.split("-");
				datePickerLayout.updateDate(Integer.parseInt(separated[0]), Integer.parseInt(separated[1]) - 1, Integer.parseInt(separated[2]));	
			}
			if (pButtonOKOnclickListener != null)
				btnOkSmall.setOnClickListener(pButtonOKOnclickListener);
			if (pButtonCancelOnclickListener != null)
				btnCancelSmall.setOnClickListener(pButtonCancelOnclickListener);

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

	// No internet connection with custom on click function
	public void showNoInternetRefreshDialog(android.view.View.OnClickListener pButtonOnClickListener) {
		this.setTheme(INFO_DIALOG, mContext.getString(R.string.message_no_internet_connection), mContext.getString(R.string.message_button_ok), "", pButtonOnClickListener, null, 0, 0, null);
		this.show();
	}

	public void hideNoInternetRefreshDialog() {
		this.hide();
	}

	/**
	 * Show error message with one button with text "OK" and close dialog action
	 * @param pMessage
	 * @param pButtonText
	 * @param pButtonClickListener
	 */
	public void showQuestionMessage(String pMessage, View.OnClickListener okButtonOnClickListener, String title)
	{
		this.setTheme(SHOW_QUESTION_MESSAGE, pMessage, mContext.getString(R.string.message_title_confirm),
				mContext.getString(R.string.cancel) ,okButtonOnClickListener, mDismissClickListener, 0, 0, title);
		this.setCancelable(false);
		this.show();
	}

}
