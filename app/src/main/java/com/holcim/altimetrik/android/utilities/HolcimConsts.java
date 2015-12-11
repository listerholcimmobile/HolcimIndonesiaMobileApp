package com.holcim.altimetrik.android.utilities;

public class HolcimConsts {

	public static final String OAUTH_BASIC_URL = "https://cs6.salesforce.com/services/oauth2/";
	public static final String WEB_SERVICE_URL = "/services/apexrest/";

	public static final String KEY_FIRST_RUN = "firstRun";

	public static final int REDIRECT_VISIT_PLAN = 0;
	public static final int REDIRECT_ACCOUNT_LIST = 1;
	public static final int REDIRECT_SYNC = 2;	

	public static enum MENU {ACCOUNT,CONTACT,PREVIOUS,SIGN,SALESEXEC,FEEDBACK,PREORDER};

	public static final String CONTACT_DIRECTORY_NAME = "contacts";
	public static final String SALE_EXECUTION_DIRECTORY_NAME = "salexecution";
	public static final String ACTIONLOG_DIRECTORY_NAME = "actionLog";
	public static final String TEMPORAL_DIRECTORY_NAME = "tempmedia";
	public static final String CONTACT_FILENAME = "contact";
	public static final String SALE_EXECUTION_PICTURE_FILENAME = "se_picture";
	public static final String ACTIONLOG_PICTURE_FILENAME = "actionLog_picture";
	public static final String SALE_EXECUTION_SSPICTURE_FILENAME = "se_sspicture";
	public static final String SALE_EXECUTION__LANDMARK_PICTURE_FILENAME = "se_landmark_picture";

	public static final String ERROR_GENERAL_STATUS = "GENERAL_ERROR";
	public static final String ERROR_GENERAL_MESSAGE = "General error with message: ";
	public static final String ERROR_PARSING_JSON_STATUS = "JSON_ERROR";
	public static final String ERROR_PARSING_JSON_MESSAGE = "There was an error parsing the JSON response: ";
	public static final String ERROR_NO_RECORDS_RETURNED_STATUS = "NO_RECORDS"; 
	public static final String ERROR_NO_RECORDS_RETURNED_MESSAGE = "No records returned by web service."; 
	public static final String ERROR_CALLING_WEB_SERVICE_STATUS = "WS_ERROR";
	public static final String ERROR_CALLING_WEB_SERVICE_MESSAGE = "Error calling web service: ";
	public static final String ERROR_CALLING_DATABASE_DAO_STATUS = "DAO_ERROR";
	public static final String ERROR_CALLING_DATABASE_DAO_MESSAGE = "Error executing DAO call: ";
	public static final String ERROR_DATABASE_INSERT_ATTACHEMNT_STATUS = "ERROR_INSERTING_UPDATING_ATTACHMENT";
	public static final String ERROR_DATABASE_INSERT_ATTACHEMNT_MESSAGE = "Error inserting or editing attachment with message: ";
	public static final String ERROR_DATABASE_DELETE_ATTACHEMNT_STATUS = "ERROR_DELETING_ATTACHMENT";
	public static final String ERROR_DATABASE_DELETE_ATTACHEMNT_MESSAGE = "Error deleting attachment with message: ";
	public static final String ERROR_DATABASE_ATTACHEMNT_FOLDER_STATUS = "ERROR_ASSOCIATING_ATTACHMENT_FOLDER";
	public static final String ERROR_DATABASE_ATTACHEMNT_FOLDER_MESSAGE = "Error associating the attachment with the folder, the folder doesn't exist.";
	public static final String SESSION_EXPIRED_STATUS = "INVALID_SESSION_ID";

	public static final String FILE_CLOSING_ERROR_STATUS = "FILE_CLOSING_ERROR";
	public static final String FILE_ERROR_STATUS = "FILE_ERROR";
	public static final String FILE_ERROR_MESSAGE = "Error in file handling: ";

	public static final String CONTACT_SF_IMAGE_FIELD_NAME = "Picture";
	public static final String SALE_EXECUTION_SF_OBJECT_NAME = "MCP_Form";
	public static final String CONTACT_SF_OBJECT_NAME = "contact";
	public static final String ACTIONLOG_SF_OBJECT_NAME = "Action_Log";
	public static final String SALE_EXECUTION_SF_LANDMARK_IMAGE_FIELD_NAME = "Landmark_Picture";
	public static final String SALE_EXECUTION_SF_PICTURE_IMAGE_FIELD_NAME = "Picture";
	public static final String SALE_EXECUTION_SF_SS_IMAGE_FIELD_NAME = "SSPicture";
	public static final String CONTACTSaleEx_IMAGE_FIELD_NAME = "CoPicture";
	public static final String ACTIONLOG_SF_IMAGE_FIELD_NAME = "picture";
    public static final String ACTIONLOG_SF_IMAGE_1_FIELD_NAME = "picture1";
    public static final String ACTIONLOG_SF_IMAGE_2_FIELD_NAME = "picture2";
    public static final String ACTIONLOG_SF_IMAGE_3_FIELD_NAME = "picture3";
    public static final String ACTIONLOG_SF_IMAGE_4_FIELD_NAME = "picture4";

	public static final String FRAGMENT_ACCOUNT_TAG = "account_fragment";
	public static final String FRAGMENT_CONTACT_TAG = "contact_fragment";
	public static final String FRAGMENT_FEEDBACK_TAG = "feedback_fragment";
	public static final String FRAGMENT_PREORDER_TAG = "preorder_fragment";
	public static final String FRAGMENT_PREVIOUS_SALE_TAG = "previous_sale_fragment";
	public static final String FRAGMENT_SALE_EXECUTION_TAG = "sale_execution_fragment";
	public static final String FRAGMENT_CAMERA_TAG = "camera_fragment";
	public static final String FRAGMENT_CAMERA_PREVIEW_TAG = "camera_preview_fragment";
	public static final String FRAGMENT_LAST_COMPETITOR_MARKET_INFO_TAG = "last_competitor_market_info_fragment";
	public static final String FRAGMENT_SHOP_SIGN_TAG = "shop_signt_fragment";
	public static final String FRAGMENT_OUTSTANDING_FEEDBACK_LIST = "outstanding_feedback_list_fragment";
	public static final String FRAGMENT_OUTSTANDING_FEEDBACK_DETAIL = "outstanding_feedback_detail_fragment";

	public static final String OBJECT_SERIALIZABLE_KEY = "com.holcim.altimetrik.android.serializable.key";
	public static final String OBJECT_LIST_SERIALIZABLE_KEY = "competitorMarketingList";

	public static final int ACTIVITY_RESULT_CODE_COMP_MARK_OK = 0;
	public static final int ACTIVITY_REQUEST_CODE_COMP_MARK = 1;
	public static final int ACTIVITY_RESULT_CODE_ACTION_LOG_OK = 2;
	public static final int ACTIVITY_REQUEST_CODE_ACTION_LOG = 3;
	public static final int ACTIVITY_RESULT_CODE_PRE_ORDER_OK = 4;
	public static final int ACTIVITY_REQUEST_PRE_ORDER_LOG = 5;
	public static final int ACTIVITY_RESULT_CODE_CAMERA_OK = 6;
	public static final int ACTIVITY_RESULT_CODE_CAMERA_CANCEL = 8;
	public static final int ACTIVITY_REQUEST_CAMERA_LOG = 7;

	public static final String DEFAULT_NATIONALITY = "Indonesian";

	public static final String TEMPORAL_FILE_NAME = "tempPhoto.jpg";
	public static final String CONTACTSaleEx_TEMPORAL_FILE_NAME = "contactSaleExTempPhoto.jpg";
	public static final String CONTACT_TEMPORAL_FILE_NAME = "contactTempPhoto.jpg";
	public static final String LANDMARK_TEMPORAL_FILE_NAME = "landmarkTempPhoto.jpg";
	public static final String ACTIONLOG_TEMPORAL_FILE_NAME_PART1 = "actionLogTempPhoto";
	public static final String ACTIONLOG_TEMPORAL_FILE_NAME_PART2 = ".jpg";

	public static final String PREORDER_UNIT_NOT_NEED_TO_KNOW_PRODUCT = "Ton";
	public static final String SALEEXECUTION_STATUS_COMPLETED = "Completed";
	public static final String SALEEXECUTION_STATUS_RESCHEDULED = "Rescheduled";
	public static final String SALEEXECUTION_STATUS_CANCELED = "Canceled";
	public static final String SALEEXECUTION_STATUS_PLANNED = "Planned";
	
	//Types of errors in sync
	public static final String UPLOAD_CONTACT_SYNC_ERROR = "Some Contacts will not be uploaded."; 
	public static final String UPLOAD_TELESALE_SYNC_ERROR = "Some Tele Sales will not be uploaded.";
	public static final String UPLOAD_SALEEXECUTION_SYNC_ERROR = "Some Sale Executions will not be uploaded.";
	public static final String UPLOAD_COMPETITOR_MARKETING_SYNC_ERROR = "Some Competitors marketing will not be uploaded.";
	public static final String UPLOAD_ACTION_LOG_SYNC_ERROR = "Some Actions Log will not be uploaded."; 
	public static final String UPLOAD_PREORDER_SYNC_ERROR = "Some Pre-orders will not be uploaded."; 
	public static final String DOWNLOAD_CONTACT_SYNC_ERROR = "Some Contacts will not be download."; 
	public static final String DOWNLOAD_TELESALE_SYNC_ERROR = "Some Tele Sales will not be download.";
	public static final String DOWNLOAD_SALEEXECUTION_SYNC_ERROR = "Some Sale Executions will not be download.";
	public static final String DOWNLOAD_COMPETITOR_MARKETING_SYNC_ERROR = "Some Competitors marketing will not be download.";
	public static final String DOWNLOAD_ACTION_LOG_SYNC_ERROR = "Some actions log will not be download.";
	public static final String DOWNLOAD_PREORDER_SYNC_ERROR = "Some Pre-orders will not be download.";
	public static final String DOWNLOAD_POSPECT_SYNC_ERROR = "Some Prospects will not be download.";
	public static final String DOWNLOAD_ACCOUNT_SYNC_ERROR = "Some Accounts will not be download.";
	public static final String DOWNLOAD_OUTSTANDING_FEEDBACK_SYNC_ERROR = "Some Outstanding Feedbacks will not be download.";
	public static final String SYNC_ERROR ="We have encountered a problem while syncing. Please Sync again...";

	public static final String TSO = "TSO";
	public static final String HOLCIM = "holcim";
}
