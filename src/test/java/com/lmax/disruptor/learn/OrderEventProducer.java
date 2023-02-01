package com.lmax.disruptor.learn;

import com.lmax.disruptor.RingBuffer;

/**
 * @author zhaiyanan
 * @date 2018/11/24 17:39
 */
public class OrderEventProducer {

    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer){

        this.ringBuffer = ringBuffer;
    }


    public void sendData(Long orderId){
        //1.获取可用的序列号
        long sequence = ringBuffer.next();
        try {

            //2.根据序号获取对应的OrderEvent
            OrderEvent orderEvent = ringBuffer.get(sequence);

            //3.填充数据
            orderEvent.setValue(orderId);
        } finally {
            //4.发布数据
            ringBuffer.publish(sequence);
        }



    }
}
