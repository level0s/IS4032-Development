package com.hkminibus.minibus;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        TabLayout.OnTabSelectedListener{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference();

    public static List<route_data> mRouteData = new ArrayList<>();
    public static List<route_data> allRouteData = new ArrayList<>();
    public static List<district_data> allDistrict = new ArrayList<>();
    public static List<landmark_data> allLandmark = new ArrayList<>();
    public static List<stop_data> allStop = new ArrayList<>();

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private search_by_no fragment1 = new search_by_no();
    private search_by_location fragment2 = new search_by_location();
    //private search_by_location fragment2 = new search_by_location();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return fragment1;
                    case 1:
                        return fragment2;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
    }

     private void addStopList() {
         for (route_data r: allRouteData) {
             final route_data current = r;
             String routeID = r.getmRouteID();
             final Query Stops = mRef.child("Stop/" + routeID);
             Stops.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
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
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //TabLayout里的TabItem被选中的时候触发
        viewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        //viewPager滑动之后显示触发
        tabLayout.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {}


}