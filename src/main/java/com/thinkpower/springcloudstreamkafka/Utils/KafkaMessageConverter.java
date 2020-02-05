package com.thinkpower.springcloudstreamkafka.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinkpower.springcloudstreamkafka.DTO.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.MimeType;

import javax.annotation.Resource;

public class KafkaMessageConverter implements MessageConverter {

    @Resource
    ObjectMapper objectMapper;

//    @Override
//    protected boolean supports(Class<?> clazz) {
//        System.err.println(MessageDTO.class.equals(clazz));
//        return MessageDTO.class.equals(clazz);
//    }
//
//    @Override
//    protected Object convertFromInternal(Message<?> message, Class<?> targetClass, Object conversionHint) {
//        Object payload = message.getPayload();
//        System.err.println(this.supports(payload.getClass()));
//
//        return payload instanceof MessageDTO ? payload : null;
//    }

    @Override
    public Object fromMessage(Message<?> message, Class<?> targetClass) {
        System.err.println(message.getPayload().toString());
        System.err.println(targetClass);
        try {
            return objectMapper.readValue(message.getPayload().toString(), MessageDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Message<?> toMessage(Object payload, MessageHeaders headers) {
//        System.err.println("toMessage");
        return MessageBuilder.withPayload(payload).copyHeaders(headers).build();
    }

}
