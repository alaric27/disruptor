package com.lmax.disruptor.dsl;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * 默认的线程执行器
 *
 */
public class BasicExecutor implements Executor
{
    private final ThreadFactory factory;

    /**
     * 保存当前执行器启动过的线程，用于打印日志
     */
    private final Queue<Thread> threads = new ConcurrentLinkedQueue<>();

    public BasicExecutor(ThreadFactory factory)
    {
        this.factory = factory;
    }


    /**
     * 执行Runnable命令时，根据ThreadFactory创建一个线程并启动，随后添加到threads队列中
     * @param command
     */
    @Override
    public void execute(Runnable command)
    {
        final Thread thread = factory.newThread(command);
        if (null == thread)
        {
            throw new RuntimeException("Failed to create thread to run: " + command);
        }

        thread.start();

        threads.add(thread);
    }

    @Override
    public String toString()
    {
        return "BasicExecutor{" +
            "threads=" + dumpThreadInfo() +
            '}';
    }

    private String dumpThreadInfo()
    {
        final StringBuilder sb = new StringBuilder();

        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        for (Thread t : threads)
        {
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(t.getId());
            sb.append("{");
            sb.append("name=").append(t.getName()).append(",");
            sb.append("id=").append(t.getId()).append(",");
            sb.append("state=").append(threadInfo.getThreadState()).append(",");
            sb.append("lockInfo=").append(threadInfo.getLockInfo());
            sb.append("}");
        }

        return sb.toString();
    }
}
