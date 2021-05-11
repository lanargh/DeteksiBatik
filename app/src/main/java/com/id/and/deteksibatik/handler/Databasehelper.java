package com.id.and.deteksibatik.handler;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Databasehelper  extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "DBbatik";
    private static final int DATABASE_VERSION = 7;
    Context context;
    public Databasehelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE batik (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "motif VARCHAR(15) NOT NULL," +
                "gambar BLOB(100) NOT NULL," +
                "lbp int(10) NOT NULL)";
        db.execSQL(sql);
        //db.execSQL("INSERT INTO batik VALUES (NULL,'Floral','floral',169 )");
        //db.execSQL("INSERT INTO batik VALUES (NULL,'Floral','floral1', 94 )");
        //db.execSQL("INSERT INTO batik VALUES (NULL,'Floral','floral2', 116 )");

        //db.execSQL("INSERT INTO batik VALUES (NULL,'Paisley', 'paisley', 140)");
        //db.execSQL("INSERT INTO batik VALUES (NULL,'Paisley', 'paisley2', 201)");
        //db.execSQL("INSERT INTO batik VALUES (NULL,'Paisley', 'paisley3', 74)");
    }

    public void loadCSV2(SQLiteDatabase db) throws IOException {
        String mCSVfile = "C:\\Users\\CV. GLOBAL SOLUSINDO\\AndroidStudioProjects\\DeteksiBatik\\DBBook.csv";
        AssetManager manager = context.getAssets();
        InputStream inStream = manager.open(mCSVfile);

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        db.beginTransaction();
        try {
            while ((line = buffer.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length != 5) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues cv = new ContentValues();
                cv.put("id", columns[0].trim());
                cv.put("motif", columns[1].trim());
                cv.put("gambar", columns[2].trim());
                cv.put("lbp", columns[3].trim());
                cv.put("latih", columns[4].trim());
                db.insert("batik", null, cv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

        // TODO: handle exception
        String sql = "DROP TABLE IF EXISTS batik";
        db.execSQL(sql);
        onCreate(db);
    }
}
