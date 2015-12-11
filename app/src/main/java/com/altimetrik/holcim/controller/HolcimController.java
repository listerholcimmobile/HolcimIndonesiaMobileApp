package com.altimetrik.holcim.controller;

import android.content.Context;
import android.widget.Toast;

import com.altimetrik.holcim.controller.webservice.HttpUtils;
import com.altimetrik.holcim.controller.webservice.WebServiceException;
import com.altimetrik.holcim.controller.webservice.WebServiceRESTBasic;
import com.altimetrik.holcim.data.managment.HolcimDataSource;
import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.exception.HolcimError;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.Account;
import com.holcim.altimetrik.android.model.ActionsLog;
import com.holcim.altimetrik.android.model.Competitor;
import com.holcim.altimetrik.android.model.CompetitorMarketing;
import com.holcim.altimetrik.android.model.Contact;
import com.holcim.altimetrik.android.model.OutstandingFeedback;
import com.holcim.altimetrik.android.model.PreOrder;
import com.holcim.altimetrik.android.model.Prospect;
import com.holcim.altimetrik.android.model.SaleExecution;
import com.holcim.altimetrik.android.model.TeleSale;
import com.holcim.altimetrik.android.model.User;
import com.holcim.altimetrik.android.utilities.AltimetrikException;
import com.holcim.altimetrik.android.utilities.AltimetrikFileHandler;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.HolcimUtility;
import com.holcim.altimetrik.android.utilities.SFUser;
import com.holcim.altimetrik.android.utilities.TextProgressBar;
import com.holcim.hsea.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.DaoException;

/**
 * Class with the methods of access web services
 *
 * @author labreu@altimetrik
 */
public class HolcimController {
    public static boolean mSynComplete;

    public static boolean Logout(Context pContext) throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimConsts.OAUTH_BASIC_URL);
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("token", HolcimApp
                    .getAccessTokens().get_access_token()));
            wsHandler.callGETString("revoke", parameters,
                    HolcimApp.GetTokenHeader(), null);
            return true;
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        }
    }

    /**
     * Web service call to get the user
     *
     * @return String user name
     * @throws LensException
     */
    public static SFUser getUser() throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(HolcimApp
                    .getAccessTokens().get_instance_url()
                    + "/id/"
                    + HolcimApp.getAccessTokens().get_org_id() + "/");
            return HolcimDataSource.JSONToUser(wsHandler.callGETString(
                    HolcimApp.getAccessTokens().get_user_id(), null,
                    HolcimApp.GetTokenHeader(), null));
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        }
    }

    /**
     * Web service call to get the accounts
     *
     * @return ArrayList<Account>
     * @throws HolcimException
     */
    public static ArrayList<Account> getAccounts(Context context)
            throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            return HolcimDataSource.JSONToAccountList(wsHandler.callGETString(
                    "getAccounts", null, HolcimApp.GetTokenHeader(),
                    HttpUtils.CONTENT_TYPE_FORM_URL_ECODED));

        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        }
    }

    /**
     * Web service call to get the user profile
     *
     * @return Profile
     * @throws HolcimException
     */
    public static String getUserProfile(Context context) throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            return HolcimDataSource.JSONToProfile(wsHandler.callGETString(
                    "getProfile", null, HolcimApp.GetTokenHeader(),
                    HttpUtils.CONTENT_TYPE_FORM_URL_ECODED));
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        }
    }

    /**
     * Web service call to check if app is logged in
     *
     * @return Boolean
     * @throws HolcimException
     */
    public static boolean checkLogin(Context context) throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            // -----------
            String response = null;
            response = wsHandler.callGETString("testServices", null,
                    HolcimApp.GetTokenHeader(),
                    HttpUtils.CONTENT_TYPE_FORM_URL_ECODED);
            if (response != null) {
                return true;
            } else {
                return false;
            }
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        }
    }

    /**
     * Web service call to check if app is logged in
     *
     * @return Boolean
     * @throws HolcimException
     */
    public static ArrayList<Contact> getContacts(Context context)
            throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            return HolcimDataSource.JSONToContactList(wsHandler.callGETString(
                    "getContacts", null, HolcimApp.GetTokenHeader(),
                    HttpUtils.CONTENT_TYPE_FORM_URL_ECODED));
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        }
    }

    public static String getContactImage(Context context, Contact contact)
            throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
                    context);
            return fileHandler.GetChecksum(fileHandler.SaveFileFromInputStream(
                    HolcimDataSource.GetContactImagePath(context, contact),
                    wsHandler.callGETInputStream("downloadImage", null,
                            HolcimController
                                    .getContactImageWebServiceHeaders(contact),
                            null)));
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        } catch (AltimetrikException e) {
            throw new HolcimException(HolcimException.FILE_EXCEPTION,
                    new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
                            e.getMessage()));
        }
    }

    private static List<NameValuePair> getContactImageWebServiceHeaders(
            Contact contact) {
        List<NameValuePair> header = HolcimApp.GetTokenHeader();
        header.add(new BasicNameValuePair("objectName",
                HolcimConsts.CONTACT_SF_OBJECT_NAME));
        header.add(new BasicNameValuePair("fieldName",
                HolcimConsts.CONTACT_SF_IMAGE_FIELD_NAME));
        header.add(new BasicNameValuePair("parentId", contact.getSalesforceId()));
        return header;
    }

    public static String getSaleExecutionImage(Context context,
                                               SaleExecution saleExecution, String field) throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
                    context);
            return fileHandler.GetChecksum(fileHandler.SaveFileFromInputStream(
                    HolcimDataSource.GetSaleExecutionImagePath(context,
                            saleExecution, field),
                    wsHandler.callGETInputStream("downloadImage", null,
                            HolcimController
                                    .getSaleExecutionImageWebServiceHeaders(
                                            saleExecution, field), null)));
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        } catch (AltimetrikException e) {
            throw new HolcimException(HolcimException.FILE_EXCEPTION,
                    new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
                            e.getMessage()));
        }
    }

    public static String getActionLogImage(Context context,
                                           ActionsLog actionlog, String field, int photoNumber) throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
                    context);
            return fileHandler.GetChecksum(fileHandler.SaveFileFromInputStream(
                    HolcimDataSource.GetActionLogImagePath(context, actionlog,
                            field, photoNumber), wsHandler.callGETInputStream(
                            "downloadImage", null, HolcimController
                                    .getActionLogImageWebServiceHeaders(
                                            actionlog, field), null)));
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        } catch (AltimetrikException e) {
            throw new HolcimException(HolcimException.FILE_EXCEPTION,
                    new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
                            e.getMessage()));
        }
    }

    public static void submitSaleExecutionImage(Context context,
                                                SaleExecution saleExecution, String field) throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
                    context);
            wsHandler.callPOSTInputStreamString("uploadImage", fileHandler
                    .GetInputStreamFromFile(saleExecution.getImagePath(context,
                            field)), HolcimController
                    .getSaleExecutionImageWebServiceHeaders(saleExecution,
                            field), null);
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        } catch (AltimetrikException e) {
            throw new HolcimException(HolcimException.FILE_EXCEPTION,
                    new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
                            e.getMessage()));
        }
    }

    public static void submitContactImage(Context context, Contact contact,
                                          String field) throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
                    context);
            wsHandler.callPOSTInputStreamString("uploadImage", fileHandler
                    .GetInputStreamFromFile(contact
                            .getImagePath(context, field)), HolcimController
                    .getContactImageWebServiceHeaders(contact, field), null);
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        } catch (AltimetrikException e) {
            throw new HolcimException(HolcimException.FILE_EXCEPTION,
                    new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
                            e.getMessage()));
        }
    }

    public static void submitActionLogImage(Context context,
                                            ActionsLog actionLog, String field, int photoNumber) throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
                    context);
            wsHandler
                    .callPOSTInputStreamString("uploadImage", fileHandler
                            .GetInputStreamFromFile(actionLog.getImagePath(
                                    context, field, photoNumber)), HolcimController
                            .getActionLogImageWebServiceHeaders(actionLog,
                                    field), null);
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        } catch (AltimetrikException e) {
            throw new HolcimException(HolcimException.FILE_EXCEPTION,
                    new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
                            e.getMessage()));
        }
    }

    private static List<NameValuePair> getSaleExecutionImageWebServiceHeaders(
            SaleExecution saleExecution, String field) {
        List<NameValuePair> header = HolcimApp.GetTokenHeader();
        header.add(new BasicNameValuePair("objectName",
                HolcimConsts.SALE_EXECUTION_SF_OBJECT_NAME));
        header.add(new BasicNameValuePair("fieldName", field));
        header.add(new BasicNameValuePair("parentId", saleExecution
                .getSalesforceId()));
        return header;
    }

    private static List<NameValuePair> getContactImageWebServiceHeaders(
            Contact contact, String field) {
        List<NameValuePair> header = HolcimApp.GetTokenHeader();
        header.add(new BasicNameValuePair("objectName",
                HolcimConsts.CONTACT_SF_OBJECT_NAME));
        header.add(new BasicNameValuePair("fieldName", field));
        header.add(new BasicNameValuePair("parentId", contact.getSalesforceId()));
        return header;
    }

    private static List<NameValuePair> getActionLogImageWebServiceHeaders(
            ActionsLog actionLog, String field) {
        List<NameValuePair> header = HolcimApp.GetTokenHeader();
        header.add(new BasicNameValuePair("objectName",
                HolcimConsts.ACTIONLOG_SF_OBJECT_NAME));
        header.add(new BasicNameValuePair("fieldName", field));
        header.add(new BasicNameValuePair("parentId", actionLog
                .getSalesforceId()));
        return header;
    }

    public static String submitContact(Context context, Contact contact)
            throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            return wsHandler.callPOSTJSONString("submitContact",
                    contact.toJson(), HolcimApp.GetTokenHeader(),
                    HttpUtils.CONTENT_TYPE_FORM_URL_ECODED);
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        } catch (HolcimException he) {
            throw he;
        }
    }

    public static String submitSaleExecution(Context context,
                                             SaleExecution saleExecution) throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            return wsHandler.callPOSTJSONString("submitSalesExecution",
                    saleExecution.toJSON().toString(),
                    HolcimApp.GetTokenHeader(),
                    HttpUtils.CONTENT_TYPE_FORM_URL_ECODED);
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        } catch (HolcimException he) {
            throw he;
        }
    }

    public static String submitCompetitorMarketing(Context context,
                                                   CompetitorMarketing competitorMarketing) throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            return wsHandler.callPOSTJSONString("submitCompetitorMarketing",
                    competitorMarketing.toJSON().toString(),
                    HolcimApp.GetTokenHeader(),
                    HttpUtils.CONTENT_TYPE_FORM_URL_ECODED);
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        } catch (HolcimException he) {
            throw he;
        }
    }

    public static String submitPreOrder(Context context, PreOrder preOrder)
            throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            return wsHandler.callPOSTJSONString("submitPreOrder", preOrder
                            .toJSON().toString(), HolcimApp.GetTokenHeader(),
                    HttpUtils.CONTENT_TYPE_FORM_URL_ECODED);
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        } catch (HolcimException he) {
            throw he;
        }
    }

    public static String submitTeleSale(Context context, TeleSale teleSale)
            throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            return wsHandler.callPOSTJSONString("submitTelesales", teleSale
                            .toJSON().toString(), HolcimApp.GetTokenHeader(),
                    HttpUtils.CONTENT_TYPE_FORM_URL_ECODED);
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        } catch (HolcimException he) {
            throw he;
        }
    }

    public static String submitActionLogs(Context context, ActionsLog actionLog)
            throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            return wsHandler.callPOSTJSONString("submitActionLog", actionLog
                            .toJSON().toString(), HolcimApp.GetTokenHeader(),
                    HttpUtils.CONTENT_TYPE_FORM_URL_ECODED);
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        } catch (HolcimException he) {
            throw he;
        }
    }

    public static String submitOutStandingFeedbacks(Context context, OutstandingFeedback outstandingFeedback)
            throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            return wsHandler.callPOSTJSONString("submitActionLog", outstandingFeedback
                            .toJSON().toString(), HolcimApp.GetTokenHeader(),
                    HttpUtils.CONTENT_TYPE_FORM_URL_ECODED);
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        } catch (HolcimException he) {
            throw he;
        }
    }


    public static ArrayList<Prospect> getProspects(Context context)
            throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            return HolcimDataSource.JSONToProspectList(wsHandler.callGETString(
                    "getProspects", null, HolcimApp.GetTokenHeader(),
                    HttpUtils.CONTENT_TYPE_FORM_URL_ECODED));
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        }
    }

    public static ArrayList<OutstandingFeedback> getOutstandingFeedbacks(
            Context context) throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            return HolcimDataSource.JSONToOutstandingFeedbackList(wsHandler
                    .callGETString("getOutstandingFeedbacks", null,
                            HolcimApp.GetTokenHeader(),
                            HttpUtils.CONTENT_TYPE_FORM_URL_ECODED));
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        }
    }

    public static ArrayList<Competitor> getCompetitors(Context context)
            throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            return HolcimDataSource.JSONToCompetitorList(wsHandler
                    .callGETString("getCompetitors", null,
                            HolcimApp.GetTokenHeader(),
                            HttpUtils.CONTENT_TYPE_FORM_URL_ECODED));
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        }
    }

    public static ArrayList<SaleExecution> getSaleExecutions(Context context)
            throws HolcimException {
        try {
            WebServiceRESTBasic wsHandler = new WebServiceRESTBasic(
                    HolcimApp.GetWebServicesURL());
            List<NameValuePair> header = HolcimApp.GetTokenHeader();
            header.add(new BasicNameValuePair("dateFrom", HolcimController
                    .getStartSyncDate()));
            header.add(new BasicNameValuePair("dateTo", HolcimController
                    .getEndSyncDate()));
            return HolcimDataSource.JSONToSaleExecutionList(wsHandler
                    .callGETString("getSalesExecutions", null, header,
                            HttpUtils.CONTENT_TYPE_FORM_URL_ECODED));
        } catch (WebServiceException e) {
            throw new HolcimException(HolcimException.WEB_SERVICE_EXCEPTION,
                    HolcimConsts.ERROR_CALLING_WEB_SERVICE_STATUS,
                    e.getMessage());
        }
    }

    public static String getStartSyncDate() {
        //return HolcimUtility.getTodayFormated("yyyy-MM-dd");
        return HolcimUtility.getDateStringFormated(
                HolcimUtility.getDateAddDaysFromToday(-14), "yyyy-MM-dd");
    }

    public static String getEndSyncDate() {
        return HolcimUtility.getDateStringFormated(
                HolcimUtility.getDateAddDaysFromToday(14), "yyyy-MM-dd");
    }

    static Double countProgress;
    static Double totalProgress;

    public static String synchronize(Context context, TextProgressBar pbar)
            throws Exception {
        mSynComplete = false;
        String error = null;
        // get user info
        try {
            HolcimApp.publishProgress(3, pbar);
            HolcimApp.getInstance().setProfile(getUserProfile(context));
            if (HolcimApp.daoSession.getUserDao().loadAll().size() == 0) {
                User user = new User();
                user.setProfile(HolcimApp.getInstance().getProfile());
                HolcimApp.daoSession.insert(user);
            } else {
                User user = HolcimApp.daoSession.getUserDao().loadAll().get(0);
                user.setProfile(HolcimApp.getInstance().getProfile());
                HolcimApp.daoSession.update(user);
            }
        } catch (Exception e) {
        }

        try {
            // UPLOAD DATA
            // Upload Contacts
            ArrayList<Contact> contactsToUpload = HolcimDataSource
                    .isContactToCreate();
            HolcimApp.publishProgress(5, pbar);
            if (contactsToUpload != null && contactsToUpload.size() > 0) {
                for (Contact contact : contactsToUpload) {
                    try {
                        boolean uploadContactPicture = false;
                        uploadContactPicture = contact
                                .needUploadContactPicture(context);
                        if (uploadContactPicture) {
                            contact.setPicturemd5(AltimetrikFileHandler
                                    .getMD5Checksum(HolcimDataSource
                                            .GetContactImagePath(context,
                                                    contact)));
                            HolcimApp.daoSession.update(contact);
                        }
                        contact.setSalesforceId(HolcimController.submitContact(
                                context, contact));
                        HolcimApp.daoSession.update(contact);
                        if (uploadContactPicture) {
                            HolcimController.submitContactImage(context,
                                    contact,
                                    HolcimConsts.CONTACT_SF_IMAGE_FIELD_NAME);
                        }
                    } catch (Exception e) {
                        error = HolcimConsts.SYNC_ERROR;
                    }
                }
            }
            ArrayList<TeleSale> teleSaleToUpload = HolcimDataSource
                    .isTeleSaleToUpload();
            HolcimApp.publishProgress(8, pbar);
            if (teleSaleToUpload != null && teleSaleToUpload.size() > 0) {
                for (TeleSale teleSale : teleSaleToUpload) {
                    try {
                        teleSale.setSalesforceId(HolcimController
                                .submitTeleSale(context, teleSale));
                        HolcimApp.daoSession.update(teleSale);
                    } catch (Exception e) {
                        error = HolcimConsts.SYNC_ERROR;
                    }
                }
            }
            // Upload Sales Executions
            ArrayList<SaleExecution> saleExecutionsToUpload = HolcimDataSource
                    .isSaleExecutionToUpload();
            HolcimApp.publishProgress(10, pbar);
            totalProgress = 10.0;
            countProgress = 0.0;
            if (saleExecutionsToUpload != null
                    && saleExecutionsToUpload.size() > 0) {

                countProgress = calculateProgressRatio((double) saleExecutionsToUpload
                        .size());
                for (SaleExecution saleExecution : saleExecutionsToUpload) {

                    try {
                        boolean uploadLandmarkPic = false;
                        boolean uploadPicture = false;
                        boolean uploadSSPic = false;

                        try {
                            uploadLandmarkPic = saleExecution
                                    .needUploadLandmarkPicture(context);
                        } catch (Exception e) {
                            // image upload fail but try to continue
                            e.printStackTrace();
                        }
                        try {
                            uploadPicture = saleExecution
                                    .needUploadPicture(context);
                            if (uploadLandmarkPic) {
                                saleExecution
                                        .setLandmarkPictureMD5(AltimetrikFileHandler
                                                .getMD5Checksum(HolcimDataSource
                                                        .GetSaleExecutionImagePath(
                                                                context,
                                                                saleExecution,
                                                                HolcimConsts.SALE_EXECUTION_SF_LANDMARK_IMAGE_FIELD_NAME)));
                                HolcimApp.daoSession.update(saleExecution);
                            }
                        } catch (Exception e) {
                            // image upload fail but try to continue
                            e.printStackTrace();
                        }
                        try {
                            uploadSSPic = saleExecution
                                    .needUploadSSPicture(context);
                            if (uploadSSPic) {
                                saleExecution
                                        .setSsPictureMD5(AltimetrikFileHandler
                                                .getMD5Checksum(HolcimDataSource
                                                        .GetSaleExecutionImagePath(
                                                                context,
                                                                saleExecution,
                                                                HolcimConsts.SALE_EXECUTION_SF_SS_IMAGE_FIELD_NAME)));
                                HolcimApp.daoSession.update(saleExecution);
                            }
                        } catch (Exception e) {
                            // image upload fail but try to continue
                            e.printStackTrace();
                        }
                        if (saleExecution.getSalesforceId() == null
                                || saleExecution.getSalesforceId().equals("")) {
                            saleExecution
                                    .setSalesforceId(HolcimController
                                            .submitSaleExecution(context,
                                                    saleExecution));
                        } else {
                            HolcimController.submitSaleExecution(context,
                                    saleExecution);
                        }
                        HolcimApp.daoSession.update(saleExecution);
                        try {
                            if (uploadLandmarkPic) {
                                HolcimController
                                        .submitSaleExecutionImage(
                                                context,
                                                saleExecution,
                                                HolcimConsts.SALE_EXECUTION_SF_LANDMARK_IMAGE_FIELD_NAME);
                            }
                        } catch (Exception e) {
                            // image upload fail but try to continue
                            e.printStackTrace();
                        }
                        try {
                            if (uploadSSPic) {
                                HolcimController
                                        .submitSaleExecutionImage(
                                                context,
                                                saleExecution,
                                                HolcimConsts.SALE_EXECUTION_SF_SS_IMAGE_FIELD_NAME);
                            }
                        } catch (Exception e) {
                            // image upload fail but try to continue
                            e.printStackTrace();
                        }
                        try {
                            if (uploadPicture) {
                                HolcimController
                                        .submitSaleExecutionImage(
                                                context,
                                                saleExecution,
                                                HolcimConsts.SALE_EXECUTION_SF_PICTURE_IMAGE_FIELD_NAME);
                            }
                        } catch (Exception e) {
                            // image upload fail but try to continue
                        }

                    } catch (Exception e) {
                        error = HolcimConsts.SYNC_ERROR;
                    }

                    totalProgress = totalProgress + countProgress;
                    HolcimApp.publishProgress((int) Math.round(totalProgress),
                            pbar);

                }
            }
            // Upload Competitor Marketing
            ArrayList<CompetitorMarketing> competitorMarketingToUpload = HolcimDataSource
                    .isCompetitorMarketingToUpload();
            HolcimApp.publishProgress(22, pbar);
            if (competitorMarketingToUpload != null
                    && competitorMarketingToUpload.size() > 0) {
                for (CompetitorMarketing compMarketing : competitorMarketingToUpload) {
                    try {
                        if (compMarketing.getSaleExecution().getSalesforceId() == null
                                || compMarketing.getSaleExecution()
                                .getSalesforceId().equals("")) {
                            compMarketing.getSaleExecution().refresh();
                        }
                        compMarketing.setSalesforceId(HolcimController
                                .submitCompetitorMarketing(context,
                                        compMarketing));
                        HolcimApp.daoSession.update(compMarketing);
                    } catch (Exception e) {
                        error = HolcimConsts.SYNC_ERROR;

                    }
                }
            }
            // Upload ActionLog
            ArrayList<ActionsLog> actionsLogToUpload = HolcimDataSource
                    .isActionsLogToUpload();
            HolcimApp.publishProgress(25, pbar);
            if (actionsLogToUpload != null && actionsLogToUpload.size() > 0) {
                for (ActionsLog actionLog : actionsLogToUpload) {
                    try {
                        boolean uploadPicture, uploadPicture1, uploadPicture2, uploadPicture3, uploadPicture4 = false;
                        uploadPicture = actionLog.needUploadPicture(context);
                        uploadPicture1 = actionLog.needUploadPicture1(context);
                        uploadPicture2 = actionLog.needUploadPicture2(context);
                        uploadPicture3 = actionLog.needUploadPicture3(context);
                        uploadPicture4 = actionLog.needUploadPicture4(context);
                        if (actionLog.getSaleExecution().getSalesforceId() == null
                                || actionLog.getSaleExecution()
                                .getSalesforceId().equals("")) {
                            actionLog.getSaleExecution().refresh();
                        }
                        if (uploadPicture || uploadPicture1 || uploadPicture2 || uploadPicture3 || uploadPicture4) {
                            if (uploadPicture) {
                                actionLog.setPicture(AltimetrikFileHandler.getMD5Checksum(HolcimDataSource.GetActionLogImagePath(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_FIELD_NAME, 0)));
                            }
                            if (uploadPicture1) {
                                actionLog.setPicture1(AltimetrikFileHandler.getMD5Checksum(HolcimDataSource.GetActionLogImagePath(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_1_FIELD_NAME, 1)));
                            }
                            if (uploadPicture2) {
                                actionLog.setPicture2(AltimetrikFileHandler.getMD5Checksum(HolcimDataSource.GetActionLogImagePath(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_2_FIELD_NAME, 2)));
                            }
                            if (uploadPicture3) {
                                actionLog.setPicture3(AltimetrikFileHandler.getMD5Checksum(HolcimDataSource.GetActionLogImagePath(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_3_FIELD_NAME, 3)));
                            }
                            if (uploadPicture4) {
                                actionLog.setPicture4(AltimetrikFileHandler.getMD5Checksum(HolcimDataSource.GetActionLogImagePath(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_4_FIELD_NAME, 4)));
                            }
                            actionLog.setSalesforceId(HolcimController
                                    .submitActionLogs(context, actionLog));
                            HolcimApp.daoSession.update(actionLog);
                            if (uploadPicture) {
                                HolcimController.submitActionLogImage(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_FIELD_NAME, 0);
                            }
                            if (uploadPicture1) {
                                HolcimController.submitActionLogImage(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_1_FIELD_NAME, 1);
                            }
                            if (uploadPicture2) {
                                HolcimController.submitActionLogImage(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_2_FIELD_NAME, 2);
                            }
                            if (uploadPicture3) {
                                HolcimController.submitActionLogImage(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_3_FIELD_NAME, 3);
                            }
                            if (uploadPicture4) {
                                HolcimController.submitActionLogImage(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_4_FIELD_NAME, 4);
                            }
                        } else {
                            actionLog.setSalesforceId(HolcimController
                                    .submitActionLogs(context, actionLog));
                            HolcimApp.daoSession.update(actionLog);
                        }
                    } catch (Exception e) {

                        error = HolcimConsts.SYNC_ERROR;
                    }
                }
            }

            // Upload outstanding feedbacks
            ArrayList<OutstandingFeedback> outstandingFeedbacksToUpload = HolcimDataSource.isOutstandingFeedbackToUpload();

            if (outstandingFeedbacksToUpload != null && outstandingFeedbacksToUpload.size() > 0) {
                for (OutstandingFeedback outFeed : outstandingFeedbacksToUpload) {
                    try {
                        outFeed.setSalesforceId(
                                HolcimController.submitOutStandingFeedbacks(context, outFeed));
                        HolcimApp.daoSession.update(outFeed);
                    } catch (Exception e) {
                        //outstanding feedbacks are also action logs in Salesforce
                        error = HolcimConsts.SYNC_ERROR;
                    }
                }
            }
            // Upload PreOrder
            ArrayList<PreOrder> preOrderToUpload = HolcimDataSource
                    .isPreOrderToUpload();
            HolcimApp.publishProgress(28, pbar);
            if (preOrderToUpload != null && preOrderToUpload.size() > 0) {
                for (PreOrder preOrder : preOrderToUpload) {
                    try {
                        if (preOrder.getSaleExecution().getSalesforceId() == null
                                || preOrder.getSaleExecution()
                                .getSalesforceId().equals("")) {
                            preOrder.getSaleExecution().refresh();
                        }
                        preOrder.setSalesforceId(HolcimController
                                .submitPreOrder(context, preOrder));
                        HolcimApp.daoSession.update(preOrder);
                    } catch (Exception e) {
                        error = HolcimConsts.SYNC_ERROR;
                    }
                }
            }
            // Download Data
            HolcimDataSource.clearDatabase();

            // Clear file before download
            try {
                File path1 = new File(
                        AltimetrikFileHandler.getDataDir(context),
                        HolcimConsts.CONTACT_DIRECTORY_NAME);
                AltimetrikFileHandler.deleteAllFilesInDirectory(context,
                        path1.getAbsolutePath());

                File path2 = new File(
                        AltimetrikFileHandler.getDataDir(context),
                        HolcimConsts.ACTIONLOG_DIRECTORY_NAME);
                AltimetrikFileHandler.deleteAllFilesInDirectory(context,
                        path2.getAbsolutePath());

                File path3 = new File(
                        AltimetrikFileHandler.getDataDir(context),
                        HolcimConsts.SALE_EXECUTION_DIRECTORY_NAME);
                AltimetrikFileHandler.deleteAllFilesInDirectory(context,
                        path3.getAbsolutePath());

            } catch (Exception e) {
                throw new HolcimException(HolcimException.FILE_EXCEPTION,
                        new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
                                e.getMessage()));
            }

            // Contact
            try {
                ArrayList<Contact> contacts = HolcimController
                        .getContacts(context);
                HolcimApp.publishProgress(35, pbar);
                totalProgress = 35.000000000;
                countProgress = 0.0000000000;
                if (contacts != null && contacts.size() > 0) {

                    countProgress = calculateProgressRatio((double) contacts
                            .size());

                    for (Contact contact : contacts) {

                        Contact existingContact = HolcimDataSource
                                .getContactBySFId(contact.getSalesforceId());
                        if (existingContact != null) {
                            contact.setId(existingContact.getId());
                            HolcimApp.daoSession.update(contact);
                        } else {
                            HolcimApp.daoSession.insert(contact);
                        }
                        if (contact.needDownloadPicture(context)) {
                            contact.setPicturemd5(HolcimController
                                    .getContactImage(context, contact));
                            HolcimApp.daoSession.update(contact);
                            if (totalProgress < 46)
                                totalProgress = totalProgress + 1;
                        }

                        totalProgress = totalProgress + countProgress;

                        HolcimApp.publishProgress(
                                (int) Math.round(totalProgress), pbar);

                    }
                }
            } catch (Exception e) {
                error = HolcimConsts.SYNC_ERROR;
            }

            // Prospects
            try {
                ArrayList<Prospect> prospects = HolcimController
                        .getProspects(context);
                HolcimApp.publishProgress(46, pbar);
                totalProgress = 46.0;
                countProgress = 0.0;
                if (prospects != null && prospects.size() > 0) {

                    countProgress = calculateProgressRatio((double) prospects
                            .size());

                    for (Prospect prospect : prospects) {
                        Prospect existingProspect = HolcimDataSource
                                .getProspectBySFId(prospect.getSalesforceId());
                        if (existingProspect != null) {
                            prospect.setId(existingProspect.getId());
                            prospect.__setDaoSession(HolcimApp.daoSession);
                            HolcimApp.daoSession.update(prospect);
                        } else {
                            HolcimApp.daoSession.insert(prospect);
                        }

                        totalProgress = totalProgress + countProgress;
                        HolcimApp.publishProgress(
                                (int) Math.round(totalProgress), pbar);

                    }
                }
            } catch (Exception e) {
                error = HolcimConsts.SYNC_ERROR;
            }

            // Accounts
            try {
                ArrayList<Account> accounts = HolcimController
                        .getAccounts(context);
                HolcimApp.publishProgress(58, pbar);
                totalProgress = 58.0;
                countProgress = 0.0;
                if (accounts != null && accounts.size() > 0) {

                    countProgress = calculateProgressRatio((double) accounts
                            .size());

                    for (Account account : accounts) {
                        Account existingAccount = HolcimDataSource
                                .getAccountBySFId(account.getSalesforceId());
                        if (existingAccount != null) {
                            account.setId(existingAccount.getId());
                            account.__setDaoSession(HolcimApp.daoSession);
                            HolcimApp.daoSession.update(account);
                        } else {
                            HolcimApp.daoSession.insert(account);


                        }

                        totalProgress = totalProgress + countProgress;
                        HolcimApp.publishProgress(
                                (int) Math.round(totalProgress), pbar);

                    }
                }
            } catch (Exception e) {
                error = HolcimConsts.SYNC_ERROR;
            }

            // Competitor
            try {
                ArrayList<Competitor> competitors = HolcimController
                        .getCompetitors(context);
                HolcimApp.publishProgress(69, pbar);
                if (competitors != null && competitors.size() > 0) {
                    for (Competitor competitor : competitors) {
                        Competitor existingCompetitor = HolcimDataSource
                                .getCompetitorBySFId(competitor
                                        .getSalesforceId());
                        if (existingCompetitor != null) {
                            competitor.setId(existingCompetitor.getId());
                            competitor.__setDaoSession(HolcimApp.daoSession);
                            HolcimApp.daoSession.update(competitor);
                        } else {
                            HolcimApp.daoSession.insert(competitor);
                        }
                    }
                }
            } catch (Exception e) {
                error = HolcimConsts.SYNC_ERROR;
            }

            // OutstandingFeedback
            try {
                ArrayList<OutstandingFeedback> outstandingFeedbacks = HolcimController
                        .getOutstandingFeedbacks(context);
                HolcimApp.publishProgress(72, pbar);
                if (outstandingFeedbacks != null
                        && outstandingFeedbacks.size() > 0) {
                    for (OutstandingFeedback outstandingFeedback : outstandingFeedbacks) {
                        OutstandingFeedback existingOutstandingFeedback = HolcimDataSource
                                .getOutstandingFeedbackBySFId(outstandingFeedback
                                        .getSalesforceId());
                        Account account = null;
                        if (outstandingFeedback.getSalesforceAccountId() != null) {
                            account = HolcimDataSource
                                    .getAccountBySFId(outstandingFeedback
                                            .getSalesforceAccountId());
                        }
                        Prospect prospect = null;
                        if (outstandingFeedback.getSalesforceProspectId() != null) {
                            prospect = HolcimDataSource
                                    .getProspectBySFId(outstandingFeedback
                                            .getSalesforceProspectId());
                        }
                        if (account != null) {
                            outstandingFeedback.setAccountId(account.getId());
                            if (existingOutstandingFeedback != null) {
                                outstandingFeedback
                                        .setId(existingOutstandingFeedback
                                                .getId());
                                outstandingFeedback
                                        .__setDaoSession(HolcimApp.daoSession);
                                HolcimApp.daoSession
                                        .update(outstandingFeedback);
                            } else {
                                HolcimApp.daoSession
                                        .insert(outstandingFeedback);
                            }
                        } else if (prospect != null) {
                            outstandingFeedback.setProspectId(prospect.getId());
                            if (existingOutstandingFeedback != null) {
                                outstandingFeedback
                                        .setId(existingOutstandingFeedback
                                                .getId());
                                outstandingFeedback
                                        .__setDaoSession(HolcimApp.daoSession);
                                HolcimApp.daoSession
                                        .update(outstandingFeedback);
                            } else {
                                HolcimApp.daoSession
                                        .insert(outstandingFeedback);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                error = HolcimConsts.SYNC_ERROR;
            }

            // SaleExecution
            ArrayList<SaleExecution> saleExecutions = HolcimController
                    .getSaleExecutions(context);
            HolcimApp.publishProgress(83, pbar);
            totalProgress = 83.0;
            countProgress = 0.0;
            if (saleExecutions != null && saleExecutions.size() > 0) {

                countProgress = calculateProgressRatio((double) saleExecutions
                        .size());

                for (SaleExecution saleExecution : saleExecutions) {
                    SaleExecution existingSaleExecution = HolcimDataSource
                            .getSaleExecutionBySFId(saleExecution
                                    .getSalesforceId());
                    if (existingSaleExecution != null) {

                        ArrayList<CompetitorMarketing> newCompMarks = null;
                        ArrayList<ActionsLog> newActionsLogs = null;
                        ArrayList<PreOrder> newPreOrders = null;
                        try {
                            newCompMarks = (ArrayList<CompetitorMarketing>) saleExecution
                                    .getCompetitorMarketings();
                        } catch (DaoException e) {
                            // do nothing, empty relationship
                        }
                        try {
                            newActionsLogs = (ArrayList<ActionsLog>) saleExecution
                                    .getActionLogs();
                        } catch (DaoException e) {
                            // do nothing, empty relationship
                        }
                        try {
                            newPreOrders = (ArrayList<PreOrder>) saleExecution
                                    .getPreOrders();
                        } catch (DaoException e) {
                            // do nothing, empty relationship
                        }
                        saleExecution.resetActionLogs();
                        saleExecution.resetCompetitorMarketings();
                        saleExecution.resetPreOrders();
                        existingSaleExecution.removeAllExistingReferences();
                        saleExecution.setId(existingSaleExecution.getId());
                        saleExecution.__setDaoSession(HolcimApp.daoSession);
                        HolcimApp.daoSession.update(saleExecution);
                        if (newPreOrders != null && newPreOrders.size() > 0) {
                            for (PreOrder preOrder : newPreOrders) {
                                preOrder.setSaleExecutionId(existingSaleExecution
                                        .getId());
                                HolcimApp.daoSession.insert(preOrder);
                            }
                        }
                        if (newCompMarks != null && newCompMarks.size() > 0) {
                            for (CompetitorMarketing compMark : newCompMarks) {
                                compMark.setSaleExecutionId(existingSaleExecution
                                        .getId());
                                HolcimApp.daoSession.insert(compMark);
                            }
                        }
                        if (newActionsLogs != null && newActionsLogs.size() > 0) {
                            for (ActionsLog actionLog : newActionsLogs) {
                                actionLog
                                        .setSaleExecutionId(existingSaleExecution
                                                .getId());
                                HolcimApp.daoSession.insert(actionLog);
                            }
                        }
                    } else {
                        ArrayList<CompetitorMarketing> newCompMarks = null;
                        ArrayList<ActionsLog> newActionsLogs = null;
                        ArrayList<PreOrder> newPreOrders = null;
                        try {
                            newCompMarks = (ArrayList<CompetitorMarketing>) saleExecution
                                    .getCompetitorMarketings();
                        } catch (DaoException e) {
                        }
                        try {
                            newActionsLogs = (ArrayList<ActionsLog>) saleExecution
                                    .getActionLogs();
                        } catch (DaoException e) {
                        }
                        try {
                            newPreOrders = (ArrayList<PreOrder>) saleExecution
                                    .getPreOrders();
                        } catch (DaoException e) {
                        }

                        saleExecution.resetActionLogs();
                        saleExecution.resetCompetitorMarketings();
                        saleExecution.resetPreOrders();
                        Long newSEId = HolcimApp.daoSession
                                .insert(saleExecution);
                        saleExecution.setId(newSEId);
                        if (newPreOrders != null && newPreOrders.size() > 0) {
                            for (PreOrder preOrder : newPreOrders) {
                                preOrder.setSaleExecutionId(newSEId);
                                HolcimApp.daoSession.insert(preOrder);
                            }
                        }
                        if (newCompMarks != null && newCompMarks.size() > 0) {
                            for (CompetitorMarketing compMark : newCompMarks) {
                                compMark.setSaleExecutionId(newSEId);
                                HolcimApp.daoSession.insert(compMark);
                            }
                        }
                        if (newActionsLogs != null && newActionsLogs.size() > 0) {
                            for (ActionsLog actionLog : newActionsLogs) {
                                actionLog.setSaleExecutionId(newSEId);
                                HolcimApp.daoSession.insert(actionLog);
                                if (actionLog.needDownloadPicture(context)) {
                                    actionLog.setPicture(HolcimController.getActionLogImage(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_FIELD_NAME, 0));
                                }
                                if (actionLog.needDownloadPicture1(context)) {
                                    actionLog.setPicture1(HolcimController.getActionLogImage(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_1_FIELD_NAME, 1));
                                }
                                if (actionLog.needDownloadPicture2(context)) {
                                    actionLog.setPicture2(HolcimController.getActionLogImage(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_2_FIELD_NAME, 2));
                                }
                                if (actionLog.needDownloadPicture3(context)) {
                                    actionLog.setPicture3(HolcimController.getActionLogImage(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_3_FIELD_NAME, 3));
                                }
                                if (actionLog.needDownloadPicture4(context)) {
                                    actionLog.setPicture4(HolcimController.getActionLogImage(context, actionLog, HolcimConsts.ACTIONLOG_SF_IMAGE_4_FIELD_NAME, 4));
                                }
                                HolcimApp.daoSession.update(actionLog);
                            }
                        }
                    }
                    try {
                        if (saleExecution.needDownloadPicture(context)) {
                            saleExecution
                                    .setPictureMD5(HolcimController
                                            .getSaleExecutionImage(
                                                    context,
                                                    saleExecution,
                                                    HolcimConsts.SALE_EXECUTION_SF_PICTURE_IMAGE_FIELD_NAME));
                            HolcimApp.daoSession.update(saleExecution);
                        }
                    } catch (Exception e) {
                        // image download fail but try to continue
                    }
                    try {
                        if (saleExecution.needDownloadLandmarkPicture(context)) {
                            saleExecution
                                    .setLandmarkPictureMD5(HolcimController
                                            .getSaleExecutionImage(
                                                    context,
                                                    saleExecution,
                                                    HolcimConsts.SALE_EXECUTION_SF_LANDMARK_IMAGE_FIELD_NAME));
                            HolcimApp.daoSession.update(saleExecution);
                        }
                    } catch (Exception e) {
                        // image download fail but try to continue
                    }
                    try {
                        if (saleExecution.needDownloadSSPicture(context)) {
                            saleExecution
                                    .setSsPictureMD5(HolcimController
                                            .getSaleExecutionImage(
                                                    context,
                                                    saleExecution,
                                                    HolcimConsts.SALE_EXECUTION_SF_SS_IMAGE_FIELD_NAME));
                            HolcimApp.daoSession.update(saleExecution);
                        }
                    } catch (Exception e) {
                        // image download fail but try to continue
                    }

                    totalProgress = totalProgress + countProgress;
                    HolcimApp.publishProgress((int) Math.round(totalProgress),
                            pbar);

                }
            }
            try {
                HolcimApp.publishProgress(99, pbar);
                //HolcimDataSource.deleteOldSaleExecutions();
                HolcimApp.publishProgress(100, pbar);
                mSynComplete = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return error;
        } catch (Exception e) {
            throw e;
        }
    }

    private static Double calculateProgressRatio(Double size) {
        countProgress = (double) ((double) 10 / size);
        return countProgress;
    }

}