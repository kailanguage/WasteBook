package com.kailang.wastebook.login;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kailang.wastebook.MainActivity;
import com.kailang.wastebook.R;
import com.kailang.wastebook.data.Entity.User;

import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btGo;
    private CardView cv;
    private FloatingActionButton fab;

    private LoginViewModel loginViewModel;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getAllUserLive().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    userList = users;
                }
            }
        });

        initView();
        setListener();
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btGo = findViewById(R.id.bt_go);
        cv = findViewById(R.id.cv);
        fab = findViewById(R.id.fab);
    }

    private void setListener() {
        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPassword.getText().toString().trim().isEmpty() || etUsername.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "请输入完整的信息!", Toast.LENGTH_SHORT).show();
                } else {
                    if (userList != null && !userList.isEmpty()) {
                        for (User u : userList) {
                            if (u.getId().equals(etUsername.getText().toString().trim()) && u.getPassword().equals(etPassword.getText().toString().trim())) {
                                Explode explode = new Explode();
                                explode.setDuration(500);
                                getWindow().setExitTransition(explode);
                                getWindow().setEnterTransition(explode);
                                ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                                Intent i2 = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i2, oc2.toBundle());
                                Toast.makeText(getApplicationContext(), "登录成功!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        Toast.makeText(getApplicationContext(), "登录失败，账号或密码错误!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "登录失败，未注册!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, fab, fab.getTransitionName());
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class), options.toBundle());
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onRestart() {
        super.onRestart();
        fab.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
    }
}
