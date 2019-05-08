package com.android.study.example.uidemo;

public class DebugPluginInfo {

    private String pluginPackageName;
    private String pluginDeviceModel;
    private boolean isCheck;

    public DebugPluginInfo() {
    }

    public DebugPluginInfo(String pluginPackageName, String pluginDeviceModel, boolean isCheck) {
        this.pluginPackageName = pluginPackageName;
        this.pluginDeviceModel = pluginDeviceModel;
        this.isCheck = isCheck;
    }

    public String getPluginPackageName() {
        return pluginPackageName;
    }

    public void setPluginPackageName(String pluginPackageName) {
        this.pluginPackageName = pluginPackageName;
    }

    public String getPluginDeviceModel() {
        return pluginDeviceModel;
    }

    public void setPluginDeviceModel(String pluginDeviceModel) {
        this.pluginDeviceModel = pluginDeviceModel;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
