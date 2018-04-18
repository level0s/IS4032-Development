package com.hkminibus.minibus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SplashScreen extends AppCompatActivity {


    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference mRef = database.getReference();

    public static List<route_data> mRouteData = new ArrayList<>();
    public static List<route_data> allRouteData = new ArrayList<>();
    public static List<district_data> allDistrict = new ArrayList<>();
    public static List<landmark_data> allLandmark = new ArrayList<>();
    public static List<stop_data> allStop = new ArrayList<>();

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000; //開啟畫面時間(3秒)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Query Routes = mRef.child("Route").orderByChild("mRouteNo");
        Routes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRouteData.clear();
                allRouteData.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    route_data mRoute = ds.getValue(route_data.class);
                    allRouteData.add(mRoute);
                }
                addStopList();
                mRouteData.addAll(allRouteData);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        final Query Districts = mRef.child("District");
        Districts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allDistrict.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    district_data mDistrict = ds.getValue(district_data.class);
                    allDistrict.add(mDistrict);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        final Query Landmarks = mRef.child("Landmark");
        Landmarks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allLandmark.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    landmark_data mLandmark = ds.getValue(landmark_data.class);
                    allLandmark.add(mLandmark);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

        @Override
        public void run() {
            // This method will be executed once the timer is over
            // Start your app main activity
            Intent intent = new Intent(SplashScreen.this,MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("allStop",(ArrayList<? extends  Parcelable>) allStop);
            bundle.putParcelableArrayList("mRouteData",(ArrayList<? extends Parcelable>) mRouteData );
            bundle.putParcelableArrayList("allRouteData", (ArrayList<? extends Parcelable>) allRouteData);
            bundle.putParcelableArrayList("allDistrict", (ArrayList<? extends Parcelable>)allDistrict);
            bundle.putParcelableArrayList("allLandmark", (ArrayList<? extends  Parcelable>)allLandmark);
            intent.putExtra("bundle",bundle);
            startActivity(intent);
            //startActivity(new Intent(SplashScreen.this, MainActivity.class)); //MainActivity為主要檔案名稱

            // close this activity
            finish();
        }
    }, SPLASH_TIME_OUT);


    }

    private void addStopList() {
        for (route_data r: allRouteData) {
            final route_data current = r;
            String routeID = r.getmRouteID();
            final Query Stops = mRef.child("Stop/" + routeID);
            Stops.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    allStop.clear();
                    current.clearmStopList();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        stop_data s = ds.getValue(stop_data.class);
                        current.setmStopList(s);
                        if(!allStop.contains(s)){
                            allStop.add(s);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

}
