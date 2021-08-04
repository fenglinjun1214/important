package com.roy.shardingDemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.roy.shardingDemo.entity.Course;
import com.roy.shardingDemo.entity.Dict;
import com.roy.shardingDemo.entity.User;
import com.roy.shardingDemo.mapper.CourseMapper;
import com.roy.shardingDemo.mapper.DictMapper;
import com.roy.shardingDemo.mapper.UserMapper;
import org.apache.shardingsphere.api.hint.HintManager;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：楼兰
 * @date ：Created in 2020/11/12
 * @description:
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShardingJDBCTest {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private DictMapper dictMapper;

    //插入数据会进行分片
    @Test
    @Transactional
    @ShardingTransactionType(TransactionType.XA)
    public void addcourse() {
        for (int i = 0; i < 10; i++) {
            Course c = new Course();
            c.setCname("java");
            c.setUserId(1001L);
            c.setCstatus("1");
            courseMapper.insert(c);
        }
    }

    @Test
    @Transactional
    @ShardingTransactionType(TransactionType.XA)
    public void updateCourse(){
        Course c = new Course();
        UpdateWrapper<Course> wrapper = new UpdateWrapper<>();
        wrapper.set("user_id","5");
        courseMapper.update(c,wrapper);
    }

    //查询同样是按照分片规则。
    //这里会测试多种规则。
    @Test
    public void queryCourse() {
        QueryWrapper<Course> wrapper = new QueryWrapper<Course>();
        wrapper.eq("user_id", 3);
//        Course course = courseMapper.selectOne(wrapper);
//        System.out.println(course);
//        wrapper.in("cid",1627057021L,1627059043L,550364727642402817L);
        List<Course> courses = courseMapper.selectList(wrapper);
//        List<Course> courses = courseMapper.selectList(null);
//        List<Course> courses = courseMapper.queryAllCourse();
        courses.forEach(course -> System.out.println(course));
    }
    @Test
    public void queryCourseRange(){
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.between("cid",550364727642402817L,550364727642402820L);
        List<Course> courses = courseMapper.selectList(wrapper);
        courses.forEach(course -> System.out.println(course));
    }

    //application01的inline策略，对于这种排序查询，也必须要带上cid的查询条件，否则解析出来的实际SQL就会出问题。
    @Test
    public void queryCourseOrder(){
        QueryWrapper<Course> wrapper = new QueryWrapper<Course>();
        wrapper.orderByDesc("user_id");
        wrapper.eq("cid",1422449906428702722L);
        Course course = courseMapper.selectOne(wrapper);
        System.out.println(course);
    }

    //复杂分片策略，所有查询条件只能有一个统一的类型。
    @Test
    public void queryCourdeComplex(){
        QueryWrapper<Course> wrapper = new QueryWrapper<Course>();
        wrapper.orderByDesc("user_id");
        wrapper.in("cid",550364727269109760L,550364727684345856L,550364727734677504L,550364727780814848L,550364727831146496L);
        wrapper.between("user_id",3L,8L);
//        wrapper.and(courseQueryWrapper -> courseQueryWrapper.between("user_id","3","8"));
        List<Course> course = courseMapper.selectList(wrapper);
        System.out.println(course);
    }

    //强制路由策略。脱离SQL自己指定分片策略。
    @Test
    public void queryCourseByHint(){
        //强制只查course_1表
        HintManager hintManager = HintManager.getInstance();
        //注意这两个属性，dataSourceBaseShardingValue用于强制分库
        // 强制查m1数据源
        hintManager.addDatabaseShardingValue("course","1");
        // 强制查course_1表
        hintManager.addTableShardingValue("course","1");

        List<Course> courses = courseMapper.selectList(null);
        courses.forEach(course -> System.out.println(course));
        //线程安全，所有用完要注意关闭。
        hintManager.close();
    }

    @Test
    public void addUser() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("user_"+i);
            user.setUstatus(String.valueOf(i%2+1));
            userMapper.insert(user);
        }
    }

    @Test
    public void queryUser() {
        List<User> users = userMapper.selectList(null);
        for(User user : users){
            System.out.println(user);
        }
    }


    /**
     * 绑定表查询
     * 这个关联查询在application03.properties配置中会报错。
     * 因为他会将user表也一起作为广播表处理。
     * 而在application04.properties中配置了绑定表后，t_user_1表将只和t_dict_1表联合，而不会和t_dict_2表联合。
     */
    @Test
    public void queryUserStatus(){
        List<User> users = userMapper.queryUserStatus();
        for(User user : users){
            System.out.println(user);
        }
    }

    //t_dict公共表测试
    @Test
    public void addDict() {
        Dict dict = new Dict();
        dict.setUstatus("1");
        dict.setUvalue("正常");
        dictMapper.insert(dict);

        Dict dict2 = new Dict();
        dict2.setUstatus("2");
        dict2.setUvalue("异常");
        dictMapper.insert(dict2);
    }

    @Test
    public void queryDict() {
        QueryWrapper<Dict> wrapper = new QueryWrapper<Dict>();
        wrapper.eq("ustatus", "1");
        List<Dict> dicts = dictMapper.selectList(wrapper);
        dicts.forEach(dict -> System.out.println(dict));
    }
    //公共表修改测试
    @Test
    public void updateDict() {
        Dict dict = new Dict();
        dict.setUstatus("1");
        dict.setUvalue("Normal");

        UpdateWrapper<Dict> wrapper = new UpdateWrapper<Dict>();
        wrapper.eq("ustatus", dict.getUstatus());
        dictMapper.update(dict, wrapper);
    }
}
