package com.example.maptest;

import android.content.Intent;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.example.maptest.databinding.ActivityMapViewBinding;

public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapViewBinding binding = ActivityMapViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.my_style));

        LatLng sydney = new LatLng(-34, 151);
        LatLng zero = new LatLng(0, 0);

        PolylineOptions polygonOptions = new PolylineOptions()
                .add(sydney, new LatLng(-34, 0), zero, new LatLng(0, 151));
        mMap.addPolyline(polygonOptions);

    }

    public void plus (View view){mMap.animateCamera(CameraUpdateFactory.zoomIn());}
    public void minus (View view){mMap.animateCamera(CameraUpdateFactory.zoomOut());}
    public void back (View view){
        Intent intent2 = new Intent(MapViewActivity.this, MainActivity.class);
        startActivity(intent2);
    }
}