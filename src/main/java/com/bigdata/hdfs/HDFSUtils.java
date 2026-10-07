package com.bigdata.hdfs;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import java.net.URI;

public class HDFSUtils{
	public static final String HDFS_URI="hdfs://localhost:9000";
	public static FileSystem getFS() throws Exception{
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS",HDFS_URI);
		return FileSystem.get(new URI(HDFS_URI),conf,"hadoop");
	}
	public static void close(FileSystem fs) throws Exception{
		if(fs != null)fs.close();
	}
}

