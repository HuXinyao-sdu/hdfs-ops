package com.bigdata.hdfs;   //声明当前类所在包

import org.apache.hadoop.conf.Configuration;  //对象配置类
import org.apache.hadoop.fs.Path;   //hdfs输入输出路径表示类
import org.apache.hadoop.io.Text;  //文本类型==String
import org.apache.hadoop.mapreduce.Job;    //Job作业对象类
import org.apache.hadoop.mapreduce.Mapper;  //Mapper基类
import org.apache.hadoop.mapreduce.Reducer;  //Reducer基类
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;  //文件输入格式化工具，设置输入路径
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;  //文件输出格式化工具，设置输出路径

import java.io.IOException;  //处理输入输出异常类

//定义主类MergeDedup
public class MergeDedup{
	//定义静态内部类DedupMapper，实现Map阶段功能
	//DedupMapper继承Mapper基类，定义泛型<Object,Text,Text,Text>表示<InKey,InValue,OutKey,OutValue>类型
	public static class DedupMapper extends Mapper<Object,Text,Text,Text>{
		private Text word=new Text();   //定义word字符串对象，避免在每次map时都要重新创建word对象
		
		//重写基类的map函数：in：<key,value>==<行偏移量，行内容>
		@Override
		protected void map(Object key,Text value,Context context)    //inKey：Object，inValue：Text，output：context
			throws IOException,InterruptedException{
				word.set(value.toString().trim());   //获取当前行内容value，转字符串类型toString()，去除首尾空格trim()，处理结果赋值给word对象
				context.write(word,new Text("")); //获取map阶段处理结果context==<key,value>==<当前行内容，空字符串>
				//输出类型：<word,new Text("")>==<Text,Text>
			}
		}
	//shuffle阶段自动将相同key分组传入一个reduce()中
	//定义静态内部类DedupReducer，实现Reduce阶段功能
	//DedupReducer类继承基类Reducer，定义泛型<Text,Text,Text,Text>==<InKey,InValue,OutKey,OutValue>
	public static class DedupReducer extends Reducer<Text,Text,Text,Text>{
		//重写基类reduce()函数
		@Override
		protected void reduce(Text key,Iterable<Text> values,Context context)  //reduce(inKeyType,inValueType,outputKey_Value)
			throws IOException,InterruptedException{
				context.write(key,new Text(""));   //对一组相同key，保证只输出一个key，实现文件行去重
			}
	}
	//定义主方法，即作业驱动函数main，String[] args表示命令行输入，args[0]为输入路径，args[1]为输出路径
	public static void main(String[] args) throws Exception{
		Configuration conf=new Configuration();  //job采用hadoop默认配置
		Job job = Job.getInstance(conf,"Merge and Dedup");  //定义job对象：(job配置，job命名)
		job.setJarByClass(MergeDedup.class);   //设置job主类
		job.setMapperClass(DedupMapper.class);  //设置jobMap类
		job.setReducerClass(DedupReducer.class);  //设置jobReduce类
		job.setOutputKeyClass(Text.class);  //设置job输出key的类型
		job.setOutputValueClass(Text.class);  //设置job输出value的类型
		FileInputFormat.addInputPath(job,new Path(args[0]));  //设置job输入路径
		FileOutputFormat.setOutputPath(job,new Path(args[1]));  //设置job输出路径
		//job设置完毕
		//提交作业job并等待完成
		System.exit(job.waitForCompletion(true) ? 0 : 1);  //exit(0):正常退出；exit(1)：异常退出
	}
}
