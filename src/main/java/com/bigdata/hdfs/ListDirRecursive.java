package com.bigdata.hdfs;
import org.apache.hadoop.fs.*;
import java.util.Date;

public class ListDirRecursive{
	public static void list(String hdfsDir) throws Exception{
		FileSystem fs=HDFSUtils.getFS();
		RemoteIterator<LocatedFileStatus> it=fs.listFiles(new Path(hdfsDir),true);
		while(it.hasNext()){
			LocatedFileStatus status=it.next();
			System.out.println("path:"+status.getPath());
			System.out.println("permission:"+status.getPermission());
			System.out.println("len:"+status.getLen()+" bytes");
			System.out.println("create time:"+new Date(status.getModificationTime()));
			System.out.println("------");
		}
		HDFSUtils.close(fs);
	}
	public static void main(String[] args) throws Exception{
		list(args[0]);
	}
}
	

