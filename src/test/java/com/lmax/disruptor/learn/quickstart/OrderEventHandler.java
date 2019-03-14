package com.lmax.disruptor.learn.quickstart;


import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaiyanan
 * @date 2018/11/24 17:14
 */
@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {

        Thread.sleep(1000);
        log.info("消费：{}" , event.getValue());

    }
}
