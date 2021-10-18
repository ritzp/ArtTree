package com.example.arttree;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class ForgotPasswordActivity extends AppCompatActivity {

    NavController navController;

     public String email = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        getSupportActionBar().hide();

        navController = Navigation.findNavController(this, R.id.forgot_nav_host_fragment);
    }

    public void replaceFragmentToProcess1() {
        navController.navigate(R.id.navigation_forgot_process1);
    }

    public void replaceFragmentToProcess2() {
        navController.navigate(R.id.navigation_forgot_process2);
    }
}
