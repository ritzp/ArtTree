package com.example.arttree;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.example.arttree.app.AppHelper;
import com.example.arttree.classes.Category;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;
    AppBarConfiguration appBarConfiguration;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getSupportActionBar().hide();

        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_categoryList, R.id.navigation_search, R.id.navigation_myPage)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void replaceFragmentByList(Category category) {
        Bundle bundle = new Bundle();
        bundle.putString("searchMethod", "category");
        bundle.putInt("icon", category.getImg());
        bundle.putString("keyword", category.getTxt());
        navController.navigate(R.id.navigation_contentList, bundle);
    }

    public void replaceFragmentBySearch(String keyword) {
        Bundle bundle = new Bundle();
        bundle.putString("searchMethod", "search");
        bundle.putString("keyword", keyword);
        navController.navigate(R.id.navigation_contentList, bundle);
    }

    public void replaceFragmentByLiked(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString("searchMethod", "liked");
        bundle.putString("keyword", userId);
        navController.navigate(R.id.navigation_contentList, bundle);
    }

    public void replaceFragmentBySubscriptions(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString("searchMethod", "subscriptions");
        bundle.putString("keyword", userId);
        navController.navigate(R.id.navigation_contentList, bundle);
    }

    public void replaceFragmentToMyContentList() {
        navController.navigate(R.id.navigation_myContentList);
    }

    public void replaceFragmentToMyPage() {
        navController.navigate(R.id.navigation_myPage);
    }

    public void replaceFragmentToSearch() {
        navController.navigate(R.id.navigation_search);
    }

    public void replaceFragmentToMyPageEdit(Bundle bundle) {
        navController.navigate(R.id.navigation_myPageEdit, bundle);
    }

    public void replaceFragmentToSettingsMain() {
        navController.navigate(R.id.navigation_settingsMain);
    }

    public void replaceFragmentToSettingsLanguage() {
        navController.navigate(R.id.navigation_settingsLanguage);
    }

    public void replaceFragmentToSettingsDeleteAccount() {
        navController.navigate(R.id.navigation_settingsDeleteAccount);
    }

    public void replaceFragmentToSettingsEmail() {
        navController.navigate(R.id.navigation_settingsEmail);
    }

    public void replaceFragmentToSettingsPassword() {
        navController.navigate(R.id.navigation_settingsPassword);
    }
}