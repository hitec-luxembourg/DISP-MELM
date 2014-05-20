package lu.hitec.pssu.melm.rest;

import javax.annotation.Nonnull;

import lu.hitec.pssu.melm.persistence.entity.MapElementLibrary;

public class CloneLibraryModel {

  private final String error;

  private final MapElementLibrary library;

  public CloneLibraryModel(@Nonnull final MapElementLibrary library) {
    assert library != null : "library is null";
    this.library = library;
    error = null;
  }

  public CloneLibraryModel(@Nonnull final MapElementLibrary library, @Nonnull final String error) {
    assert library != null : "library is null";
    assert error != null : "error is null";
    assert error.length() != 0 : "error is empty";
    this.library = library;
    this.error = error;
  }

  public String getError() {
    return error;
  }

  @Nonnull
  public MapElementLibrary getLibrary() {
    return library;
  }

}
