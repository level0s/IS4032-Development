package com.hkminibus.minibus;

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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.identity.intents.AddressConstants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmine on 27/3/2018.
 */

public class stop_main extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        TabLayout.OnTabSelectedListener{

    private Toolbar toolbar;
    private TextView mRouteName;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private stop_route_page fragment1 = new stop_route_page();
    private stop_ttb fragment2 = new stop_ttb();
    public static route_data CRouteData;
    public static List<stop_data> CStopList = new ArrayList<>();
    public static String routeID;
    public static int clicked = 0;
    public static int clickedPosition;
    public static int resetPosition;
    public static int routeID_no;
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
