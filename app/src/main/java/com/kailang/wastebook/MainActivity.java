package com.kailang.wastebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kailang.wastebook.data.Entity.Category;
import com.kailang.wastebook.data.Entity.User;
import com.kailang.wastebook.login.LoginActivity;
import com.kailang.wastebook.login.LoginViewModel;
import com.kailang.wastebook.ui.category.CategoryViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private CategoryViewModel categoryViewModel;
    private static boolean isInitCategory=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_detail, R.id.navigation_chart, R.id.navigation_person)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        loginViewModel= ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getAllUserLive().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if(users==null||users.isEmpty()){
                   startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategoriesLive().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                Log.e("MainActivity","onChanged");
                if(categories.size()==0&&!isInitCategory)initCategory();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        Navigation.findNavController(findViewById(R.id.nav_host_fragment)).navigateUp();
        return super.onSupportNavigateUp();
    }


    //初始化category数据库
    private void initCategory() {
        isInitCategory=true;
        String[] categoryINName={"搬砖","工资","奖金","卖房","股票","资金","黄金","兼职","其它"};
        String[] categoryOUTName={"餐饮","购物","服饰","健身","交通","捐赠","社交","通信","房租","教育","医疗","生活","零食","旅行","水果","其它"};
        //支出
        for(int i=0;i<categoryOUTName.length;i++) {
            Category category = new Category();
            category.setIcon("ic_category_out_"+(i+1));
            category.setType(true);
            category.setName(categoryOUTName[i]);
            category.setOrder(i);
            categoryViewModel.insertCategory(category);
        }
        //收入
        for(int i=0;i<categoryINName.length;i++) {
            Category category = new Category();
            category.setIcon("ic_category_in_"+(i+1));
            category.setType(false);
            category.setName(categoryINName[i]);
            category.setOrder(i);
            categoryViewModel.insertCategory(category);
        }
    }
}
