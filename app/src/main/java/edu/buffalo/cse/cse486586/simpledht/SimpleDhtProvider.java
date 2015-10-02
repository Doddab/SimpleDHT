package edu.buffalo.cse.cse486586.simpledht;

import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import android.content.ContentResolver;
import android.content.Context;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;

import android.database.MatrixCursor;




public class SimpleDhtProvider extends ContentProvider {

        String TAG = SimpleDhtActivity.class.getSimpleName();
    public static String pn;
    public static int slp2 = 0;
    public static final String KEY_FIELD = "key";
    public static final String VALUE_FIELD = "value";
    public static String AUTHORITY = "edu.buffalo.cse.cse486586.simpledht.provider";
    public static int slp = 0;
    public static Cursor csr2;
    public static DBHelper dbc;
    //package edu.buffalo.cse.cse486586.simpledht;

    /**
     * Created by user on 1/4/15.
     */

    public static boolean flag = true;
    public static boolean cc = true;

    public static boolean flag1 = true;
    public static int d1 = 0;
    public static int dk = 0;
    public static String pdsr = "0";
    public static String scsr = "0";
    public static Uri mUri;
    public static ContentResolver mContentResolver;
    public static Context cxt;
    public static Cursor csr3;
    int SERVER_PORT = 10000;
    public static String ps;




    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        boolean empty;
        empty = dbcheck();

        if (selection != null)
        {
            if(selection.equals("\"*\""))
            {
                if (empty == true)
                {
                    SQLiteDatabase db = dbc.getWritableDatabase();
                    int row = db.delete(dbc.TABLE_NAME,null,null);
                    String csr = String.valueOf(row);
                    Serialization msg1 = new Serialization(0,ps,scsr,null,null,"\"*\"",null,null,false,csr);
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg1);
                }
                else
                {
                    Serialization msg1 = new Serialization(0,ps,scsr,null,null,"\"*\"",null,null,false,"0");
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg1);
                }
                while(cc);
                cc = true;
                return d1;

            }
            else if(selection.equals("\"@\"") && empty == true)
            {
                SQLiteDatabase db = dbc.getWritableDatabase();
                int row = db.delete(dbc.TABLE_NAME,null,null);
                return row;

            }
            else
            {
                SQLiteDatabase db = dbc.getWritableDatabase();
                int row = db.delete(dbc.TABLE_NAME,"key = '"+selection+"'",null);
                if(row == 0)
                {
                    Serialization s2 = new Serialization(0,ps,scsr,null,null,selection,null,pn,false,"0");

                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,s2);
                    while(flag == true);
                    flag = true;
                    return dk;
                }
                else
                    return 0;
            }


        }
        else if (pdsr.equals("0") && empty == true && scsr.equals("0"))
        {
            if(!selection.equals("\"@\"") || !selection.equals("\"*\""))
            {


                SQLiteDatabase db = dbc.getWritableDatabase();
                int row = db.delete(dbc.TABLE_NAME,"key = '"+selection+"'",null);
                return row;
            }
            else
            {
                SQLiteDatabase db = dbc.getWritableDatabase();
                int row = db.delete(dbc.TABLE_NAME,null,null);
                return row;

            }
        }
        else
            return 0;
    }


    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        String RequestorKey = null;
        String pred = null;
        String me = null;


        try{
            if(pdsr == "0" || scsr == "0")
            {
              
                SQLiteDatabase db = dbc.getWritableDatabase();
               db.insertWithOnConflict(DBHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                return uri;
            }
            else
            {
                String key=values.getAsString(dbc.KEY);
                String val=values.getAsString(dbc.VALUE);
                RequestorKey = genHash(key);
                me = genHash(ps);
                pred = genHash(String.valueOf(Integer.parseInt(pdsr)/2));

                if(((RequestorKey.compareTo(pred)>0) && (RequestorKey.compareTo(me)<= 0))
                        ||
                        ((me.compareTo(pred)<0) && ((RequestorKey.compareTo(pred)>0) ||(RequestorKey.compareTo(me)<0))))
                {

                    SQLiteDatabase db = dbc.getWritableDatabase();
                    db.insert(DBHelper.TABLE_NAME,null,values);
                    return uri;
                }
                else
                {
                    Serialization s2 = new Serialization(1,ps,scsr,null,null,key,val,null,false,null);
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,s2);

                }
            }

        }catch(Exception e)
        {
            System.out.println("Ins failed");
        }
        return uri;

    }

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        cxt=getContext();
        mUri = buildUri("content","edu.buffalo.cse.cse486586.simpledht.provider");
        mContentResolver = cxt.getContentResolver();
        dbc = new DBHelper(getContext());
        TelephonyManager tel = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
        ps = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        pn = getn(ps);


        try
        {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,serverSocket);

            //calling server to listen on socket
        }catch(Exception e){
            Log.v("ERROR", "on Ser");
        }

        Serialization msg = new Serialization(5,ps,"11108",null,null,null,null,null,false,null);

        try {
           // if(!(genHash(ps).equals(genHash("5554")))){
            if((genHash(ps).equals(genHash("5556"))) || (genHash(ps).equals(genHash("5558"))) || (genHash(ps).equals(genHash("5560"))) || (genHash(ps).equals(genHash("5562")))){
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg);

            }
        }catch(Exception e){
            System.out.println("create DHT failed");
        }

        return true;

    }

    public static class ClientTask extends AsyncTask<Serialization, Void, Void>
    {
        @Override
        protected Void doInBackground(Serialization... msgs)
        {
            try
            {


                Socket socket = new Socket(InetAddress.getByAddress(new byte[]
                        { 10, 0, 2, 2 }),Integer.parseInt(msgs[0].spp2));
                ObjectOutputStream msgbuffer = null;
                msgbuffer = new ObjectOutputStream(socket.getOutputStream());
                msgbuffer.writeObject(msgs[0]);	//Send Request to Remote port
                msgbuffer.flush();
                msgbuffer.close();
                socket.close();
            }catch(Exception e){
                System.out.println("Error in Client"+e.getMessage());
            }
            return null;
        }

    }


    static class DBHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME="DHT_DB";
        public static final String TABLE_NAME="DHT_CP";
        public static String KEY="key";
        public static String VALUE="value";


        public DBHelper(Context cxt) {
            super(cxt, DATABASE_NAME, null, 1);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL( " CREATE TABLE "+TABLE_NAME+" ("+KEY+" TEXT NOT NULL PRIMARY KEY, "+VALUE+" TEXT NOT NULL);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(DBHelper.class.getName(),"New Database to be inserted");
            db.execSQL("DROP TABLE IF EXISTS "+KEY+","+VALUE+"");
            onCreate(db);
        }
    }

    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }

    public static String getn(String no){
        switch(no){
            case "5554":return "11108";
            case "5556":return "11112";
            case "5558":return "11116";
            case "5560":return "11120";
            case "5562":return "11124";
            default:return null;

        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // TODO Auto-generated method stub
        boolean check;
        check = dbcheck();
         if (selection.equals("\"*\""))
        {
            if(pdsr.equals("0") && scsr.equals("0"))
            {
                if(check == true)
                {

                    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                    qb.setTables(DBHelper.TABLE_NAME);
                    SQLiteDatabase db = dbc.getWritableDatabase();
                    Cursor cursor = qb.query(db,null,null, null,null,null,null);

                    cursor.setNotificationUri(getContext().getContentResolver(), uri);


                    return cursor;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                if(check == true)
                {

                    String csr = "";
                    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                    qb.setTables(DBHelper.TABLE_NAME);
                    SQLiteDatabase db = dbc.getWritableDatabase();
                    Cursor cursor = qb.query(db,null,null, null,null,null,null);
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);


                    for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                    {
                        csr += cursor.getString(0)+","+cursor.getString(1)+",";
                    }
                    Serialization s2 = new Serialization(2,ps,scsr,null,null,"\"*\"",null,pn,false,csr);

                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,s2);
                }
                else
                {

                    Serialization s2 = new Serialization(2,ps,scsr,null,null,"\"*\"",null,pn,false,"");

                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,s2);
                }

                while(slp2 == 0);
                slp2 = 0;

                return csr3;

            }

        }
        else if (selection.equals("\"@\""))
        {
            if(check == true){
                if(!pdsr.equals("0") || !scsr.equals("0"))
                {

                    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                    qb.setTables(DBHelper.TABLE_NAME);
                    SQLiteDatabase db = dbc.getWritableDatabase();
                    Cursor cursor = qb.query(db,null,null, null,null,null,null);
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);

                    return cursor;

                }
                else
                {

                    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                    qb.setTables(DBHelper.TABLE_NAME);
                    SQLiteDatabase db = dbc.getWritableDatabase();
                    Cursor cursor = qb.query(db,null,null, null,null,null,null);
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);

                    return cursor;

                }
            }
            else
            {
                return null;
            }
        }
        else
        {
            if(pdsr.equals("0") && scsr.equals("0"))
            {
                if(check == true)
                {

                    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                    qb.setTables(DBHelper.TABLE_NAME);
                    SQLiteDatabase db = dbc.getWritableDatabase();
                    Cursor cursor = qb.query(db,null,dbc.KEY+"="+"'"+selection+"'", null,null,null,null);
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);

                    return cursor;
                }
                else
                    return null;
            }
            else
            {
                if (check == false)
                {

                    Serialization s2 = new Serialization(2,ps,scsr,null,null,selection,null,pn,false,"");

                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,s2);
                    while(slp == 0);
                    slp = 0;
                    return csr2;


                }
                else
                {

                    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                    qb.setTables(DBHelper.TABLE_NAME);
                    SQLiteDatabase db = dbc.getWritableDatabase();
                    Cursor cursor = qb.query(db,null, DBHelper.KEY +"="+"'"+selection+"'", null,null,null,null);
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                    if(cursor.getCount() == 0)
                    {

                        Serialization s2 = new Serialization(2,ps,scsr,null,null,selection,null,pn,false,"");


                        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,s2);
                        while(slp == 0);
                        slp = 0;
                        return csr2;
                    }
                    else
                    {
                        return cursor;
                    }
                }
            }
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static boolean dbcheck(){
        boolean ff = false;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DBHelper.TABLE_NAME);
        SQLiteDatabase db = dbc.getWritableDatabase();
        Cursor cursor = qb.query(db,null,null, null,null,null,null);
        if(cursor.getCount() > 0){
            ff=true;
        }
        if(cursor.getCount() == 0){
            ff=false;
        }

        cursor.close();
        return ff;
    }
    public static String genHash(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] sha1Hash = sha1.digest(input.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : sha1Hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }


    /* Server Task*/
    public class ServerTask extends AsyncTask<ServerSocket, String, Void> {
        String Requestport = null;
        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];

            while(true)
            {
                try
                {
                    Socket temmpsock = serverSocket.accept();
                    ObjectInputStream ois = new ObjectInputStream(temmpsock.getInputStream());
                    try {
                        Serialization m = (Serialization) ois.readObject();
                        Requestport = m.n;


                        if(m.id == Serialization.rc3)
                        {
                            try {
                                joinsection(m);
                            } catch (NoSuchAlgorithmException e) {

                                e.printStackTrace();
                            }
                        }
                        else if(m.id == Serialization.r1)
                        {
                            setrequester(m);
                        }
                        else if(m.id == Serialization.rp2)
                        {
                            set_ps(m);
                        }
                        else if(m.id == Serialization.ins)
                        {
                            insert(m);
                        }
                        else if(m.id == Serialization.del)
                        {
                            if(m.key.equals("\"*\""))
                            {
                                delete(m);
                            }
                            else
                            {
                                dk(m);
                            }


                        }
                        else if(m.id == Serialization.que)
                        {
                            if(m.key.equals("\"*\""))
                            {
                                query(m);
                            }
                            else
                            {
                                keyquery(m);
                            }
                        }
                        else{
                            System.out.println("Invalid id");
                        }
                        publishProgress("< "+SimpleDhtProvider.pdsr+" : "+SimpleDhtProvider.scsr+" >\n");


                    }
                    catch (ClassNotFoundException e) {

                        e.printStackTrace();
                    }
                }catch(IOException ex){
                    System.out.println("Message receive failed:");
                }

            }
        }


        private void dk(Serialization m) {

            if(m.spp.equals(SimpleDhtProvider.pn))
            {
                SimpleDhtProvider.dk = Integer.parseInt(m.csr);
                SimpleDhtProvider.flag = false;
            }
            else if(m.flag == false)
            {

                SQLiteDatabase db = SimpleDhtProvider.dbc.getWritableDatabase();
                int row = db.delete(SimpleDhtProvider.dbc.TABLE_NAME,"key = '"+m.key+"'",null);


                if(row>0)
                {
                    m.csr = String.valueOf(row);

                    Serialization msg1 = new Serialization(0, SimpleDhtProvider.ps, SimpleDhtProvider.scsr,
                            null, null, m.key, null, m.spp, true,m.csr);
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg1);
                }
                else
                {

                    Serialization msg1 = new Serialization(0, SimpleDhtProvider.ps, SimpleDhtProvider.scsr,
                            null, null, m.key, null, m.spp, false,m.csr);
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg1);
                }
            }
            else
            {

                Serialization msg1 = new Serialization(0,SimpleDhtProvider.ps, SimpleDhtProvider.scsr,
                        null, null, m.key, null, m.spp,true,m.csr);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg1);
            }

        }


        private void delete(Serialization m) {

            if(m.spp.equals(SimpleDhtProvider.pn))
            {
                SimpleDhtProvider.d1 = Integer.parseInt(m.csr);
                SimpleDhtProvider.cc = false;
            }
            else
            {
                if(SimpleDhtProvider.dbcheck() == true)
                {
                    SQLiteDatabase db = SimpleDhtProvider.dbc.getWritableDatabase();
                    int row = db.delete(SimpleDhtProvider.dbc.TABLE_NAME,null,null);

                    int totaldeleted =	row + Integer.parseInt(m.csr);
                    String csr = String.valueOf(totaldeleted);
                    Serialization s2 = new Serialization(0,SimpleDhtProvider.ps,SimpleDhtProvider.scsr,null,null,"\"*\"",null,m.spp,false,csr);
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,s2);
                }
                else
                {

                    Serialization s2 = new Serialization(0,SimpleDhtProvider.ps,SimpleDhtProvider.scsr,null,null,"\"*\"",null,m.spp,false,m.csr);
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,s2);
                }

            }

        }


        private void keyquery(Serialization m) {


            if(m.spp.equals(SimpleDhtProvider.pn))
            {

                String feild[]={SimpleDhtProvider.KEY_FIELD,SimpleDhtProvider.VALUE_FIELD};
                MatrixCursor csr= new MatrixCursor(feild);
                String str[] = m.csr.split(",");
                csr.addRow(str);
                SimpleDhtProvider.csr2 = csr;
                SimpleDhtProvider.slp = 1;
            }
            else if(m.flag == false)
            {
                boolean check = SimpleDhtProvider.dbcheck();
                if (check == true)
                {
                    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
                    qb.setTables(DBHelper.TABLE_NAME);
                    SQLiteDatabase db = SimpleDhtProvider.dbc.getWritableDatabase();
                    Cursor cursor = qb.query(db,null,SimpleDhtProvider.dbc.KEY+"="+"'"+m.key+"'", null,null,null,null);
                    if(cursor.getCount()>0)
                    {
                        cursor.moveToFirst();
                        m.csr = cursor.getString(0)+","+cursor.getString(1);

                        Serialization msg1 = new Serialization(2, SimpleDhtProvider.ps, SimpleDhtProvider.scsr,
                                null, null, m.key, null, m.spp, true,m.csr);

                        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg1);
                    }
                    else
                    {

                        Serialization msg1 = new Serialization(2, SimpleDhtProvider.ps, SimpleDhtProvider.scsr,
                                null, null, m.key, null, m.spp, false,m.csr);

                        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg1);
                    }
                }
                else
                {
                    m.csr += ""+m.csr;
                    Serialization msg1 = new Serialization(2, SimpleDhtProvider.ps, SimpleDhtProvider.scsr,
                            null, null, m.key, null, m.spp, false,m.csr);

                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg1);
                }

            }
            else
            {

                Serialization msg1 = new Serialization(2,SimpleDhtProvider.ps, SimpleDhtProvider.scsr,
                        null, null, m.key, null, m.spp, true,m.csr);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg1);
            }
        }


        private void insert(Serialization m) {
            ContentValues cv = new ContentValues();
            cv.put(SimpleDhtProvider.KEY_FIELD, m.key);
            cv.put(SimpleDhtProvider.VALUE_FIELD, m.value);
            SimpleDhtProvider.mContentResolver.insert(SimpleDhtProvider.mUri, cv);
        }

        private void query(Serialization m)
        {
            if(m.spp.equals(SimpleDhtProvider.pn))
            {
                if(m.csr.length()>=1)
                {
                    String feild[]={SimpleDhtProvider.KEY_FIELD,SimpleDhtProvider.VALUE_FIELD};
                    MatrixCursor csr= new MatrixCursor(feild);
                    String str[] = m.csr.split(",");
                    for(int i=0;i<str.length;i=i+2)
                    {
                        String entry[]={str[i],str[i+1]};
                        csr.addRow(entry);
                        csr.moveToLast();

                    }

                    SimpleDhtProvider.csr3 = csr;
                    SimpleDhtProvider.slp2 = 1;
                }
                else
                {
                    SimpleDhtProvider.csr3 = null;
                    SimpleDhtProvider.slp2 = 1;
                }
            }
            else
            {
                boolean check = SimpleDhtProvider.dbcheck();
                if(check == true)
                {

                    SQLiteDatabase db = SimpleDhtProvider.dbc.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM "+SimpleDhtProvider.dbc.TABLE_NAME, null);
                    String csr = "";
                    for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                    {
                        csr += cursor.getString(0)+","+cursor.getString(1)+",";
                    }
                    m.csr += csr;
                    Serialization s2 = new Serialization(2,SimpleDhtProvider.ps,SimpleDhtProvider.scsr,
                            null,null,"\"*\"",null,m.spp,false,m.csr);

                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,s2);
                }
                else
                {
                    m.csr += ""+m.csr;

                    Serialization s2 = new Serialization(2,SimpleDhtProvider.ps,SimpleDhtProvider.scsr,
                            null,null,"\"*\"",null,m.spp,false,m.csr);

                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,s2);
                }
            }
        }

        private void joinsection (Serialization m) throws NoSuchAlgorithmException
        {

            String pred = null;
            String me = null;
            String requestor = null;
            String requestport = null;
            String scsr = null;
            String ppnno = null;
            String pdsr = null;
            String pnno = null;

            requestor = SimpleDhtProvider.genHash(m.n);
            pred = SimpleDhtProvider.genHash(String.valueOf(((Integer.parseInt
                    (SimpleDhtProvider.pdsr))/2)));
            me = SimpleDhtProvider.genHash(SimpleDhtProvider.ps);

            pnno = SimpleDhtProvider.ps;
            ppnno = SimpleDhtProvider.getn(pnno);
            requestport = SimpleDhtProvider.getn(m.n);
            scsr = SimpleDhtProvider.scsr;
            pdsr = SimpleDhtProvider.pdsr;


            if(SimpleDhtProvider.pdsr.equals("0") && SimpleDhtProvider.scsr.equals("0")
                    && SimpleDhtProvider.ps.equals("5554"))
            {


                String n = SimpleDhtProvider.getn(SimpleDhtProvider.ps);

                Serialization msg1 = new Serialization(3,pnno,requestport,n,n,null,null,null,false,null);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg1);
                SimpleDhtProvider.pdsr = requestport;
                SimpleDhtProvider.scsr = requestport;

            }

            else if(((requestor.compareTo(pred)>0) && (requestor.compareTo(me)<= 0))
                    ||
                    ((me.compareTo(pred)<0) && ((requestor.compareTo(pred)>0) ||(requestor.compareTo(me)<0))))
            {


                Serialization msg1 = new Serialization(3,pnno,requestport,pdsr,ppnno,null,null,null,false,null);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg1);

                Serialization msg2 = new Serialization(4,pnno,pdsr,null,requestport,null,null,null,false,null);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg2);

                SimpleDhtProvider.pdsr = requestport;
            }

            else
            {

                Serialization s2 = new Serialization(5,Requestport,scsr,null,null,null,null,null,false,null);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,s2);
            }
        }

        private void setrequester(Serialization m) {
            SimpleDhtProvider.scsr = m.scsr;
            SimpleDhtProvider.pdsr = m.pdsr;
        }

        private void set_ps(Serialization m) {
            SimpleDhtProvider.scsr = m.scsr;
        }

        protected void onProgressUpdate(String... strings ){
            super.onProgressUpdate(strings[0]);
            SimpleDhtActivity.DHTProvview.append(strings[0] + "\n");

            return;
        }
    }
}
