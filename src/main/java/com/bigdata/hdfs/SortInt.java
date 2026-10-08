package com.bigdata.hdfs;  //声明当前类所在包
//导入hadoop核心类
import org.apache.hadoop.conf.Configuration;  //job对象配置类
import org.apache.hadoop.fs.Path;  //路径表示函数
import org.apache.hadoop.io.IntWritable;  //IntWritable类
import org.apache.hadoop.io.Text;  //Text类
import org.apache.hadoop.mapreduce.Job;  //Job对象类
import org.apache.hadoop.mapreduce.Mapper;  //Mapper基类
import org.apache.hadoop.mapreduce.Reducer;  //Reducer基类
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;  //设置输入路径类
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;  //设置输出路径类

import java.io.IOException; //输入输出异常类

//定义主类SortInt
public class SortInt {
	//定义静态内部类SortMapper，继承基类Mapper，实现map阶段功能
	//input：<Object,Text>==<行内偏移量，行内容>
	//output:<IntWritable,IntWritable>==<key,value>==<待排序数字，无关值>
    public static class SortMapper extends Mapper<Object, Text, IntWritable, IntWritable> {
    	//创建一个可复用的IntWritable对象，用于保存读取到的整数
        private IntWritable num = new IntWritable();
		//重写基类map函数
        @Override
        protected void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString().trim();  //行内容value，转字符串toString()，去除首尾空白trim()，赋值给line
            if (!line.isEmpty()) {   //当前行非空==存在整数
                num.set(Integer.parseInt(line));   // 从line中解析出整数，赋值给num
                context.write(num, new IntWritable(1));  //output写入context=<key,value>==<要排序的整数，填充值1>
            }
        }
    }
    //shuffle阶段：自动key分组，输入同一个reduce中；output：<相同key，key对应value列表>
	//定义静态内部类SortReducer，继承基类Reducer，实现reduce阶段功能
	//input:<IntWriter,IntWriter>==<整数，填充值1>
	//output:<IntWriter,IntWriter>==<序号，整数>
    public static class SortReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
    	//序号记录值index
        private int index = 1;
		//重写基类的reduce函数
        @Override
        protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            context.write(new IntWritable(index), key);   //output：<序号，整数>
            index++;  //更新索引
        }
    }
	//定义主方法，作业驱动
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();  //配置
        Job job = Job.getInstance(conf, "Sort Integers");  //获取作业对象实例，命名为sort Integers
        //设置作业对象的主类-mapper类-reducer类
        job.setJarByClass(SortInt.class);  
        job.setMapperClass(SortMapper.class);
        job.setReducerClass(SortReducer.class);
        //设置作业对象输出key+value值类型，均为IntWritable
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        //设置输入输出路径from命令行参数
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        //提交作业并等待执行完成；正常完成返回true-exit(0)
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
