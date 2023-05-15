package com.android.study.example;

import org.junit.runners.model.InitializationError;
import org.robolectric.RoboSettings;
import org.robolectric.RobolectricTestRunner;

public class CustomRobolectricTestRunner extends RobolectricTestRunner {
    public CustomRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        RoboSettings.setMavenRepositoryId("alimaven");
        RoboSettings.setMavenRepositoryUrl("http://maven.aliyun.com/nexus/content/groups/public/");
    }
}