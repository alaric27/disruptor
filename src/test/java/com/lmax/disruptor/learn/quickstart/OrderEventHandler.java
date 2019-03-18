package com.lmax.disruptor.learn.quickstart;


import com.lmax.disruptor.EventHandler;

/**
 * @author zhaiyanan
 * @date 2018/11/24 17:14
 */
public class OrderEventHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {

        Thread.sleep(1000);

    }
}
