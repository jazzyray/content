package com.ontotext.content.http;

import com.ontotext.content.AnnotatedContentPublisherApplication;
import com.ontotext.content.service.AnnotatedContentPublisherService;
import com.ontotext.docio.DocumentIOJson;
import com.ontotext.docio.model.Document;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import static com.ontotext.content.util.ResourceUtil.getResourceFileAsStream;
import static org.assertj.core.api.Assertions.assertThat;

public class HTTPRequestParserTest {

    @Test
    public void parseCreateAnnotationRequest() throws Exception{
        InputStream is = getResourceFileAsStream("http-message-payload/create-annotated-content.http");
        HTTPRequestParser parser = new HTTPRequestParser(is);

        int status = parser.parseRequest();

        assertThat(status).isEqualTo(HttpURLConnection.HTTP_OK);
        System.out.println("status: " + status);
        System.out.println("body: " + parser.getBody());

        DocumentIOJson jsonPayloadDoc = new DocumentIOJson();
        Document PAYLOAD_ANNOTATED_CONTENT_DOCUMENT = jsonPayloadDoc.read(new ByteArrayInputStream(parser.getBody().getBytes(StandardCharsets.UTF_8)));
        DocumentIOJson jsonExpectedDoc = new DocumentIOJson();
        Document EXPECTED_ANNOTATED_CONTENT_DOCUMENT = jsonExpectedDoc.read(getResourceFileAsStream(AnnotatedContentPublisherService.CONTENT_JSON_FILENAME));

        assertThat(PAYLOAD_ANNOTATED_CONTENT_DOCUMENT).isEqualTo(EXPECTED_ANNOTATED_CONTENT_DOCUMENT);

        System.out.println("content-type: " + parser.getHeader(HttpHeaders.CONTENT_TYPE));
        System.out.println("transaction-id: " + parser.getHeader("X-Request-ID"));
    }

    @Test
    public void parseUpdateAnnotationRequest() throws Exception{
        InputStream is = getResourceFileAsStream("http-message-payload/update-annotated-content.http");
        HTTPRequestParser parser = new HTTPRequestParser(is);

        int status = parser.parseRequest();

        assertThat(status).isEqualTo(HttpURLConnection.HTTP_OK);
        System.out.println("status: " + status);
        System.out.println("body: " + parser.getBody());

        DocumentIOJson jsonPayloadDoc = new DocumentIOJson();
        Document PAYLOAD_ANNOTATED_CONTENT_DOCUMENT = jsonPayloadDoc.read(new ByteArrayInputStream(parser.getBody().getBytes(StandardCharsets.UTF_8)));
        DocumentIOJson jsonExpectedDoc = new DocumentIOJson();
        Document EXPECTED_ANNOTATED_CONTENT_DOCUMENT = jsonExpectedDoc.read(getResourceFileAsStream(AnnotatedContentPublisherService.CONTENT_JSON_FILENAME));

        assertThat(PAYLOAD_ANNOTATED_CONTENT_DOCUMENT).isEqualTo(EXPECTED_ANNOTATED_CONTENT_DOCUMENT);

        System.out.println("content-type: " + parser.getHeader(HttpHeaders.CONTENT_TYPE));
    }

    @Test
    public void parseDeleteAnnotationRequest() throws Exception{
        InputStream is = getResourceFileAsStream("http-message-payload/delete-annotated-content.http");
        HTTPRequestParser parser = new HTTPRequestParser(is);

        int status = parser.parseRequest();

        assertThat(status).isEqualTo(HttpURLConnection.HTTP_OK);
        System.out.println("status: " + status);

        System.out.println("content-type: " + parser.getHeader(HttpHeaders.CONTENT_TYPE));
        System.out.println("transaction-id: " + parser.getHeader("X-Request-ID"));

    }

}
