package com.lmax.disruptor.learn;

import com.lmax.disruptor.util.Util;

/**
 * @author zhaiyanan
 * @date 2019/3/13 22:50
 */
public class Demo {

    public static void main(String[] args) {

        int bufferSize = 64;
        int sequence = 64*5 + 63;
        int indexShift = Util.log2(bufferSize);
        System.out.println(indexShift);
        System.out.println(sequence >>> indexShift);
    }
}
