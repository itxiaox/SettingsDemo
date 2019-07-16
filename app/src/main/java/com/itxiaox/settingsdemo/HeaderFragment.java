package com.itxiaox.settingsdemo;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;


import com.jishimed.jspref.PrefUtils;

import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Field;

public class HeaderFragment extends PreferenceFragment {
    static final int FLAG_NONE = 0x0;
    static final int FLAG_ENGINEERING = 0x1;
    static final int FLAG_DEVELOPMENT = 0x2;
    private int modeMask = SettingsFragment.FLAG_NONE;

    private static final String TAG = "settings";
    SettingsDialogFragment dialogFragment;

    public void setFragment(SettingsDialogFragment fragment) {
        dialogFragment = fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.header);
        PreferenceManager preferenceManager = getPreferenceManager();
        PreferenceScreen rootPreferenceScreen = getPreferenceScreen();
//        boolean isEngineering = PrefUtils.get(DevPrefHandler.KEY_ENGINEERING_MODE, false);
//        boolean isDevelopment = PrefUtils.get(DevPrefHandler.KEY_DEVELOPMENT_MODE, false);
//        int modeMask = SettingsFragment.FLAG_NONE;
//        if (isEngineering) modeMask |= SettingsFragment.FLAG_ENGINEERING;
//        if (isDevelopment) modeMask |= SettingsFragment.FLAG_DEVELOPMENT;
        //通过反射从R文件中读取到相应的preference.xml文件
        Class<R.xml> clazz = R.xml.class;
        Field[] fields = clazz.getDeclaredFields();//得到所有的属性
        //Log.i(TAG, "onCreate: fields=" + fields.length);
        for (Field field : fields) {
            //打开私有访问
            field.setAccessible(true);
            String name = field.getName(); //循环得到一个属性字段名
            if (name.startsWith("pref_")) { //过滤，约定preference.xml的文件命名以pref_开头
                //Log.i(TAG, "onCreate: field, name=" + name);
                //获取属性值
                try {
                    //得到资源ID
                    int resId = field.getInt(null);
                    //int value = (int) field.get(clazz.newInstance());//得到属性值
                    //int v2 = getResources().getIdentifier(name, "xml", getContext().getPackageName());

                    Data data = readXml(resId);
                    if (data != null) {
                        if (data.flag != 0 && 0 == (data.flag & modeMask)) {
                            //工程师/开发者模式，对应模式未开启
                            continue;
                        }
                        Log.i(TAG, "onCreate: filed,value=" + resId);
                        String title = data.title;
                        if (title.charAt(0) == '@') { //String like "@2121689588"
                            title = getResources().getString(Integer.parseInt(title.substring(1)));
                        }
                        String summary = data.summary;
                        if (summary.charAt(0) == '@') {
                            summary = getResources().getString(Integer.parseInt(summary.substring(1)));
                        }

                        PreferenceScreen preference = preferenceManager.createPreferenceScreen(getActivity());
                        preference.setTitle(title);
                        preference.setSummary(summary);
                        preference.setKey(data.key);
                        preference.setFragment(Integer.toString(resId));
                        rootPreferenceScreen.addItemFromInflater(preference);
                        preference.setOnPreferenceClickListener(pref -> {
                            if (dialogFragment == null) return false;
                            int xmlResId = Integer.parseInt(pref.getFragment());
                            dialogFragment.goItemFragment(xmlResId);
                            return false;
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Data {
        String key;
        String title;
        String summary;
        int flag;// bit 0 : engineer Mode
    }

    private Data readXml(int resId) throws Exception {
        Data data = null;
        XmlResourceParser parser = getResources().getXml(resId);
        //读取文件的类型
        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            if (XmlPullParser.START_TAG == event) {  //标签开始标志
                if ("PreferenceScreen".equals(parser.getName())) {
                    String key = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "key");
                    String defaultValue = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "defaultValue");
                    String title = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "title");
                    String summary = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "summary");
                    data = new Data();
                    data.key = key;
                    data.title = title;
                    data.summary = summary;
                    if (!TextUtils.isEmpty(defaultValue)) {
                        data.flag = Integer.valueOf(defaultValue);
                    } else {
                        data.flag = 0;
                    }
                }
                break;
            }
            event = parser.next();//进行下一个标签的解析
        }
        return data;
    }
}
