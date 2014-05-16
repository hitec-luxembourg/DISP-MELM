package lu.hitec.pssu.melm.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
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
  public Response performAddIcon(@FormDataParam("displayName") final String displayName,
      @FormDataParam("largeIconFile") final InputStream file,
      @FormDataParam("largeIconFile") final FormDataContentDisposition fileDisposition) {
    try {
      final File largeIconFile = File.createTempFile("fromUpload", fileDisposition.getFileName());
      FileUtils.writeByteArrayToFile(largeIconFile, IOUtils.toByteArray(file));
      if ((largeIconFile != null) && (largeIconFile.length() <= 0)) {
        return Response.status(Status.BAD_REQUEST).entity("Invalid large icon file").build();
      }
      melmService.addIconAndFiles(displayName, largeIconFile);
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
  public Response performAddLibrary(@FormDataParam("libraryName") final String libraryName, @FormDataParam("version") final String version,
      @FormDataParam("libraryIconFile") final InputStream file,
      @FormDataParam("libraryIconFile") final FormDataContentDisposition fileDisposition) throws MELMException {
    try {
      final File libraryIconMaybeNull = File.createTempFile("fromUpload", fileDisposition.getFileName());
      FileUtils.writeByteArrayToFile(libraryIconMaybeNull, IOUtils.toByteArray(file));
      if ((libraryIconMaybeNull != null) && (libraryIconMaybeNull.length() <= 0)) {
        return Response.status(Status.BAD_REQUEST).entity("Invalid icon file").build();
      }
      final String hashForFile = melmService.addLibraryIcon(libraryIconMaybeNull);
      final int majorVersion = MELMUtils.getMajorVersion(version);
      final int minorVersion = MELMUtils.getMinorVersion(version);

      melmService.addLibrary(libraryName, majorVersion, minorVersion, hashForFile);
    } catch (final IOException e) {
      LOGGER.warn("Error in performAddLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    } catch (final MELMException e) {
      LOGGER.warn("Error in performAddLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    return gotoListLibraries();
  }

  @POST
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/add")
  public Response performAddLibraryIcon(@Context final UriInfo uriInfo, @FormParam("id") final long id,
      @FormParam("iconIndex") final int iconIndex, @FormParam("iconName") final String iconName,
      @FormParam("iconDescription") final String iconDescription, @FormParam("iconId") final long iconId) throws MELMException {
    try {
      melmService.addLibraryIcon(id, iconIndex, iconName, iconDescription, iconId);
      final URI uri = uriInfo.getBaseUriBuilder().path("/rest/libraries/icons/" + id).build();
      return Response.seeOther(uri).build();
    } catch (final MELMException e) {
      LOGGER.warn("Error in performAddLibraryIcon", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
  }

  @POST
  @Path("/libraries/icons/properties/add")
  public Response performAddProperty(@FormParam("id") final long id, @FormParam("uniqueName") @Nonnull final String uniqueName,
      @FormParam("type") @Nonnull final String type) throws MELMException {
    assert uniqueName != null : "uniqueName is null";
    assert type != null : "type is null";
    melmService.addProperty(id, uniqueName, CustomPropertyType.valueOf(type.toUpperCase()));
    return Response.ok().build();
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/clone")
  public Response performCloneLibrary(@FormDataParam("id") final long id, @FormDataParam("libraryName") final String libraryName,
      @FormDataParam("version") final String version, @FormDataParam("libraryIconFile") final InputStream file,
      @FormDataParam("libraryIconFile") final FormDataContentDisposition fileDisposition) throws MELMException {
    try {
      final File libraryIconMaybeNull = File.createTempFile("fromUpload", fileDisposition.getFileName());
      FileUtils.writeByteArrayToFile(libraryIconMaybeNull, IOUtils.toByteArray(file));
      if ((libraryIconMaybeNull != null) && (libraryIconMaybeNull.length() <= 0)) {
        return Response.status(Status.BAD_REQUEST).entity("Invalid icon file").build();
      }
      final String hashForFile = melmService.addLibraryIcon(libraryIconMaybeNull);
      final int majorVersion = MELMUtils.getMajorVersion(version);
      final int minorVersion = MELMUtils.getMinorVersion(version);

      melmService.cloneLibrary(id, libraryName, majorVersion, minorVersion, hashForFile);
    } catch (final IOException e) {
      LOGGER.warn("Error in performCloneLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
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
  public Response performImportLibrary(@FormDataParam("libraryName") final String libraryName,
      @FormDataParam("version") final String version, @FormDataParam("libraryFile") final InputStream file,
      @FormDataParam("libraryFile") final FormDataContentDisposition fileDisposition) {
    try {
      final File zipFileMaybeNull = File.createTempFile("fromUpload", fileDisposition.getFileName());
      FileUtils.writeByteArrayToFile(zipFileMaybeNull, IOUtils.toByteArray(file));
      if ((zipFileMaybeNull != null) && (zipFileMaybeNull.length() <= 0)) {
        return Response.status(Status.BAD_REQUEST).entity("Invalid zip file").build();
      }
      final int majorVersion = MELMUtils.getMajorVersion(version);
      final int minorVersion = MELMUtils.getMinorVersion(version);
      final File zipFile = melmService.importLibrary(libraryName, majorVersion, minorVersion, zipFileMaybeNull);

      final File libraryFolder = melmService.extractImportedLibrary(zipFile);
      if (libraryFolder != null) {
        final String iconMd5 = melmService.moveImportedLibraryIcon(libraryFolder, libraryName, majorVersion, minorVersion);
        final MapElementLibrary mapElementLibrary = melmService.addLibrary(libraryName, majorVersion, minorVersion, iconMd5);
        final NodeList nodeList = melmService.validateImportedLibraryAndGetNodeList(libraryName, majorVersion, minorVersion);
        melmService.moveImportedIcons(mapElementLibrary, nodeList, libraryFolder);
      }
    } catch (final IOException e) {
      LOGGER.warn("Error in performImportLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
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
  public Response performUpdateLibrary(@FormDataParam("id") final long id, @FormDataParam("libraryName") final String libraryName,
      @FormDataParam("version") final String version, @FormDataParam("libraryIconFile") final InputStream file,
      @FormDataParam("libraryIconFile") final FormDataContentDisposition fileDisposition) throws MELMException {
    try {
      final File libraryIconMaybeNull = File.createTempFile("fromUpload", fileDisposition.getFileName());
      FileUtils.writeByteArrayToFile(libraryIconMaybeNull, IOUtils.toByteArray(file));
      final int majorVersion = MELMUtils.getMajorVersion(version);
      final int minorVersion = MELMUtils.getMinorVersion(version);
      if ((libraryIconMaybeNull != null) && (libraryIconMaybeNull.length() > 0)) {
        final String hashForFile = melmService.addLibraryIcon(libraryIconMaybeNull);
        melmService.updateLibrary(id, libraryName, majorVersion, minorVersion, hashForFile);
      } else {
        melmService.updateLibrary(id, libraryName, majorVersion, minorVersion, null);
      }
    } catch (final IOException e) {
      LOGGER.warn("Error in performCloneLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    } catch (final MELMException e) {
      LOGGER.warn("Error in performUpdateLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
    return gotoListLibraries();
  }

  @POST
  @Produces(MediaType.TEXT_HTML)
  @Path("/libraries/icons/update")
  public Response performUpdateLibraryIcon(@Context final UriInfo uriInfo, @FormParam("libraryIconId") final long libraryIconId,
      @FormParam("iconIndex") final int iconIndex, @FormParam("iconName") final String iconName,
      @FormParam("iconDescription") final String iconDescription, @FormParam("iconId") final long iconId) throws MELMException {
    try {
      final long libraryId = melmService.getLibraryIcon(libraryIconId).getLibrary().getId();
      melmService.updateLibraryIcon(libraryIconId, iconIndex, iconName, iconDescription, iconId);
      final URI uri = uriInfo.getBaseUriBuilder().path("/rest/libraries/icons/" + libraryId).build();
      return Response.seeOther(uri).build();
    } catch (final MELMException e) {
      LOGGER.warn("Error in performAddLibrary", e);
      return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
  }

  @POST
  @Path("/libraries/icons/properties/update")
  public Response performUpdateProperty(@FormParam("id") final long id, @FormParam("uniqueName") @Nonnull final String uniqueName,
      @FormParam("type") @Nonnull final String type) throws MELMException {
    assert uniqueName != null : "uniqueName is null";
    assert type != null : "type is null";
    melmService.updateProperty(id, uniqueName, CustomPropertyType.valueOf(type.toUpperCase()));
    return Response.ok().build();
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

}