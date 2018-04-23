package com.hkminibus.minibus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import com.google.firebase.database.ChildEventListener;
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

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference mRef = database.getReference();
    public int realposition;
    public static List<route_data> mRouteData = new ArrayList<>();
    public static List<route_data> allRouteData = new ArrayList<>();
    public static List<district_data> allDistrict = new ArrayList<>();
    public static List<landmark_data> allLandmark = new ArrayList<>();
    public static List<stop_data> allStop = new ArrayList<>();
    public static List<location_data> allLocation = new ArrayList<>();
    public static List<driving_mini_data> allDrivingMinibus = new ArrayList<>();
    public static driving_mini_data matched;
    public static List<onclick_data> allOnclicked = new ArrayList<>();

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public ImageButton imageButton;
    private String m_Text = "";
    private static final int[] route_tab_icon = {
            R.drawable.tab_route_0,
            R.drawable.tab_route_1};

    private search_by_no fragment1 = new search_by_no();
    private search_by_location fragment2 = new search_by_location();
    //private search_by_location fragment2 = new search_by_location();
    public static route_data Updated_mRouteData_C ;
    public static String update_po;
    public static int requestCode =0;

    @Override
    protected void onCreate( Bundle savedInstanceState) {

        final Query Driving = mRef.child("Driving").orderByChild("driving").equalTo(true);
        Driving.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allDrivingMinibus.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    driving_mini_data mMini = ds.getValue(driving_mini_data.class);
                    allDrivingMinibus.add(mMini);
                    //Log.v("dataSnapshot", ds.toString());
                    Log.v("Write", ds.toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

       /* final Query Routes = mRef.child("Route").orderByChild("mRouteNo");
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
        });*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            /*allStop = savedInstanceState.getParcelableArrayList("allStop");
            mRouteData = savedInstanceState.getParcelableArrayList("mRouteData");
            allRouteData = savedInstanceState.getParcelableArrayList("allRouteData");
            allDistrict = savedInstanceState.getParcelableArrayList("allDistrict");
            allLandmark = savedInstanceState.getParcelableArrayList("allLandmark");
            allLocation.addAll(allDistrict);
            allLocation.addAll(allLandmark);
            allLocation.addAll(allStop);*/
            allStop = getIntent().getBundleExtra("bundle").getParcelableArrayList("allStop");
            mRouteData = getIntent().getBundleExtra("bundle").getParcelableArrayList("mRouteData");
            allRouteData = getIntent().getBundleExtra("bundle").getParcelableArrayList("allRouteData");
            allDistrict = getIntent().getBundleExtra("bundle").getParcelableArrayList("allDistrict");
            allLandmark = getIntent().getBundleExtra("bundle").getParcelableArrayList("allLandmark");
            allLocation.addAll(allDistrict);
            allLocation.addAll(allLandmark);
            allLocation.addAll(allStop);


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ToolbarHelper.addMiddleTitle(this, "Minibus+", toolbar);



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

        /*imageButton = (ImageButton) findViewById(R.id.pinButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("請輸入車牌號碼");

// Set up the input
                final EditText input = new EditText(getBaseContext());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        m_Text = input.getText().toString();
                        matched = new driving_mini_data();
                        for(driving_mini_data data: allDrivingMinibus){
                            if (m_Text.matches(data.getmPlateNo())){
                                if(data.isDriving() == true){
                                    matched = data;
                                }
                            }
                        }
                        if(matched.getCarSize() != null){

                            Intent i = new Intent(MainActivity.this ,on_car.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("driving", matched);// 序列化
                            i.putExtras(bundle);// 发送数据
                            startActivityForResult(i, requestCode);
                        } else {

                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MainActivity.this);
                            dlgAlert.setTitle("這輛小巴現在還沒有行駛");
                            dlgAlert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialog.cancel();
                                }
                            });
                            dlgAlert.show();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });*/

        //Set tabitem with icon and name
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (i==0){
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(getTabView(this,route_tab_icon,i, "路線搜尋" ));
            }
            else if(i==1){
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(getTabView(this,route_tab_icon,i, "地點搜尋" ));
            }
        }



    }

    /*private void addStopList() {
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
    }*/
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

    //When Stop_main back to MainActivity, it will run follow code to update other list about the rank change
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0){

            Updated_mRouteData_C = data.getParcelableExtra("Updatedrd");
            update_po = data.getStringExtra("Uposition");
            allOnclicked = data.getParcelableArrayListExtra("allOnClicked");
            Log.d("Pasing to Mainlalala", Updated_mRouteData_C + " " + update_po);
            for (route_data s : allRouteData) {
                //if the existing elements contains the search input
                if (s.getmRouteID().equals(update_po)) {
                    //adding the element to filtered list
                    realposition = allRouteData.indexOf(s);
                  System.out.println(realposition);
                }

            }
            allRouteData.set(realposition,Updated_mRouteData_C);
            mRouteData.clear();
            search_by_no.mRouteDataNo.clear();
            mRouteData.addAll(allRouteData);
            search_by_no.mRouteDataNo.addAll(allRouteData);
            search_by_no.mRouteAdapter1.notifyDataSetChanged();
            search_by_location.mRouteAdapter.notifyDataSetChanged();
        }
    }
    public static View getTabView(Context context,int [] imagelist, int position, String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView iv_tab = (ImageView) view.findViewById(R.id.iv_tab);
        TextView tv_tab = (TextView) view.findViewById(R.id.tv_tab);
        iv_tab.setImageResource(imagelist[position]);
        tv_tab.setText(text);
        tv_tab.setTextColor(Color.parseColor("#FFFFFF"));
        return view;
    }
    //callback to save the state when the activity pauses:


}