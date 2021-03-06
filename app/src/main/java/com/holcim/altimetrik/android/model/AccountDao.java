package com.holcim.altimetrik.android.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.holcim.altimetrik.android.model.Account;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table ACCOUNT.
 */
public class AccountDao extends AbstractDao<Account, Long> {

    public static final String TABLENAME = "ACCOUNT";

    /**
     * Properties of entity Account.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property SalesforceId = new Property(2, String.class, "salesforceId", false, "SALESFORCE_ID");
        public final static Property MobileAddress = new Property(3, String.class, "mobileAddress", false, "MOBILE_ADDRESS");
        public final static Property Jhid = new Property(4, String.class, "jhid", false, "JHID");
        public final static Property Kecamatan = new Property(5, String.class, "kecamatan", false, "KECAMATAN");
        public final static Property Lmbuyingvol = new Property(6, String.class, "lmbuyingvol", false, "LMBUYINGVOL");
    };

    private DaoSession daoSession;


    public AccountDao(DaoConfig config) {
        super(config);
    }

    public AccountDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ACCOUNT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'SALESFORCE_ID' TEXT," + // 2: salesforceId
                "'MOBILE_ADDRESS' TEXT," + //mobileaddress
                "'JHID' TEXT," + // 3: jhid
                "'KECAMATAN' TEXT," + // 4: kecamatan
                "'LMBUYINGVOL' REAL );"); // lmbuyingvol
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ACCOUNT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Account entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }

        String salesforceId = entity.getSalesforceId();
        if (salesforceId != null) {
            stmt.bindString(3, salesforceId);
        }

        String mobileAddress = entity.getMobileAddress();
        if (mobileAddress != null) {
            stmt.bindString(4, mobileAddress);
        }

        String jhid = entity.getJhid();
        if (jhid != null) {
            stmt.bindString(5, jhid);
        }

        String kecamatan = entity.getKecamatan();
        if (kecamatan != null) {
            stmt.bindString(6, kecamatan);
        }

        Double lmbuyingvol = entity.getLmbuyingvol();
        if (lmbuyingvol != null) {
            stmt.bindDouble(7, lmbuyingvol);
        }


    }

    @Override
    protected void attachEntity(Account entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public Account readEntity(Cursor cursor, int offset) {
        Account entity = new Account( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // salesforceId
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // salesforceId
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4),// jelajahholcimid
                cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5),// kecamatan
                cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6) //lmbuyingvol
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Account entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSalesforceId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMobileAddress(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setJhid(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setKecamatan(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setLmbuyingvol(cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6));
    }

    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Account entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /** @inheritdoc */
    @Override
    public Long getKey(Account entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

}
