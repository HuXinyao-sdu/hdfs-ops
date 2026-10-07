package com.bigdata.hdfs;
import org.apache.hadoop.fs.*;
import java.io.*;
import java.util.Scanner;

public class UploadFile{
	public static void upload(String local,String hdfs) throws Exception{
		FileSystem fs=HDFSUtils.getFS();
		Path dst=new Path(hdfs);
		Scanner sc=new Scanner(System.in);
		if (fs.exists(dst)){
			System.out.println("HDFS file has existed, please choose:1.append 2.copy");
			int choice=sc.nextInt();
			if(choice==1){
				FSDataOutputStream out=fs.append(dst);
				BufferedReader br=new BufferedReader(new FileReader(local));
				String line;
				while((line=br.readLine()) !=null){
					out.write((line+"\n").getBytes());
				}
				br.close();
				out.close();
			}
			else{
				fs.delete(dst,true);
				fs.copyFromLocalFile(new Path(local),dst);
			}
		}
		else{
			fs.copyFromLocalFile(new Path(local),dst);
		}
                HDFSUtils.close(fs);
		System.out.println("send successfully");
	}
	public static void main(String[] args) throws Exception{
		upload(args[0],args[1]);
	}
}
