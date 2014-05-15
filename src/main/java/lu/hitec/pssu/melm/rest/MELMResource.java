package lu.hitec.pssu.melm.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import lu.hitec.pssu.melm.exceptions.MELMException;
import lu.hitec.pssu.melm.persistence.dto.DTOMapElementCustomProperty;
import lu.hitec.pssu.melm.persistence.dto.DTOMapElementLibraryIcon;
import lu.hitec.pssu.melm.persistence.entity.MapElementCustomProperty;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;
import lu.hitec.pssu.melm.persistence.entity.MapElementLibraryIcon;
import lu.hitec.pssu.melm.services.MELMService;
import lu.hitec.pssu.melm.utils.CustomPropertyType;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;

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

  @POST
  @Path("/icons/delete")
  public Response deleteIcon(@FormParam("id") final long id) throws MELMException {
    try {
      melmService.deleteIconAndFiles(id);
      return Response.ok().build();
    } catch (final MELMException e) {
      LOGGER.warn("Error in deleteIcon", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
  }

  @POST
  @Path("/libraries/delete")
  public Response deleteLibrary(@FormParam("id") final long id) {
    melmService.deleteLibrary(id);
    return Response.ok().build();
  }

  @POST
  @Path("/libraries/icons/delete")
  public Response deleteLibraryIcon(@FormParam("id") final long id) {
    melmService.deleteLibraryIcon(id);
    return Response.ok().build();
  }

  @POST
  @Path("/libraries/icons/properties/delete")
  public Response deleteProperty(@FormParam("id") final long id) {
    melmService.deleteProperty(id);
    return Response.ok().build();
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
  @Path("/libraries/icon/file/{id}")
  public Response getLibraryIconFile(@PathParam("id") final long id) {
    final File file = melmService.getLibraryIconFile(id);
    if (!file.exists()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    return Response.ok(file, DEFAULT_MEDIA_TYPE).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/libraries/icons/json/{id}")
  public Response getLibraryIcons(@PathParam("id") final long id) {
    final MapElementLibrary library = melmService.getLibrary(id);
    // Convert to DTO in order to prevent jackson throwing LazyInitializationException.
    final List<DTOMapElementLibraryIcon> results = new ArrayList<>();
    final List<MapElementLibraryIcon> libraryIcons = melmService.getLibraryIcons(id);
    for (final MapElementLibraryIcon libraryIcon : libraryIcons) {
      final DTOMapElementLibraryIcon dtoLibraryIcon = new DTOMapElementLibraryIcon();
      BeanUtils.copyProperties(libraryIcon, dtoLibraryIcon);
      results.add(dtoLibraryIcon);
    }
    return Response.ok(new LibraryIconsModel(library, results)).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/icons/json")
  public Response getListIcons() {
    return Response.ok(melmService.listAllIcons()).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/libraries/json")
  public Response getListLibraries() {
    return Response.ok(melmService.listAllLibraries()).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/libraries/icons/properties/json/{id}")
  public Response getProperties(@PathParam("id") final long id) {
    final List<MapElementCustomProperty> properties = melmService.getProperties(id);
    // Convert to DTO in order to prevent jackson throwing LazyInitializationException.
    final List<DTOMapElementCustomProperty> results = new ArrayList<>();
    for (final MapElementCustomProperty property : properties) {
      final DTOMapElementCustomProperty dtoProperty = new DTOMapElementCustomProperty();
      BeanUtils.copyProperties(property, dtoProperty);
      results.add(dtoProperty);
    }
    return Response.ok(results).build();
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
  @Path("/libraries/icons/add/{id}")
  public Response gotoAddLibraryIcon(@PathParam("id") final int id) {
    final MapElementLibrary library = melmService.getLibrary(id);
    return Response.ok(new Viewable("/addLibraryIcon", new AddLibraryIconsModel(library, melmService.listAllIcons()))).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/clone/{id}")
  public Response gotoCloneLibrary(@PathParam("id") final long id) {
    final MapElementLibrary library = melmService.getLibrary(id);
    return Response.ok(new Viewable("/cloneLibrary", library)).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/import")
  public Response gotoImportLibrary() {
    return Response.ok(new Viewable("/importLibrary")).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/{id}")
  public Response gotoLibraryIcons(@PathParam("id") final long id) {
    return Response.ok(new Viewable("/libraryIcons", id)).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/icons")
  public Response gotoListIcons() {
    return Response.ok(new Viewable("/icons")).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries")
  public Response gotoListLibraries() {
    return Response.ok(new Viewable("/libraries")).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/properties/{id}")
  public Response gotoProperties(@PathParam("id") final long id) {
    return Response.ok(new Viewable("/properties", id)).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response gotoStart() {
    return Response.ok(new Viewable("/start")).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/update/{id}")
  public Response gotoUpdateLibrary(@PathParam("id") final long id) {
    final MapElementLibrary library = melmService.getLibrary(id);
    return Response.ok(new Viewable("/updateLibrary", library)).build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/update/{libraryIconId}")
  public Response gotoUpdateLibraryIcon(@PathParam("libraryIconId") final long libraryIconId) {
    final MapElementLibraryIcon libraryIcon = melmService.getLibraryIcon(libraryIconId);
    return Response.ok(new Viewable("/updateLibraryIcon", new UpdateLibraryIconsModel(libraryIcon, melmService.listAllIcons()))).build();
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
      if (iconUpload.getLargeIconFile().length() == 0) {
        return Response.status(Status.BAD_REQUEST).entity("Icon file is invalid").build();
      }
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
      final int majorVersion = MELMUtils.getMajorVersion(libraryUpload.getVersion());
      final int minorVersion = MELMUtils.getMinorVersion(libraryUpload.getVersion());

      melmService.addLibrary(libraryUpload.getLibraryName(), majorVersion, minorVersion, hashForFile);
    } catch (final MELMException e) {
      LOGGER.warn("Error in performAddLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    return gotoListLibraries();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/add")
  public Response performAddLibraryIcon(@Context final UriInfo uriInfo) throws MELMException {
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    final LibraryIconUpload libraryIconUpload = parseLibraryIconUpload();
    try {
      if (libraryIconUpload == null) {
        final String msg = "Failed to parse parameters";
        LOGGER.warn(msg);
        return Response.status(Status.BAD_REQUEST).entity(msg).build();
      }
      melmService.addLibraryIcon(Long.parseLong(libraryIconUpload.getId()), Integer.parseInt(libraryIconUpload.getIconIndex()),
          libraryIconUpload.getIconName(), libraryIconUpload.getIconDescription(), Long.parseLong(libraryIconUpload.getIconId()));
      final URI uri = uriInfo.getBaseUriBuilder().path("/rest/libraries/icons/" + Long.parseLong(libraryIconUpload.getId())).build();
      return Response.seeOther(uri).build();
    } catch (final MELMException e) {
      LOGGER.warn("Error in performAddLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
  }

  @POST
  @Path("/libraries/icons/properties/add")
  public Response performAddProperty(@FormParam("id") final long id, @FormParam("uniqueName") final String uniqueName,
      @FormParam("type") final String type) throws MELMException {
    melmService.addProperty(id, uniqueName, CustomPropertyType.valueOf(type.toUpperCase()));
    return Response.ok().build();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/clone")
  public Response performCloneLibrary() throws MELMException {
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
      final int majorVersion = MELMUtils.getMajorVersion(libraryUpload.getVersion());
      final int minorVersion = MELMUtils.getMinorVersion(libraryUpload.getVersion());
      melmService.cloneLibrary(Long.parseLong(libraryUpload.getId()), libraryUpload.getLibraryName(), majorVersion, minorVersion,
          hashForFile);
    } catch (final MELMException e) {
      LOGGER.warn("Error in performCloneLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    return gotoListLibraries();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/hello/{value}")
  public Response performHelloWorld(@PathParam("value") final String value) throws MELMException {
    final String result = String.format("Hello %s", value);
    return Response.status(Response.Status.OK).entity(result).build();
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
      final int majorVersion = MELMUtils.getMajorVersion(libraryUpload.getVersion());
      final int minorVersion = MELMUtils.getMinorVersion(libraryUpload.getVersion());
      final File zipFile = melmService
          .importLibrary(libraryUpload.getLibraryName(), majorVersion, minorVersion, libraryUpload.getZipFile());
      final File libraryFolder = melmService.extractImportedLibrary(zipFile);
      if (libraryFolder != null) {
        final String iconMd5 = melmService.moveImportedLibraryIcon(libraryFolder, libraryUpload.getLibraryName(), majorVersion,
            minorVersion);
        final MapElementLibrary mapElementLibrary = melmService.addLibrary(libraryUpload.getLibraryName(), majorVersion, minorVersion,
            iconMd5);
        final NodeList nodeList = melmService.validateImportedLibraryAndGetNodeList(libraryUpload.getLibraryName(), majorVersion,
            minorVersion);
        melmService.moveImportedIcons(mapElementLibrary, nodeList, libraryFolder);
      }
    } catch (final MELMException e) {
      LOGGER.warn("Error in performImportLibrary", e);
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
      final int majorVersion = MELMUtils.getMajorVersion(libraryUpload.getVersion());
      final int minorVersion = MELMUtils.getMinorVersion(libraryUpload.getVersion());
      if (libraryUpload.getIconFile().length() != 0) {
        final String hashForFile = melmService.addLibraryIcon(libraryUpload.getIconFile());
        melmService.updateLibrary(Long.parseLong(libraryUpload.getId()), libraryUpload.getLibraryName(), majorVersion, minorVersion,
            hashForFile);
      } else {
        melmService.updateLibrary(Long.parseLong(libraryUpload.getId()), libraryUpload.getLibraryName(), majorVersion, minorVersion, null);
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
  @Path("/libraries/icons/update")
  public Response performUpdateLibraryIcon(@Context final UriInfo uriInfo) throws MELMException {
    if (!ServletFileUpload.isMultipartContent(request)) {
      LOGGER.warn("Got invalid request, no multipart content");
      return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
    }

    final LibraryIconUpload libraryIconUpload = parseLibraryIconUpload();
    try {
      if (libraryIconUpload == null) {
        final String msg = "Failed to parse parameters";
        LOGGER.warn(msg);
        return Response.status(Status.BAD_REQUEST).entity(msg).build();
      }
      final long libraryId = melmService.getLibraryIcon(Long.parseLong(libraryIconUpload.getLibraryIconId())).getLibrary().getId();
      melmService.updateLibraryIcon(Long.parseLong(libraryIconUpload.getLibraryIconId()),
          Integer.parseInt(libraryIconUpload.getIconIndex()), libraryIconUpload.getIconName(), libraryIconUpload.getIconDescription(),
          Long.parseLong(libraryIconUpload.getIconId()));
      final URI uri = uriInfo.getBaseUriBuilder().path("/rest/libraries/icons/" + libraryId).build();
      return Response.seeOther(uri).build();
    } catch (final MELMException e) {
      LOGGER.warn("Error in performAddLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
  }

  @GET
  @Produces("application/zip")
  @Path("/libraries/zip/{name}-{majorVersion}.{minorVersion}.zip")
  public Response performZipLibrary(@PathParam("name") @Nonnull final String name, @PathParam("majorVersion") final int majorVersion,
      @PathParam("minorVersion") final int minorVersion) throws MELMException {
    assert name != null : "name is null";
    final File zipFolder = melmService.generateXmlAndPrepareZipFile(name, majorVersion, minorVersion);
    final File zipFile = melmService.generateZipFile(zipFolder);
    return Response.ok(zipFile).build();
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

  @CheckReturnValue
  private LibraryIconUpload parseLibraryIconUpload() {
    String id = null;
    String iconIndex = null;
    String iconName = null;
    String iconDescription = null;
    String iconId = null;
    String libraryIconId = null;

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
        } else if (Params.ICON_INDEX.equalsIgnoreCase(fieldName)) {
          iconIndex = Streams.asString(stream);
        } else if (Params.ICON_NAME.equalsIgnoreCase(fieldName)) {
          iconName = Streams.asString(stream);
        } else if (Params.ICON_DESCRIPTION.equalsIgnoreCase(fieldName)) {
          iconDescription = Streams.asString(stream);
        } else if (Params.ICON_ID.equalsIgnoreCase(fieldName)) {
          iconId = Streams.asString(stream);
        } else if (Params.LIBRARY_ICON_ID.equalsIgnoreCase(fieldName)) {
          libraryIconId = Streams.asString(stream);
        }
      }
    } catch (final IOException e) {
      return null;
    } catch (final FileUploadException e) {
      return null;
    } finally {
      MELMUtils.closeResource(stream);
    }
    if ((id == null) || (iconIndex == null) || (iconName == null) || (iconDescription == null) || (iconId == null)) {
      return null;
    }
    return new LibraryIconUpload(libraryIconId, id, iconIndex, iconName, iconDescription, iconId);
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

  private final class LibraryIconUpload {
    private final String iconDescription;
    private final String iconId;
    private final String iconIndex;
    private final String iconName;
    private final String id;
    private final String libraryIconId;

    public LibraryIconUpload(final String libraryIconId, final String id, final String iconIndex, final String iconName,
        final String iconDescription, final String iconId) {
      this.libraryIconId = libraryIconId;
      this.id = id;
      this.iconIndex = iconIndex;
      this.iconName = iconName;
      this.iconDescription = iconDescription;
      this.iconId = iconId;
    }

    public String getIconDescription() {
      return iconDescription;
    }

    public String getIconId() {
      return iconId;
    }

    public String getIconIndex() {
      return iconIndex;
    }

    public String getIconName() {
      return iconName;
    }

    public String getId() {
      return id;
    }

    public String getLibraryIconId() {
      return libraryIconId;
    }

  }

  private final class LibraryUpload {
    private final File iconFile;
    private final String id;
    private final String libraryName;
    private final String version;
    private final File zipFile;

    public LibraryUpload(final String id, final String libraryName, final String version, final File zipFile, final File iconFile) {
      this.id = id;
      this.libraryName = libraryName;
      this.version = version;
      this.zipFile = zipFile;
      this.iconFile = iconFile;
    }

    public File getIconFile() {
      return iconFile;
    }

    public String getId() {
      return id;
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