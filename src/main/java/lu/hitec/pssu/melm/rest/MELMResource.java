package lu.hitec.pssu.melm.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import lu.hitec.pssu.mapelement.library.xml.parser.XMLSelectionPathParser;
import lu.hitec.pssu.melm.exceptions.MELMException;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
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
  private static final String DEFAULT_MEDIA_TYPE = "image/png";

  private static final Logger LOGGER = LoggerFactory.getLogger(MELMResource.class);

  // Limit in bytes for file upload (overidden by services limits)
  private static final int MAX_FILE_SIZE = 8 * 1024 * 1024;

  @Autowired
  private MELMService melmService;

  @Context
  private HttpServletRequest request;

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/icons/delete/{id}")
  public Response deleteIcon(@PathParam("id") final long id) throws MELMException {
    try {
      melmService.deleteIconAndFiles(id);
    } catch (final MELMException e) {
      LOGGER.warn("Error in deleteIcon", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    return gotoListIcons();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/delete/{name}/{majorVersion}/{minorVersion}")
  public Response deleteLibrary(@PathParam("name") @Nonnull final String name, @PathParam("majorVersion") final int majorVersion,
      @PathParam("minorVersion") final int minorVersion) {
    assert name != null : "name is null";
    melmService.deleteLibrary(name, majorVersion, minorVersion);
    return gotoListLibraries();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/delete/{name}/{majorVersion}/{minorVersion}/{iconId}")
  public Response deleteLibraryIcon(@PathParam("name") @Nonnull final String name, @PathParam("majorVersion") final int majorVersion,
      @PathParam("minorVersion") final int minorVersion, @PathParam("iconId") final long iconId) {
    assert name != null : "name is null";
    melmService.deleteLibraryIcon(name, majorVersion, minorVersion, iconId);
    return getLibraryIcons(name, majorVersion, minorVersion);
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/{name}/{majorVersion}/{minorVersion}")
  public Response getLibraryIcons(@PathParam("name") @Nonnull final String name, @PathParam("majorVersion") final int majorVersion,
      @PathParam("minorVersion") final int minorVersion) {
    assert name != null : "name is null";
    final MapElementLibrary library = melmService.getLibrary(name, majorVersion, minorVersion);
    return Response.ok(
        new Viewable("/libraryIcons", new LibraryIconsModel(library, melmService.getLibraryIcons(name, majorVersion, minorVersion))))
        .build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/icons/details/{id}")
  public Response getIconDetails(@PathParam("id") final long id) {
    return Response.ok(new Viewable("/iconDetails", melmService.getIcon(id))).build();
  }

  /**
   * It is better to use the id of icon and the size than the path of icon for security issues. Because someone could access file system.
   */
  @GET
  @Produces("image/*")
  @Path("/icons/file/{id}/{size}")
  public Response getIconFile(@PathParam("id") final long id, @PathParam("size") @Nonnull final String size) {
    assert size != null : "Size is null";
    final File file = melmService.getIconFile(id, size);
    if (!file.exists()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    return Response.ok(file, DEFAULT_MEDIA_TYPE).build();
  }

  /**
   * Same comment as for getIconFile method.
   */
  @GET
  @Produces("image/*")
  @Path("/libraries/icon/file/{name}/{majorVersion}/{minorVersion}")
  public Response getLibraryIconFile(@PathParam("name") @Nonnull final String name, @PathParam("majorVersion") final int majorVersion,
      @PathParam("minorVersion") final int minorVersion) {
    assert name != null : "name is null";
    final File file = melmService.getLibraryIconFile(name, majorVersion, minorVersion);
    if (!file.exists()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    return Response.ok(file, DEFAULT_MEDIA_TYPE).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/icons/add")
  public Response gotoAddIcon() {
    return Response.ok(new Viewable("/addIcon")).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/add")
  public Response gotoAddLibrary() {
    return Response.ok(new Viewable("/addLibrary")).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/update/{name}/{majorVersion}/{minorVersion}")
  public Response gotoUpdateLibrary(@PathParam("name") @Nonnull final String name, @PathParam("majorVersion") final int majorVersion,
      @PathParam("minorVersion") final int minorVersion) {
    assert name != null : "name is null";
    final MapElementLibrary library = melmService.getLibrary(name, majorVersion, minorVersion);
    return Response.ok(new Viewable("/updateLibrary", library)).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/add/{name}/{majorVersion}/{minorVersion}")
  public Response gotoAddLibraryIcon(@PathParam("name") @Nonnull final String name, @PathParam("majorVersion") final int majorVersion,
      @PathParam("minorVersion") final int minorVersion) {
    assert name != null : "name is null";
    final MapElementLibrary library = melmService.getLibrary(name, majorVersion, minorVersion);
    return Response.ok(new Viewable("/addLibraryIcon", new AddLibraryIconsModel(library, melmService.listAllIcons()))).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/import")
  public Response gotoImportLibrary() {
    return Response.ok(new Viewable("/importLibrary")).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/icons")
  public Response gotoListIcons() {
    return Response.ok(new Viewable("/icons", new IconsModel(melmService.listAllIcons()))).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries")
  public Response gotoListLibraries() {
    return Response.ok(new Viewable("/libraries", new LibrariesModel(melmService.listAllLibraries()))).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response gotoStart() {
    return Response.ok(new Viewable("/start")).build();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/icons/add")
  public Response performAddIcon() {
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    try {
      final IconUpload iconUpload = parseIconUpload();
      melmService.addIconAndFiles(iconUpload.getDisplayName(), iconUpload.getLargeIconFile());
    } catch (final Exception e) {
      LOGGER.warn("Error in performAddIcon", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }

    return gotoListIcons();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/add")
  public Response performAddLibrary() throws MELMException {
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    try {
      final LibraryUpload libraryUpload = parseLibraryUpload();
      if (libraryUpload.getIconFile().length() == 0) {
        return Response.status(Status.BAD_REQUEST).entity("Icon file is invalid").build();
      }
      final String hashForFile = melmService.addLibraryIcon(libraryUpload.getIconFile());
      melmService.addLibrary(libraryUpload.getLibraryName(), libraryUpload.getVersion(), hashForFile);
    } catch (final MELMException e) {
      LOGGER.warn("Error in performAddLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    return gotoListLibraries();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/update")
  public Response performUpdateLibrary() throws MELMException {
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    try {
      final LibraryUpload libraryUpload = parseLibraryUpload();
      if (libraryUpload.getIconFile().length() != 0) {
        final String hashForFile = melmService.addLibraryIcon(libraryUpload.getIconFile());
        melmService.updateLibrary(libraryUpload.getId(), libraryUpload.getLibraryName(), libraryUpload.getVersion(), hashForFile);
      } else {
        melmService.updateLibrary(libraryUpload.getId(), libraryUpload.getLibraryName(), libraryUpload.getVersion(), null);
      }
    } catch (final MELMException e) {
      LOGGER.warn("Error in performUpdateLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    return gotoListLibraries();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/add")
  public Response performAddLibraryIcon() throws MELMException {
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    try {
      final LibraryIconUpload libraryIconUpload = parseLibraryIconUpload();
      melmService.addLibraryIcon(libraryIconUpload.getLibraryName(), libraryIconUpload.getMajorVersion(),
          libraryIconUpload.getMinorVersion(), libraryIconUpload.getIconIndex(), libraryIconUpload.getIconName(),
          libraryIconUpload.getIconDescription(), libraryIconUpload.getIconId());
    } catch (final MELMException e) {
      LOGGER.warn("Error in performAddLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    return gotoListLibraries();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/import")
  public Response performImportLibrary() {
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    try {
      final LibraryUpload libraryUpload = parseLibraryUpload();
      if (libraryUpload.getZipFile().length() == 0) {
        return Response.status(Status.BAD_REQUEST).entity("Zip file is invalid").build();
      }
      final File zipFile = melmService
          .importLibrary(libraryUpload.getLibraryName(), libraryUpload.getVersion(), libraryUpload.getZipFile());
      final File libraryFolder = melmService.extractImportedLibrary(zipFile);
      if (libraryFolder != null) {
        final XMLSelectionPathParser libraryParser = melmService.validateAndParseImportedLibrary(libraryUpload.getLibraryName(),
            libraryUpload.getVersion());
        final String iconMd5 = melmService.moveImportedLibraryIcon(libraryParser, libraryFolder);
        final MapElementLibrary mapElementLibrary = melmService.addLibrary(libraryUpload.getLibraryName(), libraryUpload.getVersion(),
            iconMd5);
        melmService.moveImportedIcons(mapElementLibrary, libraryParser, libraryFolder);
      }
    } catch (final MELMException e) {
      LOGGER.warn("Error in performImportLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }

    return gotoListLibraries();
  }

  private IconUpload parseIconUpload() {
    File largeIconFile = null;
    String displayName = null;

    final ServletFileUpload upload = new ServletFileUpload();
    upload.setFileSizeMax(MAX_FILE_SIZE);

    InputStream stream = null;
    try {
      final FileItemIterator iter = upload.getItemIterator(request);
      while (iter.hasNext()) {
        final FileItemStream item = iter.next();
        stream = item.openStream();
        final String fieldName = item.getFieldName();
        if (Params.DISPLAY_NAME.equalsIgnoreCase(fieldName)) {
          displayName = Streams.asString(stream);
        } else if (Params.LARGE_FILE.equalsIgnoreCase(fieldName)) {
          // We are using temp dir because we don't know by advance the name and the version as we are inside a loop.
          largeIconFile = File.createTempFile("fromUpload", null);
          FileUtils.writeByteArrayToFile(largeIconFile, IOUtils.toByteArray(stream));
        }
      }
    } catch (final IOException e) {
      return null;
    } catch (final FileUploadException e) {
      return null;
    } finally {
      MELMUtils.closeResource(stream);
    }
    if ((displayName == null) || (largeIconFile == null)) {
      return null;
    }
    return new IconUpload(displayName, largeIconFile);
  }

  private LibraryUpload parseLibraryUpload() {
    String id = null;
    String name = null;
    String version = null;
    File libraryZipMaybeNull = null;
    File libraryIconMaybeNull = null;

    final ServletFileUpload upload = new ServletFileUpload();
    upload.setFileSizeMax(MAX_FILE_SIZE);

    InputStream stream = null;
    try {
      final FileItemIterator iter = upload.getItemIterator(request);
      while (iter.hasNext()) {
        final FileItemStream item = iter.next();
        stream = item.openStream();
        final String fieldName = item.getFieldName();
        if (Params.ID.equalsIgnoreCase(fieldName)) {
          id = Streams.asString(stream);
        } else if (Params.NAME.equalsIgnoreCase(fieldName)) {
          name = Streams.asString(stream);
        } else if (Params.VERSION.equalsIgnoreCase(fieldName)) {
          version = Streams.asString(stream);
        } else if (Params.FILE.equalsIgnoreCase(fieldName)) {
          // We are using temp dir because we don't know by advance the name and the version as we are inside a loop.
          libraryZipMaybeNull = File.createTempFile("fromUpload", null);
          FileUtils.writeByteArrayToFile(libraryZipMaybeNull, IOUtils.toByteArray(stream));
        } else if (Params.FILE_ICON.equalsIgnoreCase(fieldName)) {
          // We are using temp dir because we don't know by advance the name and the version as we are inside a loop.
          libraryIconMaybeNull = File.createTempFile("fromUpload", null);
          FileUtils.writeByteArrayToFile(libraryIconMaybeNull, IOUtils.toByteArray(stream));
        }
      }
    } catch (final IOException e) {
      return null;
    } catch (final FileUploadException e) {
      return null;
    } finally {
      MELMUtils.closeResource(stream);
    }
    if ((name == null) || (version == null)) {
      return null;
    }
    return new LibraryUpload(id, name, version, libraryZipMaybeNull, libraryIconMaybeNull);
  }

  private LibraryIconUpload parseLibraryIconUpload() {
    String name = null;
    String majorVersion = null;
    String minorVersion = null;
    String iconIndex = null;
    String iconName = null;
    String iconDescription = null;
    String iconId = null;

    final ServletFileUpload upload = new ServletFileUpload();
    upload.setFileSizeMax(MAX_FILE_SIZE);

    InputStream stream = null;
    try {
      final FileItemIterator iter = upload.getItemIterator(request);
      while (iter.hasNext()) {
        final FileItemStream item = iter.next();
        stream = item.openStream();
        final String fieldName = item.getFieldName();
        if (Params.NAME.equalsIgnoreCase(fieldName)) {
          name = Streams.asString(stream);
        } else if (Params.MAJOR_VERSION.equalsIgnoreCase(fieldName)) {
          majorVersion = Streams.asString(stream);
        } else if (Params.MINOR_VERSION.equalsIgnoreCase(fieldName)) {
          minorVersion = Streams.asString(stream);
        } else if (Params.ICON_INDEX.equalsIgnoreCase(fieldName)) {
          iconIndex = Streams.asString(stream);
        } else if (Params.ICON_NAME.equalsIgnoreCase(fieldName)) {
          iconName = Streams.asString(stream);
        } else if (Params.ICON_DESCRIPTION.equalsIgnoreCase(fieldName)) {
          iconDescription = Streams.asString(stream);
        } else if (Params.ICON_ID.equalsIgnoreCase(fieldName)) {
          iconId = Streams.asString(stream);
        }
      }
    } catch (final IOException e) {
      return null;
    } catch (final FileUploadException e) {
      return null;
    } finally {
      MELMUtils.closeResource(stream);
    }
    if ((name == null) || (majorVersion == null) || (minorVersion == null) || (iconIndex == null) || (iconName == null)
        || (iconDescription == null) || (iconId == null)) {
      return null;
    }
    return new LibraryIconUpload(name, majorVersion, minorVersion, iconIndex, iconName, iconDescription, iconId);
  }

  private final class IconUpload {
    private final String displayName;
    private final File largeIconFile;

    public IconUpload(final String displayName, final File largeIconFile) {
      this.displayName = displayName;
      this.largeIconFile = largeIconFile;
    }

    public String getDisplayName() {
      return displayName;
    }

    public File getLargeIconFile() {
      return largeIconFile;
    }
  }

  private final class LibraryUpload {
    private final String id;
    private final String libraryName;
    private final String version;
    private final File zipFile;
    private final File iconFile;

    public LibraryUpload(final String id, final String libraryName, final String version, final File zipFile, final File iconFile) {
      this.id = id;
      this.libraryName = libraryName;
      this.version = version;
      this.zipFile = zipFile;
      this.iconFile = iconFile;
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

    public File getIconFile() {
      return iconFile;
    }

    public String getId() {
      return id;
    }
  }

  private final class LibraryIconUpload {
    private final String libraryName;
    private final String majorVersion;
    private final String minorVersion;
    private final String iconIndex;
    private final String iconName;
    private final String iconDescription;
    private final String iconId;

    public LibraryIconUpload(final String libraryName, final String majorVersion, final String minorVersion, final String iconIndex,
        final String iconName, final String iconDescription, final String iconId) {
      this.libraryName = libraryName;
      this.majorVersion = majorVersion;
      this.minorVersion = minorVersion;
      this.iconIndex = iconIndex;
      this.iconName = iconName;
      this.iconDescription = iconDescription;
      this.iconId = iconId;
    }

    public String getLibraryName() {
      return libraryName;
    }

    public String getMajorVersion() {
      return majorVersion;
    }

    public String getMinorVersion() {
      return minorVersion;
    }

    public String getIconIndex() {
      return iconIndex;
    }

    public String getIconName() {
      return iconName;
    }

    public String getIconDescription() {
      return iconDescription;
    }

    public String getIconId() {
      return iconId;
    }

  }
}