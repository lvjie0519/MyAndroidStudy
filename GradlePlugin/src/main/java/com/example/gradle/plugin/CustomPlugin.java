package com.example.gradle.plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

public class CustomPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
//        project.task("customPlugin").doLast(new Action<Task>() {
//            @Override
//            public void execute(Task task) {
//                System.out.println("I am CustomPlugin!");
//            }
//        });

//        AppExtension appExtension = project.getExtensions().findByType(AppExtension.class);
//        if (appExtension == null) {
//            return;
//        }
//        //注册Transform
//        appExtension.registerTransform(new LogTransform());
    }
}
