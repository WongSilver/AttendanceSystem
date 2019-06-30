package edu.wong.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import edu.wong.adapter.MyFragmentPagerAdapter;
import edu.wong.fragment.Fragment_attend;
import edu.wong.fragment.Fragment_home;
import edu.wong.fragment.Fragment_me;
import edu.wong.fragment.Fragment_msg;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.it_home:
                    vp.setCurrentItem(0);
                    return true;
                case R.id.it_attend:
                    vp.setCurrentItem(1);
                    return true;
                case R.id.it_msg:
                    vp.setCurrentItem(2);
                    return true;
                case R.id.it_me:
                    vp.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };
    private ViewPager vp;
    private BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //初始化页面
        initView();

        //添加监听事件
        initListener();


    }

    private void initListener() {
        vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                bnv.setSelectedItemId(bnv.getMenu().getItem(position).getItemId());
            }
        });
    }

    private void initView() {
        vp = findViewById(R.id.home_vp);
        bnv = findViewById(R.id.nav_view);
        List<Fragment> list = new ArrayList<>();
        list.add(new Fragment_home());
        list.add(new Fragment_attend());
        list.add(new Fragment_msg());
        list.add(new Fragment_me());
        PagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),list);
        vp.setAdapter(adapter);
    }

}
