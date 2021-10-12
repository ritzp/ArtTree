package com.example.creators;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.creators.app.AppHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SignUpActivity extends AppCompatActivity {

    NavController navController;

    public int method;
    public String emailPhone = "", id = "", password = "", nickname = "", introduction = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getSupportActionBar().hide();

        navController = Navigation.findNavController(this, R.id.signup_nav_host_fragment);
    }

    public void replaceFragmentToProcess1() {
        navController.navigate(R.id.navigation_signup_process1);
    }

    public void replaceFragmentToProcess2() {
        navController.navigate(R.id.navigation_signup_process2);
    }

    public void replaceFragmentToProcess3() {
        navController.navigate(R.id.navigation_signup_process3);
    }

    public void replaceFragmentToProcess4() {
        navController.navigate(R.id.navigation_signup_process4);
    }
}
