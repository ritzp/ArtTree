package com.example.creators.main_fragments.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.creators.MainActivity;
import com.example.creators.R;
import com.example.creators.SignInActivity;
import com.example.creators.app.AppHelper;

public class SettingsMainFragment extends Fragment {

    private View account, application, signOut, deleteAccount;
    private View accountMenu, applicationMenu;
    private ImageView close, accountMenuImg, applicationMenuImg;
    private TextView password, email, language;

    private boolean isAccountMenuOpened = false, isApplicationMenuOpened = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_main, container, false);

        close = root.findViewById(R.id.settings_img_close);
        account = root.findViewById(R.id.settings_account);
        application = root.findViewById(R.id.settings_application);
        signOut = root.findViewById(R.id.settings_signOut);
        deleteAccount = root.findViewById(R.id.settings_deleteAccount);
        accountMenu = root.findViewById(R.id.settings_accountMenu);
        applicationMenu = root.findViewById(R.id.settings_applicationMenu);
        accountMenuImg = root.findViewById(R.id.settings_img_account);
        applicationMenuImg = root.findViewById(R.id.settings_img_application);
        password = root.findViewById(R.id.settings_txt_changePass);
        email = root.findViewById(R.id.settings_txt_changeEmail);
        language = root.findViewById(R.id.settings_txt_changeLanguage);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)SettingsMainFragment.this.getActivity()).replaceFragmentToMyPage();
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAccountMenuOpened = !isAccountMenuOpened;

                if (isAccountMenuOpened) {
                    accountMenuImg.setImageResource(R.drawable.ic_up);
                    accountMenu.setVisibility(View.VISIBLE);
                } else {
                    accountMenuImg.setImageResource(R.drawable.ic_down);
                    accountMenu.setVisibility(View.GONE);
                }
            }
        });

        application.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isApplicationMenuOpened = !isApplicationMenuOpened;

                if (isApplicationMenuOpened) {
                    applicationMenuImg.setImageResource(R.drawable.ic_up);
                    applicationMenu.setVisibility(View.VISIBLE);
                } else {
                    applicationMenuImg.setImageResource(R.drawable.ic_down);
                    applicationMenu.setVisibility(View.GONE);
                }
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)SettingsMainFragment.this.getActivity()).replaceFragmentToSettingsLanguage();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SettingsMainFragment.this.getActivity());
                alertBuilder.setTitle(getString(R.string.sign_out_alert_title)).setMessage(getString(R.string.sign_out_alert_message));
                alertBuilder.setPositiveButton(getString(R.string.sign_out), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppHelper.setAccessingUserid(null);
                        Intent intent = new Intent(SettingsMainFragment.this.getActivity(), SignInActivity.class);
                        startActivity(intent);
                        SettingsMainFragment.this.getActivity().finish();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertBuilder.show();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)SettingsMainFragment.this.getActivity()).replaceFragmentToSettingsDeleteAccount();
            }
        });
        
        return root;
    }
}
