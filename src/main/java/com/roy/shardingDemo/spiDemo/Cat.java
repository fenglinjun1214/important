package com.roy.shardingDemo.spiDemo;

/**
 * @author ：楼兰
 * @date ：Created in 2021/1/7
 * @description:
 **/

public class Cat implements Animal {
    @Override
    public void noise() {
        System.out.println("miao miao miao");
    }
}
