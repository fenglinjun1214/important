package com.roy.shardingDemo;

import com.roy.shardingDemo.spiDemo.Animal;

import java.util.ServiceLoader;

/**
 * @author ：楼兰
 * @date ：Created in 2021/1/7
 * @description:
 **/

public class SPITest {

    public static void main(String[] args) {
        ServiceLoader<Animal> animals = ServiceLoader.load(Animal.class);
        animals.forEach(animal -> animal.noise());
    }
}
