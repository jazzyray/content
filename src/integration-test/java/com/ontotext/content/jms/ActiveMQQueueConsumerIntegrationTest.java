package com.ontotext.content.jms;

import com.mockrunner.jms.DestinationManager;
import com.ontotext.content.service.AnnotatedContentPublisherService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import static com.ontotext.content.util.ResourceUtil.getResourceFileAsString;

public class ActiveMQQueueConsumerIntegrationTest {

    ActiveMQQueueConsumer activeMQQueueConsumer;
    QueueMessageConsumer queueMessageConsumer;
    String destination = "test";
    JmsTemplate jmsTemplate;
    String brokerURL = "tcp://localhost:61616";

    @Before
    public void setUp() {
        DestinationManager destinationManager = new DestinationManager();
        this.jmsTemplate = new JmsTemplate();
        this.queueMessageConsumer = new QueueMessageConsumer(new AnnotatedContentConsumer(this.jmsTemplate, new AnnotatedContentPublisherService()), destination, 30);
        this.activeMQQueueConsumer = new ActiveMQQueueConsumer(this.jmsTemplate, brokerURL, destination, this.queueMessageConsumer);
        this.activeMQQueueConsumer.start();
    }

    @After
    public void tearDown() throws Exception  {
        Thread.sleep(10); //wait to ensure the queues are acked.
        this.activeMQQueueConsumer.stop();
    }

    @Test
    public void testMultipleSendAndReceives() {
        this.jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(getResourceFileAsString("http-message-payload/create-annotated-content.http"));
            }});

        this.jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(getResourceFileAsString("http-message-payload/create-annotated-content.http"));
            }});
    }

}
