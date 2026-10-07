package com.bigdata.hdfs;

import org.apache.hadoop.fs.*;

public class CreateDeleteFile{
	public static void create(String hdfs) throws Exception{
		FileSystem fs=HDFSUtils.getFS();
		Path path=new Path(hdfs);
		fs.mkdirs(path.getParent());
		fs.create(path).close();
		HDFSUtils.close(fs);
		System.out.println("create successfully");
	}
	
	public static void delete(String hdfs) throws Exception{
		FileSystem fs=HDFSUtils.getFS();
		boolean ok=fs.delete(new Path(hdfs),false);
		HDFSUtils.close(fs);

 		System.out.println(ok ? "delete successfully" : "delete failure/file not exists");
	}

	public static void main(String[] args) throws Exception{
		if("create".equals(args[0])) create(args[1]);
		else delete(args[1]);
	}
}
