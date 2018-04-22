package com.hkminibus.minibus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.identity.intents.AddressConstants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmine on 27/3/2018.
 */

public class stop_main extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        TabLayout.OnTabSelectedListener{


    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference mRef = database.getReference();
    private Toolbar toolbar;
    private TextView mRouteName;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private stop_route_page fragment1 = new stop_route_page();
    private stop_ttb fragment2 = new stop_ttb();
    public static route_data CRouteData;
    public ImageButton imageButton;
    public static List<driving_mini_data> allDrivingMinibus = new ArrayList<>();
    private String m_Text = "";
    public static List<stop_data> CStopList = new ArrayList<>();
    public static String routeID;
    public static int clicked = 0;
    public static int clickedPosition;
    public static int resetPosition;
    public static int routeID_no;
    public static driving_mini_data matched;
    private static final int[] stop_tab_icon = {
            R.drawable.tab_stop_0,
            R.drawable.tab_stop_1};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_main);

        //添加Toolbar的返回按钮
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(stop_main.this, MainActivity.class);
                i.putExtra("Uposition", routeID_no);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Updatedrd", CRouteData);
                i.putExtras(bundle);
                setResult(RESULT_OK,i);
                Log.d("Passingfrom !!!!!", "is going passing" + routeID_no + " " + CRouteData);
                finish();
            }
        });

        //get the clicked Route Name, No, shop list, routeID
        CRouteData = getIntent().getParcelableExtra("CRouteData");
        routeID_no =getIntent().getIntExtra("CPosition",1);
        routeID = CRouteData.getmRouteID();
        String No = CRouteData.getmRouteNo();
        String Name = CRouteData.getmRouteName();
        CStopList = CRouteData.getmStopList();

        //Set Route Name as toolbar title
        mRouteName = (TextView)findViewById(R.id.stop_route_name);
        mRouteName.setText(No + " " + Name);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        Log.d("f1", "fragment1");
                        return fragment1;
                    case 1:
                        Log.d("f2", "fragment2");
                        return fragment2;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        imageButton = (ImageButton) findViewById(R.id.pinButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(stop_main.this);
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

                            Intent i = new Intent(stop_main.this ,on_car.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("driving", matched);// 序列化
                            i.putExtras(bundle);// 发送数据
                            startActivityForResult(i, MainActivity.requestCode);
                        } else {

                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(stop_main.this);
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
        });
        //Set tabitem with icon and name
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (i==0){
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(MainActivity.getTabView(this,stop_tab_icon,i, "路線" ));
            }
            else if(i==1){
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(MainActivity.getTabView(this,stop_tab_icon,i, "時間表" ));
            }
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

    @Override
    public void onBackPressed(){
        Log.d("C", "YY");
        Intent i = new Intent(stop_main.this, MainActivity.class);
        i.putExtra("Uposition", routeID_no);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Updatedrd", CRouteData);
        i.putExtras(bundle);
        setResult(RESULT_OK,i);
        Log.d("Passingfrom !!!!!", "is going passing" + routeID_no + " " + CRouteData);
        finish();
    }
}
