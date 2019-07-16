package com.itxiaox.settingsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goSetting(View view) {
        SettingsDialogFragment settingsDialogFragment = new SettingsDialogFragment();

        settingsDialogFragment.show(getFragmentManager(),settingsDialogFragment.getClass().getSimpleName());
    }
}
