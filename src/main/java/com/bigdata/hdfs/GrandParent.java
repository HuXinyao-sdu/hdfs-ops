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
import java.util.ArrayList;
import java.util.List;

public class GrandParent {
    public static class GPMapper extends Mapper<Object, Text, Text, Text> {
        @Override
        protected void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] tokens = value.toString().trim().split("\\s+");
            if (tokens.length == 2) {
                String child = tokens[0];
                String parent = tokens[1];
                // 左表：person 是 child，后面跟 parent
                context.write(new Text(child), new Text("L:" + parent));
                // 右表：person 是 parent，后面跟 child
                context.write(new Text(parent), new Text("R:" + child));
            }
        }
    }

    public static class GPReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            List<String> parents = new ArrayList<>();
            List<String> children = new ArrayList<>();

            for (Text val : values) {
                String v = val.toString();
                if (v.startsWith("L:")) {
                    parents.add(v.substring(2));
                } else {
                    children.add(v.substring(2));
                }
            }

            // 笛卡尔积：child 和 parent 配对
            for (String c : children) {
                for (String p : parents) {
                    context.write(new Text(c), new Text(p));
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Grand Parent");
        job.setJarByClass(GrandParent.class);
        job.setMapperClass(GPMapper.class);
        job.setReducerClass(GPReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
