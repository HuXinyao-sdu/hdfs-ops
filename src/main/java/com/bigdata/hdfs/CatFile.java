package com.bigdata.hdfs;

import org.apache.hadoop.fs.*;
import java.io.*;

public class CatFile{
	public static void cat(String hdfs) throws Exception{
		FileSystem fs=HDFSUtils.getFS();
		FSDataInputStream in = fs.open(new Path(hdfs));
		BufferedReader br= new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line=br.readLine()) !=null){
			System.out.println(line);
		}
		br.close();
		HDFSUtils.close(fs);
	}
	public static void main(String[] args) throws Exception{
		cat(args[0]);
	}
}


