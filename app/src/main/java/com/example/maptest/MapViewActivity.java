package com.example.maptest;

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
    private ActivityMapViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        LatLng zero = new LatLng(0, 0);
        PolylineOptions polygonOptions = new PolylineOptions()
                .add(sydney, new LatLng(-34, 0), zero, new LatLng(0, 151), sydney);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addPolyline(polygonOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void plus (View view){mMap.animateCamera(CameraUpdateFactory.zoomIn());}
    public void minus (View view){mMap.animateCamera(CameraUpdateFactory.zoomOut());}
}