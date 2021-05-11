package com.id.and.deteksibatik;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.id.and.deteksibatik.handler.Databasehelper;
import com.id.and.deteksibatik.handler.RequestHandler;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MatchingActivity extends AppCompatActivity {
    Databasehelper dbcenter;
    SQLiteDatabase db;
    Intent in;
    Button btn;
    TextView txt;
    ProgressDialog loading;
    Bitmap bitmap;
    private Timer timer;
    String ImageDecode, urlgambar, token, status, name, reason, uploadImage = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);
        setTitle("Matching Picture");
        in = getIntent();
        in.getIntExtra("jml", 0);
        Log.wtf("inten jml", "" + in.getIntExtra("jml", 0));

        dbcenter = new Databasehelper(this);
        txt = (TextView) findViewById(R.id.txt_matching);
        btn = (Button) findViewById(R.id.scan_pencocokan);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureMatching();
            }
        });
    }

    private void PictureMatching() {
        db = dbcenter.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT count(*) as ttl from batik where lbp = '" + in.getIntExtra("jml", 0) + "'", null);
        cur.moveToFirst();
        if (cur.getDouble(0) > 0) {
            txt.setText("Motif" + cur.getString(1));
        } else {
            KirimGambar();
        }

    }

    private void KirimGambar() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MatchingActivity.this, "Scan Image", "Please wait...", true, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject res = null;
                try {
                    Log.wtf("data", s);
                    res = new JSONObject(s);
                    urlgambar = res.getString("data");
                    scanImage();
                } catch (JSONException e) {
                    loading.dismiss();
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(), "Terjadi Kesalahan", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap
                        bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put("img", uploadImage);

                String result = rh.sendPostRequest("http://cobauploaddokumen.esy.es/", data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void scanImage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.cloudsightapi.com/image_requests",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject res = new JSONObject(response);
                            token = res.getString("token");
                            Log.wtf("TOKEN CLOUD SIGHT", res.getString("token"));
                            timer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    getRequest();
                                }
                            }, 0, 5000);
                        } catch (JSONException e) {
                            loading.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Log.wtf("Error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("image_request[remote_image_url]", "http://cobauploaddokumen.esy.es/" + urlgambar);
                params.put("image_request[locale]", "en-US");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // Removed this line if you dont need it or Use application/json
                params.put("authorization", "CloudSight Iqq0Bl9wWlwh0UXS--ydlw");
                params.put("postman-token", "63c48557-30d4-17c7-aef1-18181a71492f");
                params.put("cache-control", "no-cache");
                params.put("content-type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void getRequest() {
        if ("completed".equals(status)) {
            loading.dismiss();
            timer.cancel();
            Log.wtf("name", name);
            TastyToast.makeText(getApplicationContext(), "Scanning berhasil", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        } else if ("skipped".equals(status) || "not found".equals(status) || "timeout".equals(status)) {
            loading.dismiss();
            timer.cancel();
            Log.wtf("response", reason);
            TastyToast.makeText(getApplicationContext(), "Terjadi Kesalahan", TastyToast.LENGTH_LONG, TastyToast.ERROR);
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.cloudsightapi.com/image_responses/" + token,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject res = new JSONObject(response);
                                Log.wtf("STATUS", res.getString("status"));
                                status = res.getString("status");
                                if ("completed".equals(status)) {
                                    name = res.getString("name");
                                } else if ("skipped".equals(status)) {
                                    reason = res.getString("reason");
                                }
                            } catch (JSONException e) {
                                loading.dismiss();
                                e.printStackTrace();
                                TastyToast.makeText(getApplicationContext(), "Terjadi Kesalahan", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.wtf("Error", error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    // Removed this line if you dont need it or Use application/json
                    params.put("authorization", "CloudSight Iqq0Bl9wWlwh0UXS--ydlw");
                    params.put("postman-token", "63c48557-30d4-17c7-aef1-18181a71492f");
                    params.put("cache-control", "no-cache");
                    params.put("content-type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent home = new Intent(getApplicationContext(), ScanActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.putExtra("EXIT", true);
        startActivity(home);
        finish();
    }
}
