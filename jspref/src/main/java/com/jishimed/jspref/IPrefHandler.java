package com.jishimed.jspref;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceScreen;

public interface IPrefHandler {

    /**
     * 初始化
     * @param context   Activity Context (若是Application Context，会有异常)
     * @param screen    最顶层的PreferenceScreen (root)
     */
    void init(Context context, PreferenceScreen screen);

    /**
     * 补充未完整的Preference（KEY以#开头）的信息，
     * @param key  Key of the preference, including #
     * @param preference Preference object
     * @return  true if succeeded
     */
    default boolean completePreference(String key, Preference preference) {
        return false;
    }

    /**
     * Preference修改后的被调用，用于维护Preference间的关联
     * @param key   被修改的Preference Key
     * @param preference    被修改的Preference
     * @return  true if modified.
     */
    default boolean onPreferenceChanged(String key, Preference preference) {
        return false;
    }

    /**
     * 判断Preference的值是否合法
     * @param key  Preference KEY
     * @param value  Preference Value
     * @return true if valid.
     */
    default boolean isValid(String key, String value) {
        return true;
    }

    /**
     * 当PreferenceScreen关闭前被调用
     * @param modified  是否有设置被修改
     */
    default void onClosing(boolean modified) {
    }
}
