package com.ontotext.content.service;

import com.ontotext.content.representation.AnnotatedContentResult;
import com.ontotext.content.resource.AnnotatedContentPublisherResource;
import com.ontotext.content.util.ResourceUtil;
import com.ontotext.docio.DocumentIOException;
import com.ontotext.docio.DocumentIOJson;
import com.ontotext.docio.model.Document;

import javax.ws.rs.core.UriBuilder;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class AnnotatedContentPublisherService {

    public static final String MOCK_CONTENT_ID = "tsk550fnfym8";

    public static final String CONTENT_JSON_FILENAME = "json/" + MOCK_CONTENT_ID + ".json";
    public static final String CONTENT_JSON = ResourceUtil.getResourceFileAsString(CONTENT_JSON_FILENAME);

    public static final String ASYNCH_PROCESSING_STATE_PROCESSING = "PROCESSING";
    public static final String ASYNCH_PROCESSING_STATE_COMPLETE = "COMPLETE";

    public Document contentDocument;


   public AnnotatedContentPublisherService() {
       DocumentIOJson jsonDoc = new DocumentIOJson();
       try {
           contentDocument = jsonDoc.read(new ByteArrayInputStream(CONTENT_JSON.getBytes(StandardCharsets.UTF_8)));
       } catch (DocumentIOException dioe) {
           throw new RuntimeException(dioe);
       }
   }


   public Document getContentDocument() {
       return this.contentDocument;
   }

    public AnnotatedContentResult asynchAnnotatedContentStatus(URI uri) {
        return new AnnotatedContentResult();
    }


    public AnnotatedContentResult createAnnotatedContent(URI uri, Document annotatedContent) {
       return new AnnotatedContentResult(uri, ASYNCH_PROCESSING_STATE_COMPLETE);
    }

    public AnnotatedContentResult asynchCreateAnnotatedContent(URI uri, Document annotatedContent) {
        UriBuilder uriBuilder = UriBuilder
                .fromPath(uri.toString())
                .path(UUID.randomUUID().toString());

        return new AnnotatedContentResult(uriBuilder.build(), ASYNCH_PROCESSING_STATE_PROCESSING);
    }

    public AnnotatedContentResult updateAnnotatedContent(URI uri, Document annotatedContent) {
        return new AnnotatedContentResult(uri, ASYNCH_PROCESSING_STATE_COMPLETE);
    }

    public AnnotatedContentResult asynchUpdateAnnotatedContent(URI uri, Document annotatedContent) {
        UriBuilder uriBuilder = UriBuilder
                .fromPath(uri.toString())
                .path(UUID.randomUUID().toString());

        return new AnnotatedContentResult(uriBuilder.build(), ASYNCH_PROCESSING_STATE_PROCESSING);
    }

    public AnnotatedContentResult deleteAnnotatedContent(URI uri) {
        return new AnnotatedContentResult(uri, ASYNCH_PROCESSING_STATE_COMPLETE);
    }

    public AnnotatedContentResult asynchDeleteAnnotatedContent(URI uri) {
        UriBuilder uriBuilder = UriBuilder
                .fromPath(uri.toString())
                .path(UUID.randomUUID().toString());

        return new AnnotatedContentResult(uriBuilder.build(), ASYNCH_PROCESSING_STATE_PROCESSING);
    }

}
