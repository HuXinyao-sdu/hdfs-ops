package com.bigdata.hdfs;   //当前类所在包声明

import org.apache.hadoop.conf.Configuration;  //配置类
import org.apache.hadoop.fs.Path;    //路径类
import org.apache.hadoop.io.Text;    //字符串类型
import org.apache.hadoop.mapreduce.Job;    //job对象类
import org.apache.hadoop.mapreduce.Mapper;    //mapper基类
import org.apache.hadoop.mapreduce.Reducer;   //reducer基类
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;   //文件输入格式工具类，设置输入路径
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;  //文件输出格式类，设置输出路径

import java.io.IOException;   //输入输出异常类
import java.util.ArrayList;     //在reducer中保存父母列表与孩子列表
import java.util.List;    //列表变量声明

//定义主类grandparent
public class GrandParent {
	//定义静态内部类GPMapper，实现map功能，继承基类Mapper
	//input:<Object key,Text value>
	//output:<Text key,Text key>==Context
    public static class GPMapper extends Mapper<Object, Text, Text, Text> {
    	//重写map函数
        @Override
        protected void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {//声明可能抛出输入输出异常与线程中断异常
            String[] tokens = value.toString().trim().split("\\s+"); //当前行内容转字符串，去除首尾空白，按空格进行划分，得到孩子+父母姓名组成的字符串数组
            if (tokens.length == 2) {  
                String child = tokens[0]; //获取孩子姓名
                String parent = tokens[1]; //获取父母姓名
                // 左表：person 是 child，后面跟 parent
                context.write(new Text(child), new Text("L:" + parent));
                // 右表：person 是 parent，后面跟 child
                context.write(new Text(parent), new Text("R:" + child));
            }
        }
    }
	//定义静态内部类GPReducer，继承基类Reducer，实现reduce阶段功能
    public static class GPReducer extends Reducer<Text, Text, Text, Text> {
    	//重写reduce函数
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            List<String> parents = new ArrayList<>();  //定义父母列表 
            List<String> children = new ArrayList<>();  //定义孩子列表

            for (Text val : values) {   //遍历相同key键值对中value值
                String v = val.toString();   //转字符串
                if (v.startsWith("L:")) {
                    parents.add(v.substring(2));   //加入父母列表
                } else {
                    children.add(v.substring(2));  //加入孩子列表
                }
            }
			//获取当前相同key值的孩子与父母列表
            // 笛卡尔积：child 和 parent 配对
            for (String c : children) {
                for (String p : parents) {
                    context.write(new Text(c), new Text(p));  //孩子+祖父母
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration(); //对象配置
        Job job = Job.getInstance(conf, "Grand Parent");  //对象声明
        job.setJarByClass(GrandParent.class); //设置主类
        job.setMapperClass(GPMapper.class);  //设置mapper类
        job.setReducerClass(GPReducer.class);  //设置reducer类
        job.setOutputKeyClass(Text.class);  //设置输出key类型
        job.setOutputValueClass(Text.class);  //设置输出value类型
        FileInputFormat.addInputPath(job, new Path(args[0]));  //设置文件输入路径
        FileOutputFormat.setOutputPath(job, new Path(args[1]));  //设置文件输出路径
        System.exit(job.waitForCompletion(true) ? 0 : 1);  //提交作业并等待至结束
    }
}
