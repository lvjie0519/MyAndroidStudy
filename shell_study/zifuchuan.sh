
# 测试用例： sh zifuchuan.sh standardDebug
PRODUCT_FLAVOR=$1
# 字符串比较
checkGradleTask() {
    case ${PRODUCT_FLAVOR} in 
    "autotest" )     
        GRADLE_TASK="autotest"
        ;;
    "autotestDebug" )     
        GRADLE_TASK="autotestDebug"
        ;;
    "standardDebug" )     
        GRADLE_TASK="standardDebug"
        ;;
    esac

    echo "project gradle task is ${GRADLE_TASK}"
}

# 字符串比较
compareString() {
    string1="1234"
    string2="1234"
    string3="123123"
    string4="true"
    local param=$1

    if [ -z "$param" ]
    then
        param="hello world"
    fi

    echo "param=$param"
    echo "string3=$string3"
    if [ $string1 != $string2 ] 
    then
        echo "not same string 1"
    fi

    if [ "$string3" != "$param" ]
    then
        echo "not same string 2"
    fi

    if [ "$param" == "1" ]
    then
        echo "same string4"
    fi

    echo $param
}

# 判断目录是否存在
checkDirExist() {
    dir="/run/media/lvjie/DATA/lvjie"
    if [ ! -d "$dir" ]
    then
        echo "dir not exist"
    fi
}

# 判断文件是否存在
checkFileExist(){
    local filePath="/run/media/lvjie/DATA/lvjie/shell_study"
    local fileName="${filePath}/aa.txt"
    if [ -e "$fileName" ]
    then
        echo "$fileName 文件存在"
        touch $fileName
    else
        echo "$fileName 文件不存在"
    fi
}

# 写文件
writeVersionToFile(){
    local versionFileName="aa.txt"
    echo "version.code=20" > ${versionFileName};
    echo "version.name=2.100.2" >> ${versionFileName};

    cat ${versionFileName}

    cd /run/media/lvjie/DATA/lvjie/shell_study/
    pwd
    git restore ${versionFileName}
}

# 文件内容替换
replaceFileContent(){
    local fileName="theme.js"
    # sed -i ''  's/if(m)for(n.handler/if(false)for(n.handler/g' ${fileName}    # mac
    sed -i "s#if(true)for(n.handler#if(false)for(n.handler#g" ${fileName}
}

# checkGradleTask

 param1=$1
 compareString $param1

# checkDirExist

#checkFileExist

# writeVersionToFile

# replaceFileContent

exit 0
