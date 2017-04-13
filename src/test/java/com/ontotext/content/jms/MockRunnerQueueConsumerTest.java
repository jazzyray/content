package com.ontotext.content.jms;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockQueueConnectionFactory;
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

public class MockRunnerQueueConsumerTest {

    MockRunnerQueueConsumer mockRunnerQueueConsumer;
    QueueMessageConsumer queueMessageConsumer;
    String destination = "test";
    JmsTemplate jmsTemplate;

    @Before
    public void setUp() {
        DestinationManager destinationManager = new DestinationManager();
        this.jmsTemplate = new JmsTemplate();
        this.queueMessageConsumer = new QueueMessageConsumer(new AnnotatedContentConsumer(this.jmsTemplate, new AnnotatedContentPublisherService()), destination, 30);
        this.mockRunnerQueueConsumer = new MockRunnerQueueConsumer(this.jmsTemplate, destination, this.queueMessageConsumer);
        this.mockRunnerQueueConsumer.start();
    }

    @After
    public void tearDown() throws Exception  {
        this.mockRunnerQueueConsumer.stop();
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
