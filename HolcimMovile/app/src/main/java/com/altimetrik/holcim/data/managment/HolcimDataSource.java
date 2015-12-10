package com.altimetrik.holcim.data.managment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;

import com.holcim.altimetrik.android.application.HolcimApp;
import com.holcim.altimetrik.android.exception.HolcimError;
import com.holcim.altimetrik.android.exception.HolcimException;
import com.holcim.altimetrik.android.model.Account;
import com.holcim.altimetrik.android.model.ActionsLog;
import com.holcim.altimetrik.android.model.Competitor;
import com.holcim.altimetrik.android.model.CompetitorMarketing;
import com.holcim.altimetrik.android.model.Contact;
import com.holcim.altimetrik.android.model.ContactDao.Properties;
import com.holcim.altimetrik.android.model.OutstandingFeedback;
import com.holcim.altimetrik.android.model.PreOrder;
import com.holcim.altimetrik.android.model.Prospect;
import com.holcim.altimetrik.android.model.SaleExecution;
import com.holcim.altimetrik.android.model.TeleSale;
import com.holcim.altimetrik.android.utilities.AltimetrikException;
import com.holcim.altimetrik.android.utilities.AltimetrikFileHandler;
import com.holcim.altimetrik.android.utilities.HolcimConsts;
import com.holcim.altimetrik.android.utilities.HolcimUtility;
import com.holcim.altimetrik.android.utilities.SFUser;

/**
 * Class to abstract the data access
 * 
 * @author labreu@altimetrik.com
 * 
 */
public class HolcimDataSource {

	/**
	 * Convert a JSON object to SFUser object that contains all user attributes
	 * 
	 * @param pJson
	 * @return SFUser object
	 */
	public static SFUser JSONToUser(String pJson) throws HolcimException {
		try {
			SFUser user = new SFUser();

			JSONObject jObject = new JSONObject(pJson);

			if (jObject.has("username")) {
				user.set_user_name(jObject.getString("username"));
			} else if (jObject.has("nick_name")) {
				user.set_user_nick(jObject.getString("nick_name"));
			} else if (jObject.has("display_name")) {
				user.set_user_display_name(jObject.getString("display_name"));
			} else if (jObject.has("email")) {
				user.set_user_email(jObject.getString("email"));
			} else {
				return null;
			}

			return user;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static ArrayList<Prospect> JSONToProspectList(String pJson)
			throws HolcimException {
		try {
			ArrayList<Prospect> prospects = new ArrayList<Prospect>();
			JSONArray jProspectArray = new JSONArray(pJson);
			for (int i = 0; i < jProspectArray.length(); i++) {
				prospects.add(HolcimDataSource.JSONToProspect(jProspectArray
						.getString(i)));
			}
			return prospects;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static Prospect JSONToProspect(String pJson) throws HolcimException {
		try {
			Prospect prospect = new Prospect();
			JSONObject jObject = new JSONObject(pJson);
			if (jObject.has("Name")) {
				prospect.setName(jObject.getString("Name"));
			}
			if (jObject.has("Id")) {
				prospect.setSalesforceId(jObject.getString("Id"));
			}
			if (jObject.has("MobileAddress")) {
				prospect.setMobileAddress(jObject.getString("MobileAddress"));
			}

			return prospect;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static ArrayList<Account> JSONToAccountList(String pJson)
			throws HolcimException {
		try {
			ArrayList<Account> accounts = new ArrayList<Account>();
			JSONArray jProspectArray = new JSONArray(pJson);
			for (int i = 0; i < jProspectArray.length(); i++) {
				accounts.add(HolcimDataSource.JSONToAccount(jProspectArray
						.getString(i)));
			}
			return accounts;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static String JSONToProfile(String pJson) throws HolcimException {
		try {
			String profile = null;
			JSONArray jProfileArray = new JSONArray(pJson);
			JSONObject jObject = new JSONObject(jProfileArray.getString(0));
			if (jObject.has("Name")) {
				profile = jObject.getString("Name");
			}
			return profile;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static Account JSONToAccount(String pJson) throws HolcimException {
		try {
			Account account = new Account();
			JSONObject jObject = new JSONObject(pJson);
			if (jObject.has("Name")) {
				account.setName(jObject.getString("Name"));
			}
			if (jObject.has("Id")) {
				account.setSalesforceId(jObject.getString("Id"));
			}
			if (jObject.has("MobileAddress")) {
				account.setMobileAddress(jObject.getString("MobileAddress"));
			}

			return account;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static ArrayList<Competitor> JSONToCompetitorList(String pJson)
			throws HolcimException {
		try {
			ArrayList<Competitor> competitors = new ArrayList<Competitor>();
			JSONArray jProspectArray = new JSONArray(pJson);
			for (int i = 0; i < jProspectArray.length(); i++) {
				competitors.add(HolcimDataSource
						.JSONToCompetitor(jProspectArray.getString(i)));
			}
			return competitors;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static Competitor JSONToCompetitor(String pJson)
			throws HolcimException {
		try {
			Competitor competitor = new Competitor();
			JSONObject jObject = new JSONObject(pJson);
			if (jObject.has("Name")) {
				competitor.setName(jObject.getString("Name"));
			}
			if (jObject.has("Id")) {
				competitor.setSalesforceId(jObject.getString("Id"));
			}

			return competitor;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static ArrayList<Contact> JSONToContactList(String pJson)
			throws HolcimException {
		try {
			ArrayList<Contact> contacts = new ArrayList<Contact>();
			JSONArray jProspectArray = new JSONArray(pJson);
			for (int i = 0; i < jProspectArray.length(); i++) {
				contacts.add(HolcimDataSource.JSONToContact(jProspectArray
						.getString(i)));
			}
			return contacts;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static Contact JSONToContact(String pJson) throws HolcimException {
		try {
			Contact contact = new Contact();
			JSONObject jObject = new JSONObject(pJson);
			if (jObject.has("FirstName")) {
				contact.setFirstName(jObject.getString("FirstName"));
			}
			if (jObject.has("Id")) {
				contact.setSalesforceId(jObject.getString("Id"));
			}
			if (jObject.has("LastName")) {
				contact.setLastName(jObject.getString("LastName"));
			}
			if (jObject.has("Sex__c")) {
				contact.setGender(jObject.getString("Sex__c"));
			}
			if (jObject.has("Birthdate")) {
				contact.setBirthDate(jObject.getString("Birthdate"));
			}
			if (jObject.has("Phone")) {
				contact.setPhone(jObject.getString("Phone"));
			}
			if (jObject.has("MobilePhone")) {
				contact.setMobile(jObject.getString("MobilePhone"));
			}
			if (jObject.has("Extension__c")) {
				contact.setExtension(jObject.getString("Extension__c"));
			}
			if (jObject.has("Fax")) {
				contact.setFax(jObject.getString("Fax"));
			}
			if (jObject.has("Email")) {
				contact.setEmail(jObject.getString("Email"));
			}
			if (jObject.has("MailingStreet")) {
				contact.setMailingStreet(jObject.getString("MailingStreet"));
			}
			if (jObject.has("MailingCity")) {
				contact.setMailingCity(jObject.getString("MailingCity"));
			}
			if (jObject.has("MailingState")) {
				contact.setMailingStateProvince(jObject
						.getString("MailingState"));
			}
			if (jObject.has("MailingPostalCode")) {
				contact.setMailingPostalCode(jObject
						.getString("MailingPostalCode"));
			}
			if (jObject.has("MailingCountry")) {
				contact.setMailingCountry(jObject.getString("MailingCountry"));
			}
			if (jObject.has("Nationality__c")) {
				contact.setNationality(jObject.getString("Nationality__c"));
			}
			if (jObject.has("Account")) {
				JSONObject jAccount = jObject.getJSONObject("Account");
				if (jAccount.has("Name")) {
					contact.setAccountName(jAccount.getString("Name"));
				}
				if (jAccount.has("Id")) {
					contact.setAccountId(jAccount.getString("Id"));
				}
				if (jAccount.has("Retailer_ID__c")) {
					contact.setRetailerID(jAccount.getString("Retailer_ID__c"));
				}
			}
			if (jObject.has("Picturemd5__c")) {
				contact.setPicturemd5(jObject.getString("Picturemd5__c"));
			}
			return contact;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static ArrayList<CompetitorMarketing> JSONToCompetitorMarketingList(
			String pJson) throws HolcimException {
		try {
			ArrayList<CompetitorMarketing> compMarketings = new ArrayList<CompetitorMarketing>();
			JSONArray jProspectArray = new JSONArray(pJson);
			for (int i = 0; i < jProspectArray.length(); i++) {
				compMarketings
						.add(HolcimDataSource
								.JSONToCompetitorMarketing(jProspectArray
										.getString(i)));
			}
			return compMarketings;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static CompetitorMarketing JSONToCompetitorMarketing(String pJson)
			throws HolcimException {
		try {
			CompetitorMarketing compMarketing = new CompetitorMarketing();
			JSONObject jObject = new JSONObject(pJson);
			if (jObject.has("Id")) {
				compMarketing.setSalesforceId(jObject.getString("Id"));
			}
			if (jObject.has("Buying_Price__c")) {
				compMarketing.setBuyingPrice(jObject
						.getDouble("Buying_Price__c"));
			}
			if (jObject.has("Competitor_Margin_with_HIL__c")) {
				compMarketing.setCompetitorMarginHIL(jObject
						.getDouble("Competitor_Margin_with_HIL__c"));
			}
			if (jObject.has("Competitor_Name__c")) {
				compMarketing.setCompetitorName(jObject
						.getString("Competitor_Name__c"));
			}
			if (jObject.has("Experience_with_Lead_Competitors__c")) {
				compMarketing.setExperienceLeadCompetitors(jObject
						.getString("Experience_with_Lead_Competitors__c"));
			}
			if (jObject.has("Inventory__c")) {
				compMarketing.setInventory(jObject.getInt("Inventory__c"));
			}
			if (jObject.has("Issue__c")) {
				compMarketing.setIssue(jObject.getString("Issue__c"));
			}
			if (jObject.has("Last_Month_s_Competitor_Buying_Volume__c")) {
				compMarketing.setLastMonthCompetitorBuyingVolume(jObject
						.getInt("Last_Month_s_Competitor_Buying_Volume__c"));
			}
			if (jObject.has("Program_End_Date__c")) {
				compMarketing.setProgramEndDate(jObject
						.getString("Program_End_Date__c"));
			}
			if (jObject.has("Program_Start_Date__c")) {
				compMarketing.setProgramStartDate(jObject
						.getString("Program_Start_Date__c"));
			}
			if (jObject.has("Program__c")) {
				compMarketing.setProgram(jObject.getString("Program__c"));
			}
			if (jObject.has("Promotion_End_Date__c")) {
				compMarketing.setPromotionEndDate(jObject
						.getString("Promotion_End_Date__c"));
			}
			if (jObject.has("Promotion_Start_Date__c")) {
				compMarketing.setPromotionStartDate(jObject
						.getString("Promotion_Start_Date__c"));
			}
			if (jObject.has("Promotion__c")) {
				compMarketing.setPromotion(jObject.getString("Promotion__c"));
			}
			if (jObject.has("Reason_for_Buying_from_Lead_Competitors__c")) {
				compMarketing
						.setReasonBuyingLeadCompetitors(jObject
								.getString("Reason_for_Buying_from_Lead_Competitors__c"));
			}
			if (jObject.has("Reason_For_Unsatisfied_Experience__c")) {
				compMarketing.setReasonForUnsatisfiedExperience(jObject
						.getString("Reason_For_Unsatisfied_Experience__c"));
			}
			if (jObject.has("Selling_Price__c")) {
				compMarketing.setSellingPrice(jObject
						.getDouble("Selling_Price__c"));
			}
			if (jObject.has("Specify_Other_Reason__c")) {
				compMarketing.setOtherReason(jObject
						.getString("Specify_Other_Reason__c"));
			}
			if (jObject.has("Competitor__c")) {
				compMarketing
						.setCompetitorId(HolcimDataSource.getCompetitorBySFId(
								jObject.getString("Competitor__c")).getId());
			}
			compMarketing.setIsEdited(false);
			return compMarketing;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static ArrayList<ActionsLog> JSONToActionsLogList(String pJson)
			throws HolcimException {
		try {
			ArrayList<ActionsLog> actionsLogs = new ArrayList<ActionsLog>();
			JSONArray jProspectArray = new JSONArray(pJson);
			for (int i = 0; i < jProspectArray.length(); i++) {
				actionsLogs.add(HolcimDataSource
						.JSONToActionsLog(jProspectArray.getString(i)));
			}
			return actionsLogs;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static ActionsLog JSONToActionsLog(String pJson)
			throws HolcimException {
		try {
			ActionsLog actionLog = new ActionsLog();
			JSONObject jObject = new JSONObject(pJson);
			if (jObject.has("Id")) {
				actionLog.setSalesforceId(jObject.getString("Id"));
			}
			if (jObject.has("Name")) {
				actionLog.setActionLogNumber(jObject.getString("Name"));
			}
			// if (jObject.has("Action_to_be_Taken__c")) {
			// actionLog.setActionToBeTaken(jObject.getString("Action_to_be_Taken__c"));
			// }
			// if (jObject.has("Comment_Category__c")) {
			// actionLog.setCommentCategory(jObject.getString("Comment_Category__c"));
			// }
			// if (jObject.has("Comment_Date__c")) {
			// actionLog.setCommentDate(jObject.getString("Comment_Date__c"));
			// }
			// if (jObject.has("Comment_Type__c")) {
			// actionLog.setCommentType(jObject.getString("Comment_Type__c"));
			// }
			// if (jObject.has("Date_Deadline__c")) {
			// actionLog.setDateDeadline(jObject.getString("Date_Deadline__c"));
			// }
			// if (jObject.has("Date_Escalated__c")) {
			// actionLog.setDateEscalated(jObject.getString("Date_Escalated__c"));
			// }
			if (jObject.has("Description__c")) {
				actionLog.setDescription(jObject.getString("Description__c"));
			}
			// if (jObject.has("Number__c")) {
			// actionLog.setNumber1(jObject.getString("Number__c"));
			// }
			// if (jObject.has("PIC_Name__c")) {
			// actionLog.setPCIName(jObject.getString("PIC_Name__c"));
			// }
			// if (jObject.has("PIC_Position__c")) {
			// actionLog.setPCIPosition(jObject.getString("PIC_Position__c"));
			// }
			// if (jObject.has("Reason_for_Pending_Action__c")) {
			// actionLog.setReasonForPendingAction(jObject.getString("Reason_for_Pending_Action__c"));
			// }
			// if (jObject.has("Retailer_ID__c")) {
			// actionLog.setRetailer(jObject.getString("Retailer_ID__c"));
			// }
			// if (jObject.has("Retailer_Name__c")) {
			// actionLog.setRetailerName(jObject.getString("Retailer_Name__c"));
			// }
			// if (jObject.has("Retailer_Segmentation__c")) {
			// actionLog.setRetailerSegmentation(jObject.getString("Retailer_Segmentation__c"));
			// }
			// if (jObject.has("Retailer_Type__c")) {
			// actionLog.setRetailerType(jObject.getString("Retailer_Type__c"));
			// }
			// if (jObject.has("Root_Cause_Analysis__c")) {
			// actionLog.setRootCauseAnalisys(jObject.getString("Root_Cause_Analysis__c"));
			// }
			if (jObject.has("Status__c")) {
				actionLog.setStatus(jObject.getString("Status__c"));
			}
			if (jObject.has("Complaint__c")) {
				actionLog.setComplaint(jObject.getBoolean("Complaint__c"));
			}
			if (jObject.has("Category__c")) {
				actionLog.setCategory(jObject.getString("Category__c"));
			}
			if (jObject.has("PictureMd5__c")) {
				actionLog.setPicture(jObject.getString("PictureMd5__c"));
			}
			if (jObject.has("Picture_Description__c")) {
				actionLog.setPictureDescription(jObject
						.getString("Picture_Description__c"));
			}
			// if (jObject.has("Street_Address__c")) {
			// actionLog.setStreetAddress(jObject.getString("Street_Address__c"));
			// }
			// if (jObject.has("Targeted_Outcome__c")) {
			// actionLog.setTargetedOutcome(jObject.getString("Targeted_Outcome__c"));
			// }
			actionLog.setIsEdited(false);
			return actionLog;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static ArrayList<OutstandingFeedback> JSONToOutstandingFeedbackList(
			String pJson) throws HolcimException {
		try {
			ArrayList<OutstandingFeedback> outstandingFeedbacks = new ArrayList<OutstandingFeedback>();
			JSONArray jProspectArray = new JSONArray(pJson);
			for (int i = 0; i < jProspectArray.length(); i++) {
				OutstandingFeedback of = HolcimDataSource
						.JSONToOutstandingFeedback(jProspectArray.getString(i));
				if ((of.getSalesforceAccountId() != null && !of
						.getSalesforceAccountId().equals(""))
						|| (of.getSalesforceProspectId() != null && !of
								.getSalesforceProspectId().equals(""))) {
					outstandingFeedbacks.add(of);
				}
			}
			return outstandingFeedbacks;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static OutstandingFeedback JSONToOutstandingFeedback(String pJson)
			throws HolcimException {
		try {
			OutstandingFeedback outstandingFeedback = new OutstandingFeedback();
			JSONObject jObject = new JSONObject(pJson);
			if (jObject.has("Id")) {
				outstandingFeedback.setSalesforceId(jObject.getString("Id"));
			}
			if (jObject.has("Description__c")) {
				outstandingFeedback.setDescription(jObject
						.getString("Description__c"));
			}
			if (jObject.has("Status__c")) {
				outstandingFeedback.setStatus(jObject.getString("Status__c"));
			}
			if (jObject.has("Complaint__c")) {
				outstandingFeedback.setComplaint(jObject
						.getBoolean("Complaint__c"));
			}
			if (jObject.has("Category__c")) {
				outstandingFeedback.setCategory(jObject
						.getString("Category__c"));
			}
			if (jObject.has("Account__c")) {
				outstandingFeedback.setSalesforceAccountId(jObject
						.getString("Account__c"));
			}
			if (jObject.has("Prospect__c")) {
				outstandingFeedback.setSalesforceProspectId(jObject
						.getString("Prospect__c"));
			}
			return outstandingFeedback;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static ArrayList<PreOrder> JSONToPreOrderList(String pJson)
			throws HolcimException {
		try {
			ArrayList<PreOrder> preOrders = new ArrayList<PreOrder>();
			JSONArray jProspectArray = new JSONArray(pJson);
			for (int i = 0; i < jProspectArray.length(); i++) {
				preOrders.add(HolcimDataSource.JSONToPreOrder(jProspectArray
						.getString(i)));
			}
			return preOrders;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static PreOrder JSONToPreOrder(String pJson) throws HolcimException {
		try {
			PreOrder preOrder = new PreOrder();
			JSONObject jObject = new JSONObject(pJson);
			if (jObject.has("Id")) {
				preOrder.setSalesforceId(jObject.getString("Id"));
			}
			if (jObject.has("Pre_order_Volume__c")) {
				preOrder.setPreOrderQuantity(jObject
						.getDouble("Pre_order_Volume__c"));
			}
			if (jObject.has("Preorder_Date__c")) {
				preOrder.setPreOrderDate(jObject.getString("Preorder_Date__c"));
			}
			if (jObject.has("Product__c")) {
				preOrder.setProduct(jObject.getString("Product__c"));
			}
			if (jObject.has("Reason_For_Not_Ordering__c")) {
				preOrder.setReasonForNotOrdering(jObject
						.getString("Reason_For_Not_Ordering__c"));
			}
			if (jObject.has("Unit__c")) {
				preOrder.setUnit(jObject.getString("Unit__c"));
			}
			preOrder.setIsEdited(false);
			return preOrder;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static ArrayList<SaleExecution> JSONToSaleExecutionList(String pJson)
			throws HolcimException {
		try {
			ArrayList<SaleExecution> saleExecutions = new ArrayList<SaleExecution>();
			JSONArray jProspectArray = new JSONArray(pJson);
			for (int i = 0; i < jProspectArray.length(); i++) {
				saleExecutions.add(HolcimDataSource
						.JSONToSaleExecution(jProspectArray.getString(i)));
			}
			return saleExecutions;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static SaleExecution JSONToSaleExecution(String pJson)
			throws HolcimException {
		try {
			SaleExecution saleExecution = new SaleExecution();
			JSONObject jObject = new JSONObject(pJson);
			if (jObject.has("Id")) {
				saleExecution.setSalesforceId(jObject.getString("Id"));
			}
			if (jObject.has("Academic_Title__c")) {
				saleExecution.setAcademicTitle(jObject
						.getString("Academic_Title__c"));
			}
			if (jObject.has("Account_Name__c")) {
				saleExecution.setAccountName(jObject
						.getString("Account_Name__c"));
			}
			if (jObject.has("Account_Type__c")) {
				saleExecution.setAccountType(jObject
						.getString("Account_Type__c"));
			}
			if (jObject.has("Account__c")) {
				Account account = HolcimDataSource.getAccountBySFId(jObject
						.getString("Account__c"));
				if (account != null) {
					saleExecution.setAccountId(account.getId());
				}
			}
			if (jObject.has("Action_Status__c")) {
				saleExecution.setActionStatus(jObject
						.getString("Action_Status__c"));
			}
			if (jObject.has("Action__c")) {
				saleExecution.setAction(jObject.getString("Action__c"));
			}
			if (jObject.has("Account_Record_Type__c")) {
				saleExecution.setAccountRecordType(jObject
						.getString("Account_Record_Type__c"));
			}
			if (jObject.has("Unplanned_visit_reason__c")) {
				saleExecution.setUnplannedVisitReason(jObject
						.getString("unplanned_visit_reason__c"));
			}
			if (jObject.has("Reason_For_cancelling_visit__c")) {
				saleExecution.setReasonForCancelling(jObject
						.getString("Reason_For_cancelling_visit__c"));
			}
			if (jObject.has("Actual_Visit_Date__c")) {
				saleExecution.setActualVisitDate(jObject
						.getString("Actual_Visit_Date__c"));
			}
			if (jObject.has("Availability_of_HIL_Shop_Sign__c")) {
				saleExecution.setAvailabilityOfHILShopSign(jObject
						.getString("Availability_of_HIL_Shop_Sign__c"));
			}
			if (jObject.has("Bank_Account_Branch_1__c")) {
				saleExecution.setBankAccountBranch1(jObject
						.getString("Bank_Account_Branch_1__c"));
			}
			if (jObject.has("Bank_Account_Branch_2__c")) {
				saleExecution.setBankAccountBranch2(jObject
						.getString("Bank_Account_Branch_2__c"));
			}
			if (jObject.has("Bank_Account_Branch_3__c")) {
				saleExecution.setBankAccountBranch3(jObject
						.getString("Bank_Account_Branch_3__c"));
			}
			if (jObject.has("Bank_Account_Name_1__c")) {
				saleExecution.setBankAccountName1(jObject
						.getString("Bank_Account_Name_1__c"));
			}
			if (jObject.has("Bank_Account_Name_2__c")) {
				saleExecution.setBankAccountName2(jObject
						.getString("Bank_Account_Name_2__c"));
			}
			if (jObject.has("Bank_Account_Name_3__c")) {
				saleExecution.setBankAccountName3(jObject
						.getString("Bank_Account_Name_3__c"));
			}
			if (jObject.has("Bank_Account_Number_1__c")) {
				saleExecution.setBankAccountNumber1(jObject
						.getString("Bank_Account_Number_1__c"));
			}
			if (jObject.has("Bank_Account_Number_2__c")) {
				saleExecution.setBankAccountNumber2(jObject
						.getString("Bank_Account_Number_2__c"));
			}
			if (jObject.has("Bank_Account_Number_3__c")) {
				saleExecution.setBankAccountNumber3(jObject
						.getString("Bank_Account_Number_3__c"));
			}
			if (jObject.has("Birthdate__c")) {
				saleExecution.setBirthDate(jObject.getString("Birthdate__c"));
			}
			if (jObject.has("Bad_Debt__c")) {
				saleExecution.setBadDebt(jObject.getBoolean("Bad_Debt__c"));
			}
			if (jObject.has("Business_Entity_Status__c")) {
				saleExecution.setBusinessEntityStatus(jObject
						.getString("Business_Entity_Status__c"));
			}
			if (jObject.has("Buy_Competitor_Name__c")) {
				saleExecution.setBuyCompetitorName(jObject
						.getString("Buy_Competitor_Name__c"));
			}
			if (jObject.has("Buying_Volume_Competitor_Name__c")) {
				saleExecution.setBuyingVolumeCompetitorName(jObject
						.getString("Buying_Volume_Competitor_Name__c"));
			}
			if (jObject.has("Call_Note__c")) {
				saleExecution.setCallNote(jObject.getString("Call_Note__c"));
			}
			if (jObject.has("Capacity_Current_Month_tonnes_month__c")) {
				saleExecution.setCapacityCurrentMonth(jObject
						.getDouble("Capacity_Current_Month_tonnes_month__c"));
			}
			if (jObject.has("Cleanness_Condition__c")) {
				saleExecution.setCleanessCondition(jObject
						.getString("Cleanness_Condition__c"));
			}
			if (jObject.has("Color_Condition__c")) {
				saleExecution.setColorCondition(jObject
						.getString("Color_Condition__c"));
			}
			if (jObject.has("Comments__c")) {
				saleExecution.setComments(jObject.getString("Comments__c"));
			}
			if (jObject.has("Company_Name__c")) {
				saleExecution.setCompanyName(jObject
						.getString("Company_Name__c"));
			}
			if (jObject.has("Competitor_Buying_Price__c")) {
				saleExecution.setLastBuyingPrice(jObject
						.getDouble("Competitor_Buying_Price__c"));
			}
			if (jObject.has("Competitor_Buying_Price_Date__c")) {
				saleExecution.setLastBuyingPriceDate(jObject
						.getString("Competitor_Buying_Price_Date__c"));
			}
			if (jObject.has("Competitor_Selling_Price__c")) {
				saleExecution.setLastSellingPrice(jObject
						.getDouble("Competitor_Selling_Price__c"));
			}
			if (jObject.has("Competitor_Selling_Price_Date__c")) {
				saleExecution.setLastSellingPriceDate(jObject
						.getString("Competitor_Selling_Price_Date__c"));
			}
			if (jObject.has("Competitor_Inventory_Price__c")) {
				saleExecution.setLastInventory(jObject
						.getDouble("Competitor_Inventory_Price__c"));
			}
			if (jObject.has("Competitor_Inventory_Price_Date__c")) {
				saleExecution.setLastInventoryDate(jObject
						.getString("Competitor_Inventory_Price_Date__c"));
			}
			if (jObject.has("Competitor_Buying_Volume__c")) {
				saleExecution.setLastBuyingVolume(jObject
						.getDouble("Competitor_Buying_Volume__c"));
			}
			if (jObject.has("Competitor_Buying_Volume_Date__c")) {
				saleExecution.setLastBuyingVolumeDate(jObject
						.getString("Competitor_Buying_Volume_Date__c"));
			}
			if (jObject.has("Completed__c")) {
				saleExecution.setCompleted(jObject.getBoolean("Completed__c"));
			}
			if (jObject.has("Condition_of_Shop_Sign__c")) {
				saleExecution.setConditionOfShopSign(jObject
						.getString("Condition_of_Shop_Sign__c"));
			}
			if (jObject.has("Contact_Email_1__c")) {
				saleExecution.setContactEmail1(jObject
						.getString("Contact_Email_1__c"));
			}
			if (jObject.has("Contact_ID__c")) {
				saleExecution.setContactId(jObject.getString("Contact_ID__c"));
			}
			if (jObject.has("Contact_Mobile_1__c")) {
				saleExecution.setContactMobile1(jObject
						.getString("Contact_Mobile_1__c"));
			}
			if (jObject.has("Contact_Mobile_2__c")) {
				saleExecution.setContactMobile2(jObject
						.getString("Contact_Mobile_2__c"));
			}
			if (jObject.has("Contact_Name__c")) {
				saleExecution.setContactName(jObject
						.getString("Contact_Name__c"));
			}
			if (jObject.has("Contact_Status__c")) {
				saleExecution.setContactStatus(jObject
						.getString("Contact_Status__c"));
			}
			if (jObject.has("Customer_Class__c")) {
				saleExecution.setCustomerClass(jObject
						.getString("Customer_Class__c"));
			}
			if (jObject.has("Delivery_Remark__c")) {
				saleExecution.setDeliveryRemark(jObject
						.getString("Delivery_Remark__c"));
			}
			if (jObject.has("District__c")) {
				saleExecution.setDistrict(jObject.getString("District__c"));
			}
			if (jObject.has("EightT_Truck_Armada__c")) {
				saleExecution.setEightTTruckArmada(jObject
						.getString("EightT_Truck_Armada__c"));
			}
			if (jObject.has("Email_1__c")) {
				saleExecution.setEmail1(jObject.getString("Email_1__c"));
			}
			if (jObject.has("Event__c")) {
				saleExecution.setEvent(jObject.getString("Event__c"));
			}
			if (jObject.has("Extension_1__c")) {
				saleExecution
						.setExtension1(jObject.getString("Extension_1__c"));
			}
			if (jObject.has("Experience_with_Lead_Competitor__c")) {
				saleExecution.setExperienceWithLeadCompetitor(jObject
						.getString("Experience_with_Lead_Competitor__c"));
			}
			if (jObject.has("Favourite_Activities__c")) {
				saleExecution.setFavouriteActivities(jObject
						.getString("Favourite_Activities__c"));
			}
			if (jObject.has("Favourite_Drink__c")) {
				saleExecution.setFavouriteDrink(jObject
						.getString("Favourite_Drink__c"));
			}
			if (jObject.has("Favourite_Food__c")) {
				saleExecution.setFavouriteFood(jObject
						.getString("Favourite_Food__c"));
			}
			if (jObject.has("Favourite_Sports__c")) {
				saleExecution.setFavouriteSports(jObject
						.getString("Favourite_Sports__c"));
			}
			if (jObject.has("Fax_1__c")) {
				saleExecution.setFax1(jObject.getString("Fax_1__c"));
			}
			if (jObject.has("First_Name__c")) {
				saleExecution.setFirstName(jObject.getString("First_Name__c"));
			}
			if (jObject.has("Gender__c")) {
				saleExecution.setGender(jObject.getString("Gender__c"));
			}
			if (jObject.has("HIL_Buying_Price_Date__c")) {
				saleExecution.setHilBuyingPriceDate(jObject
						.getString("HIL_Buying_Price_Date__c"));
			}
			if (jObject.has("HIL_Buying_Price__c")) {
				saleExecution.setHilBuyingPrice(jObject
						.getDouble("HIL_Buying_Price__c"));
			}
			if (jObject.has("HIL_Buying_Volume_Date__c")) {
				saleExecution.setHilBuyingVolumeDate(jObject
						.getString("HIL_Buying_Volume_Date__c"));
			}
			if (jObject.has("HIL_Buying_Volume__c")) {
				saleExecution.setHilBuyingVolume(jObject
						.getDouble("HIL_Buying_Volume__c"));
			}
			if (jObject.has("HIL_Inventory_Price_Date__c")) {
				saleExecution.setHilBuyingInventoryPriceDate(jObject
						.getString("HIL_Inventory_Price_Date__c"));
			}
			if (jObject.has("HIL_Inventory_Price__c")) {
				saleExecution.setHilBuyingInventoryPrice(jObject
						.getDouble("HIL_Inventory_Price__c"));
			}
			if (jObject.has("HIL_Selling_Price_Date__c")) {
				saleExecution.setHilBuyingSellingPriceDate(jObject
						.getString("HIL_Selling_Price_Date__c"));
			}
			if (jObject.has("HIL_Selling_Price__c")) {
				saleExecution.setHilBuyingSellingPrice(jObject
						.getDouble("HIL_Selling_Price__c"));
			}
			if (jObject.has("HIL_SoW_Current_Month__c")) {
				saleExecution.setHilSoWCurrentMonth(jObject
						.getDouble("HIL_SoW_Current_Month__c"));
			}
			if (jObject.has("HMHIL_Buying_Price__c")) {
				saleExecution.setHmhilBuyingPrice(jObject
						.getDouble("HMHIL_Buying_Price__c"));
			}
			if (jObject.has("HMHIL_Inventory__c")) {
				saleExecution.setHmhilInventory(jObject
						.getDouble("HMHIL_Inventory__c"));
			}
			if (jObject.has("HMHIL_Selling_Price__c")) {
				saleExecution.setHmhilSellingPrice(jObject
						.getDouble("HMHIL_Selling_Price__c"));
			}
			if (jObject.has("HVL_Stock_Status__c")) {
				saleExecution.setHvlStockStatus(jObject
						.getBoolean("HVL_Stock_Status__c"));
			}
			if (jObject.has("HVL_Stock_Volume_Ton_Month__c")) {
				saleExecution.setHvlStockVolumeTonMonth(jObject
						.getDouble("HVL_Stock_Volume_Ton_Month__c"));
			}
			if (jObject.has("Hobbies__c")) {
				saleExecution.setHobbies(jObject.getString("Hobbies__c"));
			}
			if (jObject.has("Holcimeter_Balance__c")) {
				saleExecution.setHolcimeterBalance(jObject
						.getDouble("Holcimeter_Balance__c"));
			}
			if (jObject.has("Holcimeter_Bonus__c")) {
				saleExecution.setHolcimeterBonus(jObject
						.getDouble("Holcimeter_Bonus__c"));
			}
			if (jObject.has("Holcimeter_Total__c")) {
				saleExecution.setHolcimeterTotal(jObject
						.getDouble("Holcimeter_Total__c"));
			}
			if (jObject.has("Inspection_Date_Time__c")) {
				saleExecution.setInspectionDate(jObject
						.getString("Inspection_Date_Time__c"));
			}
			if (jObject.has("Inv_Competitor_Name__c")) {
				saleExecution.setInvCompetitorName(jObject
						.getString("Inv_Competitor_Name__c"));
			}
			if (jObject.has("Is_DSR__c")) {
				saleExecution.setIsDSR(jObject.getBoolean("Is_DSR__c"));
			}
			if (jObject.has("Jelajah_Holcim_ID__c")) {
				saleExecution.setJelajahHolcimID(jObject
						.getString("Jelajah_Holcim_ID__c"));
			}
			if (jObject.has("Jelajah_Holcim_Membership__c")) {
				saleExecution.setJelajahHolcimMembership(jObject
						.getBoolean("Jelajah_Holcim_Membership__c"));
			}
			if (jObject.has("Job_Title__c")) {
				saleExecution.setJobTitle(jObject.getString("Job_Title__c"));
			}
			if (jObject.has("Kecamatan__c")) {
				saleExecution.setKecamatan(jObject.getString("Kecamatan__c"));
			}
			if (jObject.has("Kelurahan__c")) {
				saleExecution.setKelurahan(jObject.getString("Kelurahan__c"));
			}
			if (jObject.has("Key_Retailer__c")) {
				saleExecution.setKeyRetailer(jObject
						.getBoolean("Key_Retailer__c"));
			}
			if (jObject.has("Kota__c")) {
				saleExecution.setKota(jObject.getString("Kota__c"));
			}
			if (jObject.has("Land_Status__c")) {
				saleExecution
						.setLandStatus(jObject.getString("Land_Status__c"));
			}
			if (jObject.has("Landmark_Description__c")) {
				saleExecution.setLandmarkDescription(jObject
						.getString("Landmark_Description__c"));
			}
			if (jObject.has("Landmark_Picturemd5__c")) {
				saleExecution.setLandmarkPictureMD5(jObject
						.getString("Landmark_Picturemd5__c"));
			}
			if (jObject.has("Last_Actual_Visit_Date__c")) {
				saleExecution.setActualVisitDate(jObject
						.getString("Last_Actual_Visit_Date__c"));
			}
			if (jObject.has("Last_Actual_Visit_Date__c")) {
				saleExecution.setActualVisitDate(jObject
						.getString("Last_Actual_Visit_Date__c"));
			}
			if (jObject.has("Last_Dispatch_Date__c")) {
				saleExecution.setLastDispatchDate(jObject
						.getString("Last_Dispatch_Date__c"));
			}
			if (jObject.has("Last_Month_s_HIL_Buying_Volume__c")) {
				saleExecution.setLastHILBuyingVolume(jObject
						.getDouble("Last_Month_s_HIL_Buying_Volume__c"));
			}
			if (jObject.has("Last_Name__c")) {
				saleExecution.setLastName(jObject.getString("Last_Name__c"));
			}
			if (jObject.has("Next_Planned_Visit_Date__c")) {
				saleExecution.setNextPlannedVisitDate(jObject
						.getString("Next_Planned_Visit_Date__c"));
			}
			if (jObject.has("Last_Planned_Visit_Date__c")) {
				saleExecution.setLastPlannedVisitDate(jObject
						.getString("Last_Planned_Visit_Date__c"));
			}
			if (jObject.has("Last_Reward_Redeemed__c")) {
				saleExecution.setLastRewardRedeemed(jObject
						.getString("Last_Reward_Redeemed__c"));
			}
			if (jObject.has("Latitude__c")) {
				saleExecution.setLatitude(jObject.getString("Latitude__c"));
			}
			if (jObject.has("Longitude__c")) {
				saleExecution.setLongitude(jObject.getString("Longitude__c"));
			}
			if (jObject.has("Mailing_Address__c")) {
				saleExecution.setMailingAddress(jObject
						.getString("Mailing_Address__c"));
			}
			if (jObject.has("Marital_Status__c")) {
				saleExecution.setMaritalStatus(jObject
						.getString("Marital_Status__c"));
			}
			if (jObject.has("Mobile_1__c")) {
				saleExecution.setMobile1(jObject.getString("Mobile_1__c"));
			}
			if (jObject.has("Mobile_2__c")) {
				saleExecution.setMobile2(jObject.getString("Mobile_2__c"));
			}
			if (jObject.has("National_ID__c")) {
				saleExecution
						.setNationalId(jObject.getString("National_ID__c"));
			}
			if (jObject.has("Nationality__c")) {
				saleExecution.setNationality(jObject
						.getString("Nationality__c"));
			}
			if (jObject.has("Not_Favourite_Drink__c")) {
				saleExecution.setNotFavouriteDrink(jObject
						.getString("Not_Favourite_Drink__c"));
			}
			if (jObject.has("Not_Favourite_Food__c")) {
				saleExecution.setNotFavouriteFood(jObject
						.getString("Not_Favourite_Food__c"));
			}
			if (jObject.has("Note__c")) {
				saleExecution.setNote(jObject.getString("Note__c"));
			}
			if (jObject.has("Number__c")) {
				saleExecution.setNumber(jObject.getString("Number__c"));
			}
			if (jObject.has("Number_of_permanent_employee__c")) {
				saleExecution.setNumberOfPermanentEmployees(jObject
						.getDouble("Number_of_permanent_employee__c"));
			}
			if (jObject.has("Order_Date__c")) {
				saleExecution.setOrderDate(jObject.getString("Order_Date__c"));
			}
			if (jObject.has("Order_Volume__c")) {
				saleExecution.setOrderVolume(jObject
						.getDouble("Order_Volume__c"));
			}
			if (jObject.has("Owner_Contact_Number__c")) {
				saleExecution.setOwnerContactNumber(jObject
						.getString("Owner_Contact_Number__c"));
			}
			if (jObject.has("Phone_1__c")) {
				saleExecution.setPhone1(jObject.getString("Phone_1__c"));
			}
			if (jObject.has("Phone_2__c")) {
				saleExecution.setPhone2(jObject.getString("Phone_2__c"));
			}
			if (jObject.has("Physical_Condition__c")) {
				saleExecution.setPhysicalCondition(jObject
						.getString("Physical_Condition__c"));
			}
			if (jObject.has("Pick_up_2T_Armada__c")) {
				saleExecution.setPickUp2TArmada(jObject
						.getString("Pick_up_2T_Armada__c"));
			}
			if (jObject.has("Postal_Code__c")) {
				saleExecution
						.setPostalCode(jObject.getString("Postal_Code__c"));
			}
			if (jObject.has("Preferred_Name__c")) {
				saleExecution.setPreferredName(jObject
						.getString("Preferred_Name__c"));
			}
			if (jObject.has("Preferred_contact_method__c")) {
				saleExecution.setPreferredContactMethod(jObject
						.getString("Preferred_contact_method__c"));
			}
			if (jObject.has("Priority__c")) {
				saleExecution.setPriority(jObject.getString("Priority__c"));
			}
			if (jObject.has("Promotion__c")) {
				saleExecution.setPromotion(jObject.getString("Promotion__c"));
			}
			if (jObject.has("Promotion_End_Date__c")) {
				saleExecution.setPromotionEndDate(jObject
						.getString("Promotion_End_Date__c"));
			}
			if (jObject.has("Promotion_Start_Date__c")) {
				saleExecution.setPromotionStartDate(jObject
						.getString("Promotion_Start_Date__c"));
			}
			if (jObject.has("Program__c")) {
				saleExecution.setProgram(jObject.getString("Program__c"));
			}
			if (jObject.has("Program_End_Date__c")) {
				saleExecution.setProgramEndDate(jObject
						.getString("Program_End_Date__c"));
			}
			if (jObject.has("Program_Start_Date__c")) {
				saleExecution.setProgramStartDate(jObject
						.getString("Program_Start_Date__c"));
			}
			if (jObject.has("Prospect__c")) {
				Prospect prospect = HolcimDataSource.getProspectBySFId(jObject
						.getString("Prospect__c"));
				if (prospect != null) {
					saleExecution.setProspectId(prospect.getId());
				}
			}
			if (jObject.has("Province__c")) {
				saleExecution.setProvince(jObject.getString("Province__c"));
			}
			if (jObject.has("Reason_For_Not_Achieving_Target__c")) {
				saleExecution.setReasonForNotAchievingTarget(jObject
						.getString("Reason_For_Not_Achieving_Target__c"));
			}
			if (jObject.has("Religion__c")) {
				saleExecution.setReligion(jObject.getString("Religion__c"));
			}
			if (jObject.has("Retailer_Credit_Limit__c")) {
				saleExecution.setRetailerCreditLimit(jObject
						.getDouble("Retailer_Credit_Limit__c"));
			}
			if (jObject.has("Retailer_ID__c")) {
				saleExecution
						.setRetailerId(jObject.getString("Retailer_ID__c"));
			}
			if (jObject.has("Reason_for_Buying_from_Lead_Competitor__c")) {
				saleExecution
						.setReasonForBuyingFromLeadCompetitor(jObject
								.getString("Reason_for_Buying_from_Lead_Competitor__c"));
			}
			if (jObject.has("Reason_For_Unsatisfied_Experience__c")) {
				saleExecution.setReasonForUnsatisfiedExperience(jObject
						.getString("Reason_For_Unsatisfied_Experience__c"));
			}
			if (jObject.has("Account_Name__c")) {
				saleExecution.setRetailerName(jObject
						.getString("Account_Name__c"));
			}
			if (jObject.has("Retailer_Status__c")) {
				saleExecution.setRetailerStatus(jObject
						.getString("Retailer_Status__c"));
			}
			if (jObject.has("Retailer_Term_of_Payment__c")) {
				String value = jObject.getString("Retailer_Term_of_Payment__c");
				saleExecution.setRetailerTermOfPayment(value);
			}
			if (jObject.has("SSCompetitor_Name__c")) {
				saleExecution.setSsCompetitorName(jObject
						.getString("SSCompetitor_Name__c"));
			}
			if (jObject.has("SSPicture_Date__c")) {
				saleExecution.setSsPictureDate(jObject
						.getString("SSPicture_Date__c"));
			}
			if (jObject.has("SSPicture_Description__c")) {
				saleExecution.setSsPictureDescription(jObject
						.getString("SSPicture_Description__c"));
			}
			if (jObject.has("SSPicturemd5__c")) {
				saleExecution.setSsPictureMD5(jObject
						.getString("SSPicturemd5__c"));
			}
			if (jObject.has("Sales_Actual_MTD__c")) {
				saleExecution.setSalesActualMTD(jObject
						.getDouble("Sales_Actual_MTD__c"));
			}
			if (jObject.has("Sales_Call_Note__c")) {
				saleExecution.setSalesCallNote(jObject
						.getString("Sales_Call_Note__c"));
			}
			if (jObject.has("Sales_Officer__c")) {
				saleExecution.setSalesOfficerId(jObject
						.getString("Sales_Officer__c"));
			}
			if (jObject.has("Sales_Officer_Name__c")) {
				saleExecution.setSalesOfficer(jObject
						.getString("Sales_Officer_Name__c"));
			}
			if (jObject.has("Sales_Target_Current_Month__c")) {
				saleExecution.setSalesTargetCurrentMonth(jObject
						.getDouble("Sales_Target_Current_Month__c"));
			}
			if (jObject.has("Sales_Target_MTD__c")) {
				saleExecution.setSalesTargetMTD(jObject
						.getDouble("Sales_Target_MTD__c"));
			}
			if (jObject.has("Salesman_in_charge__c")) {
				saleExecution.setSalesmanInChargeId(jObject
						.getString("Salesman_in_charge__c"));
			}
			if (jObject.has("Salesman_In_Charge_Name__c")) {
				saleExecution.setSalesmanInCharge(jObject
						.getString("Salesman_In_Charge_Name__c"));
			}
			if (jObject.has("Send_Alert__c")) {
				saleExecution.setSendAlert(jObject.getBoolean("Send_Alert__c"));
			}
			if (jObject.has("Shop_Sign_Remark__c")) {
				saleExecution.setShopSignRemark(jObject
						.getString("Shop_Sign_Remark__c"));
			}
			if (jObject.has("Sign_Board__c")) {
				saleExecution.setSignBoard(jObject.getString("Sign_Board__c"));
			}
			if (jObject.has("Starting_Year__c")) {
				saleExecution.setStartingYear(jObject
						.getInt("Starting_Year__c"));
			}
			if (jObject.has("Status__c")) {
				saleExecution.setStatus(jObject.getString("Status__c"));
			}
			if (jObject.has("Street_Address__c")) {
				saleExecution.setStreetAddress(jObject
						.getString("Street_Address__c"));
			}
			if (jObject.has("Submitted__c")) {
				saleExecution.setSubmitted(jObject.getBoolean("Submitted__c"));
			}
			if (jObject.has("Supplier_Name__c")) {
				saleExecution.setSupplierName(jObject
						.getString("Supplier_Name__c"));
			}
			if (jObject.has("Tier_KLAB_History__c")) {
				saleExecution.setTierKLABHistory(jObject
						.getString("Tier_KLAB_History__c"));
			}
			if (jObject.has("Tier_KLAB__c")) {
				saleExecution.setTierKLAB(jObject.getString("Tier_KLAB__c"));
			}
			if (jObject.has("Title__c")) {
				saleExecution.setTitle(jObject.getString("Title__c"));
			}
			if (jObject.has("Twenty_Four_T_Truck_Armada__c")) {
				saleExecution.setTwentyFourTTruckArmada(jObject
						.getString("Twenty_Four_T_Truck_Armada__c"));
			}
			if (jObject.has("Type_of_Shop_Sign__c")) {
				saleExecution.setTypeOfShopSign(jObject
						.getString("Type_of_Shop_Sign__c"));
			}
			if (jObject.has("Visibility_of_HIL_Shop_Sign__c")) {
				saleExecution.setVisibilityOfHILShopSign(jObject
						.getString("Visibility_of_HIL_Shop_Sign__c"));
			}
			if (jObject.has("Visibility_of_competitor_Shop_Sign__c")) {
				saleExecution.setVisibilityOfCompetitorShopSign(jObject
						.getString("Visibility_of_competitor_Shop_Sign__c"));
			}
			if (jObject.has("Visit_Date__c")) {
				saleExecution.setVisitDate(jObject.getString("Visit_Date__c"));
			}
			if (jObject.has("X1st_Desired_Reward_Points__c")) {
				saleExecution.setX1stDesiredRewardPoints(jObject
						.getDouble("X1st_Desired_Reward_Points__c"));
			}
			if (jObject.has("X1st_Desired_Reward__c")) {
				saleExecution.setX1stDesiredReward(jObject
						.getString("X1st_Desired_Reward__c"));
			}
			if (jObject.has("X2nd_Desired_Reward_Points__c")) {
				saleExecution.setX2ndDesiredRewardPoints(jObject
						.getDouble("X2nd_Desired_Reward_Points__c"));
			}
			if (jObject.has("X2nd_Desired_Reward__c")) {
				saleExecution.setX2ndDesiredReward(jObject
						.getString("X2nd_Desired_Reward__c"));
			}
			if (jObject.has("X3rd_Desired_Reward_Points__c")) {
				saleExecution.setX3rdDesiredRewardPoints(jObject
						.getDouble("X3rd_Desired_Reward_Points__c"));
			}
			if (jObject.has("X3rd_Desired_Reward__c")) {
				saleExecution.setX3rdDesiredReward(jObject
						.getString("X3rd_Desired_Reward__c"));
			}
			if (jObject.has("isProspect__c")) {
				saleExecution
						.setIsProspect(jObject.getBoolean("isProspect__c"));
			}
			saleExecution.setIsEdited(false);

			if (jObject.has("Competitor_Marketings__r")) {
				JSONObject jComMark = jObject
						.getJSONObject("Competitor_Marketings__r");
				if (jComMark != null && jComMark.has("records")) {
					JSONArray jCompMarkArray = jComMark.getJSONArray("records");
					if (jCompMarkArray != null && jCompMarkArray.length() > 0) {
						saleExecution.setCompetitorMarketings(HolcimDataSource
								.JSONToCompetitorMarketingList(jCompMarkArray
										.toString()));
					}
				}
			}

			if (jObject.has("Actions_Log__r")) {
				JSONObject jActionLogs = jObject
						.getJSONObject("Actions_Log__r");
				if (jActionLogs != null && jActionLogs.has("records")) {
					JSONArray jActionLogsArray = jActionLogs
							.getJSONArray("records");
					if (jActionLogsArray != null
							&& jActionLogsArray.length() > 0) {
						saleExecution.setActionLogs(HolcimDataSource
								.JSONToActionsLogList(jActionLogsArray
										.toString()));
					}
				}
			}

			if (jObject.has("Preorders__r")) {
				JSONObject jPreOrders = jObject.getJSONObject("Preorders__r");
				if (jPreOrders != null && jPreOrders.has("records")) {
					JSONArray jPreOrdersArray = jPreOrders
							.getJSONArray("records");
					if (jPreOrdersArray != null && jPreOrdersArray.length() > 0) {
						saleExecution
								.setPreOrders(HolcimDataSource
										.JSONToPreOrderList(jPreOrdersArray
												.toString()));
					}
				}
			}

			return saleExecution;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.PARSE_EXCEPTION,
					new HolcimError(HolcimConsts.ERROR_PARSING_JSON_STATUS,
							HolcimConsts.ERROR_PARSING_JSON_MESSAGE
									+ e.getMessage()));
		}
	}

	public static ArrayList<Contact> isContactToCreate() {
		return (ArrayList<Contact>) HolcimApp.daoSession
				.getContactDao()
				.queryBuilder()
				.whereOr(Properties.SalesforceId.isNull(),
						Properties.SalesforceId.eq("")).list();
	}

	public static ArrayList<SaleExecution> isSaleExecutionToUpload() {
		return (ArrayList<SaleExecution>) HolcimApp.daoSession
				.getSaleExecutionDao()
				.queryBuilder()
				.whereOr(
						com.holcim.altimetrik.android.model.SaleExecutionDao.Properties.SalesforceId
								.isNull(),
						com.holcim.altimetrik.android.model.SaleExecutionDao.Properties.SalesforceId
								.eq(""),
						com.holcim.altimetrik.android.model.SaleExecutionDao.Properties.IsEdited
								.eq(true)).list();
	}

	public static ArrayList<PreOrder> isPreOrderToUpload() {
		return (ArrayList<PreOrder>) HolcimApp.daoSession
				.getPreOrderDao()
				.queryBuilder()
				.whereOr(
						com.holcim.altimetrik.android.model.PreOrderDao.Properties.SalesforceId
								.isNull(),
						com.holcim.altimetrik.android.model.PreOrderDao.Properties.SalesforceId
								.eq(""),
						com.holcim.altimetrik.android.model.PreOrderDao.Properties.IsEdited
								.eq(true)).list();
	}

	public static ArrayList<TeleSale> isTeleSaleToUpload() {
		return (ArrayList<TeleSale>) HolcimApp.daoSession
				.getTeleSaleDao()
				.queryBuilder()
				.whereOr(
						com.holcim.altimetrik.android.model.TeleSaleDao.Properties.SalesforceId
								.isNull(),
						com.holcim.altimetrik.android.model.TeleSaleDao.Properties.SalesforceId
								.eq("")).list();
	}

	public static ArrayList<CompetitorMarketing> isCompetitorMarketingToUpload() {
		return (ArrayList<CompetitorMarketing>) HolcimApp.daoSession
				.getCompetitorMarketingDao()
				.queryBuilder()
				.whereOr(
						com.holcim.altimetrik.android.model.CompetitorMarketingDao.Properties.SalesforceId
								.isNull(),
						com.holcim.altimetrik.android.model.CompetitorMarketingDao.Properties.SalesforceId
								.eq(""),
						com.holcim.altimetrik.android.model.CompetitorMarketingDao.Properties.IsEdited
								.eq(true)).list();
	}

	public static ArrayList<ActionsLog> isActionsLogToUpload() {
		return (ArrayList<ActionsLog>) HolcimApp.daoSession
				.getActionsLogDao()
				.queryBuilder()
				.whereOr(
						com.holcim.altimetrik.android.model.ActionsLogDao.Properties.SalesforceId
								.isNull(),
						com.holcim.altimetrik.android.model.ActionsLogDao.Properties.SalesforceId
								.eq(""),
						com.holcim.altimetrik.android.model.ActionsLogDao.Properties.IsEdited
								.eq(true)).list();
	}

	public static Prospect getProspectBySFId(String salesforceID) {
		return HolcimApp.daoSession
				.getProspectDao()
				.queryBuilder()
				.where(com.holcim.altimetrik.android.model.ProspectDao.Properties.SalesforceId
						.eq(salesforceID)).unique();
	}

	public static Account getAccountBySFId(String salesforceID) {
		return HolcimApp.daoSession
				.getAccountDao()
				.queryBuilder()
				.where(com.holcim.altimetrik.android.model.AccountDao.Properties.SalesforceId
						.eq(salesforceID)).unique();
	}

	public static OutstandingFeedback getOutstandingFeedbackBySFId(
			String salesforceID) {
		return HolcimApp.daoSession
				.getOutstandingFeedbackDao()
				.queryBuilder()
				.where(com.holcim.altimetrik.android.model.OutstandingFeedbackDao.Properties.SalesforceId
						.eq(salesforceID)).unique();
	}

	public static Competitor getCompetitorBySFId(String salesforceID) {
		return HolcimApp.daoSession
				.getCompetitorDao()
				.queryBuilder()
				.where(com.holcim.altimetrik.android.model.CompetitorDao.Properties.SalesforceId
						.eq(salesforceID)).unique();
	}

	public static Contact getContactBySFId(String salesforceID) {
		return HolcimApp.daoSession.getContactDao().queryBuilder()
				.where(Properties.SalesforceId.eq(salesforceID)).unique();
	}

	public static ActionsLog getActionsLogBySFId(String salesforceID) {
		return HolcimApp.daoSession
				.getActionsLogDao()
				.queryBuilder()
				.where(com.holcim.altimetrik.android.model.ActionsLogDao.Properties.SalesforceId
						.eq(salesforceID)).unique();
	}

	public static CompetitorMarketing getCompetitorMarketingBySFId(
			String salesforceID) {
		return HolcimApp.daoSession
				.getCompetitorMarketingDao()
				.queryBuilder()
				.where(com.holcim.altimetrik.android.model.CompetitorMarketingDao.Properties.SalesforceId
						.eq(salesforceID)).unique();
	}

	public static SaleExecution getSaleExecutionBySFId(String salesforceID) {
		return HolcimApp.daoSession
				.getSaleExecutionDao()
				.queryBuilder()
				.where(com.holcim.altimetrik.android.model.SaleExecutionDao.Properties.SalesforceId
						.eq(salesforceID)).unique();
	}

	public static void clearDatabase() {
		HolcimApp.daoSession.getContactDao().deleteAll();
		HolcimApp.daoSession.getPreOrderDao().deleteAll();
		HolcimApp.daoSession.getActionsLogDao().deleteAll();
		HolcimApp.daoSession.getOutstandingFeedbackDao().deleteAll();
		HolcimApp.daoSession.getCompetitorMarketingDao().deleteAll();
		HolcimApp.daoSession.getCompetitorDao().deleteAll();
		HolcimApp.daoSession.getSaleExecutionDao().deleteAll();
		HolcimApp.daoSession.getAccountDao().deleteAll();
		HolcimApp.daoSession.getProspectDao().deleteAll();
	}

	public static void updateActionLog(List<ActionsLog> actionLogs,
			SaleExecution saleExecution) {
		for (ActionsLog actionLog : actionLogs) {
			actionLog.setSaleExecutionId(saleExecution.getId());
			HolcimApp.daoSession.update(actionLog);
		}
	}

	public static void saveSaleExecution(Context context,
			SaleExecution saleExecution,
			ArrayList<CompetitorMarketing> comeptitorsMarketings,
			ArrayList<ActionsLog> actionLogs, ArrayList<PreOrder> preOrders)
			throws HolcimException {
		try {
			if (saleExecution != null) {
				long seId = -1;
				saleExecution.setIsEdited(true);
				if (saleExecution.getId() == null || saleExecution.getId() < 0) {
					seId = HolcimApp.daoSession.insert(saleExecution);
				} else {
					HolcimApp.daoSession.update(saleExecution);
					seId = saleExecution.getId();
				}
				if (seId >= 0) {
					if (AltimetrikFileHandler.isFileExist(saleExecution
							.getTempSSPictureFilePath(context))) {
						if (saleExecution.isPictureFileExist(context)) {
							AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
									context);
							try {
								fileHandler
										.moveFile(
												saleExecution
														.getTempSSPictureFilePath(context),
												HolcimDataSource
														.getSaleExecutionDir(context)
														+ File.separator
														+ saleExecution
																.getTempSSPictureFileName(context));
								AltimetrikFileHandler.DeleteFile(saleExecution
										.getSSPicturePath(context));
								AltimetrikFileHandler
										.renameFile(
												HolcimDataSource
														.getSaleExecutionDir(context)
														+ File.separator
														+ saleExecution
																.getTempSSPictureFileName(context),
												HolcimDataSource
														.getSaleExecutionDir(context)
														+ File.separator
														+ saleExecution
																.getSSPictureFileName());
							} catch (Exception e) {
								// TODO: handle exception
							}
						} else {
							try {
								AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
										context);
								fileHandler
										.moveFile(
												saleExecution
														.getTempSSPictureFilePath(context),
												HolcimDataSource
														.getSaleExecutionDir(context)
														+ File.separator
														+ saleExecution
																.getTempSSPictureFileName(context));
								AltimetrikFileHandler
										.renameFile(
												HolcimDataSource
														.getSaleExecutionDir(context)
														+ File.separator
														+ saleExecution
																.getTempSSPictureFileName(context),
												HolcimDataSource
														.getSaleExecutionDir(context)
														+ File.separator
														+ saleExecution
																.getSSPictureFileName());
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
					if (AltimetrikFileHandler.isFileExist(saleExecution
							.getTempLandmarkPictureFilePath(context))) {
						if (saleExecution.isLandmarkPictureFileExist(context)) {
							AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
									context);
							try {
								fileHandler
										.moveFile(
												saleExecution
														.getTempLandmarkPictureFilePath(context),
												HolcimDataSource
														.getSaleExecutionDir(context)
														+ File.separator
														+ saleExecution
																.getTempLandmarkPictureFileName(context));
								AltimetrikFileHandler.DeleteFile(saleExecution
										.getLandmarkPicturePath(context));
								AltimetrikFileHandler
										.renameFile(
												HolcimDataSource
														.getSaleExecutionDir(context)
														+ File.separator
														+ saleExecution
																.getTempLandmarkPictureFileName(context),
												HolcimDataSource
														.getSaleExecutionDir(context)
														+ File.separator
														+ saleExecution
																.getLandmarkPictureFileName());
							} catch (Exception e) {
								// TODO: handle exception
							}
						} else {
							try {
								AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
										context);
								fileHandler
										.moveFile(
												saleExecution
														.getTempLandmarkPictureFilePath(context),
												HolcimDataSource
														.getSaleExecutionDir(context)
														+ File.separator
														+ saleExecution
																.getTempLandmarkPictureFileName(context));
								AltimetrikFileHandler
										.renameFile(
												HolcimDataSource
														.getSaleExecutionDir(context)
														+ File.separator
														+ saleExecution
																.getTempLandmarkPictureFileName(context),
												HolcimDataSource
														.getSaleExecutionDir(context)
														+ File.separator
														+ saleExecution
																.getLandmarkPictureFileName());
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
					for (PreOrder preOrder : preOrders) {
						preOrder.setSaleExecutionId(seId);
						if (preOrder.getId() == null || preOrder.getId() < 0) {
							HolcimApp.daoSession.insert(preOrder);
						} else {
							preOrder.setIsEdited(true);
							HolcimApp.daoSession.update(preOrder);
						}
					}
					for (CompetitorMarketing compMrk : comeptitorsMarketings) {
						compMrk.setSaleExecutionId(seId);
						if (compMrk.getId() == null || compMrk.getId() < 0) {
							HolcimApp.daoSession.insert(compMrk);
						} else {
							compMrk.setIsEdited(true);
							HolcimApp.daoSession.update(compMrk);
						}
					}
				}

			}
		} catch (HolcimException e) {
			throw e;
		}
	}

	public static void CreateFilesDir(Context context) throws HolcimException {
		try {
			AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
					context);
			if (!fileHandler
					.isDirectoryExistOnAppDirectory(HolcimConsts.CONTACT_DIRECTORY_NAME)) {
				fileHandler
						.createDirectoryOnAppDirectory(HolcimConsts.CONTACT_DIRECTORY_NAME);
			}
			if (!fileHandler
					.isDirectoryExistOnAppDirectory(HolcimConsts.SALE_EXECUTION_DIRECTORY_NAME)) {
				fileHandler
						.createDirectoryOnAppDirectory(HolcimConsts.SALE_EXECUTION_DIRECTORY_NAME);
			}
			if (!fileHandler
					.isDirectoryExistOnAppDirectory(HolcimConsts.TEMPORAL_DIRECTORY_NAME)) {
				fileHandler
						.createDirectoryOnAppDirectory(HolcimConsts.TEMPORAL_DIRECTORY_NAME);
			}
			if (!fileHandler
					.isDirectoryExistOnAppDirectory(HolcimConsts.ACTIONLOG_DIRECTORY_NAME)) {
				fileHandler
						.createDirectoryOnAppDirectory(HolcimConsts.ACTIONLOG_DIRECTORY_NAME);
			}
		} catch (Exception e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static String getTempMediaDir(Context context)
			throws HolcimException {
		try {
			File path = new File(context.getFilesDir(),
					HolcimConsts.TEMPORAL_DIRECTORY_NAME);
			File imageFile = new File(path + File.separator
					+ HolcimConsts.TEMPORAL_FILE_NAME);
			imageFile.getParentFile().mkdirs();
			return path.getAbsolutePath();
		} catch (Exception e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static String getTempMediaDirLandmark(Context context)
			throws HolcimException {
		try {
			File path = new File(context.getFilesDir(),
					HolcimConsts.TEMPORAL_DIRECTORY_NAME);
			File imageFile = new File(path + File.separator
					+ HolcimConsts.LANDMARK_TEMPORAL_FILE_NAME);
			imageFile.getParentFile().mkdirs();
			return path.getAbsolutePath();
		} catch (Exception e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static String getTempMediaDirActionLog(Context context,
			Long actionLogId) throws HolcimException {
		try {
			File path = new File(context.getFilesDir(),
					HolcimConsts.TEMPORAL_DIRECTORY_NAME);
			File imageFile = new File(path + File.separator
					+ HolcimConsts.ACTIONLOG_TEMPORAL_FILE_NAME_PART1
					+ actionLogId
					+ HolcimConsts.ACTIONLOG_TEMPORAL_FILE_NAME_PART2);
			imageFile.getParentFile().mkdirs();
			return path.getAbsolutePath();
		} catch (Exception e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static File getTempActionLogPhotoFile(Context context,
			Long actionLogId) throws HolcimException {
		try {
			File path = new File(context.getFilesDir(),
					HolcimConsts.TEMPORAL_DIRECTORY_NAME);
			File imageFile = new File(path + File.separator
					+ HolcimConsts.ACTIONLOG_TEMPORAL_FILE_NAME_PART1
					+ actionLogId
					+ HolcimConsts.ACTIONLOG_TEMPORAL_FILE_NAME_PART2);
			imageFile.getParentFile().mkdirs();
			if (!imageFile.exists())
				try {
					imageFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			return imageFile;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static File getTempPhotoFile(Context context) throws HolcimException {
		try {
			File path = new File(context.getFilesDir(),
					HolcimConsts.TEMPORAL_DIRECTORY_NAME);
			File imageFile = new File(path + File.separator
					+ HolcimConsts.TEMPORAL_FILE_NAME);
			imageFile.getParentFile().mkdirs();
			if (!imageFile.exists())
				try {
					imageFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return imageFile;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static File getTempLandmarkPhotoFile(Context context)
			throws HolcimException {
		try {
			File path = new File(context.getFilesDir(),
					HolcimConsts.TEMPORAL_DIRECTORY_NAME);
			File imageFile = new File(path + File.separator
					+ HolcimConsts.LANDMARK_TEMPORAL_FILE_NAME);
			imageFile.getParentFile().mkdirs();
			if (!imageFile.exists())
				try {
					imageFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			return imageFile;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static File getTempContactPhotoFile(Context context)
			throws HolcimException {
		try {
			File path = new File(context.getFilesDir(),
					HolcimConsts.TEMPORAL_DIRECTORY_NAME);
			File imageFile = new File(path + File.separator
					+ HolcimConsts.CONTACT_TEMPORAL_FILE_NAME);
			imageFile.getParentFile().mkdirs();
			if (!imageFile.exists())
				try {
					imageFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			return imageFile;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static boolean isTempFileExist(Context context)
			throws HolcimException {
		try {
			File path = new File(context.getFilesDir(),
					HolcimConsts.TEMPORAL_DIRECTORY_NAME);
			File imageFile = new File(path + File.separator
					+ HolcimConsts.TEMPORAL_FILE_NAME);
			imageFile.getParentFile().mkdirs();
			return imageFile.exists();
		} catch (Exception e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static String getSaleExecutionDir(Context context)
			throws HolcimException {
		try {
			return AltimetrikFileHandler.getDataDir(context) + File.separator
					+ HolcimConsts.SALE_EXECUTION_DIRECTORY_NAME;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static String getContactDir(Context context) throws HolcimException {
		try {
			return AltimetrikFileHandler.getDataDir(context) + File.separator
					+ HolcimConsts.CONTACT_DIRECTORY_NAME;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static String getActionLogDir(Context context)
			throws HolcimException {
		try {
			return AltimetrikFileHandler.getDataDir(context) + File.separator
					+ HolcimConsts.ACTIONLOG_DIRECTORY_NAME;
		} catch (Exception e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static void deleteAllTemporalFiles(Context context)
			throws HolcimException {
		try {
			AltimetrikFileHandler.deleteAllFilesInDirectory(context,
					HolcimDataSource.getTempMediaDir(context));
		} catch (Exception e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static String GetContactImagePath(Context context, Contact contact)
			throws HolcimException {
		try {
			return AltimetrikFileHandler.getDataDir(context) + File.separator
					+ HolcimConsts.CONTACT_DIRECTORY_NAME + File.separator
					+ contact.getImageFileName();
		} catch (AltimetrikException e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static String GetSaleExecutionImagePath(Context context,
			SaleExecution saleExecution, String field) throws HolcimException {
		String filename = "";
		if (field
				.equals(HolcimConsts.SALE_EXECUTION_SF_LANDMARK_IMAGE_FIELD_NAME)) {
			filename = saleExecution.getLandmarkPictureFileName();
		} else if (field
				.equals(HolcimConsts.SALE_EXECUTION_SF_PICTURE_IMAGE_FIELD_NAME)) {
			filename = saleExecution.getPictureFileName();
		} else if (field
				.equals(HolcimConsts.SALE_EXECUTION_SF_SS_IMAGE_FIELD_NAME)) {
			filename = saleExecution.getSSPictureFileName();
		}
		return HolcimDataSource.getSaleExecutionDir(context) + File.separator
				+ filename;
	}

	public static String GetActionLogImagePath(Context context,
			ActionsLog actionLog, String field) throws HolcimException {
		String filename = "";
		if (field.equals(HolcimConsts.ACTIONLOG_SF_IMAGE_FIELD_NAME)) {
			filename = actionLog.getPictureFileName();
		}
		return HolcimDataSource.getActionLogDir(context) + File.separator
				+ filename;
	}

	public static void SaveSaleExecutionImage(Context context,
			SaleExecution saleExecution, String field, Bitmap bitmap)
			throws HolcimException {
		try {
			AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
					context);
			if (field
					.equals(HolcimConsts.SALE_EXECUTION_SF_LANDMARK_IMAGE_FIELD_NAME)) {
				fileHandler.WriteBitmapToFile(
						saleExecution.getLandmarkPicturePath(context), bitmap);
			} else if (field
					.equals(HolcimConsts.SALE_EXECUTION_SF_PICTURE_IMAGE_FIELD_NAME)) {
				fileHandler.WriteBitmapToFile(
						saleExecution.getPicturePath(context), bitmap);
			} else if (field
					.equals(HolcimConsts.SALE_EXECUTION_SF_SS_IMAGE_FIELD_NAME)) {
				fileHandler.WriteBitmapToFile(
						saleExecution.getSSPicturePath(context), bitmap);
			}
		} catch (HolcimException e) {
			throw e;
		} catch (AltimetrikException e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static void SaveActionLogImage(Context context,
			ActionsLog actionLog, String field, Bitmap bitmap)
			throws HolcimException {
		try {
			AltimetrikFileHandler fileHandler = new AltimetrikFileHandler(
					context);
			if (field.equals(HolcimConsts.ACTIONLOG_SF_IMAGE_FIELD_NAME)) {
				fileHandler.WriteBitmapToFile(
						actionLog.getPicturePath(context), bitmap);
			}
		} catch (HolcimException e) {
			throw e;
		} catch (AltimetrikException e) {
			throw new HolcimException(HolcimException.FILE_EXCEPTION,
					new HolcimError(HolcimConsts.FILE_ERROR_STATUS,
							e.getMessage()));
		}
	}

	public static void TestSyncMethod() {
		ArrayList<Account> allAccounts = (ArrayList<Account>) HolcimApp.daoSession
				.getAccountDao().loadAll();
		ArrayList<Prospect> allProspects = (ArrayList<Prospect>) HolcimApp.daoSession
				.getProspectDao().loadAll();
		ArrayList<SaleExecution> allSaleExecution = (ArrayList<SaleExecution>) HolcimApp.daoSession
				.getSaleExecutionDao().loadAll();
		ArrayList<Competitor> allCompetitors = (ArrayList<Competitor>) HolcimApp.daoSession
				.getCompetitorDao().loadAll();
		ArrayList<CompetitorMarketing> allCompetitorMarketings = (ArrayList<CompetitorMarketing>) HolcimApp.daoSession
				.getCompetitorMarketingDao().loadAll();
		ArrayList<PreOrder> allPreOrder = (ArrayList<PreOrder>) HolcimApp.daoSession
				.getPreOrderDao().loadAll();
		ArrayList<ActionsLog> allActionlog = (ArrayList<ActionsLog>) HolcimApp.daoSession
				.getActionsLogDao().loadAll();

		// Create contact
		Contact newContact = new Contact();
		newContact.setBirthDate("1983-05-02");
		newContact.setEmail("testcontact@test.com");
		newContact.setFirstName("pepe");
		newContact.setLastName("prueba");
		HolcimApp.daoSession.insert(newContact);

		// create sale execution
		SaleExecution newSE = new SaleExecution();
		if (allAccounts != null && allAccounts.size() > 0) {
			newSE.setAccountId(allAccounts.get(0).getId());
		}
		if (allProspects != null && allProspects.size() > 0) {
			newSE.setProspectId(allProspects.get(0).getId());
		}
		newSE.setVisitDate("2014-07-11");
		newSE.setContactEmail1("test@test.com");
		newSE.setHilBuyingSellingPrice(22.50);
		newSE.setHvlStockStatus(true);
		newSE.setHmhilSellingPrice(22.00);
		newSE.setEvent("00UN0000002LRKhMAO");
		long newSEId = HolcimApp.daoSession.insert(newSE);

		// edit sale execution
		long editedSEId = -1;
		if (allSaleExecution != null && allSaleExecution.size() > 0) {
			SaleExecution editedSE = allSaleExecution.get(0);
			editedSE.setComments("this is a edit test for this sale execution");
			editedSE.setIsEdited(true);
			editedSEId = editedSE.getId();
			HolcimApp.daoSession.update(editedSE);
		}

		// create competitor marketing
		CompetitorMarketing newCM = new CompetitorMarketing();
		newCM.setSaleExecutionId(newSEId);
		newCM.setBuyingPrice(87.90);
		newCM.setInventory(2000);
		newCM.setOtherReason("new competitor marketing");
		newCM.setSellingPrice(12.00);
		if (allCompetitors != null && allCompetitors.size() > 0) {
			newCM.setCompetitorId(allCompetitors.get(0).getId());
		}
		HolcimApp.daoSession.insert(newCM);

		CompetitorMarketing newCM1 = new CompetitorMarketing();
		newCM1.setSaleExecutionId(newSEId);
		newCM1.setBuyingPrice(855.90);
		newCM1.setInventory(45000);
		newCM1.setOtherReason("new competitor marketing number 2");
		newCM1.setSellingPrice(125.00);
		if (allCompetitors != null && allCompetitors.size() > 0) {
			newCM1.setCompetitorId(allCompetitors
					.get(allCompetitors.size() - 1).getId());
		}
		HolcimApp.daoSession.insert(newCM1);

		CompetitorMarketing newCM2 = new CompetitorMarketing();
		newCM2.setSaleExecutionId(editedSEId);
		newCM2.setBuyingPrice(1.90);
		newCM2.setInventory(450);
		newCM2.setOtherReason("new competitor marketing number 1");
		newCM2.setSellingPrice(15.00);
		if (allCompetitors != null && allCompetitors.size() > 0) {
			newCM2.setCompetitorId(allCompetitors
					.get(allCompetitors.size() - 1).getId());
		}
		HolcimApp.daoSession.insert(newCM2);

		// edit competitor marketing
		if (allCompetitorMarketings != null
				&& allCompetitorMarketings.size() > 0) {
			CompetitorMarketing editedCM = allCompetitorMarketings.get(0);
			editedCM.setBuyingPrice(800.00);
			if (allCompetitors != null && allCompetitors.size() > 0) {
				editedCM.setCompetitorId(allCompetitors.get(
						allCompetitors.size() - 1).getId());
			}
			editedCM.setIsEdited(true);
			HolcimApp.daoSession.update(editedCM);
		}

		// create action log
		ActionsLog newAL = new ActionsLog();
		newAL.setComplaint(true);
		newAL.setDescription("this is a new al");
		newAL.setStatus("On Track");
		newAL.setSaleExecutionId(newSEId);
		HolcimApp.daoSession.insert(newAL);

		ActionsLog newAL1 = new ActionsLog();
		newAL1.setComplaint(true);
		newAL1.setStatus("Pending");
		newAL1.setDescription("this is a new al again");
		newAL1.setSaleExecutionId(newSEId);
		HolcimApp.daoSession.insert(newAL1);

		ActionsLog newAL2 = new ActionsLog();
		newAL2.setComplaint(true);
		newAL2.setStatus("Pending");
		newAL2.setDescription("this is a new al again and again");
		newAL2.setSaleExecutionId(editedSEId);
		HolcimApp.daoSession.insert(newAL2);

		// edit action log
		if (allActionlog != null && allActionlog.size() > 0) {
			ActionsLog editedAL = allActionlog.get(0);
			editedAL.setDescription("edited action log");
			editedAL.setIsEdited(true);
			HolcimApp.daoSession.update(editedAL);
		}

		// create preorder
		PreOrder newPO = new PreOrder();
		newPO.setPreOrderDate("2014-07-23");
		newPO.setPreOrderQuantity(800.00);
		newPO.setSaleExecutionId(editedSEId);
		HolcimApp.daoSession.insert(newPO);

		// edit pre order
		if (allPreOrder != null && allPreOrder.size() > 0) {
			PreOrder editedPO = allPreOrder.get(0);
			editedPO.setPreOrderDate("2014-08-16");
			editedPO.setIsEdited(true);
			HolcimApp.daoSession.update(editedPO);
		}

	}

	public static ArrayList<SaleExecution> getOldSalesExecutions() {
		List<SaleExecution> allSE = HolcimApp.daoSession.getSaleExecutionDao()
				.loadAll();
		if (allSE != null && allSE.size() > 0) {
			ArrayList<SaleExecution> oldSaleExecutions = new ArrayList<SaleExecution>();
			for (SaleExecution saleExecution : allSE) {
				Calendar today = Calendar.getInstance();
				today.add(Calendar.DAY_OF_MONTH, -1);
				if (HolcimUtility.getDateFromString(
						saleExecution.getVisitDate(), "yyyy-MM-dd").before(
						today)) {
					oldSaleExecutions.add(saleExecution);
				}
			}
			return oldSaleExecutions;
		} else {
			return null;
		}

	}

	public static void deleteOldSaleExecutions() {
		ArrayList<SaleExecution> oldSaleExecutions = HolcimDataSource
				.getOldSalesExecutions();
		if (oldSaleExecutions != null && oldSaleExecutions.size() > 0) {
			for (SaleExecution saleExecution : oldSaleExecutions) {
				List<CompetitorMarketing> compMarks = saleExecution
						.getCompetitorMarketings();
				if (compMarks != null && compMarks.size() > 0) {
					for (CompetitorMarketing competitorMarketing : compMarks) {
						HolcimApp.daoSession.delete(competitorMarketing);
					}
				}
				List<ActionsLog> actionsLogs = saleExecution.getActionLogs();
				if (actionsLogs != null && actionsLogs.size() > 0) {
					for (ActionsLog actionsLog : actionsLogs) {
						HolcimApp.daoSession.delete(actionsLog);
					}
				}
				List<PreOrder> preOrders = saleExecution.getPreOrders();
				if (preOrders != null && preOrders.size() > 0) {
					for (PreOrder preOrder : preOrders) {
						HolcimApp.daoSession.delete(preOrder);
					}
				}
				HolcimApp.daoSession.delete(saleExecution);
			}
		}
	}

}
