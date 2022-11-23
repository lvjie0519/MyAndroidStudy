PROJECT_CODE="myproject"
SDK_DEMO_PROJECTS=($PROJECT_CODE/tests/SDKDemo $PROJECT_CODE/tests/SubAppDemo $PROJECT_CODE/tests/HostAppDemo)
SDK_DEMO_APKS=(SDKDemo_release.apk SubAppDemo_release.apk HostAppDemo_release.apk)

#编译项目工程
BUILD_DEMO_PROJECT()
{
    PROJECT_BUILD_PATH=$1
    PROJECT_BUILD_APK=$2
    echo "PROJECT_BUILD_PATH:$PROJECT_BUILD_PATH  APK:$PROJECT_BUILD_APK"
    return 0
}

for(( i=0;i<${#SDK_DEMO_PROJECTS[@]};i++)) do
#${#SDK_DEMO_PROJECTS[@]}获取数组长度用于循环
BUILD_DEMO_PROJECT ${SDK_DEMO_PROJECTS[i]} ${SDK_DEMO_APKS[i]}
done;
