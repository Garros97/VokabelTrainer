package com.voctrainer;
/*
    Mobile Interaction Design - Group 5
    VocTrainer 1.0
    von Fabrice S., Sara A., Garros S. und Sara M.
*/

import static com.voctrainer.R.drawable.button_bg_round;
import static com.voctrainer.R.drawable.button_bg_round_unclickable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GeoMap extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private final double AREA_RADIUS = 30.0;
    private final int GPS_REQUEST_CODE = 1;
    private final int LEVEL_UP = 70; // Level is reached of a progress quote of 70%
    private final long MIN_TIME = 500; // Milli sec.
    private final float MIN_DIST = 1.0f; // Meters
    private final float MIN_ZOOM_SIZE_MAP = 14.0f; // Max size for zooming out
    private final float MAX_ZOOM_SIZE_MAP = 19.0f; // Max size for zooming in
    //private final LatLngBounds UNIVERSITY_AREA_BOUNDS = new LatLngBounds(
    //        new LatLng(52.36793879457692, 9.697743528455748), // SW bounds
    //        new LatLng(52.39735265600114, 9.746175318289547)  // NE bounds
    //);

    private final String USER_DATA_KEY_LAT = "userDataKeyLat";
    private final String USER_DATA_KEY_LON = "userDataKeyLon";
    private final String SELECTED_AREA = "selectedArea";

    private final LatLng COORDS_AREA_PHYSIK = new LatLng(52.38820423962352, 9.710702083217582);
    private final LatLng COORDS_AREA_WIRTSCHAFT = new LatLng(52.378316453594074, 9.724494898171324);
    private final LatLng COORDS_AREA_SE = new LatLng(52.382673590028475, 9.716884204104959);
    private final LatLng COORDS_AREA_ETECHNIK = new LatLng(52.38944092518481, 9.71510862426616);
    private final LatLng COORDS_AREA_SOZIOLOGIE = new LatLng(52.38586900257287, 9.713364899880526);

    private final int MARKER_USER = -1;
    private final int MARKER_AREA_BLUE = 0;
    private final int MARKER_AREA_BLUE_1_STAR = 1;
    private final int MARKER_AREA_BLUE_2_STARS = 2;
    private final int MARKER_AREA_BLUE_3_STARS = 3;
    private final int MARKER_AREA_GREY = 4;

    /*
   User data Keys
   */
    private final String USER_DATA_FILE_NAME = "userProgressData";
    private final String USER_DATA_FG0_LVL1 = "userDataKeyFg0L1";
    private final String USER_DATA_FG0_LVL2 = "userDataKeyFg0L2";
    private final String USER_DATA_FG0_LVL3 = "userDataKeyFg0L3";

    private final String USER_DATA_FG1_LVL1 = "userDataKeyFg1L1";
    private final String USER_DATA_FG1_LVL2 = "userDataKeyFg1L2";
    private final String USER_DATA_FG1_LVL3 = "userDataKeyFg1L3";

    private final String USER_DATA_FG2_LVL1 = "userDataKeyFg2L1";
    private final String USER_DATA_FG2_LVL2 = "userDataKeyFg2L2";
    private final String USER_DATA_FG2_LVL3 = "userDataKeyFg2L3";

    private final String USER_DATA_FG3_LVL1 = "userDataKeyFg3L1";
    private final String USER_DATA_FG3_LVL2 = "userDataKeyFg3L2";
    private final String USER_DATA_FG3_LVL3 = "userDataKeyFg3L3";

    private final String USER_DATA_FG4_LVL1 = "userDataKeyFg4L1";
    private final String USER_DATA_FG4_LVL2 = "userDataKeyFg4L2";
    private final String USER_DATA_FG4_LVL3 = "userDataKeyFg4L3";

    private GoogleMap mMap;
    private int areaID = -1; // Physik=0, Wirtschaft=1, SE=2, ETechnik=3, Soziologie=4
    private LatLng current_latLng = new LatLng(52.3825973407738, 9.717844806973376);

    private int progress_P_Lvl_1;
    private int progress_P_Lvl_2;
    private int progress_P_Lvl_3;

    private int progress_W_Lvl_1;
    private int progress_W_Lvl_2;
    private int progress_W_Lvl_3;

    private int progress_S_Lvl_1;
    private int progress_S_Lvl_2;
    private int progress_S_Lvl_3;

    private int progress_E_Lvl_1;
    private int progress_E_Lvl_2;
    private int progress_E_Lvl_3;

    private int progress_C_Lvl_1;
    private int progress_C_Lvl_2;
    private int progress_C_Lvl_3;

    private Marker marker_user;
    private boolean userPosSaved = false;
    private boolean updateNetworkAndGPS = true;
    private boolean updateUserLocation = true;
    private boolean hasLoaded = false;
    private boolean registerUserIsInArea = true;
    private boolean areaIsAvailable = false;
    private boolean hasPressedFocusOnMeButton = false;

    private LocationListener locationListener;
    private LocationManager locationManager;
    private ConnectivityManager connectivityManager;
    private NetworkInfo activeNetworkInfo;

    private Button btn_cameraOnUser;
    private Button btn_focusOnUser;
    private Button btn_areaAccepted;
    private Circle circleArea0;
    private Circle circleArea1;
    private Circle circleArea2;
    private Circle circleArea3;
    private Circle circleArea4;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gui4_geo_map);
        this.setTitle("Fachgebiet finden");

        btn_cameraOnUser = (Button) findViewById(R.id.button_center);
        btn_cameraOnUser.setBackgroundResource(R.drawable.button_center_transparent);
        btn_cameraOnUser.setText("");
        btn_cameraOnUser.setVisibility(View.INVISIBLE);
        btn_cameraOnUser.setOnClickListener(this);

        btn_focusOnUser = (Button) findViewById(R.id.button_focusOnMe);
        btn_focusOnUser.setBackgroundResource(R.drawable.button_track_me);
        btn_focusOnUser.setText("");
        btn_focusOnUser.setVisibility(View.INVISIBLE);
        btn_focusOnUser.setOnClickListener(this);

        btn_areaAccepted = (Button) findViewById(R.id.button_areaAcception);
        btn_areaAccepted.setText("Fachgebiet wählen");
        btn_areaAccepted.setBackgroundResource(button_bg_round_unclickable);
        btn_areaAccepted.setOnClickListener(this);

        loadingBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        init();
    }

    private void init() {
        updateNetworkAndGPS();
        loadUserData();
        loadingBar.setVisibility(View.VISIBLE);
    }

    // Used to get the highest reached level by calculating progress of predecessor
    private int getLevelStatus(int progressL1, int progressL2, int progressL3) {
        int lvl = 1;
        if (hasReachedHigherLvl(progressL1)) lvl = 2;
        if (hasReachedHigherLvl(progressL2)) lvl = 3;
        if (hasReachedHigherLvl(progressL3)) lvl = 4;
        return lvl;
    }

    private boolean hasReachedHigherLvl(int progress) {
        if (progress < LEVEL_UP) return false;
        else return true;
    }

    /*
    Load User Data selected by the area user has chosen
    */
    private void loadUserData() {
        try {
            SharedPreferences sharedPref = getSharedPreferences(USER_DATA_FILE_NAME, Context.MODE_PRIVATE);

            // Physik=0, Wirtschaft=1, SE=2, ETechnik=3, Soziologie=4
            this.progress_P_Lvl_1 = sharedPref.getInt(USER_DATA_FG0_LVL1, 0);
            this.progress_P_Lvl_2 = sharedPref.getInt(USER_DATA_FG0_LVL2, 0);
            this.progress_P_Lvl_3 = sharedPref.getInt(USER_DATA_FG0_LVL3, 0);

            this.progress_W_Lvl_1 = sharedPref.getInt(USER_DATA_FG1_LVL1, 0);
            this.progress_W_Lvl_2 = sharedPref.getInt(USER_DATA_FG1_LVL2, 0);
            this.progress_W_Lvl_3 = sharedPref.getInt(USER_DATA_FG1_LVL3, 0);

            this.progress_S_Lvl_1 = sharedPref.getInt(USER_DATA_FG2_LVL1, 0);
            this.progress_S_Lvl_2 = sharedPref.getInt(USER_DATA_FG2_LVL2, 0);
            this.progress_S_Lvl_3 = sharedPref.getInt(USER_DATA_FG2_LVL3, 0);

            this.progress_E_Lvl_1 = sharedPref.getInt(USER_DATA_FG3_LVL1, 0);
            this.progress_E_Lvl_2 = sharedPref.getInt(USER_DATA_FG3_LVL2, 0);
            this.progress_E_Lvl_3 = sharedPref.getInt(USER_DATA_FG3_LVL3, 0);

            this.progress_C_Lvl_1 = sharedPref.getInt(USER_DATA_FG4_LVL1, 0);
            this.progress_C_Lvl_2 = sharedPref.getInt(USER_DATA_FG4_LVL2, 0);
            this.progress_C_Lvl_3 = sharedPref.getInt(USER_DATA_FG4_LVL3, 0);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "ERROR FOUND! In (GetUserData) SharedPreferences not loading.", Toast.LENGTH_LONG).show();
        }
    }

    // Update if GPS or Network is enabled
    private void updateNetworkAndGPS() {
        if (updateNetworkAndGPS) {
            checkNetworkConnection();
            checkGPSEnabled();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateNetworkAndGPS();
                }
            }, 10000);
        }
    }

    private boolean isNetworkAvailable() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkNetworkConnection() {
        if (!isNetworkAvailable()) {
            updateNetworkAndGPS = false;
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("Netzwerkverbindung ausgeschaltet")
                    .setMessage("Es muss eine Internetverbindung für diese Applikation existieren. Bitte schalte deine Internetverbindung jetzt ein.")
                    .setPositiveButton("Yes", ((dialogInterface, i) -> {
                        updateNetworkAndGPS = true;
                        Intent intent = new Intent(GeoMap.this, GeoMap.class);
                        startActivity(intent);
                    }))
                    .setCancelable(false)
                    .show();
        }
    }

    private void checkGPSEnabled() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!providerEnable) {
            updateNetworkAndGPS = false;
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Erlaubnis")
                    .setMessage("Deine Standortbestimmung ist für diese Applikation erforderlich. Bitte schalte dein GPS an.")
                    .setPositiveButton("Yes", ((dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, GPS_REQUEST_CODE);
                    }))
                    .setCancelable(false)
                    .show();
        }
    }

    // Physik=0, Wirtschaft=1, SE=2, ETechnik=3, Soziologie=4
    private int getMarkerTypeByLevel(int areaID) {
        int curLevel = 0;
        int marker = 0;

        if (areaID == 0)
            curLevel = getLevelStatus(progress_P_Lvl_1, progress_P_Lvl_2, progress_P_Lvl_3);
        else if (areaID == 1)
            curLevel = getLevelStatus(progress_W_Lvl_1, progress_W_Lvl_2, progress_W_Lvl_3);
        else if (areaID == 2)
            curLevel = getLevelStatus(progress_S_Lvl_1, progress_S_Lvl_2, progress_S_Lvl_3);
        else if (areaID == 3)
            curLevel = getLevelStatus(progress_E_Lvl_1, progress_E_Lvl_2, progress_E_Lvl_3);
        else if (areaID == 4)
            curLevel = getLevelStatus(progress_C_Lvl_1, progress_C_Lvl_2, progress_C_Lvl_3);

        if (curLevel == 1) marker = MARKER_AREA_BLUE;
        else if (curLevel == 2) marker = MARKER_AREA_BLUE_1_STAR;
        else if (curLevel == 3) marker = MARKER_AREA_BLUE_2_STARS;
        else if (curLevel == 4) marker = MARKER_AREA_BLUE_3_STARS;
        else marker = MARKER_AREA_GREY;

        return marker;
    }

    // Scales the marker icons
    private Bitmap createSmallIcon(int markerID, int height, int width) {
        BitmapDrawable bitmapDraw;
        if (markerID == -1)
            bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_user);
        else if (markerID == 0)
            bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_area_blue);
        else if (markerID == 1)
            bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_area_blue1star);
        else if (markerID == 2)
            bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_area_blue2stars);
        else if (markerID == 3)
            bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_area_blue3stars);
        else bitmapDraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_area_grey);
        return Bitmap.createScaledBitmap(bitmapDraw.getBitmap(), width, height, false);
    }

    private float distanceBetweenLocations(LatLng ll1, LatLng ll2) {
        float[] results = new float[1];
        Location.distanceBetween(ll1.latitude, ll1.longitude, ll2.latitude, ll2.longitude, results);
        return results[0];
    }

    private boolean isUserInArea(LatLng userPos, LatLng areaPos) {
        double dist = distanceBetweenLocations(userPos, areaPos);
        if (dist < AREA_RADIUS) return true;
        else return false;
    }

    private void saveUserPos(LatLng ll) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_DATA_KEY_LAT, String.valueOf(ll.latitude));
        editor.putString(USER_DATA_KEY_LON, String.valueOf(ll.longitude));
    }

    private LatLng getUserPos() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String latStr = sharedPref.getString(USER_DATA_KEY_LAT, null);
        String lonStr = sharedPref.getString(USER_DATA_KEY_LON, null);
        double latDou = 0.0, lonDou = 0.0;
        if (latStr != null) latDou = Double.valueOf(latStr);
        if (lonStr != null) lonDou = Double.valueOf(lonStr);
        return new LatLng(latDou, lonDou);
    }

    /*
    Is called if the user entered an area.
    Physik=0, Wirtschaft=1, SE=2, ETechnik=3, Soziologie=4
    */
    private void onUserEnteredArea(int areaID) {
        this.areaID = areaID;
        if (registerUserIsInArea) {

            registerUserIsInArea = false;
            btn_areaAccepted.setBackgroundResource(button_bg_round);
            areaIsAvailable = true;

            if (areaID == 0) {
                circleArea0.remove();
                circleArea0 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_PHYSIK).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 255, 0)));
            } else if (areaID == 1) {
                circleArea1.remove();
                circleArea1 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_WIRTSCHAFT).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 255, 0)));
            } else if (areaID == 2) {
                circleArea2.remove();
                circleArea2 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_SE).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 255, 0)));
            } else if (areaID == 3) {
                circleArea3.remove();
                circleArea3 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_ETECHNIK).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 255, 0)));
            } else if (areaID == 4) {
                circleArea4.remove();
                circleArea4 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_SOZIOLOGIE).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 255, 0)));
            }
        }
    }

    private void stop() {
        this.updateUserLocation = false;
        this.updateNetworkAndGPS = false;
        this.locationManager.removeUpdates(this.locationListener);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*
         Properties of the Map
        // Constrain the camera target to the university-area bounds.
        */
        //mMap.setLatLngBoundsForCameraTarget(UNIVERSITY_AREA_BOUNDS);
        mMap.setMinZoomPreference(MIN_ZOOM_SIZE_MAP); // Max size for zooming out
        mMap.setMaxZoomPreference(MAX_ZOOM_SIZE_MAP); // Max size for zooming in

        marker_user = mMap.addMarker(new MarkerOptions().position(getUserPos()).title("My Position").icon(BitmapDescriptorFactory.fromBitmap(createSmallIcon(MARKER_USER, 100, 100))));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_latLng, 16.0f));

        // The Areas

        mMap.addMarker(new MarkerOptions().position(COORDS_AREA_PHYSIK).title("Fachbereich: Gravitationsphysik").icon(BitmapDescriptorFactory.fromBitmap(createSmallIcon(getMarkerTypeByLevel(0), 200, 150))));
        circleArea0 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_PHYSIK).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 81, 159)));

        mMap.addMarker(new MarkerOptions().position(COORDS_AREA_WIRTSCHAFT).title("Fachbereich: Wirtschaftswissenschaft").icon(BitmapDescriptorFactory.fromBitmap(createSmallIcon(getMarkerTypeByLevel(1), 200, 150))));
        circleArea1 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_WIRTSCHAFT).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 81, 159)));

        mMap.addMarker(new MarkerOptions().position(COORDS_AREA_SE).title("Fachbereich: Software Engineering").icon(BitmapDescriptorFactory.fromBitmap(createSmallIcon(getMarkerTypeByLevel(2), 200, 150))));
        circleArea2 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_SE).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 81, 159)));

        mMap.addMarker(new MarkerOptions().position(COORDS_AREA_ETECHNIK).title("Fachbereich: Elektrotechnik").icon(BitmapDescriptorFactory.fromBitmap(createSmallIcon(getMarkerTypeByLevel(3), 200, 150))));
        circleArea3 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_ETECHNIK).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 81, 159)));

        mMap.addMarker(new MarkerOptions().position(COORDS_AREA_SOZIOLOGIE).title("Fachbereich: Soziologie").icon(BitmapDescriptorFactory.fromBitmap(createSmallIcon(getMarkerTypeByLevel(4), 200, 150))));
        circleArea4 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_SOZIOLOGIE).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 81, 159)));

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if(hasLoaded == false){
                    hasLoaded = true;
                    loadingBar.setVisibility(View.GONE);
                    btn_cameraOnUser.setVisibility(View.VISIBLE);
                    btn_focusOnUser.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Deinen Standort siehst du als blauen Punkt auf der Karte.", Toast.LENGTH_LONG).show();
                    setOneTimePosition();
                }

                current_latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if(!userPosSaved){
                    saveUserPos(current_latLng);
                    userPosSaved = true;
                }
                if(marker_user != null) marker_user.remove();

                marker_user = mMap.addMarker(new MarkerOptions().position(current_latLng).title("My Position").icon(BitmapDescriptorFactory.fromBitmap(createSmallIcon(MARKER_USER, 100, 100))));

                if(hasPressedFocusOnMeButton){
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(current_latLng));
                }

                if(updateUserLocation){
                    if(isUserInArea(current_latLng, COORDS_AREA_PHYSIK)) onUserEnteredArea(0);  // Physik=0, Wirtschaft=1, SE=2, ETechnik=3, Soziologie=4
                    else if(isUserInArea(current_latLng, COORDS_AREA_WIRTSCHAFT)) onUserEnteredArea(1);
                    else if(isUserInArea(current_latLng, COORDS_AREA_SE)) onUserEnteredArea(2);
                    else if(isUserInArea(current_latLng, COORDS_AREA_ETECHNIK)) onUserEnteredArea(3);
                    else if(isUserInArea(current_latLng, COORDS_AREA_SOZIOLOGIE)) onUserEnteredArea(4);
                    else {
                        if(registerUserIsInArea == false){
                            registerUserIsInArea = true;
                            areaIsAvailable = false;
                            btn_areaAccepted.setBackgroundResource(button_bg_round_unclickable);
                            circleArea0.remove();
                            circleArea1.remove();
                            circleArea2.remove();
                            circleArea3.remove();
                            circleArea4.remove();
                            circleArea0 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_PHYSIK).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 81, 159)));
                            circleArea1 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_WIRTSCHAFT).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 81, 159)));
                            circleArea2 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_SE).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 81, 159)));
                            circleArea3 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_ETECHNIK).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 81, 159)));
                            circleArea4 = mMap.addCircle(new CircleOptions().center(COORDS_AREA_SOZIOLOGIE).radius(AREA_RADIUS).strokeColor(Color.argb(0, 0, 81, 159)).fillColor(Color.argb(50, 0, 81, 159)));
                        }
                    }
                }
            }
            @Override
            public void onProviderDisabled (String provider){
            }
            @Override
            public void onProviderEnabled (String provider){
            }
            @Override
            public void onStatusChanged (String provider,int status, Bundle ext){
            }
        };
        // First network provider for quick start and than GPS Provider
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
    }

    private void setOneTimePosition(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(current_latLng));
            }
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GPS_REQUEST_CODE) {
            updateNetworkAndGPS = true;
            boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (providerEnable) {
                Toast.makeText(this, "GPS wurde eingeschaltet", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GeoMap.this, GeoMap.class);
                startActivity(intent);
                this.finish();
            } else {
                Toast.makeText(this, "Dein GPS ist ausgeschaltet, daher kann die Applikation nicht verwendet werden", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GeoMap.this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_center) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current_latLng));
            }
        else if(v.getId() == R.id.button_focusOnMe) {
            if(hasPressedFocusOnMeButton == false) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(current_latLng));
                btn_cameraOnUser.setVisibility(View.INVISIBLE);
                hasPressedFocusOnMeButton = true;
                btn_focusOnUser.setBackgroundResource(R.drawable.button_dont_track_me);
            }
            else {
                hasPressedFocusOnMeButton = false;
                btn_cameraOnUser.setVisibility(View.VISIBLE);
                btn_focusOnUser.setBackgroundResource(R.drawable.button_track_me);
            }
        }
        else if(v.getId() == R.id.button_areaAcception) {
            if(areaIsAvailable){
                stop();
                Intent intent = new Intent(GeoMap.this, LevelSelection.class);
                intent.putExtra(SELECTED_AREA, this.areaID);
                startActivity(intent);
                this.finish();
            }
            else Toast.makeText(this, "Du kannst kein Fachgebiet auswählen, weil du dich in keinem Fachgebiet befindest.", Toast.LENGTH_SHORT).show();
        }
    }
    public void onBackPressed(){
        Intent intent = new Intent(GeoMap.this, MovingCounter.class);
        startActivity(intent);
        this.finish();
    }
}