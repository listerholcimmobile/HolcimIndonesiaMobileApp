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

import com.holcim.altimetrik.android.model.PreOrder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PRE_ORDER.
*/
public class PreOrderDao extends AbstractDao<PreOrder, Long> {

    public static final String TABLENAME = "PRE_ORDER";

    /**
     * Properties of entity PreOrder.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PreOrderDate = new Property(1, String.class, "preOrderDate", false, "PRE_ORDER_DATE");
        public final static Property SalesforceId = new Property(2, String.class, "salesforceId", false, "SALESFORCE_ID");
        public final static Property PreOrderQuantity = new Property(3, Double.class, "preOrderQuantity", false, "PRE_ORDER_QUANTITY");
        public final static Property Product = new Property(4, String.class, "product", false, "PRODUCT");
        public final static Property Unit = new Property(5, String.class, "unit", false, "UNIT");
        public final static Property ReasonForNotOrdering = new Property(6, String.class, "reasonForNotOrdering", false, "REASON_FOR_NOT_ORDERING");
        public final static Property IsEdited = new Property(7, Boolean.class, "isEdited", false, "IS_EDITED");
        public final static Property SaleExecutionId = new Property(8, long.class, "saleExecutionId", false, "SALE_EXECUTION_ID");
    };

    private DaoSession daoSession;

    private Query<PreOrder> saleExecution_PreOrdersQuery;

    public PreOrderDao(DaoConfig config) {
        super(config);
    }
    
    public PreOrderDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PRE_ORDER' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'PRE_ORDER_DATE' TEXT," + // 1: preOrderDate
                "'SALESFORCE_ID' TEXT," + // 2: salesforceId
                "'PRE_ORDER_QUANTITY' REAL," + // 3: preOrderQuantity
                "'PRODUCT' TEXT," + // 4: product
                "'UNIT' TEXT," + // 5: unit
                "'REASON_FOR_NOT_ORDERING' TEXT," + // 6: reasonForNotOrdering
                "'IS_EDITED' INTEGER," + // 7: isEdited
                "'SALE_EXECUTION_ID' INTEGER NOT NULL );"); // 8: saleExecutionId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PRE_ORDER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PreOrder entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String preOrderDate = entity.getPreOrderDate();
        if (preOrderDate != null) {
            stmt.bindString(2, preOrderDate);
        }
 
        String salesforceId = entity.getSalesforceId();
        if (salesforceId != null) {
            stmt.bindString(3, salesforceId);
        }
 
        Double preOrderQuantity = entity.getPreOrderQuantity();
        if (preOrderQuantity != null) {
            stmt.bindDouble(4, preOrderQuantity);
        }
 
        String product = entity.getProduct();
        if (product != null) {
            stmt.bindString(5, product);
        }
 
        String unit = entity.getUnit();
        if (unit != null) {
            stmt.bindString(6, unit);
        }
 
        String reasonForNotOrdering = entity.getReasonForNotOrdering();
        if (reasonForNotOrdering != null) {
            stmt.bindString(7, reasonForNotOrdering);
        }
 
        Boolean isEdited = entity.getIsEdited();
        if (isEdited != null) {
            stmt.bindLong(8, isEdited ? 1l: 0l);
        }
        stmt.bindLong(9, entity.getSaleExecutionId());
    }

    @Override
    protected void attachEntity(PreOrder entity) {
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
    public PreOrder readEntity(Cursor cursor, int offset) {
        PreOrder entity = new PreOrder( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // preOrderDate
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // salesforceId
            cursor.isNull(offset + 3) ? null : cursor.getDouble(offset + 3), // preOrderQuantity
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // product
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // unit
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // reasonForNotOrdering
            cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0, // isEdited
            cursor.getLong(offset + 8) // saleExecutionId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PreOrder entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPreOrderDate(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSalesforceId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPreOrderQuantity(cursor.isNull(offset + 3) ? null : cursor.getDouble(offset + 3));
        entity.setProduct(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUnit(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setReasonForNotOrdering(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIsEdited(cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0);
        entity.setSaleExecutionId(cursor.getLong(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PreOrder entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PreOrder entity) {
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
    
    /** Internal query to resolve the "preOrders" to-many relationship of SaleExecution. */
    public List<PreOrder> _querySaleExecution_PreOrders(long saleExecutionId) {
        synchronized (this) {
            if (saleExecution_PreOrdersQuery == null) {
                QueryBuilder<PreOrder> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.SaleExecutionId.eq(null));
                saleExecution_PreOrdersQuery = queryBuilder.build();
            }
        }
        Query<PreOrder> query = saleExecution_PreOrdersQuery.forCurrentThread();
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
            builder.append(" FROM PRE_ORDER T");
            builder.append(" LEFT JOIN SALE_EXECUTION T0 ON T.'SALE_EXECUTION_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected PreOrder loadCurrentDeep(Cursor cursor, boolean lock) {
        PreOrder entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        SaleExecution saleExecution = loadCurrentOther(daoSession.getSaleExecutionDao(), cursor, offset);
         if(saleExecution != null) {
            entity.setSaleExecution(saleExecution);
        }

        return entity;    
    }

    public PreOrder loadDeep(Long key) {
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
    public List<PreOrder> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<PreOrder> list = new ArrayList<PreOrder>(count);
        
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
    
    protected List<PreOrder> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<PreOrder> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}