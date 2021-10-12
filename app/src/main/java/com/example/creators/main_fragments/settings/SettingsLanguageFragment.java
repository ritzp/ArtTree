package com.example.creators.main_fragments.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.creators.MainActivity;
import com.example.creators.R;

import java.util.Locale;

public class SettingsLanguageFragment extends Fragment {

    private ImageView back;
    private Spinner languageSpinner;
    private Button submit;

    private int languageCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_language, container, false);

        back = root.findViewById(R.id.settingslan_img_back);
        submit = root.findViewById(R.id.settingslan_btn_submit);
        languageSpinner = root.findViewById(R.id.settingslan_spn_language);

        final String[] languages = {"English", "한국어"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.listitem_stringspinner, R.id.stringSpinner_txt, languages);
        languageSpinner.setAdapter(adapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                languageCode = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragmentToSettingsMain();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                String localeString = null;
                switch (languageCode) {
                    case 0:
                        localeString = "en";
                        break;
                    case 1:
                        localeString = "ko";
                        break;
                    default:
                        localeString = "en";
                        break;
                }
                changeConfiguration(localeString);
            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void changeConfiguration(String localeString) {
        Locale locale = new Locale(localeString);
        Locale.setDefault(locale);

        Configuration configuration = getActivity().getBaseContext().getResources().getConfiguration();
        configuration.locale = locale;
        getActivity().getBaseContext().createConfigurationContext(configuration);
        getActivity().getBaseContext().getResources().updateConfiguration(configuration,
                getActivity().getBaseContext().getResources().getDisplayMetrics());

        Toast.makeText(getActivity(), getString(R.string.settings_completed), Toast.LENGTH_SHORT).show();
        ((MainActivity)getActivity()).replaceFragmentToSettingsLanguage();
    }
}
