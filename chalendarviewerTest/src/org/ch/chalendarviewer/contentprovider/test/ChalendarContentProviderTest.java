package org.ch.chalendarviewer.contentprovider.test;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.util.Log;

import org.ch.chalendarviewer.contentprovider.AuthUser;
import org.ch.chalendarviewer.contentprovider.ChalendarContentProvider;

import java.util.Calendar;

/**
 * Class for testing purposes
 * @author vitor
 */
public class ChalendarContentProviderTest extends ProviderTestCase2<ChalendarContentProvider> {

    /** Tag for logging */
    private static final String TAG = "PruebaContent";
    
    /** Provider testing object */
    private ContentProvider provider;
    
    /** 
     * Constructor
     */
    public ChalendarContentProviderTest() {
        super(ChalendarContentProvider.class, ChalendarContentProvider.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        provider = getProvider();
    }
    
    /**
     * Test method
     */
    public void testDatabase(){
        //Insert some data
        insertData();
        //Modify a record
        modifyData();
        //Assert data inserted
        queryData();
        //Delete data inserted
        deleteData();
        //Assert table is empty
        queryDeletedData();
    }
    
    /**
     * Example of how to query data
     */
    private void queryData() {
        // Form an array specifying which columns to return. 
        String[] projection = new String[] {
                AuthUser._ID,                      
                AuthUser.ACCESS_TOKEN,
                AuthUser.REFRESH_TOKEN,
                AuthUser.EMAIL,
                AuthUser.EXPIRATION_DATE,
                AuthUser.ACTIVE_USER
        };

        // Get the base URI for the Auth users table content provider.
        Uri authUsers =  AuthUser.CONTENT_URI;

        String where = AuthUser.ACTIVE_USER + "=?";
        String[] whereParams = new String[]{"1"};  
        
        // Make the query. 
        Cursor managedCursor = provider.query(authUsers,
                projection, // Which columns to return 
                where,       // Which rows to return (all rows)
                whereParams,       // Selection arguments (none)
                // Put the results in ascending order by email
                AuthUser.EMAIL + " ASC");       

        assertNotNull(managedCursor);
        assertTrue(managedCursor.getCount() == 1);
        
        if (managedCursor.moveToFirst()) {

            String email; 
            String token; 
            String authCode;
            String expDate;
            int emailColumn = managedCursor.getColumnIndex(AuthUser.EMAIL); 
            int tokenColumn = managedCursor.getColumnIndex(AuthUser.ACCESS_TOKEN);
            int authColdeColumn = managedCursor.getColumnIndex(AuthUser.REFRESH_TOKEN);
            int expDateColumn = managedCursor.getColumnIndex(AuthUser.EXPIRATION_DATE);
            
            Log.d(TAG,"EMAIL\t  \tTOKEN\t  \tAUTH_CODE");
            do {
                // Get the field values
                email = managedCursor.getString(emailColumn);
                token = managedCursor.getString(tokenColumn);
                authCode = managedCursor.getString(authColdeColumn);
                expDate = managedCursor.getString(expDateColumn);
               
                
                Log.d(TAG, email + "\t" + token + "\t" + authCode + "\t" + expDate);
                
                if(email.equals("vitor@gmail.com")){
                    assertEquals(token, "vitor_at");
                }               
                
            } while (managedCursor.moveToNext());

        }
    }
    
    /**
     * Query data to assert that database is empty
     */
    private void queryDeletedData() {
        // Form an array specifying which columns to return. 
        String[] projection = new String[] {
                AuthUser._ID,
                AuthUser.ACCESS_TOKEN,
                AuthUser.REFRESH_TOKEN,
                AuthUser.EMAIL,
                AuthUser.EXPIRATION_DATE
        };

        // Get the base URI for the Auth users table content provider.
        Uri authUsers =  AuthUser.CONTENT_URI;

        // Make the query. 
        Cursor managedCursor = provider.query(authUsers,
                projection, // Which columns to return 
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                // Put the results in ascending order by name
                AuthUser.EMAIL + " ASC");       

        assertNotNull(managedCursor);
        assertTrue(managedCursor.getCount() == 0);
        
 
    }
    
    /**
     * Example of how to insertData
     */
    private void insertData(){
        
        ContentValues values = new ContentValues();

         values.put(AuthUser.ACCESS_TOKEN, "uno_at");
         values.put(AuthUser.REFRESH_TOKEN, "uno_ac");
         values.put(AuthUser.EMAIL, "tomas@gmail");
         values.put(AuthUser.ACTIVE_USER, false);
         
         
         Uri uri = provider.insert(AuthUser.CONTENT_URI, values);
         Log.d(TAG, "Result insert: " + uri);
         
         values.put(AuthUser.ACCESS_TOKEN, "dos_at");
         values.put(AuthUser.REFRESH_TOKEN, "dos_ac");
         values.put(AuthUser.EMAIL, "juan@gmail");
         values.put(AuthUser.ACTIVE_USER, false);
         values.put(AuthUser.EXPIRATION_DATE, "2012-03-17 11:11:11.111");
         
         uri = provider.insert(AuthUser.CONTENT_URI, values);
         Log.d(TAG, "Result insert: " + uri);
         
         values.put(AuthUser.ACCESS_TOKEN, "tres_at");
         values.put(AuthUser.REFRESH_TOKEN, "tres_ac");
         values.put(AuthUser.EMAIL, "vitor@gmail");
         values.put(AuthUser.ACTIVE_USER, true);
         
         uri = provider.insert(AuthUser.CONTENT_URI, values);
         Log.d(TAG, "Result insert: " + uri);
    }
    
    /**
     * Example of how to modify data
     */
    private void modifyData(){
        ContentValues values = new ContentValues();

        values.put(AuthUser.REFRESH_TOKEN, "vitor_ac");
        values.put(AuthUser.ACCESS_TOKEN,"vitor_at");
        values.put(AuthUser.ACTIVE_USER, false);
        
        String where = AuthUser.EMAIL + "=?";
        String[] whereParams = new String[]{"vitor@gmail"};        
        int result = provider.update(AuthUser.CONTENT_URI, values, where, whereParams);
        
        Log.d(TAG, "Result update1: " + result);
        
        values.clear();
        values.put(AuthUser.ACTIVE_USER, true);
        
        where = AuthUser.EMAIL + "=?";
        whereParams = new String[]{"juan@gmail"};        
        result = provider.update(AuthUser.CONTENT_URI, values, where, whereParams);
        
        Log.d(TAG, "Result update2: " + result);
    }
    
    
    /**
     * Example of how to deleteData
     */
    private void deleteData(){
        
        String where = AuthUser.EMAIL + "=?";
        String[] whereParams = new String[]{"tomas@gmail"};        
        int result = provider.delete(AuthUser.CONTENT_URI, where, whereParams);
        Log.d(TAG, "Result delete: " + result);
        
        whereParams = new String[]{"juan@gmail"};        
        result = provider.delete(AuthUser.CONTENT_URI, where, whereParams);
        Log.d(TAG, "Result delete: " + result);
        
        whereParams = new String[]{"vitor@gmail"};        
        result = provider.delete(AuthUser.CONTENT_URI, where, whereParams);
        Log.d(TAG, "Result delete: " + result);
        
    }
    
    

}
