package com.bigdata.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MergeDedup{
	//Mapper泛型参数：<Object,Text,Text,Text>，
	//input{key:Object,value:Text},output{key:Text,value:Text}
	public static class DedupMapper extends Mapper<Object,Text,Text,Text>{
		private Text word=new Text();
		
		@Override
		protected void map(Object key,Text value,Context context) 
			throws IOException,InterruptedException{
				word.set(value.toString().trim());//获取输入行内容，去除首尾空格，并设置到word对象
				context.write(word,new Text(""));//将处理后内容作为key写出，value写入一个空的Text
			}
		}
	public static class DedupReducer extends Reducer<Text,Text,Text,Text>{
		@Override
		protected void reduce(Text key,Iterable<Text> values,Context context)
			throws IOException,InterruptedException{
				context.write(key,new Text(""));
			}
	}
	public static void main(String[] args) throws Exception{
		Configuration conf=new Configuration();
		Job job = Job.getInstance(conf,"Merge and Dedup");
		job.setJarByClass(MergeDedup.class);
		job.setMapperClass(DedupMapper.class);
		job.setReducerClass(DedupReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job,new Path(args[0]));
		FileOutputFormat.setOutputPath(job,new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
