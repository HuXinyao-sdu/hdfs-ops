package com.bigdata.hdfs;

import org.apache.hadoop.fs.*;
import java.util.Date;

public class FileInfo{
	public static void show(String hdfs) throws Exception{
		FileSystem fs=HDFSUtils.getFS();
		FileStatus status=fs.getFileStatus(new Path(hdfs));
		
		System.out.println("path:"+status.getPath());
		System.out.println("permission:"+status.getPermission());
		System.out.println("len:"+status.getLen()+" bytes");
		System.out.println("create time:"+new Date(status.getModificationTime()));
		HDFSUtils.close(fs);
	}
	
	public static void main(String[] args) throws Exception{
		show(args[0]);
	}
}

