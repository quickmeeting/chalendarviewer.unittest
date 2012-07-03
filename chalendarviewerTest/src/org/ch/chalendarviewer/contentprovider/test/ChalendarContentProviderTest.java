package org.ch.chalendarviewer.contentprovider.test;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.util.Log;

import org.ch.chalendarviewer.contentprovider.AccountColumns;
import org.ch.chalendarviewer.contentprovider.ChalendarContentProvider;
import org.ch.chalendarviewer.contentprovider.Resource;

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
        insertResourcesData();
        //Modify a record
        modifyData();
        modifyResourceData();
        //Assert data inserted
        queryData();
        queryResources();
        //Delete data inserted
        deleteData();
        deleteResourcesData();
        //Assert table is empty
        queryDeletedData();
        queryDeletedResourceData();
    }
    
    /**
     * Example of how to query data
     */
    private void queryData() {
        // Form an array specifying which columns to return. 
        String[] projection = new String[] {
                AccountColumns._ID,                      
                AccountColumns.ACCESS_TOKEN,
                AccountColumns.REFRESH_TOKEN,
                AccountColumns.EMAIL,
                AccountColumns.EXPIRATION_DATE,
                AccountColumns.ACTIVE_USER
        };

        // Get the base URI for the Auth users table content provider.
        Uri authUsers =  AccountColumns.CONTENT_URI;

        String where = AccountColumns.ACTIVE_USER + "=?";
        String[] whereParams = new String[]{"1"};  
        
        // Make the query. 
        Cursor managedCursor = provider.query(authUsers,
                projection, // Which columns to return 
                where,       // Which rows to return (all rows)
                whereParams,       // Selection arguments (none)
                // Put the results in ascending order by email
                AccountColumns.EMAIL + " ASC");       

        assertNotNull(managedCursor);
        assertTrue(managedCursor.getCount() == 1);
        
        if (managedCursor.moveToFirst()) {

            String email; 
            String token; 
            String authCode;
            String expDate;
            int emailColumn = managedCursor.getColumnIndex(AccountColumns.EMAIL); 
            int tokenColumn = managedCursor.getColumnIndex(AccountColumns.ACCESS_TOKEN);
            int authColdeColumn = managedCursor.getColumnIndex(AccountColumns.REFRESH_TOKEN);
            int expDateColumn = managedCursor.getColumnIndex(AccountColumns.EXPIRATION_DATE);
            
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
                AccountColumns._ID,
                AccountColumns.ACCESS_TOKEN,
                AccountColumns.REFRESH_TOKEN,
                AccountColumns.EMAIL,
                AccountColumns.EXPIRATION_DATE
        };

        // Get the base URI for the Auth users table content provider.
        Uri authUsers =  AccountColumns.CONTENT_URI;

        // Make the query. 
        Cursor managedCursor = provider.query(authUsers,
                projection, // Which columns to return 
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                // Put the results in ascending order by name
                AccountColumns.EMAIL + " ASC");       

        assertNotNull(managedCursor);
        assertTrue(managedCursor.getCount() == 0);
        
 
    }
    
    /**
     * Example of how to insertData
     */
    private void insertData(){
        
        ContentValues values = new ContentValues();

         values.put(AccountColumns.ACCESS_TOKEN, "uno_at");
         values.put(AccountColumns.REFRESH_TOKEN, "uno_ac");
         values.put(AccountColumns.EMAIL, "tomas@gmail");
         values.put(AccountColumns.ACTIVE_USER, false);
         
         
         Uri uri = provider.insert(AccountColumns.CONTENT_URI, values);
         Log.d(TAG, "Result insert: " + uri);
         
         values.put(AccountColumns.ACCESS_TOKEN, "dos_at");
         values.put(AccountColumns.REFRESH_TOKEN, "dos_ac");
         values.put(AccountColumns.EMAIL, "juan@gmail");
         values.put(AccountColumns.ACTIVE_USER, false);
         values.put(AccountColumns.EXPIRATION_DATE, "2012-03-17 11:11:11.111");
         
         uri = provider.insert(AccountColumns.CONTENT_URI, values);
         Log.d(TAG, "Result insert: " + uri);
         
         values.put(AccountColumns.ACCESS_TOKEN, "tres_at");
         values.put(AccountColumns.REFRESH_TOKEN, "tres_ac");
         values.put(AccountColumns.EMAIL, "vitor@gmail");
         values.put(AccountColumns.ACTIVE_USER, true);
         
         uri = provider.insert(AccountColumns.CONTENT_URI, values);
         Log.d(TAG, "Result insert: " + uri);
    }
    
    /**
     * Example of how to modify data
     */
    private void modifyData(){
        ContentValues values = new ContentValues();

        values.put(AccountColumns.REFRESH_TOKEN, "vitor_ac");
        values.put(AccountColumns.ACCESS_TOKEN,"vitor_at");
        values.put(AccountColumns.ACTIVE_USER, false);
        
        String where = AccountColumns.EMAIL + "=?";
        String[] whereParams = new String[]{"vitor@gmail"};        
        int result = provider.update(AccountColumns.CONTENT_URI, values, where, whereParams);
        
        Log.d(TAG, "Result update1: " + result);
        
        values.clear();
        values.put(AccountColumns.ACTIVE_USER, true);
        
        where = AccountColumns.EMAIL + "=?";
        whereParams = new String[]{"juan@gmail"};        
        result = provider.update(AccountColumns.CONTENT_URI, values, where, whereParams);
        
        Log.d(TAG, "Result update2: " + result);
    }
    
    
    /**
     * Example of how to deleteData
     */
    private void deleteData(){
        
        String where = AccountColumns.EMAIL + "=?";
        String[] whereParams = new String[]{"tomas@gmail"};        
        int result = provider.delete(AccountColumns.CONTENT_URI, where, whereParams);
        Log.d(TAG, "Result delete: " + result);
        
        whereParams = new String[]{"juan@gmail"};        
        result = provider.delete(AccountColumns.CONTENT_URI, where, whereParams);
        Log.d(TAG, "Result delete: " + result);
        
        whereParams = new String[]{"vitor@gmail"};        
        result = provider.delete(AccountColumns.CONTENT_URI, where, whereParams);
        Log.d(TAG, "Result delete: " + result);
        
    }
    
    /**
     * Query Resource table
     */
    private void queryResources() {
        // Form an array specifying which columns to return. 
        String[] projection = new String[] {
                Resource._ID,
                Resource.LINK,
                Resource.NAME,
                Resource.DISPLAY_NAME
        };
        
        

        // Get the base URI for the Resources table.
        Uri resources = Uri.parse(AccountColumns.CONTENT_URI + "/1/" + "resources"); 

        // Make the query. 
        Cursor managedCursor = provider.query(resources,
                projection, // Which columns to return 
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                // Put the results in ascending order by email
                AccountColumns.EMAIL + " ASC");       


        Log.d(TAG, Integer.toString(managedCursor.getCount()));

        assertNotNull(managedCursor);
        assertTrue(managedCursor.getCount() == 1);

        if (managedCursor.moveToFirst()) {

            String email ="" ;
            String name = "";
            String displayName ="";
            int emailColumn = managedCursor.getColumnIndex(Resource.LINK); 
            int nameColumn = managedCursor.getColumnIndex(Resource.NAME);
            int displayNameColumn = managedCursor.getColumnIndex(Resource.DISPLAY_NAME);

            Log.d(TAG, "EMAIL\t\tNAME\t\tDISPLAY NAME");
            do {
                // Get the field values
                email = managedCursor.getString(emailColumn);
                name = managedCursor.getString(nameColumn);
                displayName = managedCursor.getString(displayNameColumn);

                Log.d(TAG, email + "\t\t" + name + "\t\t" + displayName);

            } while (managedCursor.moveToNext());

            assertEquals(displayName, "SALA 5");
        }
    }
    
    /**
     * Query data to assert that database is empty
     */
    private void queryDeletedResourceData() {
        // Form an array specifying which columns to return. 
        
        String[] projection = new String[] {
                Resource._ID,
                Resource.LINK
        };

     // Get the base URI for the Resources table.
        Uri resources = Uri.parse(AccountColumns.CONTENT_URI + "/1/" + "resources"); 

        // Make the query. 
        Cursor managedCursor = provider.query(resources,
                projection, // Which columns to return 
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                // Put the results in ascending order by name
                AccountColumns.EMAIL + " ASC");       

        assertNotNull(managedCursor);
        assertTrue(managedCursor.getCount() == 0);
        
 
    }
    
    /**
     * Example of how to insert resource Data
     */
    private void insertResourcesData(){
        
        ContentValues values = new ContentValues();

         values.put(Resource.NAME, "Sala1");
         values.put(Resource.LINK, "sala1@gmail");
         values.put(Resource.DISPLAY_NAME, "SALA 1");
         Uri resources = Uri.parse(AccountColumns.CONTENT_URI + "/1/" + "resources"); 
         Uri uri = provider.insert(resources, values);
         Log.d(TAG, "Result insert: " + uri);
         
    }
    
    /**
     * Example of how to modify resource data
     */
    private void modifyResourceData(){
        ContentValues values = new ContentValues();

        values.put(Resource.DISPLAY_NAME, "SALA 5");
        
        String where = Resource.LINK + "=? ";
        String[] whereParams = new String[]{"sala1@gmail"}; 
        Uri resources = Uri.parse(AccountColumns.CONTENT_URI + "/1/" + "resources"); 
        int result = provider.update(resources, values, where, whereParams);
        
        Log.d(TAG, "Result update: " + result);
    }
    
    /**
     * Example of how to deleteResourcesData
     */
    private void deleteResourcesData(){
        
        String where = Resource.LINK + "=?";
        String[] whereParams = new String[]{"sala1@gmail"};
        
        Uri resources = Uri.parse(AccountColumns.CONTENT_URI + "/1/" + "resources"); 
        
        int result = provider.delete(resources, where, whereParams);
        Log.d(TAG, "Result resource delete: " + result);
        
    }
    
    

}
