hdfs-ops：《大数据管理与分析》实验一源码
## 内容
- Linux与Hadoop基本操作
- HDFS操作(Shell+JavaAPI)
- MapReduce初级编程
## 目录结构
hdfs-ops/
├── .gitignore
├── README.md
├── pom.xml
└── src/main/java/com/bigdata/hdfs/
    ├── HDFSUtils.java              # 工具类
    ├── UploadFile.java             # 功能1：上传
    ├── DownloadFile.java           # 功能2：下载
    ├── CatFile.java                # 功能3：查看内容
    ├── FileInfo.java               # 功能4：文件信息
    ├── ListDirRecursive.java       # 功能5：递归列出
    ├── CreateDeleteFile.java       # 功能6：创建/删除文件
    ├── CreateDeleteDir.java        # 功能7：创建/删除目录
    ├── AppendContent.java          # 功能8：追加内容
    ├── DeleteFile.java             # 功能9：删除文件
    ├── MoveFile.java               # 功能10：移动文件
    ├── MergeDedup.java             # MapReduce任务1：合并去重
    ├── SortInt.java                # MapReduce任务2：整数排序
    └── GrandParent.java            # MapReduce任务3：祖孙挖掘
