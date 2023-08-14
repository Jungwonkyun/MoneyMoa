package com.d210.moneymoa.config;

import com.d210.moneymoa.domain.redis.RedisSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@EnableRedisRepositories
public class RedisConfig {
   @Value("${spring.redis.host}")
   private String redisHost;

   @Value("${spring.redis.port}")
   private int redisPort;

   @Bean
   public RedisConnectionFactory redisConnectionFactory() {
       return new LettuceConnectionFactory(redisHost, redisPort);
   }



   /**
    * redis pub/sub 메시지를 처리하는 listener 설정
    */
   @Bean
   public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory) {
       RedisMessageListenerContainer container = new RedisMessageListenerContainer();
       container.setConnectionFactory(connectionFactory);
       return container;
   }


   /**
    * 어플리케이션에서 사용할 redisTemplate 설정
    */
   @Bean
   public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
       RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
       redisTemplate.setConnectionFactory(connectionFactory);
       redisTemplate.setKeySerializer(new StringRedisSerializer());
       redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
       return redisTemplate;
   }


}

// @Configuration
// @EnableRedisRepositories
// public class RedisConfig {
//     @Value("${spring.redis.host}")
//     private String redisHost;

//     @Value("${spring.redis.port}")
//     private int redisPort;

//     @Bean
//     public RedisConnectionFactory redisConnectionFactory() {
//         return new LettuceConnectionFactory(redisHost, redisPort);
//     }

//     @Bean
//     public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory,
//                                                               MessageListenerAdapter messageListenerAdapter,
//                                                               MessageListenerAdapter directMessageListenerAdapter) {
//         RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//         container.setConnectionFactory(connectionFactory);
//         container.addMessageListener(messageListenerAdapter, new PatternTopic("chat.general")); // 일반 채널
//         container.addMessageListener(directMessageListenerAdapter, new PatternTopic("chat.dm")); // DM 채널
//         return container;
//     }

//     @Bean
//     public MessageListenerAdapter messageListenerAdapter(RedisSubscriber redisSubscriber) {
//         return new MessageListenerAdapter(redisSubscriber, "onMessage");
//     }

//     @Bean
//     public MessageListenerAdapter directMessageListenerAdapter(RedisSubscriber redisSubscriber) {
//         return new MessageListenerAdapter(redisSubscriber, "onDirectMessage");
//     }

//     @Bean
//     public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//         RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//         redisTemplate.setConnectionFactory(connectionFactory);
//         redisTemplate.setKeySerializer(new StringRedisSerializer());
//         redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//         return redisTemplate;
//     }
// }
