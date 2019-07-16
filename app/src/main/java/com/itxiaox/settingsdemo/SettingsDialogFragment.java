package com.itxiaox.settingsdemo;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SettingsDialogFragment extends DialogFragment {
    private static final String TAG = "settings";
    FragmentManager ft;
    HeaderFragment headerFragment;
    Fragment currFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //noinspection Annotator
        View view = inflater.inflate(R.layout.fragment_dialog_settings, container, false);
        Toolbar bar = view.findViewById(R.id.toolbar);
        headerFragment = new HeaderFragment();
        headerFragment.setFragment(this);
        ft = getChildFragmentManager();

        switchFragment(headerFragment);
        bar.setNavigationOnClickListener(v -> back());

        this.getDialog().setOnKeyListener((arg0, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                back();
                return true;
            }
            return false;
        });
        return view;
    }

    private void back() {
        if (currFragment == headerFragment) {
            dismiss();
        } else {
            switchFragment(headerFragment);
        }
    }

    SettingsFragment settingsFragment;

    public void goItemFragment(int resId) {
        if (settingsFragment == null)
            settingsFragment = new SettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("XmlResId", resId);
        settingsFragment.setArguments(bundle);
        switchFragment(settingsFragment);
    }

    private void switchFragment(Fragment targetFragment) {
        currFragment = targetFragment;
        ft.beginTransaction().replace(R.id.flay_settings, currFragment).commitAllowingStateLoss();
    }

}
