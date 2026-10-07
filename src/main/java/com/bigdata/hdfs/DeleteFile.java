package com.bigdata.hdfs;
import org.apache.hadoop.fs.*;

public class DeleteFile{
	public static void delete(String hdfs) throws Exception{
		FileSystem fs=HDFSUtils.getFS();
		boolean ok=fs.delete(new Path(hdfs),false);
		HDFSUtils.close(fs);
		System.out.println(ok ? "delete successfully" : "delete failure/file not exists");
	}
	public static void main(String[] args) throws Exception{
		delete(args[0]);
	}
}

