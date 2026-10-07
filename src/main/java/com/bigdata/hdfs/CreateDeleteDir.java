package com.bigdata.hdfs;

import org.apache.hadoop.fs.*;

public class CreateDeleteDir{
	public static void create(String hdfsDir) throws Exception{
		FileSystem fs=HDFSUtils.getFS();
		fs.mkdirs(new Path(hdfsDir));
		HDFSUtils.close(fs);
		System.out.println("directory create successfully");
	}
	public static void delete(String hdfsDir) throws Exception{
		FileSystem fs=HDFSUtils.getFS();
		boolean ok=fs.delete(new Path(hdfsDir),false);
		HDFSUtils.close(fs);

		System.out.println(ok ? "directory delete successfully" : "directory not empty or not exists,delete failure");
	}
	public static void main(String[] args) throws Exception{
		if("create".equals(args[0])) create(args[1]);
		else delete(args[1]);
	}
}
