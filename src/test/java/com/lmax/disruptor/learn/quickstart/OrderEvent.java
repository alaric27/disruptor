package com.lmax.disruptor.learn.quickstart;


/**
 * @author zhaiyanan
 * @date 2018/11/24 17:11
 */
public class OrderEvent {
    private Long value;

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
