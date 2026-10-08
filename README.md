hdfs-ops：《大数据管理与分析》实验一源码
## 内容
- Linux与Hadoop基本操作
- HDFS操作(Shell+JavaAPI)
- MapReduce初级编程
## 目录结构

共 14 个 Java 源文件，涵盖 HDFS 操作与 MapReduce 编程。

## 文件功能说明

| 文件 | 功能 |
|---|---|
| `HDFSUtils.java` | 工具类，封装 `FileSystem` 的获取与关闭 |
| `UploadFile.java` | 上传文件，若已存在由用户选择追加或覆盖 |
| `DownloadFile.java` | 下载文件，本地同名时自动重命名 |
| `CatFile.java` | 输出 HDFS 文件内容到终端 |
| `FileInfo.java` | 显示文件权限、大小、创建时间、路径 |
| `ListDirRecursive.java` | 递归输出目录下所有文件信息 |
| `CreateDeleteFile.java` | 创建/删除文件，父目录不存在时自动创建 |
| `CreateDeleteDir.java` | 创建/删除目录，非空目录不删除 |
| `AppendContent.java` | 向文件追加内容，可指定追加到开头或结尾 |
| `DeleteFile.java` | 删除 HDFS 指定文件 |
| `MoveFile.java` | 将文件从源路径移动到目的路径 |
| `MergeDedup.java` | MapReduce 任务1：合并两个文件并去重 |
| `SortInt.java` | MapReduce 任务2：对整数升序排序并编号 |
| `GrandParent.java` | MapReduce 任务3：从父子表挖掘祖孙关系 |

## 编译运行

```bash
mvn clean package
hadoop jar target/hdfs-ops-1.0-SNAPSHOT.jar com.bigdata.hdfs.UploadFile /home/hadoop/.bashrc /user/hadoop/test/bashrc.txt
