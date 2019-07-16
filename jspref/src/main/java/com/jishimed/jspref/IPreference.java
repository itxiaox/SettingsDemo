package com.jishimed.jspref;

public interface IPreference {
    boolean isFromWidgetLayout();
    boolean setFromWidgetLayout(boolean fromLayout);
    int getFlag();
    boolean isNeedNotify();
    boolean isNeedComplete();
}
