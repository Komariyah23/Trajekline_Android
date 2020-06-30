package com.project.trajekline.wisata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.project.trajekline.R;
import com.project.trajekline.akun.Sign_In;
import com.project.trajekline.config.AppController;
import com.project.trajekline.config.AuthData;
import com.project.trajekline.config.ServerAccess;
import com.project.trajekline.dashboard.Dashboard;
import com.project.trajekline.transaksi.Transaksi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Pesan extends BottomSheetDialogFragment {
    EditText tanggal_tour;
    Button pesan;
    ProgressDialog pd;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_pesan, container, false);
        tanggal_tour = v.findViewById(R.id.tanggal_tour);
        pesan = v.findViewById(R.id.pesan);
        tanggal_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilkanTanggal();
            }
        });
        tanggal_tour.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                tampilkanTanggal();
            }
        });
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        pd = new ProgressDialog(getContext());
        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesan();
            }
        });
        return v;
    }
    private void tampilkanTanggal() {

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tanggal_tour.setText(dateFormatter.format(newDate.getTime()));
//                reload(dateFormatter.format(newDate.getTime()));
                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }
    private void pesan(){
        pd.setMessage("Authenticating...");
        pd.setCancelable(false);
        pd.show();
        //mengecek username apakah kosong apa tidak. jika kosong maka akan menampilkan alert
        if (tanggal_tour.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Tanggal Tour Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
//            berfungsi untuk menentukan fokus form
            tanggal_tour.setFocusable(true);
            pd.dismiss();

        }else{
//            fungsi dibawah ini berfungsi untuk melakukan request ke api yang sudah tersedia
            StringRequest senddata = new StringRequest(Request.Method.POST, ServerAccess.TRANSAKSI+"pesan", new Response.Listener<String>() {
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
//                            JSONArray d = r.getJSONArray("");
//                            menyimpan data login ke class authdata
//                            menampilkan pesan jika login berhasil
                            Toast.makeText(getContext(), res.getString("message"), Toast.LENGTH_SHORT).show();
//                            berganti halaman setelah login berhasil
                            Intent intent = new Intent(getContext(), Transaksi.class);

                            startActivity(intent);
                            pd.dismiss();
                        }else{
                            pd.dismiss();
                            Toast.makeText(getContext(), "Gagal Pesan", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Gagal Pesan", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.cancel();

                    Toast.makeText(getContext(), "Gagal Login, "+error, Toast.LENGTH_SHORT).show();


                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
//                    mengirim request username dan password ke api
                    params.put("id_paket", getArguments().getString("kode"));
                    params.put("tgl_tour", tanggal_tour.getText().toString());
                    params.put("id", AuthData.getInstance(getContext()).getId_user());

                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(senddata);
        }
    }
}
