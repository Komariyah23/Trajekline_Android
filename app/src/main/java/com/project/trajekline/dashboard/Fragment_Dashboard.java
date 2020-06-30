package com.project.trajekline.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.project.trajekline.R;
import com.project.trajekline.kategori.Kategori;
import com.project.trajekline.transaksi.Buat_Paket;
import com.project.trajekline.transaksi.Transaksi;

public class Fragment_Dashboard extends Fragment {
    LinearLayout profil, paket, buat_paket, transaksi, bantuan;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        profil = v.findViewById(R.id.profil);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().findViewById(R.id.container);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new Fragment_Profil())
                        .commit();
            }
        });
        paket = v.findViewById(R.id.paket);
        buat_paket = v.findViewById(R.id.buat_paket);
        buat_paket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Buat_Paket.class));
            }
        });
        transaksi = v.findViewById(R.id.transaksi);
        bantuan = v.findViewById(R.id.bantuan);
        bantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().findViewById(R.id.container);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new Fragment_Bantuan())
                            .commit();
            }
        });
        paket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Kategori.class));
            }
        });
        transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Transaksi.class));
            }
        });
        return v;
    }
}
