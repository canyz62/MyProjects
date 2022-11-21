// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.example.mapwithmarker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
// [START maps_marker_on_map_ready]
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private ImageView favoriten;
    private ImageView search;
    private ImageView settings;
    public static ArrayList<ChargingStation> Stationen;
    public static String Role;
    public static boolean fromMapsActivity = false;
    private List<Marker> markers = new ArrayList<>();
    private static int UmkreisInMeter = 10000; //10km Standardmaessig
    private static boolean DpisrunOnce = false;

    private GoogleMap mMap;

    private static final int LOCATION_PERMISSION_CODE = 101;

    // [START_EXCLUDE]
    // [START maps_marker_get_map_async]
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);


         if (DpisrunOnce) {
             Intent NameIntent = getIntent();
             String Username = NameIntent.getExtras().getString("UsernameIntent");
             DBHelper DB = new DBHelper(this);
             Role = DB.GetRole(Username);


             favoriten = (ImageView) findViewById(R.id.button2);
             favoriten.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     openActivityFavoriten();
                 }
             });

             search = (ImageView) findViewById(R.id.button3);
             search.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     openActivitySearch();
                 }
             });

             settings = (ImageView) findViewById(R.id.button4);
             settings.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     openActivitySettings();
                 }
             });

             if (isLocationPermissionGranted()) {
                 // Get the SupportMapFragment and request notification when the map is ready to be used.
                 SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                         .findFragmentById(R.id.map);
                 mapFragment.getMapAsync(this);

             } else {
                 requestLocationPermission();
             }

         }else{

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Stationen = DownloadService.getStations();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Intent NameIntent = getIntent();
        String Username = NameIntent.getExtras().getString("UsernameIntent");
        DBHelper DB = new DBHelper(this);
        Role = DB.GetRole(Username);


        favoriten = (ImageView) findViewById(R.id.button2);
        favoriten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityFavoriten();
            }
        });

        search = (ImageView) findViewById(R.id.button3);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivitySearch();
            }
        });

        settings = (ImageView) findViewById(R.id.button4);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivitySettings();
            }
        });

        if (isLocationPermissionGranted()) {
            // Get the SupportMapFragment and request notification when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        } else {
            requestLocationPermission();
        }
        DpisrunOnce = true;
    }
    }

    // [END maps_marker_get_map_async]
    // [END_EXCLUDE]

    // [START_EXCLUDE silent]
    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    // [END_EXCLUDE]
    // [START maps_marker_on_map_ready_add_marker]
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // [START_EXCLUDE silent]
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        // [END_EXCLUDE]
        LatLng michelstadt = new LatLng(49.679396, 9.001215);
       // mMap.addMarker(new MarkerOptions().position(michelstadt).title("Ladesäule Michelstadt Altstadt"));
        // [START_EXCLUDE silent]
        mMap.moveCamera(CameraUpdateFactory.newLatLng(michelstadt));
        // [END_EXCLUDE]

        for (int i = 0 ; i < Stationen.size(); i++) {
        LatLng heroldstatt = new LatLng(Stationen.get(i).getLat() , Stationen.get(i).getLon());
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(heroldstatt)
                .visible(false) //erstmal nicht zu sehen
                .title(Stationen.get(i).getOperator() + " | " + Stationen.get(i).getStreet() + " " + Stationen.get(i).getNumber() + ", " + Stationen.get(i).getPostal_code() + " " + Stationen.get(i).getLocation())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                markers.add(marker);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        //Zeigt alle Location innerhalb eines bestimmten Umkreises an (in Metern)
        for (Marker marker : markers) {
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());

            if (SphericalUtil.computeDistanceBetween(currentUserLocation, marker.getPosition()) < UmkreisInMeter) {
                marker.setVisible(true);
            }
        }
        for (int i = 0; i < Stationen.size(); i++)
        {
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude()); //USER LOCATION
            LatLng StationLocation = new LatLng(Stationen.get(i).getLat() , Stationen.get(i).getLon()); //STATION LOCATION

            if (SphericalUtil.computeDistanceBetween(currentUserLocation, StationLocation) > UmkreisInMeter) {
                Stationen.get(i).setInDerNaehe(false);
            }
            if (SphericalUtil.computeDistanceBetween(currentUserLocation, StationLocation) < UmkreisInMeter) {
                Stationen.get(i).setInDerNaehe(true);
            }
        }
    }
    // [END maps_marker_on_map_ready_add_marker]

    private boolean isLocationPermissionGranted(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            else{
               return false;
            }
    }

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_CODE);

    }


    public void openActivityFavoriten() {
        Intent intent = new Intent(this, Favoriten.class);
        fromMapsActivity = true;
        startActivity(intent);
    }

    public void openActivitySearch() {
        Intent intent = new Intent(this, Search.class);
        fromMapsActivity = true;
        intent.putExtra("fromMapsActivity", fromMapsActivity);
        //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void openActivitySettings() {
        Intent NameIntent = getIntent();
        String Username = NameIntent.getExtras().getString("UsernameIntent");


        Intent intent = new Intent(this, Settings.class);
        intent.putExtra("UsernameIntentToSettings", Username);

            mStartForResult.launch(intent); //New Way Result

        //startActivity(intent);        //Ohne Result
        //startActivityForResult(intent,1); //Old Way Result

        /////mStartForResult.launch(intent); //New Way Result
    }

    public static void setUmkreis(int newKey){
        UmkreisInMeter = newKey;
    }

    //NEW WAY for ActivityResult
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() { // 1
                @SuppressLint("MissingPermission")
                @Override
                public void onActivityResult (ActivityResult result)
                { // 2
                    if (result.getResultCode() == Activity.RESULT_OK)
                    { // 3
                        Intent data = result.getData();

                        int ergebnis = data.getIntExtra(Settings.EXTRA_UMKREIS, 0);
                        setUmkreis(ergebnis);

                        String newlat = data.getExtras().getString("latitude");
                        String newlong = data.getExtras().getString("longitude");

                        if(newlong.equals("") || newlat.equals("")){
                            for (Marker marker : markers) {
                                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                LatLng currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());

                                if (SphericalUtil.computeDistanceBetween(currentUserLocation, marker.getPosition()) > UmkreisInMeter) {
                                    marker.setVisible(false);
                                }

                                if (SphericalUtil.computeDistanceBetween(currentUserLocation, marker.getPosition()) < UmkreisInMeter) {
                                    marker.setVisible(true);
                                }
                            }

                            for (int i = 0; i < Stationen.size(); i++)
                            {
                                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                LatLng currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude()); //USER LOCATION
                                LatLng StationLocation = new LatLng(Stationen.get(i).getLat() , Stationen.get(i).getLon()); //STATION LOCATION

                                if (SphericalUtil.computeDistanceBetween(currentUserLocation, StationLocation) > UmkreisInMeter) {
                                    Stationen.get(i).setInDerNaehe(false);
                                }
                                if (SphericalUtil.computeDistanceBetween(currentUserLocation, StationLocation) < UmkreisInMeter) {
                                    Stationen.get(i).setInDerNaehe(true);
                                }
                            }
                        }else{
                            double latitude = Double.parseDouble(newlat);
                            double longitude = Double.parseDouble(newlong);

                            for (Marker marker : markers) {
                                LatLng newCenter = new LatLng(latitude, longitude);

                                if (SphericalUtil.computeDistanceBetween(newCenter, marker.getPosition()) > UmkreisInMeter) {
                                    marker.setVisible(false);
                                }

                                if (SphericalUtil.computeDistanceBetween(newCenter, marker.getPosition()) < UmkreisInMeter) {
                                    marker.setVisible(true);
                                }
                            }

                            for (int i = 0; i < Stationen.size(); i++)
                            {
                                LatLng newCenter = new LatLng(latitude, longitude); //USER LOCATION
                                LatLng StationLocation = new LatLng(Stationen.get(i).getLat() , Stationen.get(i).getLon()); //STATION LOCATION

                                if (SphericalUtil.computeDistanceBetween(newCenter, StationLocation) > UmkreisInMeter) {
                                    Stationen.get(i).setInDerNaehe(false);
                                }
                                if (SphericalUtil.computeDistanceBetween(newCenter, StationLocation) < UmkreisInMeter) {
                                    Stationen.get(i).setInDerNaehe(true);
                                }
                            }
                        }
                        //.. weitere spezifische Aktionen
                    } // 3
                } // 2
            }); // 1

    @Override
    public void onBackPressed() {

    }

}

// [END maps_marker_on_map_ready]
