# SettingsDemo
自定义灵活配置APP的setting界面，使用Android官方的 Preference来实现

几点说明：

1.  官方Setting Preference保存的SharePreference的路径为
    /data/data/packagename/shared_prefs/package_preference.xml
2. 修改上面文件的里的值会触发 PreferenceFragment的下面回调方法

```

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
	
```	

	在jspref中对回调方法进行了拦截，只有在xml中设置了 isNeedNotify = true,才会进行回调通知
	
```
	<com.jishimed.jspref.JSSpinnerPreference
        android:key="dev_spinner_login_type"
        android:title="@string/setting_login_type"
        pref:needNotify="true"
        pref:entries="@array/app_login_types"
        pref:entryValues="@array/app_login_values"
        />
````