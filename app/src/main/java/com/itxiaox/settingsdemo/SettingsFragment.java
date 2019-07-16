package com.itxiaox.settingsdemo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.jishimed.JSUtils;
import com.jishimed.jspref.IPrefHandler;
import com.jishimed.jspref.IPreference;
import com.jishimed.jspref.PrefUtils;


public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "SettingsFragment";

    static final int FLAG_NONE = 0x0;
    static final int FLAG_ENGINEERING = 0x1;
    static final int FLAG_DEVELOPMENT = 0x2;

    private IPrefHandler prefHandler;
    private boolean isModified;     //Preferences are changed?
    private int modeMask = SettingsFragment.FLAG_NONE;

    public SettingsFragment(){
        isModified = false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        addPreferencesFromResource(bundle.getInt("XmlResId"));

        PrefUtils.init(getActivity());
        PrefUtils.getDefault().registerOnSharedPreferenceChangeListener(this);

        isModified = false;

        boolean isEngineering = PrefUtils.get(DevPrefHandler.KEY_ENGINEERING_MODE, false);
        boolean isDevelopment = PrefUtils.get(DevPrefHandler.KEY_DEVELOPMENT_MODE, false);
        if (isEngineering) modeMask |= SettingsFragment.FLAG_ENGINEERING;
        if (isDevelopment) modeMask |= SettingsFragment.FLAG_DEVELOPMENT;

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        String classKey = preferenceScreen.getKey();

        if( prefHandler == null || !prefHandler.getClass().getCanonicalName().equals(classKey)){
            try {
                if (!TextUtils.isEmpty(classKey)) {
                    Class<?> class1 = Class.forName(classKey);
                    prefHandler = (IPrefHandler) class1.newInstance();
                }
            } catch (ClassNotFoundException | IllegalAccessException | java.lang.InstantiationException e) {
                e.printStackTrace();
            }
        }

        if (prefHandler != null) {
            prefHandler.init(getActivity(), preferenceScreen);
            int count = preferenceScreen.getPreferenceCount();
            for (int i = count-1; i >=0; i--) {
                Preference preference = preferenceScreen.getPreference(i);
                if (preference instanceof IPreference) {
                    IPreference jsPref = (IPreference) preference;
                    int flag = jsPref.getFlag();
                    if (flag != 0 && 0 == (flag & modeMask)) {
                        preferenceScreen.removePreference(preference);
                    }
                    else if (jsPref.isNeedComplete()) {
                        prefHandler.completePreference(preference.getKey(), preference);
                    }
                }
            }
        }

        //保证第一个JSPreference必须是从WidgetLayoutXml创建的，否则Widget会偶尔显示不出来
        if (preferenceScreen.getPreferenceCount() > 0) {
            Preference preference = preferenceScreen.getPreference(0);
            if (preference instanceof IPreference) {
                IPreference jsPref = (IPreference) preference;
                if (!jsPref.isFromWidgetLayout()) {
                    jsPref.setFromWidgetLayout(true);
                }
            }
        }
    }

    @Override
    public void onStop() {
        if (prefHandler != null) {
            JSUtils.d(TAG, "SettingFragment onStop: Handler=%s. Modified=%d",
                    prefHandler.getClass().getCanonicalName(), isModified?1:0);
            prefHandler.onClosing(isModified);
        }
        super.onStop();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(TAG, "onSharedPreferenceChanged: key=" + key);

        isModified = true;
        if (prefHandler != null && key != null) {
            Preference preference = findPreference(key);
            if (preference instanceof IPreference) {
                IPreference jsPref = (IPreference) preference;
                if (jsPref.isNeedNotify()) {
                    prefHandler.onPreferenceChanged(key, preference);
                }
            }
        }
    }
}
