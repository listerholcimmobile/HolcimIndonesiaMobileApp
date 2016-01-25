package com.holcim.altimetrik.android.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.TypedArray;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.holcim.altimetrik.android.activities.HolcimMyVisitPlanListActivity;
import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.altimetrik.holcim.fragments.FragmentAccountInfo;
import com.altimetrik.holcim.fragments.FragmentAccountInfo.FragmentAccountInfoActions;
import com.altimetrik.holcim.fragments.FragmentContactInfo;
import com.altimetrik.holcim.fragments.FragmentContactInfo.FragmentContactInfoActions;
import com.altimetrik.holcim.fragments.FragmentFeedback;
import com.altimetrik.holcim.fragments.FragmentFeedback.FragmentFeedbackActions;
import com.altimetrik.holcim.fragments.FragmentLastCompetitorMarketInfo;
import com.altimetrik.holcim.fragments.FragmentLastCompetitorMarketInfo.FragmentLastCompetitorMarketInfoActions;
import com.altimetrik.holcim.fragments.FragmentOutstandingFeedback;
import com.altimetrik.holcim.fragments.FragmentOutstandingFeedback.FragmentOutstandingFeedbackActions;
import com.altimetrik.holcim.fragments.FragmentOutstandingFeedbackDetail;
import com.altimetrik.holcim.fragments.FragmentOutstandingFeedbackDetail.FragmentOutstandingFeedbackDetailActions;
import com.altimetrik.holcim.fragments.FragmentPreorder;
import com.altimetrik.holcim.fragments.FragmentPreorder.FragmentPreorderActions;
import com.altimetrik.holcim.fragments.FragmentPreviousSales;
import com.altimetrik.holcim.fragments.FragmentPreviousSales.FragmentPreviousSalesActions;
import com.altimetrik.holcim.fragments.FragmentSalesExec;
import com.altimetrik.holcim.fragments.FragmentSalesExec.FragmentSalesExecActions;
import com.altimetrik.holcim.fragments.FragmentShopSign;
import com.altimetrik.holcim.fragments.FragmentShopSign.FragmentShopSignActions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
//import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.holcim.hsea.R;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.ActionsLog;
import com.holcim.altimetrik.android.model.CompetitorMarketing;
import com.holcim.altimetrik.android.model.OutstandingFeedback;
import com.holcim.altimetrik.android.model.PreOrder;
import com.holcim.altimetrik.android.model.SaleExecution;
import com.holcim.altimetrik.android.utilities.AltimetrikException;
import com.holcim.altimetrik.android.utilities.AltimetrikFileHandler;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.HolcimUtility;
import com.holcim.altimetrik.android.utilities.NavDrawerItem;
import com.holcim.altimetrik.android.utilities.NavDrawerListAdapter;

public class HolcimSelectedRetailerActivity extends HolcimCustomActivity
        implements FragmentAccountInfoActions, FragmentContactInfoActions,
        FragmentPreviousSalesActions, FragmentShopSignActions,
        FragmentSalesExecActions, FragmentFeedbackActions,
        FragmentPreorderActions, FragmentLastCompetitorMarketInfoActions,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        FragmentOutstandingFeedbackDetailActions,
        FragmentOutstandingFeedbackActions {

    private String date;
    private boolean canEditByDate;
    SaleExecution saleExecution;
    ArrayList<CompetitorMarketing> comeptitorsMarketings;
    ArrayList<CompetitorMarketing> comeptitorsMarketingsToRemoveChanges;
    ArrayList<ActionsLog> actionLogs;
    ArrayList<PreOrder> preOrders;
    ArrayList<PreOrder> preOrdersToRemoveChanges;
    ArrayList<ActionsLog> actionLogsToDelete;
    ArrayList<ActionsLog> actionLogsToRemoveChanges;
    FragmentAccountInfo fragmentAccountInfo;
    FragmentContactInfo fragmentContactInfo;
    FragmentShopSign fragmentShopSign;
    private boolean isShowReasonForNotOrderingModalVisible;
    private boolean isAccount;

    // Slide Menu------------------------------------

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    // // nav drawer title
    // private CharSequence mDrawerTitle;
    //
    // // used to store app title
    // private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    LocationManager lm;
    String provider;
    Location l;

    // -----------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();
        //mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        //mLocationClient.disconnect();
        super.onStop();
    }

    public boolean isAccount() {
        return isAccount;
    }

    public void setAccount(boolean isAccount) {
        this.isAccount = isAccount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestCustomTitle();
        setContentView(R.layout.selected_retailer);
        super.onCreate(savedInstanceState);

        // setCustomTitle(getResources().getString(R.string.selected_retailer_title));

		/*
         * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
        //mLocationClient = new LocationClient(this, this, this);
        //mGoogleApiClient = new GoogleApiClient.Builder(this)
        //        .addConnectionCallbacks(this)
        //        .addOnConnectionFailedListener(this)
        //        .addApi(LocationServices.API)
        //        .build();
		/*
		 * Slide Menu
		 */

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        if (mDrawerList != null) {
            navDrawerItems = new ArrayList<NavDrawerItem>();

            // adding nav drawer items to array
            // Account
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
                    .getResourceId(0, -1)));
            // Contact
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
                    .getResourceId(1, -1)));
            // HIL Last Market Info
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
                    .getResourceId(2, -1)));
            // Last Competitor Market Info
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
                    .getResourceId(3, -1)));
            // Shop Sign
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
                    .getResourceId(4, -1)));
            // Market Info
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
                    .getResourceId(5, -1)));
            // Feedback
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons
                    .getResourceId(6, -1)));
            // Preorder
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons
                    .getResourceId(7, -1)));

            // Recycle the typed array
            navMenuIcons.recycle();

            mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
            // setting the nav drawer list adapter
            adapter = new NavDrawerListAdapter(getApplicationContext(),
                    navDrawerItems);
            mDrawerList.setAdapter(adapter);
        }
		/*
		 * End - Slide Menu
		 */

        if (getIntent().getExtras() != null) {
            date = getIntent().getExtras().getString("date");
            actionLogsToDelete = new ArrayList<ActionsLog>();
            actionLogsToRemoveChanges = new ArrayList<ActionsLog>();
            preOrdersToRemoveChanges = new ArrayList<PreOrder>();
            comeptitorsMarketingsToRemoveChanges = new ArrayList<CompetitorMarketing>();

            if (date != null) {
                saleExecution = HolcimApp.daoSession.getSaleExecutionDao()
                        .load(getIntent().getExtras().getLong(
                                "salesExcecutionId"));
                if (saleExecution != null) {
                    comeptitorsMarketings = (ArrayList<CompetitorMarketing>) saleExecution
                            .getCompetitorMarketings();
                    actionLogs = (ArrayList<ActionsLog>) saleExecution
                            .getActionLogs();
                    preOrders = (ArrayList<PreOrder>) saleExecution
                            .getPreOrders();
                    preOrdersToRemoveChanges.addAll(preOrders);
                    actionLogsToRemoveChanges.addAll(actionLogs);
                    comeptitorsMarketingsToRemoveChanges
                            .addAll(comeptitorsMarketings);

                    if (saleExecution.getAccount() != null) {
                        setCustomTitle(saleExecution.getAccountName());
                        isAccount = true;
                    } else if (saleExecution.getProspect() != null
                            && saleExecution.getProspect().getName() != null) {
                        setCustomTitle(saleExecution.getProspect().getName());
                        isAccount = false;
                    } else {
                        setCustomTitle(getString(R.string.selected_retailer_title));
                    }
                    goBackFromContactInfo(saleExecution, comeptitorsMarketings,
                            actionLogs, preOrders);
                }

                Calendar c = Calendar.getInstance();
                DateFormat formatToCompare = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = formatToCompare.format(c.getTime());
                if (date.equals(formattedDate)) {
                    canEditByDate = true;
                } else {
                    canEditByDate = false;
                }
            } else {
                canEditByDate = true;
                saleExecution = new SaleExecution();
                saleExecution.setUnplannedVisitReason(getIntent().getExtras()
                        .getString("reason"));
                if (getIntent().getExtras().getBoolean("isAccount") == true) {
                    isAccount = true;
                    saleExecution.setAccount(HolcimApp.daoSession
                            .getAccountDao().load(
                                    getIntent().getExtras()
                                            .getLong("accountId")));
                    saleExecution.setAccountName(saleExecution.getAccount()
                            .getName());
                    setCustomTitle(saleExecution.getAccountName());
                } else {
                    saleExecution.setProspect(HolcimApp.daoSession
                            .getProspectDao().load(
                                    getIntent().getExtras().getLong(
                                            "prospectId")));
                    setCustomTitle(saleExecution.getProspect().getName());
                    isAccount = false;
                }
                setShowBack(false);
                comeptitorsMarketings = new ArrayList<CompetitorMarketing>();
                actionLogs = new ArrayList<ActionsLog>();
                preOrders = new ArrayList<PreOrder>();
                preOrdersToRemoveChanges.addAll(preOrders);
                actionLogsToRemoveChanges.addAll(actionLogs);
                comeptitorsMarketingsToRemoveChanges
                        .addAll(comeptitorsMarketings);
                goToSaleExecution(saleExecution, comeptitorsMarketings, actionLogs,
                        preOrders);
                //goToShopSign(saleExecution, comeptitorsMarketings, actionLogs,
                //preOrders);
            }
        }
    }

    public boolean isCanEditByDate() {
        return canEditByDate;
    }

    public void setCanEditByDate(boolean canEditByDate) {
        this.canEditByDate = canEditByDate;
    }

    @Override
    public void resetSaleExecution() {
        HolcimApp.daoSession.clear();
    }

    String reasonForNot = null;

    public void showReasonForNotOrderingModal() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View modalView = layoutInflater.inflate(
                R.layout.modal_reason_for_not_ordering, null);

        final PopupWindow popUpReject = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, false);
        popUpReject.setOutsideTouchable(false);

        // 20141217 -- Add reason for not ordering
        // Dev -- Piyawat Natpisarnwanit

        Spinner spnReasonforNot = (Spinner) modalView
                .findViewById(R.id.spinner_reason_for_not_ordering);
        final List<String> reasonforNotList = Arrays.asList(getResources()
                .getStringArray(R.array.reason_for_not_ordering_values));
        ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner, reasonforNotList);
        spnReasonforNot.setAdapter(reasonAdapter);
        // ------

        final EditText edtReason = (EditText) modalView
                .findViewById(R.id.edt_reason_for_not_ordering);
        Button btnOk = (Button) modalView.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) modalView.findViewById(R.id.btn_cancel);

        final TextView tvError = (TextView) modalView
                .findViewById(R.id.tv_reason_for_not_ordering_error);

        isShowReasonForNotOrderingModalVisible = true;

        // 20141217 -- Add reason for not ordering
        // Dev -- Piyawat Natpisarnwanit
        spnReasonforNot.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                reasonForNot = reasonforNotList.get(position);

                if (reasonForNot.equals("Other (please mention)")) {
                    edtReason.setText("");
                    edtReason.setVisibility(View.VISIBLE);
                } else {
                    edtReason.setText(reasonForNot);
                    edtReason.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(edtReason.getText()) == null
                        || String.valueOf(edtReason.getText()).equals("")) {
                    tvError.setVisibility(View.VISIBLE);
                } else {
                    PreOrder p = new PreOrder();
                    p.setReasonForNotOrdering(String.valueOf(edtReason
                            .getText()));
                    preOrders.add(p);
                    isShowReasonForNotOrderingModalVisible = false;
                    finishFlow(saleExecution, comeptitorsMarketings,
                            actionLogs, preOrders);
                }
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isShowReasonForNotOrderingModalVisible = false;
                modalView.setVisibility(View.GONE);
            }
        });

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        this.addContentView(modalView, layoutParams);
    }

    // SLIDE MENU
    // ----------------------------------------------------------------

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            switch (position) {
                case 0: {
                    if (date != null) {
                        saveChanges();
                        goBackFromContactInfo(saleExecution, comeptitorsMarketings,
                                actionLogs, preOrders);
                    } else {
                        dialog.showError(
                                getString(R.string.unplanned_visit_error_account),
                                getString(R.string.ok), new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                    break;
                }
                case 1: {
                    if (date != null) {
                        if (saleExecution.getAccount() != null) {

                            if (saveChanges())
                                goToContactInfo(saleExecution,
                                        comeptitorsMarketings, actionLogs,
                                        preOrders);
                            else
                                mDrawerLayout.closeDrawer(mDrawerList);
                        } else {
                            mDrawerLayout.closeDrawer(mDrawerList);
                            dialog.showError(
                                    getString(R.string.contact_info_for_prospect_error),
                                    getString(R.string.ok), new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    } else {
                        mDrawerLayout.closeDrawer(mDrawerList);
                        dialog.showError(
                                getString(R.string.unplanned_visit_error_contact),
                                getString(R.string.ok), new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                    break;
                }
                case 2: {
                    if (date != null) {
                        if (saveChanges())
                            goToPreviousSale(saleExecution, comeptitorsMarketings,
                                    actionLogs, preOrders);
                        else
                            mDrawerLayout.closeDrawer(mDrawerList);
                    } else {
                        mDrawerLayout.closeDrawer(mDrawerList);
                        dialog.showError(
                                getString(R.string.unplanned_visit_error_market),
                                getString(R.string.ok), new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                    break;
                }
                case 3: {
                    if (date != null) {
                        if (saveChanges())
                            goToLastCompetitorMarketinfo(saleExecution,
                                    comeptitorsMarketings, actionLogs, preOrders);
                        else
                            mDrawerLayout.closeDrawer(mDrawerList);
                    } else {
                        mDrawerLayout.closeDrawer(mDrawerList);
                        dialog.showError(
                                getString(R.string.unplanned_visit_error_competitor),
                                getString(R.string.ok), new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                    break;
                }
                case 4: {
                    if (saveChanges()) {
                        goToShopSign(saleExecution, comeptitorsMarketings,
                                actionLogs, preOrders);
                    } else
                        mDrawerLayout.closeDrawer(mDrawerList);
                    break;
                }
                case 5: {
                    if (saveChanges()) {
                        goToSaleExecution(saleExecution, comeptitorsMarketings,
                                actionLogs, preOrders);
                    } else
                        mDrawerLayout.closeDrawer(mDrawerList);
                    break;
                }
                case 6: {
                    if (saveChanges()) {
                        goToFeedback(saleExecution, comeptitorsMarketings,
                                actionLogs, preOrders);
                    } else
                        mDrawerLayout.closeDrawer(mDrawerList);
                    break;
                }
                case 7: {
                    if (HolcimApp.getInstance().getProfile() != null
                            && HolcimApp.getInstance().getProfile()
                            .equalsIgnoreCase(HolcimConsts.TSO)) {
                        mDrawerLayout.closeDrawer(mDrawerList);
                        dialog.showError(getString(R.string.TSO_preorder_error),
                                getString(R.string.ok), new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                    } else if (isAccount) {
                        if (saveChanges()) {
                            goToPreOrder(saleExecution, comeptitorsMarketings,
                                    actionLogs, preOrders);
                        } else
                            mDrawerLayout.closeDrawer(mDrawerList);
                    } else {
                        mDrawerLayout.closeDrawer(mDrawerList);
                        dialog.showError(
                                getString(R.string.unplanned_visit_error_preorder),
                                getString(R.string.ok), new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                    break;
                }
            }
        }
    }

    private Boolean saveChanges() {
        boolean ret = true;
        if (canEditByDate) {
            if (fragmentContactInfo != null) {
                fragmentContactInfo.saveChanges();
            }
            if (fragmentShopSign != null) {
                fragmentShopSign.saveChanges();
            }
            if (fragmentAccountInfo != null) {
                if (saleExecution.getAccount() != null) {
                    ret = fragmentAccountInfo.setFieldsAccount();
                } else {
                    ret = fragmentAccountInfo.setFieldsProspect();
                }
            }
        }
        return ret;
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     */
    public void displayView(int position) {
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    // END SLIDE MENU
    // ------------------------------------------------------------

    private boolean isAccountFragmentVisible() {
        return getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_ACCOUNT_TAG) != null
                && getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_ACCOUNT_TAG).isVisible();
    }

    private boolean isContactFragmentVisible() {
        return getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_CONTACT_TAG) != null
                && getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_CONTACT_TAG).isVisible();
    }

    private boolean isFeddbackFragmentVisible() {
        return getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_FEEDBACK_TAG) != null
                && getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_FEEDBACK_TAG).isVisible();
    }

    private boolean isPreorderFragmentVisible() {
        return getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_PREORDER_TAG) != null
                && getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_PREORDER_TAG).isVisible();
    }

    private boolean isPreviousSaleFragmentVisible() {
        return getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_PREVIOUS_SALE_TAG) != null
                && getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_PREVIOUS_SALE_TAG).isVisible();
    }

    private boolean isSaleExecutionFragmentVisible() {
        return getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_SALE_EXECUTION_TAG) != null
                && getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_SALE_EXECUTION_TAG).isVisible();
    }

    private boolean isLastCompetitorMarketInfoFragmentVisible() {
        return getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_LAST_COMPETITOR_MARKET_INFO_TAG) != null
                && getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_LAST_COMPETITOR_MARKET_INFO_TAG)
                .isVisible();
    }

    private boolean isShopSignFragmentVisible() {
        return getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_SHOP_SIGN_TAG) != null
                && getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_SHOP_SIGN_TAG).isVisible();
    }

    private boolean isOutstandingFeedbackListFragmentVisible() {
        return getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_OUTSTANDING_FEEDBACK_LIST) != null
                && getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_OUTSTANDING_FEEDBACK_LIST)
                .isVisible();
    }

    private boolean isOutstandingFeedbackDetailFragmentVisible() {
        return getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_OUTSTANDING_FEEDBACK_DETAIL) != null
                && getFragmentManager().findFragmentByTag(
                HolcimConsts.FRAGMENT_OUTSTANDING_FEEDBACK_DETAIL)
                .isVisible();
    }

    private void resetSaleExecutionFields() {
        try {
            if (saleExecution.isLandmarkPictureTempFileExist(this)) {
                AltimetrikFileHandler.DeleteFile(saleExecution
                        .getTempLandmarkPictureFilePath(this));
            }
            HolcimApp.daoSession.getSaleExecutionDao().refresh(saleExecution);
        } catch (HolcimException e) {
            e.printStackTrace();
        } catch (AltimetrikException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (!HolcimCustomActivity.blockback) {
            if (!isShowReasonForNotOrderingModalVisible) {
                HolcimCustomActivity.setOnback(true);
                if (isAccountFragmentVisible()) {
                    resetSaleExecutionFields();
                    goBackFromAccountInfo();
                } else if (isContactFragmentVisible()) {
                    goBackFromContactInfo(saleExecution, comeptitorsMarketings,
                            actionLogs, preOrders);
                } else if (isPreviousSaleFragmentVisible()) {
                    goBackFromPreviousSales(saleExecution,
                            comeptitorsMarketings, actionLogs, preOrders);
                } else if (isLastCompetitorMarketInfoFragmentVisible()) {
                    goBackFromLastCompetitorMarketInfo(saleExecution,
                            comeptitorsMarketings, actionLogs, preOrders);
                } else if (isShopSignFragmentVisible() && getShowBack()) {
                    goBackFromShopSign(saleExecution, comeptitorsMarketings,
                            actionLogs, preOrders);
                } else if (isShopSignFragmentVisible() && !getShowBack()) {
                    try {
                        AltimetrikFileHandler.DeleteFile(saleExecution
                                .getTempSSPictureFilePath(this));
                        Intent intent = new Intent(
                                HolcimSelectedRetailerActivity.this,
                                HolcimMainActivity.class);
                        startActivity(intent);
                    } catch (AltimetrikException e) {
                        e.printStackTrace();
                    } catch (HolcimException e) {
                        e.printStackTrace();
                    }
                } else if (isSaleExecutionFragmentVisible()) {
                    goBackFromSalesExec(saleExecution, comeptitorsMarketings,
                            actionLogs, preOrders);
                } else if (isFeddbackFragmentVisible()) {
                    goBackFromFeedback(saleExecution, comeptitorsMarketings,
                            actionLogs, preOrders);
                } else if (isPreorderFragmentVisible()) {
                    goBackFromPreorder(saleExecution, comeptitorsMarketings,
                            actionLogs, preOrders);
                } else if (isOutstandingFeedbackListFragmentVisible()) {
                    goBackFromOutstandingFeedback(saleExecution,
                            comeptitorsMarketings, actionLogs, preOrders);
                } else if (isOutstandingFeedbackDetailFragmentVisible()) {
                    goBackFromFragmentOutstandingFeedbackDetail(saleExecution,
                            comeptitorsMarketings, actionLogs, preOrders);
                }
            }
        }
    }

    @Override
    public void goBackFromAccountInfo() {
        Intent nextAct = new Intent(HolcimSelectedRetailerActivity.this,
                HolcimVisitDetailActivity.class);
        nextAct.putExtra("date", date);
        startActivity(nextAct);
    }

    @Override
    public void goToContactInfo(SaleExecution saleExecution,
                                ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        if (saleExecution.getAccount() != null) {
            fragmentContactInfo = new FragmentContactInfo(saleExecution,
                    comeptitorsMarketings, actionLogs, preOrders);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, fragmentContactInfo,
                            HolcimConsts.FRAGMENT_CONTACT_TAG).commit();
            displayView(1);
        } else {
            goToPreviousSale(saleExecution, comeptitorsMarketings, actionLogs,
                    preOrders);
        }
    }

    @Override
    public void goBackFromContactInfo(SaleExecution saleExecution,
                                      ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                      ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        fragmentAccountInfo = new FragmentAccountInfo(saleExecution,
                comeptitorsMarketings, actionLogs, preOrders);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragmentAccountInfo,
                        HolcimConsts.FRAGMENT_ACCOUNT_TAG).commit();
        displayView(0);
    }

    @Override
    public void goToPreviousSale(SaleExecution saleExecution,
                                 ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                 ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        Fragment fragment = new FragmentPreviousSales(saleExecution,
                comeptitorsMarketings, actionLogs, preOrders);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment,
                        HolcimConsts.FRAGMENT_PREVIOUS_SALE_TAG).commit();
        displayView(2);

    }

    @Override
    public void goBackFromPreviousSales(SaleExecution saleExecution,
                                        ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                        ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        if (saleExecution.getAccount() != null) {
            goToContactInfo(saleExecution, comeptitorsMarketings, actionLogs,
                    preOrders);
        } else {
            goBackFromContactInfo(saleExecution, comeptitorsMarketings,
                    actionLogs, preOrders);
        }
    }

    @Override
    public void goBackFromLastCompetitorMarketInfo(SaleExecution saleExecution,
                                                   ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                                   ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        goToPreviousSale(saleExecution, comeptitorsMarketings, actionLogs,
                preOrders);
    }

    @Override
    public void goToShopSign(SaleExecution saleExecution,
                             ArrayList<CompetitorMarketing> comeptitorsMarketings,
                             ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        fragmentShopSign = new FragmentShopSign(saleExecution,
                comeptitorsMarketings, actionLogs, preOrders);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragmentShopSign,
                        HolcimConsts.FRAGMENT_SHOP_SIGN_TAG).commit();
        displayView(4);
    }

    @Override
    public void goToLastCompetitorMarketinfo(SaleExecution saleExecution,
                                             ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                             ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        Fragment fragment = new FragmentLastCompetitorMarketInfo(saleExecution,
                comeptitorsMarketings, actionLogs, preOrders);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment,
                        HolcimConsts.FRAGMENT_LAST_COMPETITOR_MARKET_INFO_TAG)
                .commit();
        displayView(3);
    }

    @Override
    public void goBackFromShopSign(SaleExecution saleExecution,
                                   ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                   ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        goToLastCompetitorMarketinfo(saleExecution, comeptitorsMarketings,
                actionLogs, preOrders);
    }

    @Override
    public void goToSaleExecution(SaleExecution saleExecution,
                                  ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                  ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        Fragment fragment = new FragmentSalesExec(saleExecution,
                comeptitorsMarketings, actionLogs, preOrders);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment,
                        HolcimConsts.FRAGMENT_SALE_EXECUTION_TAG).commit();
        displayView(5);
    }

    @Override
    public void goBackFromSalesExec(SaleExecution saleExecution,
                                    ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                    ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        if (saleExecution.getUnplannedVisitReason() != null) {
            if (saleExecution.getId() != null) {
                goToLastCompetitorMarketinfo(saleExecution,
                        comeptitorsMarketings, actionLogs, preOrders);
            } else {
                Intent intent = new Intent(
                        HolcimSelectedRetailerActivity.this,
                        HolcimUnplannedVisitActivity.class);
                intent.putExtra("reason", saleExecution.getUnplannedVisitReason());
                startActivity(intent);
            }
        } else {
            goToShopSign(saleExecution, comeptitorsMarketings, actionLogs,
                    preOrders);
        }
    }

    @Override
    public void goToFeedback(SaleExecution saleExecution,
                             ArrayList<CompetitorMarketing> comeptitorsMarketings,
                             ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        Fragment fragment = new FragmentFeedback(saleExecution,
                comeptitorsMarketings, actionLogs, preOrders);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment,
                        HolcimConsts.FRAGMENT_FEEDBACK_TAG).commit();
        displayView(6);

    }

    @Override
    public void callCreateEditCompetitorMarketing(
            CompetitorMarketing competitorMarketing,
            ArrayList<CompetitorMarketing> competitorMarketings, int position) {
        Intent mIntent;
        if (canEditByDate) {
            mIntent = new Intent(this, HolcimCreateCompetitorMarketing.class);
        } else {
            mIntent = new Intent(this, HolcimMarketInfoActivity.class);
        }
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(HolcimConsts.OBJECT_SERIALIZABLE_KEY,
                competitorMarketing);
        mBundle.putSerializable(HolcimConsts.OBJECT_LIST_SERIALIZABLE_KEY,
                competitorMarketings);
        mIntent.putExtra("accountId", this.saleExecution.getAccount().getId());
        mIntent.putExtra("saleExecutionId", this.saleExecution.getId());
        mIntent.putExtra("isEditing", position);
        mIntent.putExtras(mBundle);
        startActivityForResult(mIntent,
                HolcimConsts.ACTIVITY_REQUEST_CODE_COMP_MARK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == HolcimConsts.ACTIVITY_REQUEST_CODE_COMP_MARK) {
            if (resultCode == HolcimConsts.ACTIVITY_RESULT_CODE_COMP_MARK_OK) {
                CompetitorMarketing compMark = (CompetitorMarketing) data
                        .getSerializableExtra(HolcimConsts.OBJECT_SERIALIZABLE_KEY);
                int position = data.getExtras().getInt("isEditing");
                if (compMark != null) {
                    if (position >= 0) {
                        this.comeptitorsMarketings.remove(position);
                        this.comeptitorsMarketings.add(compMark);
                    } else {
                        this.comeptitorsMarketings.add(compMark);
                    }
                }
                refreshCurrentFragment();
            }
        } else if (requestCode == HolcimConsts.ACTIVITY_REQUEST_CODE_ACTION_LOG) {
            if (resultCode == HolcimConsts.ACTIVITY_RESULT_CODE_ACTION_LOG_OK) {
                ActionsLog actLog = (ActionsLog) data
                        .getSerializableExtra(HolcimConsts.OBJECT_SERIALIZABLE_KEY);
                int position = data.getExtras().getInt("isEditing");
                if (actLog != null) {
                    if (position >= 0) {
                        this.actionLogs.remove(position);
                        this.actionLogs.add(actLog);
                    } else {
                        this.actionLogs.add(actLog);
                    }
                    if (actLog.getSalesforceId() == null
                            || actLog.getSalesforceId().equals("")) {
                        actionLogsToDelete.add(actLog);
                    }
                }
                refreshCurrentFragment();
            }
        } else if (requestCode == HolcimConsts.ACTIVITY_REQUEST_PRE_ORDER_LOG) {
            if (resultCode == HolcimConsts.ACTIVITY_RESULT_CODE_PRE_ORDER_OK) {
                PreOrder preOrder = (PreOrder) data
                        .getSerializableExtra(HolcimConsts.OBJECT_SERIALIZABLE_KEY);
                int position = data.getExtras().getInt("isEditing");
                if (preOrder != null) {
                    if (position >= 0) {
                        this.preOrders.remove(position - 1);
                        this.preOrders.add(preOrder);
                    } else {
                        this.preOrders.add(preOrder);
                    }
                }
            }
            refreshCurrentFragment();
        } else if (requestCode == HolcimConsts.ACTIVITY_REQUEST_CAMERA_LOG) {
            if (resultCode == HolcimConsts.ACTIVITY_RESULT_CODE_CAMERA_OK) {
                refreshCurrentFragment();
            }
        }

        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
				/*
				 * Try the request again
				 */
                        break;
                }
        }

    }

    private void refreshCurrentFragment() {
        if (isFeddbackFragmentVisible()) {
            FragmentFeedback fragFeedback = (FragmentFeedback) getFragmentManager()
                    .findFragmentByTag(HolcimConsts.FRAGMENT_FEEDBACK_TAG);
            if (fragFeedback != null) {
                fragFeedback.refreshAdapter(this.actionLogs);
            }
        } else if (isSaleExecutionFragmentVisible()) {
            FragmentSalesExec fragSaleExec = (FragmentSalesExec) getFragmentManager()
                    .findFragmentByTag(HolcimConsts.FRAGMENT_SALE_EXECUTION_TAG);
            if (fragSaleExec != null) {
                fragSaleExec.refreshAdapter(this.comeptitorsMarketings);
            }
        } else if (isPreorderFragmentVisible()) {
            FragmentPreorder fragPreOrder = (FragmentPreorder) getFragmentManager()
                    .findFragmentByTag(HolcimConsts.FRAGMENT_PREORDER_TAG);
            if (fragPreOrder != null) {
                ArrayList<PreOrder> pList = new ArrayList<PreOrder>();
                if (preOrders.size() > 1) {
                    for (PreOrder p : preOrders) {
                        if (p.getReasonForNotOrdering() == null) {
                            pList.add(p);
                        }
                    }
                    fragPreOrder.refreshAdapter(pList);
                } else {
                    fragPreOrder.refreshAdapter(preOrders);
                }
            }
        } else if (isShopSignFragmentVisible()) {
            FragmentShopSign fragShopSign = (FragmentShopSign) getFragmentManager()
                    .findFragmentByTag(HolcimConsts.FRAGMENT_SHOP_SIGN_TAG);
            if (fragShopSign != null) {
                fragShopSign.refreshImage();
            }
        } else if (isAccountFragmentVisible()) {
            FragmentAccountInfo fragAccountInfo = (FragmentAccountInfo) getFragmentManager()
                    .findFragmentByTag(HolcimConsts.FRAGMENT_ACCOUNT_TAG);
            if (fragAccountInfo != null) {
                fragAccountInfo.refreshImage();
            }
        } else if (isContactFragmentVisible()) {
            FragmentContactInfo fragContact = (FragmentContactInfo) getFragmentManager()
                    .findFragmentByTag(HolcimConsts.FRAGMENT_CONTACT_TAG);
            if (fragContact != null) {
                fragContact.refreshImage();
            }
        }
    }

    @Override
    /**
     * Delete all actionLog objects related to the salesExecution selected
     * when the user does not finish the flow
     * If the actionLog has salesforce id the changes are undone
     */
    public void deleteActionLogs() {
        if (actionLogsToDelete != null && actionLogsToDelete.size() != 0) {
            for (ActionsLog actionLog : actionLogsToDelete) {
                HolcimApp.daoSession.delete(actionLog);
                actionLogs.remove(actionLogs.indexOf(actionLog));
            }
        }
        if (actionLogsToRemoveChanges != null
                && actionLogsToRemoveChanges.size() != 0) {
            actionLogs.clear();
            actionLogs.addAll(actionLogsToRemoveChanges);
            actionLogsToDelete.clear();
        }
    }

    @Override
    /**
     * Reset Pre-orders
     */
    public void deletePreOrders() {
        if (preOrdersToRemoveChanges != null
                && preOrdersToRemoveChanges.size() != 0) {
            preOrders.clear();
            preOrders.addAll(preOrdersToRemoveChanges);
        } else {
            preOrders.clear();
        }
    }

    @Override
    /**
     * Reset Competitors Marketing
     */
    public void deleteCompetitorMarketing() {
        if (comeptitorsMarketingsToRemoveChanges != null
                && comeptitorsMarketingsToRemoveChanges.size() != 0) {
            comeptitorsMarketings.clear();
            comeptitorsMarketingsToRemoveChanges
                    .addAll(comeptitorsMarketingsToRemoveChanges);
        } else {
            comeptitorsMarketings.clear();
        }
    }

    /**
     * @param: ActionsLog object
     * <p/>
     * Add all actionLog objects related to the salesExecution selected
     * to know what actionLog delete when the user does not finish the
     * flow
     */
    public void addActionLogsToDelete(ActionsLog actionLog) {
        if (actionLogsToDelete != null && actionLog != null) {
            actionLogsToDelete.add(actionLog);
        }
    }

    @Override
    public void goBackFromFeedback(SaleExecution saleExecution,
                                   ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                   ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        goToSaleExecution(saleExecution, comeptitorsMarketings, actionLogs,
                preOrders);
    }

    @Override
    public void goToPreOrder(SaleExecution saleExecution,
                             ArrayList<CompetitorMarketing> comeptitorsMarketings,
                             ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        Fragment fragment = new FragmentPreorder(saleExecution,
                comeptitorsMarketings, actionLogs, preOrders);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment,
                        HolcimConsts.FRAGMENT_PREORDER_TAG).commit();
        displayView(6);
    }

    @Override
    public void callCreateEditActionLog(ActionsLog actionLog, int position) {
        Intent mIntent = new Intent(this, HolcimCreateFeedbackActivity.class);

        if (canEditByDate) {
            mIntent = new Intent(this, HolcimCreateFeedbackActivity.class);
        } else {
            mIntent = new Intent(this, HolcimShowFeedbackActivity.class);
        }
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(HolcimConsts.OBJECT_SERIALIZABLE_KEY, actionLog);
        mIntent.putExtra("saleExecutionId", this.saleExecution.getId());
        mIntent.putExtra("isEditing", position);
        mIntent.putExtras(mBundle);
        startActivityForResult(mIntent,
                HolcimConsts.ACTIVITY_REQUEST_CODE_ACTION_LOG);
    }

    @Override
    public void goBackFromPreorder(SaleExecution saleExecution,
                                   ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                   ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        goToFeedback(saleExecution, comeptitorsMarketings, actionLogs,
                preOrders);
    }

    @Override
    public void finishFlow(SaleExecution saleExecution,
                           ArrayList<CompetitorMarketing> comeptitorsMarketings,
                           ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        try {
            saleExecution.setIsFinished(true);

            //Add if status == cancel set to "" again
            if (saleExecution.getStatus() == HolcimConsts.SALEEXECUTION_STATUS_CANCELED) {
                saleExecution.setStatus(HolcimConsts.SALEEXECUTION_STATUS_PLANNED);
            }
            if (date != null) {
                HolcimDataSource.saveSaleExecution(this, saleExecution,
                        comeptitorsMarketings, actionLogs, preOrders);
                goBackFromAccountInfo();
            } else {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                date = format.format(c.getTime());
                saleExecution.setVisitDate(date);

                HolcimDataSource.saveSaleExecution(this, saleExecution,
                        comeptitorsMarketings, actionLogs, preOrders);
                HolcimDataSource.updateActionLog(actionLogs, saleExecution);

                Intent intent = new Intent(HolcimSelectedRetailerActivity.this,
                        HolcimMyVisitPlanListActivity.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.v("Feedback Exception....", e + "");
        }
    }

    @Override
    public void callCreateEditPreOrder(PreOrder preOrder, int position) {
        Intent mIntent = new Intent(this, HolcimCreatePreorderActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(HolcimConsts.OBJECT_SERIALIZABLE_KEY, preOrder);
        mIntent.putExtra("saleExecutionId", this.saleExecution.getId());
        mIntent.putExtra("tele_sale", false);
        mIntent.putExtra("isEditing", position);
        if (saleExecution.getAccountName() != null) {
            mIntent.putExtra("accountOrProspectName",
                    saleExecution.getAccountName());
        } else if (saleExecution.getProspect().getName() != null) {
            mIntent.putExtra("accountOrProspectName", saleExecution
                    .getProspect().getName());
        }
        mIntent.putExtras(mBundle);
        startActivityForResult(mIntent,
                HolcimConsts.ACTIVITY_REQUEST_PRE_ORDER_LOG);
    }

    // Global constants
	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    //private LocationClient mLocationClient;

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    public LatLng getCurrentLocation() {
        //Location CurrentLocation = mLocationClient.getLastLocation();
        //Location CurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //if (CurrentLocation != null) {
        //	return new LatLng(CurrentLocation.getLatitude(),
        //			CurrentLocation.getLongitude());
        //}
        //CurrentLocation = null;

        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c = new Criteria();
        provider = lm.getBestProvider(c, true);
        if (!provider.equals("passive")) {
            l = lm.getLastKnownLocation(provider);
            if (l == null)
                l = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (l != null) {
                return new LatLng(l.getLatitude(), l.getLongitude());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
            // showErrorDialog(connectionResult.getErrorCode());
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        // Display the connection status
        // Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnected() {
        // Display the connection status
        // Toast.makeText(this, "Disconnected. Please re-connect.",
        // Toast.LENGTH_SHORT).show();

    }

    @Override
    public void getLocation() {
        LatLng location = getCurrentLocation();
        if (location == null) {
            Toast.makeText(getApplication(),
                    "Unable to capture current location.", Toast.LENGTH_SHORT)
                    .show();
        } else {
            if (location != null) {
                saleExecution.setLatitude(String.valueOf(location.latitude));
                saleExecution.setLongitude(String.valueOf(location.longitude));
            }
            saleExecution.setCheckInDateTime(HolcimUtility
                    .getTodayFormated("yyyy-MM-dd HH:mm"));
            if (fragmentAccountInfo != null
                    && saleExecution.getAccount() == null) {
                fragmentAccountInfo.setLatitude(String
                        .valueOf(location.latitude));
                fragmentAccountInfo.setLongitude(String
                        .valueOf(location.longitude));
            }
            Toast.makeText(getApplication(), "Current location captured.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void goBackFromOutstandingFeedback(SaleExecution saleExecution,
                                              ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                              ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        goToFeedback(saleExecution, comeptitorsMarketings, actionLogs,
                preOrders);
    }

    @Override
    public void goToOutstandingFeedbackDetail(SaleExecution saleExecution,
                                              ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                              ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders,
                                              OutstandingFeedback outstandingFeedback) {
        Fragment fragment = new FragmentOutstandingFeedbackDetail(
                saleExecution, comeptitorsMarketings, actionLogs, preOrders,
                outstandingFeedback);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment,
                        HolcimConsts.FRAGMENT_OUTSTANDING_FEEDBACK_DETAIL)
                .commit();
        displayView(5);
    }

    @Override
    public void goBackFromFragmentOutstandingFeedbackDetail(
            SaleExecution saleExecution,
            ArrayList<CompetitorMarketing> comeptitorsMarketings,
            ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        goToOutstandingFeedback(saleExecution, comeptitorsMarketings,
                actionLogs, preOrders);
    }

    @Override
    public void goToOutstandingFeedback(SaleExecution saleExecution,
                                        ArrayList<CompetitorMarketing> comeptitorsMarketings,
                                        ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders) {
        Fragment fragment = new FragmentOutstandingFeedback(saleExecution,
                comeptitorsMarketings, actionLogs, preOrders);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, fragment,
                        HolcimConsts.FRAGMENT_OUTSTANDING_FEEDBACK_LIST)
                .commit();
        displayView(5);
    }

    @Override
    public boolean isEditing() {
        return date != null;
    }

    @Override
    public void takePhoto() {
        Intent mIntent = new Intent(this, HolcimCameraActivity.class);
        if (isAccountFragmentVisible()) {
            mIntent.putExtra("isLandmarkPicture", true);
            mIntent.putExtra("isContactPicture", false);
            mIntent.putExtra("isFeedback", false);
        } else if (isContactFragmentVisible()) {
            mIntent.putExtra("isLandmarkPicture", false);
            mIntent.putExtra("isContactPicture", true);
            mIntent.putExtra("isFeedback", false);
        }
        startActivityForResult(mIntent,
                HolcimConsts.ACTIVITY_REQUEST_CAMERA_LOG);
    }

}