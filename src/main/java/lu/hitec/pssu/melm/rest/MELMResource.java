package lu.hitec.pssu.melm.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import lu.hitec.pssu.mapelement.library.xml.BaseNodeType;
import lu.hitec.pssu.mapelement.library.xml.parser.XMLSelectionPathParser;
import lu.hitec.pssu.melm.exceptions.MELMException;
import lu.hitec.pssu.melm.services.MELMService;
import lu.hitec.pssu.melm.utils.MELMUtils;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/rest")
@Component
@SuppressWarnings("static-method")
public class MELMResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(MELMResource.class);

  // Limit in bytes for file upload (overidden by services limits)
  private final int maxFileSize = 8 * 1024 * 1024;

  @Autowired
  private MELMService melmService;

  @Context
  private HttpServletRequest request;

  @SuppressWarnings("unused")
  @GET
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/icons")
  public Response gotoIcons(@Context final UriInfo uriInfo, @Context final HttpServletRequest requestParam) throws URISyntaxException {
    return Response.ok(new Viewable("/icons")).build();
  }

  @SuppressWarnings("unused")
  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/icons/import")
  public Response gotoImportIcon(@Context final UriInfo uriInfo, @Context final HttpServletRequest requestParam) throws URISyntaxException {
    return Response.ok(new Viewable("/importIcon")).build();
  }

  @SuppressWarnings("unused")
  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/import")
  public Response gotoImportLibrary(@Context final UriInfo uriInfo, @Context final HttpServletRequest requestParam)
      throws URISyntaxException {
    return Response.ok(new Viewable("/importLibrary")).build();
  }

  @SuppressWarnings("unused")
  @GET
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries")
  public Response gotoLibraries(@Context final UriInfo uriInfo, @Context final HttpServletRequest requestParam) throws URISyntaxException {
    return Response.ok(new Viewable("/libraries")).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response gotoStart() {
    return Response.ok(new Viewable("/start")).build();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/import")
  public Response performImportLibrary(@Context final UriInfo uriInfo,
      @SuppressWarnings("unused") @Context final HttpServletRequest requestParam) throws URISyntaxException {
    if (uriInfo != null) {
      LOGGER.debug(String.format("URI Info %s", uriInfo));
    }
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    LibraryUpload libraryUpload = null;
    try {
      libraryUpload = parseLibraryUpload(request);
    } catch (final Exception e) {
      LOGGER.warn("Failed to get library from request", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }

    try {
      final File zipFile = melmService.addZipFile(libraryUpload.getLibraryName(), libraryUpload.getVersion(), libraryUpload.getZipFile());
      melmService.extractZipFile(zipFile);
      final XMLSelectionPathParser libraryParser = melmService.validateAndParse(libraryUpload.getLibraryName(), libraryUpload.getVersion());

      // FIXME Move this part in MELMService.
      System.out.println(String.format("Library Id %s", libraryParser.getLibraryId()));
      System.out.println(String.format("Library Display Name %s", libraryParser.getLibraryDisplayName()));
      System.out.println(String.format("Library Type %s", libraryParser.getLibraryType()));
      System.out.println(String.format("Library Version %s", libraryParser.getLibraryVersion()));
      System.out.println(String.format("Library icon Path %s", libraryParser.getLibraryIconRelativePath()));
      final Map<String, BaseNodeType> mapOfNodesByUniqueCode = libraryParser.getMapOfNodesByUniqueCode();
      final Iterator<Map.Entry<String, BaseNodeType>> iterator = mapOfNodesByUniqueCode.entrySet().iterator();
      while (iterator.hasNext()) {
        final Map.Entry<String, BaseNodeType> mapEntry = iterator.next();
        System.out.println("The key is: " + mapEntry.getKey() + ",value is :" + mapEntry.getValue().getDescription());
      }
    } catch (final MELMException e) {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Error in melmService.addZipFile", e);
      }
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }

    return buildRedirectResponse(uriInfo, "/rest/libraries");
  }

  private LibraryUpload parseLibraryUpload(@SuppressWarnings("unused") @Context final HttpServletRequest requestParam) {
    File libraryZip = null;
    String name = null;
    String version = null;

    final ServletFileUpload upload = new ServletFileUpload();
    upload.setFileSizeMax(maxFileSize);

    InputStream stream = null;
    try {
      final FileItemIterator iter = upload.getItemIterator(request);
      while (iter.hasNext()) {
        // TODO do not know what we should close here.
        final FileItemStream item = iter.next();
        stream = item.openStream();
        if (item.isFormField()) {
          final String fieldName = item.getFieldName();
          final String value = Streams.asString(stream);
          if (Params.NAME.equalsIgnoreCase(fieldName)) {
            name = value;
          } else if (Params.VERSION.equalsIgnoreCase(fieldName)) {
            version = value;
          }
        } else {
          // We are using temp dir because we don't know by advance the name and the version as we are inside a loop.
          libraryZip = File.createTempFile("fromUpload", null);
          FileUtils.writeByteArrayToFile(libraryZip, IOUtils.toByteArray(stream));
        }
      }
    } catch (final IOException e) {
      return null;
    } catch (final FileUploadException e) {
      return null;
    } finally {
      MELMUtils.closeResource(stream);
    }
    if ((libraryZip == null) || (name == null) || (version == null)) {
      return null;
    }
    return new LibraryUpload(libraryZip, name, version);
  }

  private static Response buildRedirectResponse(@Context final UriInfo uriInfo, @Nonnull final String path) {
    final URI newURI = uriInfo.getBaseUriBuilder().path(path).build();
    return Response.seeOther(newURI).build();
  }

  private final class LibraryUpload {
    private final String libraryName;
    private final String version;
    private final File zipFile;

    public LibraryUpload(final File zipFile, final String libraryName, final String version) {
      this.zipFile = zipFile;
      this.libraryName = libraryName;
      this.version = version;
    }

    public String getLibraryName() {
      return libraryName;
    }

    public String getVersion() {
      return version;
    }

    public File getZipFile() {
      return zipFile;
    }
  }
}