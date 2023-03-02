package com.mzaxd.noodles.config;

import com.mzaxd.noodles.constant.RabbitMqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mzaxd
 * @since 2023-02-06 12:51
 */
@Configuration
public class RabbitMqConfig implements InitializingBean {

    /**
     * 自动注入RabbitTemplate模板
     */
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息JSON序列化
     */
    @Override
    public void afterPropertiesSet() {
        //使用JSON序列化
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    @Bean
    public Queue rabbitmqDynamicDataDirectQueue() {
        /**
         * 1、name:    队列名称
         * 2、durable: 是否持久化
         * 3、exclusive: 是否独享、排外的。如果设置为true，定义为排他队列。则只有创建者可以使用此队列。也就是private私有的。
         * 4、autoDelete: 是否自动删除。也就是临时队列。当最后一个消费者断开连接后，会自动删除。
         * */
        Map<String,Object> arguments = new HashMap<>();
        // 这里的key参数需要从web图形化界面中获得，后面一定要是整形，单位是毫秒
        arguments.put("x-message-ttl",5000);
        return new Queue(RabbitMqConstant.DYNAMIC_DATA_TOPIC, true, false, false, arguments);
    }

    @Bean
    public DirectExchange rabbitmqDynamicDataDirectExchange() {
        //Direct交换机
        return new DirectExchange(RabbitMqConstant.DYNAMIC_DATA_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindDirect() {
        //链式写法，绑定交换机和队列，并设置匹配键
        return BindingBuilder
                //绑定队列
                .bind(rabbitmqDynamicDataDirectQueue())
                //到交换机
                .to(rabbitmqDynamicDataDirectExchange())
                //并设置匹配键
                .with(RabbitMqConstant.DYNAMIC_DATA_ROUTING);
    }
}
