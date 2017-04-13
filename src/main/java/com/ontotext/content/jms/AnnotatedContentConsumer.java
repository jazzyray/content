package com.ontotext.content.jms;

import com.ontotext.commons.web.docio.CesDocumentMediaType;
import com.ontotext.content.exception.MalformedRequestLineException;
import com.ontotext.content.exception.PayloadHttpParseException;
import com.ontotext.content.exception.SetJMSListenerException;
import com.ontotext.content.exception.UnsupportedMimeTypeException;
import com.ontotext.content.http.HTTPRequestParser;
import com.ontotext.content.service.AnnotatedContentPublisherService;
import com.ontotext.docio.DocumentIOException;
import com.ontotext.docio.DocumentIOJson;
import com.ontotext.docio.DocumentIOXml;
import com.ontotext.docio.model.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import javax.ws.rs.core.HttpHeaders;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.net.URI;

public class AnnotatedContentConsumer {

    JmsTemplate jmsTemplate;
    AnnotatedContentPublisherService annotatedContentPublisherService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public AnnotatedContentConsumer(JmsTemplate jmsTemplate, AnnotatedContentPublisherService annotatedContentPublisherService) {
        this.jmsTemplate = jmsTemplate;
        this.annotatedContentPublisherService = annotatedContentPublisherService;
    }

    public Document getDocument(String payload, String mimeType) throws DocumentIOException {
        if (mimeType.equals(CesDocumentMediaType.GENERIC_DOCUMENT_JSON_VALUE)) {
            DocumentIOJson jsonDoc = new DocumentIOJson();
            return jsonDoc.read(new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8)));
        } else {
            DocumentIOXml xmlDoc = new DocumentIOXml();
            return xmlDoc.read(new ByteArrayInputStream(payload.getBytes(StandardCharsets.UTF_8)));
        }
    }

    private String getMimeType(HTTPRequestParser httpRequestParser) {
        String mimeType = httpRequestParser.getHeader((HttpHeaders.CONTENT_TYPE));
        if ((!mimeType.equals(CesDocumentMediaType.GENERIC_DOCUMENT_JSON_VALUE)) && (!mimeType.equals(CesDocumentMediaType.GENERIC_DOCUMENT_XML_VALUE))) {
            throw new UnsupportedMimeTypeException();
        }
        return mimeType;
    }

    public String processMessage() {
        try {
            TextMessage textMessage = (TextMessage) jmsTemplate.receive();
            String stringPayload = textMessage.getText();

            HTTPRequestParser httpRequestParser = new HTTPRequestParser(new ByteArrayInputStream(stringPayload.getBytes(StandardCharsets.UTF_8)));
            int status = httpRequestParser.parseRequest();
            if (status == HttpURLConnection.HTTP_OK) {

                URI uri = new URI(httpRequestParser.getRequestURL());
                if (httpRequestParser.getMethod().equals("POST")) {
                    String mimeType = getMimeType(httpRequestParser);
                    Document document = getDocument(httpRequestParser.getBody(), mimeType);
                    this.annotatedContentPublisherService.createAnnotatedContent(uri, document);
                    log.info("Created document [" + stringPayload + "]");
                } else if (httpRequestParser.getMethod().equals("PUT")) {
                    String mimeType = getMimeType(httpRequestParser);
                    Document document = getDocument(httpRequestParser.getBody(), mimeType);
                    this.annotatedContentPublisherService.updateAnnotatedContent(uri, document);
                    log.info("Updated document [" + stringPayload + "]");

                } else {
                    this.annotatedContentPublisherService.deleteAnnotatedContent(uri);
                    log.info("Deleted document [" + stringPayload + "]");
                }
            } else {
               throw new PayloadHttpParseException();
            }

            return stringPayload;
        } catch (JMSException je) {
           throw new SetJMSListenerException();
        } catch (IOException io) {
            throw new PayloadHttpParseException();
        } catch (DocumentIOException dio) {
            throw new PayloadHttpParseException();
        } catch (URISyntaxException uri) {
            throw new MalformedRequestLineException();
        }
    }

}
