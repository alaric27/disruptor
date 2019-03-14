package com.lmax.disruptor.learn.quickstart;

import com.lmax.disruptor.EventFactory;

/**
 * @author zhaiyanan
 * @date 2018/11/24 17:12
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        return new OrderEvent();
    }
}
