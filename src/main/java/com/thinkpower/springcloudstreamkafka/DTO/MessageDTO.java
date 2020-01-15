package com.thinkpower.springcloudstreamkafka.DTO;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MessageDTO {

    private Long id;
    private String content;
    private boolean hasConsume = false;

    public MessageDTO(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHasConsume() {
        return hasConsume;
    }

    public void setHasConsume(boolean hasConsume) {
        this.hasConsume = hasConsume;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
