/*
 * Copyright 2011 LMAX Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lmax.disruptor;


/**
 *
 * 生产者和消费者的协调者
 * {@link SequenceBarrier} handed out for gating {@link EventProcessor}s on a cursor sequence and optional dependent {@link EventProcessor}(s),
 * using the given WaitStrategy.
 */
final class ProcessingSequenceBarrier implements SequenceBarrier
{
    /**
     * 等待策略
     */
    private final WaitStrategy waitStrategy;


    private final Sequence dependentSequence;
    private volatile boolean alerted = false;

    /**
     * 生产者已经生产的序列号
     */
    private final Sequence cursorSequence;

    /**
     * RingBuffer中的Sequencer
     */
    private final Sequencer sequencer;

    /**
     *
     * @param sequencer  生产者序号生成者
     * @param waitStrategy 等待策略
     * @param cursorSequence 生产者生产进度标识
     * @param dependentSequences 当前序号栅栏的依赖序号
     */
    ProcessingSequenceBarrier(
        final Sequencer sequencer,
        final WaitStrategy waitStrategy,
        final Sequence cursorSequence,
        final Sequence[] dependentSequences)
    {
        this.sequencer = sequencer;
        this.waitStrategy = waitStrategy;
        this.cursorSequence = cursorSequence;

        // 如果依赖序号长度为零，则表示当前栅栏只需要保证不超过生产者序号即可
        if (0 == dependentSequences.length)
        {
            dependentSequence = cursorSequence;
        }
        // 如果依赖序号不为空，则把依赖序号聚合为单一视图
        else
        {
            dependentSequence = new FixedSequenceGroup(dependentSequences);
        }
    }

    @Override
    public long waitFor(final long sequence)
        throws AlertException, InterruptedException, TimeoutException
    {
        checkAlert();

        long availableSequence = waitStrategy.waitFor(sequence, cursorSequence, dependentSequence, this);

        if (availableSequence < sequence)
        {
            return availableSequence;
        }

        return sequencer.getHighestPublishedSequence(sequence, availableSequence);
    }

    @Override
    public long getCursor()
    {
        return dependentSequence.get();
    }

    @Override
    public boolean isAlerted()
    {
        return alerted;
    }

    @Override
    public void alert()
    {
        alerted = true;
        waitStrategy.signalAllWhenBlocking();
    }

    @Override
    public void clearAlert()
    {
        alerted = false;
    }

    @Override
    public void checkAlert() throws AlertException
    {
        if (alerted)
        {
            throw AlertException.INSTANCE;
        }
    }
}