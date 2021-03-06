package com.holcim.altimetrik.android.model;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.holcim.altimetrik.android.model.ActionsLog;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ACTIONS_LOG.
*/
public class ActionsLogDao extends AbstractDao<ActionsLog, Long> {

    public static final String TABLENAME = "ACTIONS_LOG";

    /**
     * Properties of entity ActionsLog.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SalesforceId = new Property(1, String.class, "salesforceId", false, "SALESFORCE_ID");
        public final static Property ActionLogNumber = new Property(2, String.class, "actionLogNumber", false, "ACTION_LOG_NUMBER");
        public final static Property Description = new Property(3, String.class, "description", false, "DESCRIPTION");
        public final static Property Status = new Property(4, String.class, "status", false, "STATUS");
        public final static Property Complaint = new Property(5, Boolean.class, "complaint", false, "COMPLAINT");
        public final static Property Category = new Property(6, String.class, "category", false, "CATEGORY");
        public final static Property Picture = new Property(7, String.class, "picture", false, "PICTURE");
        public final static Property PictureDescription = new Property(8, String.class, "pictureDescription", false, "PICTURE_DESCRIPTION");
        public final static Property Picture1 = new Property(9, String.class, "picture1", false, "PICTURE_1");
        public final static Property PictureDescription1 = new Property(10, String.class, "pictureDescription1", false, "PICTURE_DESCRIPTION_1");
        public final static Property Picture2 = new Property(11, String.class, "picture2", false, "PICTURE_2");
        public final static Property PictureDescription2 = new Property(12, String.class, "pictureDescription2", false, "PICTURE_DESCRIPTION_2");
        public final static Property Picture3 = new Property(13, String.class, "picture3", false, "PICTURE_3");
        public final static Property PictureDescription3 = new Property(14, String.class, "pictureDescription3", false, "PICTURE_DESCRIPTION_3");
        public final static Property Picture4 = new Property(15, String.class, "picture4", false, "PICTURE_4");
        public final static Property PictureDescription4 = new Property(16, String.class, "pictureDescription4", false, "PICTURE_DESCRIPTION_4");
        public final static Property IsEdited = new Property(17, Boolean.class, "isEdited", false, "IS_EDITED");
        public final static Property SaleExecutionId = new Property(18, long.class, "saleExecutionId", false, "SALE_EXECUTION_ID");
    };

    private DaoSession daoSession;

    private Query<ActionsLog> saleExecution_ActionLogsQuery;

    public ActionsLogDao(DaoConfig config) {
        super(config);
    }
    
    public ActionsLogDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ACTIONS_LOG' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'SALESFORCE_ID' TEXT," + // 1: salesforceId
                "'ACTION_LOG_NUMBER' TEXT," + // 2: actionLogNumber
                "'DESCRIPTION' TEXT," + // 3: description
                "'STATUS' TEXT," + // 4: status
                "'COMPLAINT' INTEGER," + // 5: complaint
                "'CATEGORY' TEXT," + // 6: category
                "'PICTURE' TEXT," + // 7: picture
                "'PICTURE_DESCRIPTION' TEXT," + // 8: pictureDescription
                "'PICTURE_1' TEXT," + // 9: picture1
                "'PICTURE_DESCRIPTION_1' TEXT," + // 10: pictureDescription1
                "'PICTURE_2' TEXT," + // 11: picture2
                "'PICTURE_DESCRIPTION_2' TEXT," + // 12: pictureDescription2
                "'PICTURE_3' TEXT," + // 13: picture3
                "'PICTURE_DESCRIPTION_3' TEXT," + // 14: pictureDescription3
                "'PICTURE_4' TEXT," + // 15: picture4
                "'PICTURE_DESCRIPTION_4' TEXT," + // 16: pictureDescription4
                "'IS_EDITED' INTEGER," + // 17: isEdited
                "'SALE_EXECUTION_ID' INTEGER NOT NULL );"); // 18: saleExecutionId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ACTIONS_LOG'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ActionsLog entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String salesforceId = entity.getSalesforceId();
        if (salesforceId != null) {
            stmt.bindString(2, salesforceId);
        }
 
        String actionLogNumber = entity.getActionLogNumber();
        if (actionLogNumber != null) {
            stmt.bindString(3, actionLogNumber);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(4, description);
        }
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(5, status);
        }
 
        Boolean complaint = entity.getComplaint();
        if (complaint != null) {
            stmt.bindLong(6, complaint ? 1l: 0l);
        }
 
        String category = entity.getCategory();
        if (category != null) {
            stmt.bindString(7, category);
        }
 
        String picture = entity.getPicture();
        if (picture != null) {
            stmt.bindString(8, picture);
        }
 
        String pictureDescription = entity.getPictureDescription();
        if (pictureDescription != null) {
            stmt.bindString(9, pictureDescription);
        }

        String picture1 = entity.getPicture1();
        if (picture1 != null) {
            stmt.bindString(10, picture1);
        }

        String pictureDescription1 = entity.getPictureDescription1();
        if (pictureDescription1 != null) {
            stmt.bindString(11, pictureDescription1);
        }

        String picture2 = entity.getPicture2();
        if (picture2 != null) {
            stmt.bindString(12, picture2);
        }

        String pictureDescription2 = entity.getPictureDescription2();
        if (pictureDescription2 != null) {
            stmt.bindString(13, pictureDescription2);
        }

        String picture3 = entity.getPicture3();
        if (picture3 != null) {
            stmt.bindString(14, picture3);
        }

        String pictureDescription3 = entity.getPictureDescription3();
        if (pictureDescription3 != null) {
            stmt.bindString(15, pictureDescription3);
        }

        String picture4 = entity.getPicture4();
        if (picture4 != null) {
            stmt.bindString(16, picture4);
        }

        String pictureDescription4 = entity.getPictureDescription4();
        if (pictureDescription4 != null) {
            stmt.bindString(17, pictureDescription4);
        }
 
        Boolean isEdited = entity.getIsEdited();
        if (isEdited != null) {
            stmt.bindLong(18, isEdited ? 1l: 0l);
        }
        stmt.bindLong(19, entity.getSaleExecutionId());
    }

    @Override
    protected void attachEntity(ActionsLog entity) {
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
    public ActionsLog readEntity(Cursor cursor, int offset) {
        ActionsLog entity = new ActionsLog( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // salesforceId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // actionLogNumber
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // description
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // status
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0, // complaint
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // category
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // picture
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // pictureDescription
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // picture1
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // pictureDescription1
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // picture2
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // pictureDescription2
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // picture3
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // pictureDescription3
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // picture4
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // pictureDescription4
            cursor.isNull(offset + 17) ? null : cursor.getShort(offset + 17) != 0, // isEdited
            cursor.getLong(offset + 18) // saleExecutionId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ActionsLog entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSalesforceId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setActionLogNumber(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDescription(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setStatus(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setComplaint(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
        entity.setCategory(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPicture(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPictureDescription(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setPicture1(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setPictureDescription1(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setPicture2(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setPictureDescription2(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setPicture3(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setPictureDescription3(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setPicture4(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setPictureDescription4(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setIsEdited(cursor.isNull(offset + 17) ? null : cursor.getShort(offset + 17) != 0);
        entity.setSaleExecutionId(cursor.getLong(offset + 18));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ActionsLog entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ActionsLog entity) {
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
    
    /** Internal query to resolve the "actionLogs" to-many relationship of SaleExecution. */
    public List<ActionsLog> _querySaleExecution_ActionLogs(long saleExecutionId) {
        synchronized (this) {
            if (saleExecution_ActionLogsQuery == null) {
                QueryBuilder<ActionsLog> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.SaleExecutionId.eq(null));
                saleExecution_ActionLogsQuery = queryBuilder.build();
            }
        }
        Query<ActionsLog> query = saleExecution_ActionLogsQuery.forCurrentThread();
        query.setParameter(0, saleExecutionId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getSaleExecutionDao().getAllColumns());
            builder.append(" FROM ACTIONS_LOG T");
            builder.append(" LEFT JOIN SALE_EXECUTION T0 ON T.'SALE_EXECUTION_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ActionsLog loadCurrentDeep(Cursor cursor, boolean lock) {
        ActionsLog entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        SaleExecution saleExecution = loadCurrentOther(daoSession.getSaleExecutionDao(), cursor, offset);
         if(saleExecution != null) {
            entity.setSaleExecution(saleExecution);
        }

        return entity;    
    }

    public ActionsLog loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<ActionsLog> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ActionsLog> list = new ArrayList<ActionsLog>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<ActionsLog> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ActionsLog> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
