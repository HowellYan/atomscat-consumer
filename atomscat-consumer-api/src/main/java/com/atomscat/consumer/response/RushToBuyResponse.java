package com.atomscat.consumer.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class RushToBuyResponse implements Serializable {
    String code;
    String msg;
}
