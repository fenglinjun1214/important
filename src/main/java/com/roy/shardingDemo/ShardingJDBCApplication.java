package com.roy.shardingDemo;

import com.roy.shardingDemo.entity.Course;
import com.roy.shardingDemo.mapper.CourseMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author ：楼兰
 * @date ：Created in 2020/11/12
 * @description:
 **/
@SpringBootApplication
@MapperScan("com.roy.shardingDemo.mapper")
public class ShardingJDBCApplication{

    public static void main(String[] args) {
        SpringApplication.run(ShardingJDBCApplication.class,args);
    }

}
