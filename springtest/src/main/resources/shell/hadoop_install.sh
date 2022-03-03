#!/bin/bash

pwd

# 判断参数个数
if (($# < 2)); then
    echo "请输入hadoop安装包路径和需要的安装路径"
    exit 1
fi

file=$1
path=$2

# 判断文件
if [[ ! -e $file ]]; then
    echo "文件不存在"
    exit 1
elif [[ ! -f $file ]]; then
    echo "不是文件"
    exit 1
else
    if [[ ! -e $path && ! -d $path ]]; then
        echo "安装路径错误"
        exit 1
    fi
    if [[ ! -w /etc/profile ]]; then
        echo "/etc/profile文件不能写, 请切换到root用户"
        exit 1
    fi



    # 安装
    if echo $file | grep -q "[\S]*.tar.gz$"; then
        echo "===开始解压==="
        # 获取解压过程中的第一条信息
        unzip_path=$(tar -xvzf $file -C $path | sed -n '1p')
        # 获取到文件夹信息
        unzip_dir=${unzip_path%%/*}
        install_path=$(cd $path/$unzip_dir;pwd)

        echo "解压后hadoop路径: $install_path"


        echo "# 配置hadoop home" >> /etc/profile
        echo "export HADOOP_HOME=$install_path" >> /etc/profile
        echo 'export PATH=$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$PATH' >> /etc/profile
        . /etc/profile

    else
        echo "无法解析的安装包"
        exit 1
    fi
fi


