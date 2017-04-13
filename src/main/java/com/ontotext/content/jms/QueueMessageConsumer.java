package com.ontotext.content.jms;

import com.ontotext.content.exception.AnnotatedContentPublisherException;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class QueueMessageConsumer implements Managed, Runnable {

    private final Logger log = LoggerFactory.getLogger(getClass());


    private AnnotatedContentConsumer annotatedContentConsumer;
    private final Thread thread;
    protected final long shutdownWaitInSeconds;
    private AtomicBoolean shouldStop = new AtomicBoolean(false);
    private AtomicBoolean isReceiving = new AtomicBoolean(false);

    public QueueMessageConsumer(AnnotatedContentConsumer annotatedContentConsumer, String destination, long shutdownWaitInSeconds) {
        this.thread = new Thread(this, "Receiver [" + destination + "]");
        this.shutdownWaitInSeconds = shutdownWaitInSeconds;
        this.annotatedContentConsumer = annotatedContentConsumer;
    }

    public void start() throws Exception {
        thread.start();
    }

    public void stop() throws Exception {
        if (thread.isAlive()) {
            shouldStop.set(true);
            final long start = System.currentTimeMillis();
            while (thread.isAlive()) {
                if (((System.currentTimeMillis() - start) / 1000) >= shutdownWaitInSeconds) {
                    break;
                }
                Thread.sleep(200);
            }
        }
    }

    public void run() {
        while(!shouldStop.get()) {
            try {
                isReceiving.set(true);
                runReceiveLoop(annotatedContentConsumer);
            } finally {
                isReceiving.set(false);
            }
        }
    }

    private void runReceiveLoop(AnnotatedContentConsumer messageConsumer) {
        while(!shouldStop.get()) {
            try {
                messageConsumer.processMessage();
            } catch (AnnotatedContentPublisherException ae) {
                log.info(ae.toString());
            }
        }
    }
}
