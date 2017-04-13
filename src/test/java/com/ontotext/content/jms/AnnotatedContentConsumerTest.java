package com.ontotext.content.jms;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockQueueConnectionFactory;
import com.ontotext.content.service.AnnotatedContentPublisherService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import static com.ontotext.content.util.ResourceUtil.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;

public class AnnotatedContentConsumerTest {

    AnnotatedContentConsumer annotatedContentConsumer;
    JmsTemplate jmsTemplate;
    Destination destination;

    @Before
    public void setUp() {
        DestinationManager destinationManager = new DestinationManager();
        this.jmsTemplate = new JmsTemplate();
        MockQueueConnectionFactory mockRunnerConnectionFactory = new MockQueueConnectionFactory(destinationManager, new ConfigurationManager());
        this.destination = destinationManager.createQueue("mock-queue");
        this.jmsTemplate.setConnectionFactory(mockRunnerConnectionFactory);

        this.jmsTemplate.setDefaultDestination(this.destination);
        this.annotatedContentConsumer = new AnnotatedContentConsumer(jmsTemplate, new AnnotatedContentPublisherService());
    }

    @Test
    public void receiveAnnotatedContent() {
        final String payload =  getResourceFileAsString("http-message-payload/create-annotated-content.http");
        this.jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(payload);
            }});

        String processedPayload = annotatedContentConsumer.processMessage();
        assertThat(processedPayload).isEqualTo(payload);
    }

}
