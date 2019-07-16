package com.itxiaox.settingsdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceScreen;

import com.jishimed.jspref.IPrefHandler;
import com.jishimed.jspref.PrefUtils;

public class DevPrefHandler implements IPrefHandler {
    private static final String TAG = "DevPrefHandler";

    public static final String KEY_ENGINEERING_MODE = "dev_engineering_mode";
    public static final String KEY_DEVELOPMENT_MODE = "dev_development_mode";
    public static final String KEY_AI_VISIBLE = "dev_ai_visible";
    public static final String KEY_BIT_RATE = "dev_bit_rate";
    public static final String KEY_COMPRESS_QUALITY = "dev_compress_quality";
    public static final String KEY_ENABLE_OEM = "dev_enable_oem";

    private Context context;

    @Override
    public void init(Context context, PreferenceScreen screen) {
        this.context = context;
    }

    @Override
    public void onClosing(boolean modified) {
        if (!modified) return;

        //OEM
        boolean enabled = PrefUtils.get(KEY_ENABLE_OEM, false);
//        enableComponent(".Activity_JS", !enabled);
//        enableComponent(".MainActivity_OEM", enabled);
        enableComponent(".SplashActivity_JS", !enabled);
        enableComponent(".SplashActivity_OEM", enabled);

        //Engineering Mode
        boolean isEngineering = PrefUtils.get(KEY_ENGINEERING_MODE, false);
        if (!isEngineering) {
            PrefUtils.set(DevPrefHandler.KEY_DEVELOPMENT_MODE, false);
        }


    }

    private void enableComponent(String name, boolean enabled) {
        String packageName = context.getApplicationContext().getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + name);

        int oldState = context.getPackageManager().getComponentEnabledSetting(componentName);
        int newState = enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;

        if (newState != oldState) {
            context.getPackageManager().setComponentEnabledSetting(componentName,
                    newState, PackageManager.DONT_KILL_APP);
        }
    }
}
