package com.bigdata.hdfs;
import org.apache.hadoop.fs.*;
import java.io.File;

public class DownloadFile{
	public static void download(String hdfs,String localDir) throws Exception{
		FileSystem fs = HDFSUtils.getFS();
		Path src=new Path(hdfs);
		String fileName=src.getName();
		File localFile = new File(localDir,fileName);
	
		if(localFile.exists()){
			String newName = fileName.substring(0,fileName.lastIndexOf('.'))
				+"_"+System.currentTimeMillis()
				+fileName.substring(fileName.lastIndexOf('.'));
			localFile=new File(localDir,newName);
		}
		fs.copyToLocalFile(src,new Path(localFile.getAbsolutePath()));
		HDFSUtils.close(fs);
		System.out.println("download to:"+localFile.getAbsolutePath());
	}
	public static void main(String[] args) throws Exception{
		download(args[0],args[1]);
	}
}
	
