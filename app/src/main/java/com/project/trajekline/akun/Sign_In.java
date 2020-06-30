package com.project.trajekline.akun;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.project.trajekline.R;
import com.project.trajekline.config.AppController;
import com.project.trajekline.config.AuthData;
import com.project.trajekline.config.ServerAccess;
import com.project.trajekline.dashboard.Dashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sign_In extends AppCompatActivity {
    EditText username, password;
    Button login;
    TextView create_account;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        create_account = findViewById(R.id.create_account);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Sign_Up.class));
            }
        });
        login = findViewById(R.id.login);
        pd = new ProgressDialog(Sign_In.this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
        onLogin();
        requestMultiplePermissions();
    }
    private void onLogin(){
//        fungsi ini berfungsi untuk mengecek apakah user sudah pernah login apa belum. jika sudah maka akan diarahkan ke halaman dashboard
        if(AuthData.getInstance(this).isLoggedIn()){
            Sign_In.this.finish();
            startActivity(new Intent(getBaseContext(), Dashboard.class));
        }
    }
    private void requestMultiplePermissions(){

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
    private void doLogin(){
        pd.setMessage("Authenticating...");
        pd.setCancelable(false);
        pd.show();
        //mengecek username apakah kosong apa tidak. jika kosong maka akan menampilkan alert
        if (username.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Username Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            username.setFocusable(true);
            pd.dismiss();

        }else if (password.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Password Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            password.setFocusable(true);
            pd.dismiss();
        }else{
//            fungsi dibawah ini berfungsi untuk melakukan request ke api yang sudah tersedia
            StringRequest senddata = new StringRequest(Request.Method.POST, ServerAccess.LOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.cancel();
                    try {
//                        fungsi ini berfungsi untuk mengubah string menjadi jsonObject
                        JSONObject res = new JSONObject(response);
                        Log.d("pesan", res.toString());
//                        mengecek data apakah null atau tidak jika tidak null maka akan di eksekusi di blok if dibawah ini
                        if (res.getString("status").equals("true")) {
//                            berfungsi untuk mengambil object dengan nama data
                            JSONArray d = res.getJSONArray("data");
//                            JSONArray d = r.getJSONArray("");
                            JSONObject r = d.getJSONObject(0);
//                            menyimpan data login ke class authdata
                            AuthData.getInstance(getBaseContext()).setdatauser(r.getString("id"), r.getString("username"));
//                            menampilkan pesan jika login berhasil
                            Toast.makeText(Sign_In.this, "Berhasil Login", Toast.LENGTH_SHORT).show();
//                            berganti halaman setelah login berhasil
                            Intent intent = new Intent(getBaseContext(), Dashboard.class);

                            startActivity(intent);
                            pd.dismiss();
                        }else{
                            pd.dismiss();
                            Toast.makeText(Sign_In.this, "Gagal login Cek username dan password anda", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Sign_In.this, "Gagal login Cek username dan password anda", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.cancel();

                    Toast.makeText(getBaseContext(), "Gagal Login, "+error, Toast.LENGTH_SHORT).show();


                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
//                    mengirim request username dan password ke api
                    params.put("txt_user", username.getText().toString());
                    params.put("txt_pass", password.getText().toString());

                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(senddata);
        }
    }
}
