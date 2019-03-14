package com.lmax.disruptor.learn.quickstart;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * @author zhaiyanan
 * @date 2018/11/24 17:18
 */
public class OrderEventTest {


    public static void main(String[] args) {

        //1. 实例化disruptor对象
        OrderEventFactory orderEventFactory = new OrderEventFactory();
        int ringBufferSize = 1024*1024;
        Disruptor<OrderEvent> disruptor = new Disruptor<>(orderEventFactory,
                                        ringBufferSize, DaemonThreadFactory.INSTANCE,
                                        ProducerType.SINGLE, new BlockingWaitStrategy());

        //2. 添加消费者的监听
        disruptor.handleEventsWith(new OrderEventHandler());

        //3. 启动
        disruptor.start();

        //4. 获取实际存储数据的容器
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();

        //

        OrderEventProducer orderEventProducer = new OrderEventProducer(ringBuffer);
        for (long i = 0; i < 10000; i++) {
            orderEventProducer.sendData(i);
        }

        disruptor.shutdown();


    }
}
