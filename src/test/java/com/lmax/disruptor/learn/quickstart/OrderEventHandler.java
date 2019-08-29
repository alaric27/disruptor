package com.lmax.disruptor.learn.quickstart;


import com.lmax.disruptor.EventHandler;

import java.util.Random;

/**
 * @author zhaiyanan
 * @date 2018/11/24 17:14
 */
public class OrderEventHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {

        Random random = new Random();
        Thread.sleep(random.nextInt(1000));
        System.out.println(sequence + "::::::::" + event.getValue());

    }
}
