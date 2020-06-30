package com.project.trajekline.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.trajekline.R;
import com.project.trajekline.akun.Sign_In;
import com.project.trajekline.config.AuthData;

public class Dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(Dashboard.this);
        navigation.setSelectedItemId(R.id.home);
        loadFragment(new Fragment_Dashboard());
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.home:
                fragment = new Fragment_Dashboard();
                break;
            case R.id.profil:
                fragment = new Fragment_Profil();
                break;
            case R.id.bantuan:
                fragment = new Fragment_Bantuan();
                break;

        }
        return loadFragment(fragment);
    }

    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logo)
                .setTitle("Keluar Akun")
                .setMessage("Apakah Anda Yakin Ingin Keluar Dari Akun Ini ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AuthData.getInstance(getBaseContext()).logout();
                        startActivity(new Intent(getBaseContext(), Sign_In.class));
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
}
