package com.pangaea.subsribingserver.models;
import lombok.Data;

@Data
public class Request {
    private String topic;
    private Object data;
}
