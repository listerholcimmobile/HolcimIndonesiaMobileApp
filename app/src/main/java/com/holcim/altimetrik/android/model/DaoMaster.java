package com.holcim.altimetrik.android.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import com.holcim.altimetrik.android.model.UserDao;
import com.holcim.altimetrik.android.model.SaleExecutionDao;
import com.holcim.altimetrik.android.model.CompetitorMarketingDao;
import com.holcim.altimetrik.android.model.ActionsLogDao;
import com.holcim.altimetrik.android.model.OutstandingFeedbackDao;
import com.holcim.altimetrik.android.model.AccountDao;
import com.holcim.altimetrik.android.model.CompetitorDao;
import com.holcim.altimetrik.android.model.PreOrderDao;
import com.holcim.altimetrik.android.model.TeleSaleDao;
import com.holcim.altimetrik.android.model.ProspectDao;
import com.holcim.altimetrik.android.model.ContactDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * Master of DAO (schema version 3): knows all DAOs.
 */
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 3;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        UserDao.createTable(db, ifNotExists);
        SaleExecutionDao.createTable(db, ifNotExists);
        CompetitorMarketingDao.createTable(db, ifNotExists);
        ActionsLogDao.createTable(db, ifNotExists);
        OutstandingFeedbackDao.createTable(db, ifNotExists);
        AccountDao.createTable(db, ifNotExists);
        CompetitorDao.createTable(db, ifNotExists);
        PreOrderDao.createTable(db, ifNotExists);
        TeleSaleDao.createTable(db, ifNotExists);
        ProspectDao.createTable(db, ifNotExists);
        ContactDao.createTable(db, ifNotExists);
    }

    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        UserDao.dropTable(db, ifExists);
        SaleExecutionDao.dropTable(db, ifExists);
        CompetitorMarketingDao.dropTable(db, ifExists);
        ActionsLogDao.dropTable(db, ifExists);
        OutstandingFeedbackDao.dropTable(db, ifExists);
        AccountDao.dropTable(db, ifExists);
        CompetitorDao.dropTable(db, ifExists);
        PreOrderDao.dropTable(db, ifExists);
        TeleSaleDao.dropTable(db, ifExists);
        ProspectDao.dropTable(db, ifExists);
        ContactDao.dropTable(db, ifExists);
    }

    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(UserDao.class);
        registerDaoClass(SaleExecutionDao.class);
        registerDaoClass(CompetitorMarketingDao.class);
        registerDaoClass(ActionsLogDao.class);
        registerDaoClass(OutstandingFeedbackDao.class);
        registerDaoClass(AccountDao.class);
        registerDaoClass(CompetitorDao.class);
        registerDaoClass(PreOrderDao.class);
        registerDaoClass(TeleSaleDao.class);
        registerDaoClass(ProspectDao.class);
        registerDaoClass(ContactDao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

}
