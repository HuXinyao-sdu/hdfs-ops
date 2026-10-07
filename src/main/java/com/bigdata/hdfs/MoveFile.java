package com.bigdata.hdfs;

import org.apache.hadoop.fs.*;

public class MoveFile{
	public static void move(String src,String dst) throws Exception{
		FileSystem fs=HDFSUtils.getFS();
		boolean ok=fs.rename(new Path(src),new Path(dst));
		HDFSUtils.close(fs);
		System.out.println(ok ? "move successfully" : "move failure");
	}
	public static void main(String[] args) throws Exception{
		move(args[0],args[1]);
	}
}
