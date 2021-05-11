package com.id.and.deteksibatik;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.id.and.deteksibatik.handler.BitmapAdapter;
import com.id.and.deteksibatik.handler.Databasehelper;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class ScanActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    private static final int RQS_OPEN_DOCUMENT_TREE = 3;
    private int jml;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;
    private int t = 0;
    Intent data;
    Bitmap bitmap;
    ProgressDialog loading;
    GridView imggrid;
    ImageView imgbiner, imageView;
    Button btn_select_image, btn_pro_img, btn_insert_img;
    String ImageDecode, urlgambar, token, status, name, reason, uploadImage = "";
    Databasehelper dbcenter;
    SQLiteDatabase db;
    private Timer timer;
    File pathDir;

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.wtf(TAG, "OpenCV not loaded");
        } else {
            Log.wtf(TAG, "OpenCV loaded");
        }
    }

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    //DO YOUR WORK/STUFF HERE
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        setTitle("Scan Motif");
        timer = new Timer();
        dbcenter = new Databasehelper(this);
        db = dbcenter.getReadableDatabase();
        btn_select_image = (Button) findViewById(R.id.ambil_gambar);
        imageView = (ImageView) findViewById(R.id.img_foto);
        imgbiner = (ImageView) findViewById(R.id.img_binary);
        imggrid = (GridView) findViewById(R.id.img_grid);

        //tombol untuk mengambil gambar
        btn_select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageOption();
            }
        });

        //memprosses gambar
        btn_pro_img = (Button) findViewById(R.id.scan_gambar);
        btn_pro_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LBP(data);
            }
        });

       /* btn_insert_img = (Button) findViewById(R.id.insert_gambar);
        btn_insert_img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //ChooseFolderIntent();
                //String path = "/sdcard/Pictures/Batik300";
                String path = "/sdcard/3mulya/Batik300";
                pathDir = new File (path);
                Filewalker fw = new Filewalker();
                fw.walk(pathDir);
            }
        });
*/
    }

    //fungsi untuk mengambil gambar
    private void selectImageOption() {
        final CharSequence[] items = {"Capture Photo", "Choose from Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
        builder.setTitle("Add Photo!!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Capture Photo")) {
                    TakePictureIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    ChooseImageIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //perintah untuk mengambil gambar dari foto
    private void TakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            loading = ProgressDialog.show(ScanActivity.this, "Mengambil gambar", "Please wait...", true, false);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }

    //perintah untuk mengambil gambar dari galeri
    private void ChooseImageIntent() {
        Intent chooseImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (chooseImageIntent.resolveActivity(getPackageManager()) != null) {
            loading = ProgressDialog.show(ScanActivity.this, "Mengambil gambar", "Please wait...", true, false);
            startActivityForResult(chooseImageIntent, REQUEST_IMAGE_GALLERY);
        }
    }

    // perintah untuk menampilkan gambar
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imageView.setImageBitmap(imageBitmap);
            //toBinary(imageBitmap);
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            bitmap = drawable.getBitmap();

        }
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            Uri URI = data.getData();
            String[] FILE = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(URI, FILE, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(FILE[0]);
            ImageDecode = cursor.getString(columnIndex);
            cursor.close();
            imageView.setImageBitmap(BitmapFactory.decodeFile(ImageDecode));
            //toBinary(BitmapFactory.decodeFile(ImageDecode));
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), URI);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loading.dismiss();
    }

    // prerintah yang mempunyai fungsi untuk memberi hak akses membaca memori eksternal
    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    // langkah akhir lbp untuk merubah gambar ke bentuk biner
    private Bitmap toBinary(Bitmap bmpOriginal) {
        int width, height, threshold;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        threshold = 127;
        System.out.println("Height : " + height + " Width : " + width);

        //final Bitmap bmpBinary = null;
        Bitmap bmpBinary = Bitmap.createBitmap(bmpOriginal);
        t = 0;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                //get one pixel color
                int pixel = bmpOriginal.getPixel(x, y);
                int red = Color.red(pixel) & 0xff;
                int green = Color.green(pixel) & 0xff;
                int blue = Color.blue(pixel) & 0xff;
                System.out.println("pixel " +x +" , " + y);
                System.out.println("R: " +red);
                System.out.println("G: " +green);
                System.out.println("B: " +blue);
                //get grayscale value
                int gray = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                System.out.println("Gray: " +gray);
                t += gray;
                System.out.println("Biner value: " +t);
            }
        }
        //hasil akhir biner
        jml = t / (width * height);
        System.out.println("CIRI : " +jml);
        Log.wtf("Hasil LBP", "" + jml);

        ArrayList<Bitmap> capture = new ArrayList<>();

        //membaca nilai lbp dari data base
        dbcenter = new Databasehelper(this);
        System.out.println("dbcenter");
        db = dbcenter.getReadableDatabase();

        System.out.println("db");
        //Cursor cur = db.rawQuery("SELECT * from batik where (lbp - "+ jml + ") < 1", null);
        Cursor cur = db.rawQuery("SELECT * from batik where lbp = " +jml, null);
        System.out.println("cur");
        System.out.println(cur.getCount() + "row " + cur.getCount());

        byte[] lbp_db;
        if (cur != null && cur.moveToFirst()){
            System.out.println("Banyaknya row " +cur.getCount());
            while (!cur.isAfterLast()){
                lbp_db = cur.getBlob(2);
                capture.add(getImage(lbp_db));
                //System.out.println("LBP : " + lbp_db);
                //Log.wtf("data", "" + lbp_db);
                cur.moveToNext();
            }
            System.out.println("GRID MASUK");
            Bitmap[] hasilCapture = new Bitmap [capture.size()];
            capture.toArray(hasilCapture);
            BitmapAdapter bitmapAdapter = new BitmapAdapter(ScanActivity.this, hasilCapture);
            imggrid.setAdapter(bitmapAdapter);
        }

        //db.close();

        //final String motif = cur.getString(1);
        //Log.wtf("Perbandingan", "" + motif);

        //menampilkan pemberitahuan hasil deteksi motif
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TastyToast.makeText(getApplicationContext(), "Motif : " + motif, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                Log.wtf("motif", "" + motif);
            }
        }, 2000);
*/


        return bmpBinary;
    }

    // langkah awal lbp untuk merubah gambar ke grayscale / warna abu abu
    private Bitmap toGrayScale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayScale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayScale);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        c.drawBitmap(bmpOriginal, 0, 0, paint);

        return bmpGrayScale;
    }

    // perintah untuk memproses gambar menggunakan LBP opencv
    private void LBP(Intent data) {
        this.data = new Intent();
        imageView = (ImageView) findViewById(R.id.img_foto);
        imageView.setImageBitmap(toGrayScale(bitmap));

        Log.wtf("gambar", "" + bitmap);
        boolean sukses = false;

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        imgbiner = (ImageView) findViewById(R.id.img_binary);
        Bitmap binaryBitmap = toBinary(bitmap);
        imgbiner.setImageBitmap(binaryBitmap);
        Date date = new Date();
        int numb = 0;

    /*
        String name = "kunci" + numb + 1;
        File image = new File(bitmap.toString(), name + ".PNG");
        sukses = false;

        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(image);
            binaryBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);

            outStream.flush();
            outStream.close();
            sukses = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_REQ_CODE);
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this,
                    mOpenCVCallBack);
            return;
        }
    }

    //kembali ke menu sebelumnya
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent home = new Intent(getApplicationContext(), MainActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.putExtra("EXIT", true);
        startActivity(home);
        finish();
    }

    public static byte[] getBytes(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        //stream.close();
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public void addEntry(String name, byte[] image, int lbp) throws SQLiteException {
        ContentValues cv = new  ContentValues();
        cv.put("motif",    name);
        cv.put("gambar",   image);
        cv.put("lbp",   lbp);
        db.insert( "batik", null, cv );
    }

    class background_thread extends AsyncTask<File[], Void, String> {

        @Override
        protected String doInBackground(File[]... strings) {
            File[] list = strings[0];
            int i = 1;
            for (File f : list) {
                    //Log.d("", "File: " + f.getAbsoluteFile());
                    System.out.println(f.getAbsoluteFile());
                    Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsoluteFile().getPath());
                    bitmap = toGrayScale(bitmap);
                    toBinary(bitmap);
                    String kelas = f.getAbsoluteFile().toString().length()==32 ? f.getAbsoluteFile().toString().substring(24,28) : f.getAbsoluteFile().toString().substring(24,29);
                try {
                    addEntry(kelas, getBytes(bitmap), jml);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //insertImg(i, bitmap);
            }
            return null;
        }
    }

    public class Filewalker {

        public void walk(File root) {
            File[] list = root.listFiles();
            new background_thread().execute(list);

        }
    }
}
