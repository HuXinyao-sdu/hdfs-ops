package com.bigdata.hdfs;

import org.apache.hadoop.fs.*;
import java.io.*;

public class AppendContent{
	public static void append(String hdfs,String content,boolean atHead) throws Exception{
		FileSystem fs=HDFSUtils.getFS();
		Path path = new Path(hdfs);
		
		if(atHead){
			FSDataInputStream in = fs.open(path);
			BufferedReader br= new BufferedReader(new InputStreamReader(in));
			StringBuilder sb=new StringBuilder();
			String line;
			while((line=br.readLine()) != null) sb.append(line).append("\n");
			br.close();
			
			FSDataOutputStream out=fs.create(path,true);
			out.write((content+"\n").getBytes());
			out.write(sb.toString().getBytes());
			out.close();

		}
		else{
			FSDataOutputStream out=fs.append(path);
			out.write((content+"\n").getBytes());
			out.close();
		}
		HDFSUtils.close(fs);
		System.out.println("append successfully");
	}
	
	public static void main(String[] args) throws Exception{
		append(args[0],args[1],"head".equals(args[2]));
	}
}

