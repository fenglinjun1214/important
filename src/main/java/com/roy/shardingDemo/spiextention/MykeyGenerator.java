package com.roy.shardingDemo.spiextention;

import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ：楼兰
 * @date ：Created in 2020/12/17
 * @description: 扩展自定义的主键生成策略
 **/

public class MykeyGenerator implements ShardingKeyGenerator {

    private AtomicLong atom = new AtomicLong(0);

    private Properties properties = new Properties();

    public Comparable<?> generateKey() {
        //读取了一个自定义属性
//        String prefix = properties.getProperty("mykey-offset", "100");
        LocalDateTime ldt = LocalDateTime.now();
        String timestampS = DateTimeFormatter.ofPattern("HHmmssSSS").format(ldt);
        return Long.parseLong(""+timestampS+atom.incrementAndGet());
    }
    //扩展算法的类型
    public String getType() {
        return "MYKEY";
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
