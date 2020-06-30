package com.project.trajekline.akun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.project.trajekline.R;
import com.project.trajekline.config.AppController;
import com.project.trajekline.config.AuthData;
import com.project.trajekline.config.ServerAccess;
import com.project.trajekline.dashboard.Dashboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Edit_Profile extends AppCompatActivity {
    EditText username, email, alamat, no_telp, nama_depan, nama_belakang, tipe_identitas, no_identitas, no_rek, nama_rek;
    ProgressDialog pd;
    Button simpan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        Intent data = getIntent();
        pd = new ProgressDialog(Edit_Profile.this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        alamat = findViewById(R.id.alamat);
        no_telp = findViewById(R.id.no_telp);
        nama_depan = findViewById(R.id.nama_depan);
        nama_belakang = findViewById(R.id.nama_belakang);
        tipe_identitas = findViewById(R.id.tipe_identitas);
        no_identitas = findViewById(R.id.no_identitas);
        no_rek = findViewById(R.id.no_rek);
        nama_rek = findViewById(R.id.nama_rek);
        simpan = findViewById(R.id.simpan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });
        loadJson();
    }
    private void loadJson()
    {
        StringRequest senddata = new StringRequest(Request.Method.GET, ServerAccess.auth+"info/"+ AuthData.getInstance(getBaseContext()).getNama(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONObject data = res.getJSONObject("data");
                    nama_depan.setText(data.getString("nama_depan"));
                    nama_belakang.setText(data.getString("nama_belakang"));
                    email.setText(data.getString("email"));
                    no_telp.setText(data.getString("no_telepon"));
                    alamat.setText(data.getString("alamat"));
                    username.setText(data.getString("username"));
                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                    Log.d("pesan", "error "+e.getMessage());
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(senddata);
    }
    private void simpan(){
        pd.setMessage("Proses...");
        pd.setCancelable(false);
        pd.show();
        if (nama_depan.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Nama Depan Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            nama_depan.setFocusable(true);
            pd.dismiss();
        }else if (nama_belakang.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Nama Belakang Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            nama_belakang.setFocusable(true);
            pd.dismiss();
        }else if (email.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Email Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            email.setFocusable(true);
            pd.dismiss();
        }else if (alamat.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Alamat Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            alamat.setFocusable(true);
            pd.dismiss();
        }else if (no_telp.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "No Telepon Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            no_telp.setFocusable(true);
            pd.dismiss();
        }else if (username.getText().toString().isEmpty()) {
            Toast.makeText(getBaseContext(), "Username Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
            username.setFocusable(true);
            pd.dismiss();
        }else{
            StringRequest senddata = new StringRequest(Request.Method.POST, ServerAccess.auth+"update", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.cancel();
                    try {
                        JSONObject res = new JSONObject(response);
                        Log.d("pesan", res.toString());
//                        if(res.getBoolean("status")){
//                            Toast.makeText(Edit_Profile.this, res.getString("message"), Toast.LENGTH_SHORT).show();
//                            finish();
//                            Intent intent = new Intent(getBaseContext(), Dashboard.class);
//                            startActivity(intent);
//                        }else{
//                            Toast.makeText(getBaseContext(), res.getString("message"), Toast.LENGTH_SHORT).show();
//                        }
                        pd.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.cancel();

                    Log.e("errornyaa ", "" + error);
                    Toast.makeText(getBaseContext(), "Gagal Login, "+error, Toast.LENGTH_SHORT).show();


                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", AuthData.getInstance(getBaseContext()).getId_user());
                    params.put("nama_depan", nama_depan.getText().toString());
                    params.put("username", username.getText().toString());
                    params.put("nama_belakang", nama_belakang.getText().toString());
                    params.put("email", email.getText().toString());
                    params.put("tipe_identitas", tipe_identitas.getText().toString());
                    params.put("no_identitas", no_identitas.getText().toString());
                    params.put("no_telepon", no_telp.getText().toString());
                    params.put("no_rek", no_rek.getText().toString());
                    params.put("nama_rek", nama_rek.getText().toString());
                    params.put("alamat", alamat.getText().toString());
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(senddata);
        }
    }
}
