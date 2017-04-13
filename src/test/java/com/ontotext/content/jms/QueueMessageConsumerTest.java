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

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import static com.ontotext.content.util.ResourceUtil.getResourceFileAsString;

public class QueueMessageConsumerTest {

    QueueMessageConsumer queueMessageConsumer;
    AnnotatedContentConsumer annotatedContentConsumer;
    JmsTemplate jmsTemplate;
    Destination destination;

    @Before
    public void setUp() throws Exception {
        final String DESTINATION_QUEUE_NAME = "mock-queue";
        DestinationManager destinationManager = new DestinationManager();
        this.jmsTemplate = new JmsTemplate();
        MockQueueConnectionFactory mockRunnerConnectionFactory = new MockQueueConnectionFactory(destinationManager, new ConfigurationManager());
        this.destination = destinationManager.createQueue(DESTINATION_QUEUE_NAME);
        this.jmsTemplate.setConnectionFactory(mockRunnerConnectionFactory);

        this.jmsTemplate.setDefaultDestination(this.destination);
        this.annotatedContentConsumer = new AnnotatedContentConsumer(this.jmsTemplate, new AnnotatedContentPublisherService());
        this.queueMessageConsumer = new QueueMessageConsumer(this.annotatedContentConsumer, DESTINATION_QUEUE_NAME, 30);
        this.queueMessageConsumer.start();
    }

    @After
    public void tearDown() throws Exception{
        this.queueMessageConsumer.stop();
    }

    @Test
    public void testMultipleSends() {
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
