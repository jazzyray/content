package com.ontotext.content.resource;


import com.codahale.metrics.annotation.Timed;
import com.ontotext.commons.web.docio.CesDocumentMediaType;
import com.ontotext.content.representation.AnnotatedContentResult;
import com.ontotext.content.service.AnnotatedContentPublisherService;
import com.ontotext.docio.model.Document;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.HttpURLConnection;
import java.net.URI;

import static com.ontotext.content.service.AnnotatedContentPublisherService.MOCK_CONTENT_ID;

@Api("Annotated Content Publisher API")
@Path("/annotatedcontent")
public class AnnotatedContentPublisherResource {

    AnnotatedContentPublisherService annotatedContentPublisherService;

    public AnnotatedContentPublisherResource(AnnotatedContentPublisherService annotatedContentPublisherService) {
        this.annotatedContentPublisherService = annotatedContentPublisherService;
    }

    // GET /annotatedcontent/{contentId}
    @GET
    @Timed
    @Produces({CesDocumentMediaType.GENERIC_DOCUMENT_JSON_VALUE, CesDocumentMediaType.GENERIC_DOCUMENT_XML_VALUE})
    @Path("/{annotatedContentId}")
    @ApiOperation(value = "Get Annotated Content by Id", response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Annotated Content Id supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Annotated Content not found"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Success",
                    responseHeaders = {@ResponseHeader(name = "X-Cache", description = "Explains whether or not a cache was used", response = Boolean.class),
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotated Content ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)}),
            @ApiResponse(code = HttpURLConnection.HTTP_ACCEPTED, message = "Success",
                    responseHeaders = {@ResponseHeader(name = "X-Cache", description = "Explains whether or not a cache was used", response = Boolean.class),
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotated Content ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)})})
    public Response getAnnotatedContent(@ApiParam(value = "Content Id to be retrieved.", required = true, defaultValue = MOCK_CONTENT_ID) @PathParam("annotatedContentId") String annotatedContentId,
                               @ApiParam(value = "Transaction Id", required = false) @HeaderParam("X-Request-ID") String transactionId) {
        if (!annotatedContentId.equals(annotatedContentPublisherService.MOCK_CONTENT_ID)) {
            Response.status(HttpURLConnection.HTTP_NOT_FOUND).build();
        }

        return Response.status(HttpURLConnection.HTTP_OK).entity(annotatedContentPublisherService.getContentDocument()).build();
    }


    // POST /annotatedcontent/{annotatedContentId}?asych={boolean}
    @POST
    @Timed
    @Consumes({CesDocumentMediaType.GENERIC_DOCUMENT_JSON_VALUE, CesDocumentMediaType.GENERIC_DOCUMENT_XML_VALUE})
    @Path("/{annotatedContentId}")
    @ApiOperation(value = "Get Annotated Content by Id", response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Unable to parse annotated document"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Success",
                    responseHeaders = {@ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)}),
            @ApiResponse(code = HttpURLConnection.HTTP_ACCEPTED, message = "Success",
                    responseHeaders = {@ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)})})
    public Response createAnnotatedContent(@ApiParam(name = "body", value = "Annotated Content to create", required = true) Document annotatedContent,
                                @ApiParam(value = "Annotated Content to be created.", required = true, defaultValue = MOCK_CONTENT_ID) @PathParam("annotatedContentId") String annotatedContentId,
                                @ApiParam(value = "Asynchronous", required = true, defaultValue = "false") @QueryParam("asynch") Boolean asynch,
                                @ApiParam(value = "Transaction Id", required = false) @HeaderParam("X-Request-ID") String transactionId,
                                           @Context UriInfo uriInfo) {
        if (!asynch) {
            URI syncLnk = uriInfo.getBaseUriBuilder().path("annotatedcontent").path(annotatedContentId).build();
            AnnotatedContentResult createAnnotatedContentResult = this.annotatedContentPublisherService.createAnnotatedContent(syncLnk, annotatedContent);
            return Response.ok(createAnnotatedContentResult.getLocation()).entity(createAnnotatedContentResult)
                    .header("Location", createAnnotatedContentResult.getLocation())
                    .header(HttpHeaders.VARY, "Accept")
                    .header(HttpHeaders.ETAG, "_87e52ce126126")
                    .header(HttpHeaders.ALLOW, HttpMethod.POST)
                    .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                    .build();
        } else {
            URI asyncLnk = uriInfo.getBaseUriBuilder().path("annotatedcontent").path(annotatedContentId).path("status").build();
            AnnotatedContentResult annotationResult = this.annotatedContentPublisherService.asynchCreateAnnotatedContent(asyncLnk, annotatedContent);
            return Response.accepted(annotationResult.getLocation()).entity(annotationResult)
                    .header("Location", annotationResult.getLocation())
                    .header(HttpHeaders.VARY, "Accept")
                    .header(HttpHeaders.ETAG, "_87e52ce126126")
                    .header(HttpHeaders.ALLOW, HttpMethod.POST)
                    .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                    .build();
        }
    }

    // PUT /annotatedcontent/{annotatedContentId}?asych={boolean}
    @PUT
    @Timed
    @Consumes({CesDocumentMediaType.GENERIC_DOCUMENT_JSON_VALUE, CesDocumentMediaType.GENERIC_DOCUMENT_XML_VALUE})
    @Path("/{annotatedContentId}")
    @ApiOperation(value = "Get Annotated Content by Id", response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Unable to parse annotated document"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Success",
                    responseHeaders = {@ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)}),
            @ApiResponse(code = HttpURLConnection.HTTP_ACCEPTED, message = "Success",
                    responseHeaders = {@ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)})})
    public Response updateAnnotatedContent(@ApiParam(name = "body", value = "Annotated Content to create", required = true ) Document annotatedContent,
                                @ApiParam(value = "Annotated Content to be created.", required = true, defaultValue = MOCK_CONTENT_ID) @PathParam("annotatedContentId") String annotatedContentId,
                                @ApiParam(value = "Asynchronous", required = true, defaultValue = "false") @QueryParam("asynch") Boolean asynch,
                                @ApiParam(value = "Transaction Id", required = false) @HeaderParam("X-Request-ID") String transactionId,
                                           @Context UriInfo uriInfo) {

        if (!asynch) {
            URI syncLnk = uriInfo.getBaseUriBuilder().path("annotatedcontent").path(annotatedContentId).build();
            AnnotatedContentResult createAnnotatedContentResult = this.annotatedContentPublisherService.updateAnnotatedContent(syncLnk, annotatedContent);
            return Response.ok(createAnnotatedContentResult.getLocation()).entity(createAnnotatedContentResult)
                    .header("Location", createAnnotatedContentResult.getLocation())
                    .header(HttpHeaders.VARY, "Accept")
                    .header(HttpHeaders.ETAG, "_87e52ce126126")
                    .header(HttpHeaders.ALLOW, HttpMethod.POST)
                    .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                    .build();
        } else {
            URI asyncLnk = uriInfo.getBaseUriBuilder().path("annotatedcontent").path(annotatedContentId).path("status").build();
            AnnotatedContentResult annotationResult = this.annotatedContentPublisherService.asynchUpdateAnnotatedContent(asyncLnk, annotatedContent);
            return Response.accepted(annotationResult.getLocation()).entity(annotationResult)
                    .header("Location", annotationResult.getLocation())
                    .header(HttpHeaders.VARY, "Accept")
                    .header(HttpHeaders.ETAG, "_87e52ce126126")
                    .header(HttpHeaders.ALLOW, HttpMethod.POST)
                    .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                    .build();
        }

    }


    // GET /annotatedcontent/{annotatedContentId}/status/{processId}
    @GET
    @Path("/{annotatedContentId}/status/{processId}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Asynchronous publication/creation annotation status")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Unknown annotation and processId"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Ok",
                    responseHeaders = {@ResponseHeader(name = "X-Cache", description = "Explains whether or not a cache was used", response = Boolean.class),
                            @ResponseHeader(name = HttpHeaders.VARY, description = "Make sure proxies cache by Vary", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ETAG, description = "Annotation ETag for cache control", response = String.class),
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)}) })
    public Response statusContentAnnotatedAsynch(@ApiParam(value = "Asynchronous Transaction Id", required = true) @PathParam("processId") String processId,
                                           @ApiParam(value = "Annotation Id", required = false ) @PathParam("annotatedContentId") String annotatedContentId,
                                           @ApiParam(value = "Transaction Id", required = false ) @HeaderParam("X-Request-ID") String transactionId,
                                           @Context UriInfo uriInfo) {
        URI link = uriInfo.getBaseUriBuilder().path("annotatedcontent").path(annotatedContentId).path("status").path(processId).build();
        AnnotatedContentResult annotatedContentResult = this.annotatedContentPublisherService.asynchAnnotatedContentStatus(link);

        return Response.ok().entity(annotatedContentResult)
                .header("Location",annotatedContentResult.getLocation())
                .header(HttpHeaders.VARY, "Accept")
                .header(HttpHeaders.ETAG, "_87e52ce126126")
                .header(HttpHeaders.ALLOW, HttpMethod.POST)
                .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                .build();
    }


    // DELETE /annotatedcontent/{annotatedContentId}?asych={boolean}
    @DELETE
    @Timed
    @Produces({CesDocumentMediaType.GENERIC_DOCUMENT_JSON_VALUE, CesDocumentMediaType.GENERIC_DOCUMENT_XML_VALUE})
    @Path("/{annotatedContentId}")
    @ApiOperation(value = "Delete Annotated Content by Id", response = Document.class)
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Annotated Content Id supplied"),
            @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Annotated Content not found"),
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Success",
                    responseHeaders = {
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)}),
            @ApiResponse(code = HttpURLConnection.HTTP_ACCEPTED, message = "Success",
                    responseHeaders = {
                            @ResponseHeader(name = HttpHeaders.ALLOW, description = "CORS Allowed Methods", response = String.class)})})
    public Response deleteAnnotatedContent(@ApiParam(value = "Annotated Content Id to be deleted.", required = true, defaultValue = MOCK_CONTENT_ID) @PathParam("annotatedContentId") String annotatedContentId,
                               @ApiParam(value = "Transaction Id", required = false) @HeaderParam("X-Request-ID") String transactionId,
                               @ApiParam(value = "Asynchronous", required = true, defaultValue = "false") @QueryParam("asynch") Boolean asynch,
                                           @Context UriInfo uriInfo) {
        if (!annotatedContentId.equals(annotatedContentPublisherService.MOCK_CONTENT_ID)) {
            Response.status(HttpURLConnection.HTTP_NOT_FOUND).build();
        }

        if (!asynch) {
            URI syncLnk = uriInfo.getBaseUriBuilder().path("annotatedcontent").path(annotatedContentId).build();
            AnnotatedContentResult createAnnotatedContentResult = this.annotatedContentPublisherService.deleteAnnotatedContent(syncLnk);
            return Response.ok(createAnnotatedContentResult.getLocation()).entity(createAnnotatedContentResult)
                    .header("Location", createAnnotatedContentResult.getLocation())
                    .header(HttpHeaders.VARY, "Accept")
                    .header(HttpHeaders.ETAG, "_87e52ce126126")
                    .header(HttpHeaders.ALLOW, HttpMethod.POST)
                    .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                    .build();
        } else {
            URI asyncLnk = uriInfo.getBaseUriBuilder().path("annotatedcontent").path(annotatedContentId).path("status").build();
            AnnotatedContentResult annotationResult = this.annotatedContentPublisherService.asynchDeleteAnnotatedContent(asyncLnk);
            return Response.accepted(annotationResult.getLocation()).entity(annotationResult)
                    .header("Location", annotationResult.getLocation())
                    .header(HttpHeaders.VARY, "Accept")
                    .header(HttpHeaders.ETAG, "_87e52ce126126")
                    .header(HttpHeaders.ALLOW, HttpMethod.POST)
                    .header(HttpHeaders.ALLOW, HttpMethod.PUT)
                    .build();
        }
    }
}
