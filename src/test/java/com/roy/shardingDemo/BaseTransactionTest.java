package com.roy.shardingDemo;

import com.roy.shardingDemo.transaction.SeataOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ：楼兰
 * @date ：Created in 2021/4/22
 * @description: Base柔性事务测试案例
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseTransactionTest {

    @Autowired
    private SeataOrderService orderService;

    @Test
    public void executeOrderService() {
        orderService.init();
        orderService.insert(10);
        System.out.println("1 == "+orderService.selectAll());
        orderService.insertFailed(20);
        System.out.println("2 == "+orderService.selectAll());
        orderService.cleanup();
    }

}
