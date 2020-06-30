package com.project.trajekline.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.trajekline.R;
import com.project.trajekline.akun.Edit_Profile;
import com.project.trajekline.akun.Sign_In;
import com.project.trajekline.config.AppController;
import com.project.trajekline.config.AuthData;
import com.project.trajekline.config.ServerAccess;

import org.json.JSONException;
import org.json.JSONObject;

public class Fragment_Profil extends Fragment {
    Button edit_profil, logout;
    TextView username, email, alamat, no_telp, nama_depan, nama_belakang;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_profil, container, false);
        edit_profil = v.findViewById(R.id.edit_profil);
        logout = v.findViewById(R.id.logout);
        username = v.findViewById(R.id.username);
        email = v.findViewById(R.id.email);
        alamat = v.findViewById(R.id.alamat);
        no_telp = v.findViewById(R.id.no_telp);
        nama_depan = v.findViewById(R.id.nama_depan);
        nama_belakang = v.findViewById(R.id.nama_belakang);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthData.getInstance(getContext()).logout();
                startActivity(new Intent(getContext(), Sign_In.class));
            }
        });
        edit_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Edit_Profile.class));
            }
        });
        loadJson();
        return v;
    }
    private void loadJson()
    {
        StringRequest senddata = new StringRequest(Request.Method.GET, ServerAccess.auth+"info/"+AuthData.getInstance(getContext()).getNama(), new Response.Listener<String>() {
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
                    Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                    Log.d("pesan", "error "+e.getMessage());
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Data Kosong", Toast.LENGTH_SHORT).show();
                        Log.d("volley", "errornya : " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(senddata);
    }
}
