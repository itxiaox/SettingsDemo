package com.itxiaox.settingsdemo;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.text.TextUtils;

import com.jishimed.jspref.IPrefHandler;
import com.jishimed.jspref.JSEditTextPreference;
import com.jishimed.jspref.PrefUtils;

public class AppPrefHandler implements IPrefHandler {
    private static final String TAG = "AppPrefHandler";

    private static final String KEY_INSTITUTION_NAME = "app_institution_name";

    @Override
    public void init(Context context, PreferenceScreen screen) {
        Preference pref = screen.findPreference(KEY_INSTITUTION_NAME);
        if (pref instanceof JSEditTextPreference) {
            boolean isOEM = PrefUtils.get(DevPrefHandler.KEY_ENABLE_OEM, false);
            JSEditTextPreference prefEdit = (JSEditTextPreference)pref;
            String value = prefEdit.getValue();
            String js = context.getString(R.string.company_name);
            String oem = "汇影超声";
            if (isOEM) {
                if (TextUtils.equals(js, value) || TextUtils.isEmpty(value)) prefEdit.setValue(oem);
            }
            else {
                if (TextUtils.equals(oem, value) || TextUtils.isEmpty(value)) prefEdit.setValue(js);
            }
        }
    }
}
