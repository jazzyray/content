package com.ontotext.content.resource;

import com.ontotext.commons.web.docio.CesDocumentMediaType;
import com.ontotext.content.representation.AnnotatedContentResult;
import com.ontotext.content.service.AnnotatedContentPublisherService;
import com.ontotext.docio.DocumentIOJson;
import com.ontotext.docio.model.Document;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnnotatedContentPublisherResourceTest {

    private static final AnnotatedContentPublisherService ANNOTATED_CONTENT_PUBLISHER_SERVICE = mock(AnnotatedContentPublisherService.class);

    @Rule
    public final ResourceTestRule RULE = ResourceTestRule.builder()
            .addResource(new AnnotatedContentPublisherResource(ANNOTATED_CONTENT_PUBLISHER_SERVICE))
            .build();


    @Before
    public void setUp() {
    }

    @After
    public void tearDown() throws Exception {
    }

    // GET /annotatedcontent/{contentId}
    @Test
    public void getMockAnnotedContentSucess() throws Exception {
        URI uri = UriBuilder.fromPath("/annotatedcontent")
                .path(AnnotatedContentPublisherService.MOCK_CONTENT_ID).build();

        AnnotatedContentResult annotationResult = new AnnotatedContentResult(uri, AnnotatedContentPublisherService.ASYNCH_PROCESSING_STATE_COMPLETE);
        AnnotatedContentPublisherService annotatedContentPublisherService = new AnnotatedContentPublisherService();
        when(ANNOTATED_CONTENT_PUBLISHER_SERVICE.getContentDocument()).thenReturn(annotatedContentPublisherService.getContentDocument());

        final Response response = RULE.client().target("/annotatedcontent")
                .path(AnnotatedContentPublisherService.MOCK_CONTENT_ID)
                .request(CesDocumentMediaType.GENERIC_DOCUMENT_JSON_VALUE)
                .get();

        String annotatedContentResultFromCall = response.readEntity(String.class);

        DocumentIOJson jsonDoc = new DocumentIOJson();
        Document contentDocument = jsonDoc.read(new ByteArrayInputStream(annotatedContentResultFromCall.getBytes(StandardCharsets.UTF_8)));
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        assertThat(contentDocument).isEqualTo(annotatedContentPublisherService.getContentDocument());
        System.out.println("Status: " + response.getStatusInfo());
        System.out.println("Response: " + annotatedContentResultFromCall);

    }

    // POST /annotatedcontent/{annotatedContentId}?asych={boolean}
    @Test
    public void createMockAnnotedContentSucess() throws Exception {
        URI uri = UriBuilder.fromPath("/annotatedcontent")
                .path(AnnotatedContentPublisherService.MOCK_CONTENT_ID).build();

        AnnotatedContentResult annotationResult = new AnnotatedContentResult(uri, AnnotatedContentPublisherService.ASYNCH_PROCESSING_STATE_COMPLETE);
        AnnotatedContentPublisherService annotatedContentPublisherService = new AnnotatedContentPublisherService();
        when(ANNOTATED_CONTENT_PUBLISHER_SERVICE.getContentDocument()).thenReturn(annotatedContentPublisherService.getContentDocument());
        when(ANNOTATED_CONTENT_PUBLISHER_SERVICE.createAnnotatedContent(any(URI.class),eq(annotatedContentPublisherService.getContentDocument()))).thenReturn(annotationResult);

        final Response response = RULE.client().target("/annotatedcontent")
                .path(AnnotatedContentPublisherService.MOCK_CONTENT_ID)
                .queryParam("asynch", "false")
                .request(CesDocumentMediaType.GENERIC_DOCUMENT_JSON_VALUE)
                .post(Entity.entity(annotatedContentPublisherService.getContentDocument(), CesDocumentMediaType.GENERIC_DOCUMENT_JSON_VALUE));

        String annotatedContentResultFromCall = response.readEntity(String.class);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        System.out.println("Status: " + response.getStatusInfo());
        System.out.println("Response: " + annotatedContentResultFromCall);

    }

    // PUT /annotatedcontent/{annotatedContentId}?asych={boolean}
    @Test
    public void updateMockAnnotedContentSucess() throws Exception {
        URI uri = UriBuilder.fromPath("/annotatedcontent")
                .path(AnnotatedContentPublisherService.MOCK_CONTENT_ID).build();

        AnnotatedContentResult annotationResult = new AnnotatedContentResult(uri, AnnotatedContentPublisherService.ASYNCH_PROCESSING_STATE_COMPLETE);
        AnnotatedContentPublisherService annotatedContentPublisherService = new AnnotatedContentPublisherService();
        when(ANNOTATED_CONTENT_PUBLISHER_SERVICE.getContentDocument()).thenReturn(annotatedContentPublisherService.getContentDocument());
        when(ANNOTATED_CONTENT_PUBLISHER_SERVICE.updateAnnotatedContent(any(URI.class),eq(annotatedContentPublisherService.getContentDocument()))).thenReturn(annotationResult);

        final Response response = RULE.client().target("/annotatedcontent")
                .path(AnnotatedContentPublisherService.MOCK_CONTENT_ID)
                .queryParam("asynch", "false")
                .request(CesDocumentMediaType.GENERIC_DOCUMENT_JSON_VALUE)
                .put(Entity.entity(annotatedContentPublisherService.getContentDocument(), CesDocumentMediaType.GENERIC_DOCUMENT_JSON_VALUE));

        String annotatedContentResultFromCall = response.readEntity(String.class);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        System.out.println("Status: " + response.getStatusInfo());
        System.out.println("Response: " + annotatedContentResultFromCall);

    }

    // DELETE /annotatedcontent/{annotatedContentId}?asych={boolean}
    @Test
    public void deleteMockAnnotedContentSucess() throws Exception {
        URI uri = UriBuilder.fromPath("/annotatedcontent")
                .path(AnnotatedContentPublisherService.MOCK_CONTENT_ID).build();

        AnnotatedContentResult annotationResult = new AnnotatedContentResult(uri, AnnotatedContentPublisherService.ASYNCH_PROCESSING_STATE_COMPLETE);
        AnnotatedContentPublisherService annotatedContentPublisherService = new AnnotatedContentPublisherService();
        when(ANNOTATED_CONTENT_PUBLISHER_SERVICE.getContentDocument()).thenReturn(annotatedContentPublisherService.getContentDocument());
        when(ANNOTATED_CONTENT_PUBLISHER_SERVICE.deleteAnnotatedContent(any(URI.class))).thenReturn(annotationResult);

        final Response response = RULE.client().target("/annotatedcontent")
                .path(AnnotatedContentPublisherService.MOCK_CONTENT_ID)
                .queryParam("asynch", "false")
                .request(CesDocumentMediaType.GENERIC_DOCUMENT_JSON_VALUE)
                .delete();

        String annotatedContentResultFromCall = response.readEntity(String.class);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        System.out.println("Status: " + response.getStatusInfo());
        System.out.println("Response: " + annotatedContentResultFromCall);

    }

}
