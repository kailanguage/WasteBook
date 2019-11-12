package com.kailang.wastebook.ui.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.kailang.wastebook.R;
import com.kailang.wastebook.ui.add.PagerAdapter;


public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        FragmentManager fragmentManager =getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.coordinatorlayout_category,CategoryFragment.newInstance(3))
        .commit();
        PagerAdapter sectionsPagerAdapter = new PagerAdapter(this,fragmentManager);
        ViewPager viewPager = findViewById(R.id.view_pager_category);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs_category);
        tabs.setupWithViewPager(viewPager);
    }


}
