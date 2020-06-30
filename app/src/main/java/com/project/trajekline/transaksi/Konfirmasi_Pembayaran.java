package com.project.trajekline.transaksi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.project.trajekline.R;
import com.project.trajekline.config.ServerAccess;
import com.project.trajekline.config.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Konfirmasi_Pembayaran extends AppCompatActivity {
    Bitmap bitmap;
    private ImageView gambar;
    private int GALLERY = 1, CAMERA = 2;
    int image = 1;
    ProgressDialog pd;
    Button upload, konfirmasi;
    private Uri contentURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pembayaran);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        Intent data = getIntent();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        upload = findViewById(R.id.upload);
        gambar = findViewById(R.id.gambar);
        konfirmasi = findViewById(R.id.konfirmasi);
        pd = new ProgressDialog(Konfirmasi_Pembayaran.this);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image = 1;
                showPictureDialog();
            }
        });
        upload.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove_image(gambar);
                return true;
            }
        });
        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image = 1;
                showPictureDialog();
            }
        });
        gambar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove_image(gambar);
                return true;
            }
        });
        konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konfirmasi();
            }
        });
    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
        }
    }
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }
    private void remove_image(final ImageView imageView){
        new AlertDialog.Builder(Konfirmasi_Pembayaran.this)
                .setIcon(R.drawable.logo)
                .setTitle("Hapus Gambar")
                .setMessage("Apakah Anda Yakin Ingin Hapus Gambar ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imageView.setImageResource(R.drawable.bg_default);
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    gambar.setImageBitmap(bitmap);
                    gambar.setVisibility(View.VISIBLE);
                    upload.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Konfirmasi_Pembayaran.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Log.d("active",Integer.toString(image));
            bitmap = (Bitmap) data.getExtras().get("data");
            gambar.setImageBitmap(bitmap);
            gambar.setVisibility(View.VISIBLE);
            upload.setVisibility(View.GONE);
            Toast.makeText(Konfirmasi_Pembayaran.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
    private void konfirmasi(){
        final Intent data = getIntent();
        pd.show();
        if (bitmap == null){
            Toast.makeText(
                    Konfirmasi_Pembayaran.this,
                    "Bukti tidak boleh kosong",
                    Toast.LENGTH_LONG
            ).show();
            pd.dismiss();
        }else {
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(
                    Request.Method.POST,
                    ServerAccess.KONFIRMASI +data.getStringExtra("kode"),
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            pd.dismiss();
                            try {
                                JSONObject obj = new JSONObject(new String(response.data));
                                if (obj.getBoolean("status")) {
                                    Toast.makeText(
                                            Konfirmasi_Pembayaran.this,
                                            obj.getString("message"),
                                            Toast.LENGTH_LONG
                                    ).show();
                                    finish();
                                    startActivity(new Intent(getBaseContext(), Transaksi.class));
                                } else {
                                    Toast.makeText(
                                            Konfirmasi_Pembayaran.this,
                                            obj.getString("message"),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            } catch (JSONException e) {

                                Toast.makeText(
                                        Konfirmasi_Pembayaran.this,
                                        e.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                            Toast.makeText(
                                    Konfirmasi_Pembayaran.this,
                                    "error " + error.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

//                    params.put("status", data.getStringExtra("status"));
//                    params.put("bayar", data.getStringExtra("jumlah"));
                    return params;
                }

                /*
                 * Here we are passing image by renaming it with a unique name
                 * */

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    if (bitmap == null) {
                        Log.d("pesan", "bitmap null");
                    } else {
                        long imagename = System.currentTimeMillis();
                        params.put("userfile", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                    }
                    return params;
                }
            };
            //adding the request to volley
            Volley.newRequestQueue(this).add(volleyMultipartRequest);
        }
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
