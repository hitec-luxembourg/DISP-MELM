package lu.hitec.pssu.melm.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

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
import javax.ws.rs.core.UriInfo;

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
	private static final String DEFAULT_MEDIA_TYPE = "image/png";

	private static final Logger LOGGER = LoggerFactory.getLogger(MELMResource.class);

	// Limit in bytes for file upload (overidden by services limits)
	private static final int MAX_FILE_SIZE = 8 * 1024 * 1024;

	@Autowired
	private MELMService melmService;

	@Context
	private HttpServletRequest request;

	@GET
	@Path("/icons/delete/{id}")
	@Produces(MediaType.TEXT_HTML)
	public Response deleteIcon(@PathParam("id") final long id) {
		melmService.deleteIconAndFiles(id);
		return gotoListIcons();
	}

	@GET
	@Path("/icons/details/{id}")
	@Produces(MediaType.TEXT_HTML)
	public Response getIconDetails(@PathParam("id") final long id) {
		return Response.ok(new Viewable("/iconDetails", melmService.getIcon(id))).build();
	}

	/**
	 * It is better to use the id of icon and the size than the path of icon for security issues. Because someone could access file system.
	 */
	@GET
	@Path("/icons/file/{id}/{size}")
	@Produces("image/*")
	public Response getIconFile(@PathParam("id") final long id, @PathParam("size") @Nonnull final String size) {
		assert size != null : "Size is null";
		final File file = melmService.getIconFile(id, size);
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
	@Path("/icons/add")
	public Response performAddIcon(@Context final UriInfo uriInfo) {
		if (uriInfo != null) {
			LOGGER.debug(String.format("URI Info %s", uriInfo));
		}
		if (!ServletFileUpload.isMultipartContent(request)) {
			LOGGER.warn("Got invalid request, no multipart content");
			return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
		}

		IconUpload iconUpload = null;
		try {
			iconUpload = parseIconUpload();
		} catch (final Exception e) {
			LOGGER.warn("Failed to get icon upload from request", e);
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

		try {
			melmService.addIconAndFiles(iconUpload.getDisplayName(), iconUpload.getLargeIconFile());
		} catch (final MELMException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error in performAddIcon", e);
			}
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

		return buildRedirectResponse(uriInfo, "/rest/icons");
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_HTML)
	@Path("/libraries/import")
	public Response performImportLibrary(@Context final UriInfo uriInfo) {
		if (uriInfo != null) {
			LOGGER.debug(String.format("URI Info %s", uriInfo));
		}
		if (!ServletFileUpload.isMultipartContent(request)) {
			LOGGER.warn("Got invalid request, no multipart content");
			return Response.status(Status.BAD_REQUEST).entity("Invalid request, no multipart content").build();
		}

		LibraryUpload libraryUpload = null;
		try {
			libraryUpload = parseLibraryUpload();
		} catch (final Exception e) {
			LOGGER.warn("Failed to get library upload from request", e);
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

		try {
			final File zipFile = melmService.importLibrary(libraryUpload.getLibraryName(), libraryUpload.getVersion(), libraryUpload.getZipFile());
			final File libraryFolder = melmService.extractImportedLibrary(zipFile);
			if (libraryFolder != null) {
				final XMLSelectionPathParser libraryParser = melmService.validateAndParseImportedLibrary(libraryUpload.getLibraryName(), libraryUpload.getVersion());
				melmService.moveImportedIcons(libraryFolder);

				// FIXME Move this part in MELMService.
				// System.out.println(String.format("Library Id %s", libraryParser.getLibraryId()));
				// System.out.println(String.format("Library Display Name %s", libraryParser.getLibraryDisplayName()));
				// System.out.println(String.format("Library Type %s", libraryParser.getLibraryType()));
				// System.out.println(String.format("Library Version %s", libraryParser.getLibraryVersion()));
				// System.out.println(String.format("Library icon Path %s", libraryParser.getLibraryIconRelativePath()));
				// final Map<String, BaseNodeType> mapOfNodesByUniqueCode = libraryParser.getMapOfNodesByUniqueCode();
				// final Iterator<Map.Entry<String, BaseNodeType>> iterator = mapOfNodesByUniqueCode.entrySet().iterator();
				// while (iterator.hasNext()) {
				// final Map.Entry<String, BaseNodeType> mapEntry = iterator.next();
				// System.out.println(String.format("The key is: %s ,value is : %s", mapEntry.getKey(), mapEntry.getValue().getDescription()));
				// }
			}
		} catch (final MELMException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error in performImportLibrary", e);
			}
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

		return buildRedirectResponse(uriInfo, "/rest/libraries");
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
		String name = null;
		String version = null;
		File libraryZip = null;

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
				} else if (Params.VERSION.equalsIgnoreCase(fieldName)) {
					version = Streams.asString(stream);
				} else if (Params.FILE.equalsIgnoreCase(fieldName)) {
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
		if ((name == null) || (version == null) || (libraryZip == null)) {
			return null;
		}
		return new LibraryUpload(name, version, libraryZip);
	}

	private static Response buildRedirectResponse(@Context final UriInfo uriInfo, @Nonnull final String path) {
		final URI newURI = uriInfo.getBaseUriBuilder().path(path).build();
		return Response.seeOther(newURI).build();
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
		private final String libraryName;
		private final String version;
		private final File zipFile;

		public LibraryUpload(final String libraryName, final String version, final File zipFile) {
			this.libraryName = libraryName;
			this.version = version;
			this.zipFile = zipFile;
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