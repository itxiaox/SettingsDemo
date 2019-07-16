package com.itxiaox.settingsdemo;//package com.jishimed.pocus.setting;
//
//import android.content.SharedPreferences;
//import android.content.res.XmlResourceParser;
//import android.os.Bundle;
//import android.preference.PreferenceActivity;
//import android.preference.PreferenceManager;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.widget.LinearLayout;
//
//import com.jishimed.pocus.R;
//
//import org.xmlpull.v1.XmlPullParser;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//@Deprecated
//public class SettingsActivity extends PreferenceActivity {
//    private static final String TAG = "SettingsActivity";
//    private final List<Header> headerList = new ArrayList<>();
//
//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//
//        //PreferenceActivity 添加ActionBar, 返回
//        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
//        @SuppressWarnings("Annotator")
//        LinearLayout layout_title = (LinearLayout) LayoutInflater.from(this)
//                .inflate(R.layout.settings_toolbar, root, false);
//        Toolbar bar = layout_title.findViewById(R.id.toolbar);
//        root.addView(layout_title, 0); // insert at top
//        bar.setNavigationOnClickListener(v -> onBackPressed());
//    }
//
//    @Override
//    protected boolean isValidFragment(String fragmentName) {
//        return true;
//    }
//
//    @Override
//    public void onBuildHeaders(List<Header> target) {
//        addHeaders(target);
//    }
//
//    private void addHeaders(List<Header> target) {
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
//        boolean isEngineering = sharedPref.getBoolean(AppPrefHandler.KEY_ENGINEERING_MODE, false);
//        boolean isDevelopment = sharedPref.getBoolean(AppPrefHandler.KEY_DEVELOPMENT_MODE, false);
//        int modeMask = SettingsFragment.FLAG_NONE;
//        if (isEngineering) modeMask |= SettingsFragment.FLAG_ENGINEERING;
//        if (isDevelopment) modeMask |= SettingsFragment.FLAG_DEVELOPMENT;
//
//        //通过反射从R文件中读取到相应的preference.xml文件
//        Class<R.xml> clazz = R.xml.class;
//        Field[] fields = clazz.getDeclaredFields();//得到所有的属性
//        for (Field field : fields) {
//            //打开私有访问
//            field.setAccessible(true);
//            String name = field.getName(); //循环得到一个属性字段名
//            if (name.startsWith("pref_")) { //过滤，约定preference.xml的文件命名以pre开头
//                //获取属性值
//                try {
//                    int value = (int) field.get(clazz.newInstance());//得到属性值
//                    Log.i(TAG, "onCreate: filed,value=" + value);
//                    Data data = readXml(value);
//                    if (data != null) {
//                        if (data.flag != 0 && 0 == (data.flag & modeMask)) {
//                            //工程师/开发者模式，对应模式未开启
//                            continue;
//                        }
//
//                        //创建header 动态添加到header列表中
//                        Header header = new Header();
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("settings", value);
//                        header.fragmentArguments = bundle;
//                        String title = data.title;
//                        if (title.contains("@")) { //@2121689588
//                            title = (String) getResources().getText(Integer.valueOf(title.replace("@", "")));
//                        }
//                        String summary = data.summary;
//                        if (summary.contains("@")) {
//                            summary = (String) getResources().getText(Integer.valueOf(summary.replace("@", "")));
//                        }
//                        header.title = title;
//                        header.summary = summary;
//                        header.fragment = "com.jishimed.pocus.setting.SettingsFragment";
//                        headerList.add(header);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        target.addAll(headerList);
//    }
//
//    class Data {
//        String key;
//        String title;
//        String summary;
//        int flag;// bit 0 : engineer Mode
//    }
//
//    private Data readXml(int resId) throws Exception {
//        Data data = null;
//        XmlResourceParser parser = getResources().getXml(resId);
//        //读取文件的类型
//        int event = parser.getEventType();
//        while (event != XmlPullParser.END_DOCUMENT) {
//            if (XmlPullParser.START_TAG == event) {  //标签开始标志
//                if ("PreferenceScreen".equals(parser.getName())) {
//                    String key = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "key");
//                    String defaultValue = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "defaultValue");
//                    String title = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "title");
//                    String summary = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "summary");
//                    data = new Data();
//                    data.key = key;
//                    data.title = title;
//                    data.summary = summary;
//                    if (!TextUtils.isEmpty(defaultValue)) {
//                        data.flag = Integer.valueOf(defaultValue);
//                    } else {
//                        data.flag = 0;
//                    }
//                }
//                break;
//            }
//            event = parser.next();//进行下一个标签的解析
//        }
//        return data;
//    }
//}
