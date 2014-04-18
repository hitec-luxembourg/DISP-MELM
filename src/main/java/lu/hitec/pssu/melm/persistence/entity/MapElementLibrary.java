package lu.hitec.pssu.melm.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "map_element_library", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "major_version", "minor_version" }))
@SequenceGenerator(name = "map_element_library_seq", sequenceName = "map_element_library_seq")
public class MapElementLibrary {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "map_element_library_seq")
	@Column(name = "id", nullable = false)
	private long id;

	@Column(name = "name", nullable = false, updatable = false)
	private String name;

	@Column(name = "major_version", nullable = false, updatable = false)
	private int majorVersion;

	@Column(name = "minor_version", nullable = false, updatable = false)
	private int minorVersion;

	public MapElementLibrary(final String name, final int majorVersion, final int minorVersion) {
		super();
		this.name = name;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
	}

	public MapElementLibrary() {
		super();
	}

	public long getId() {
		return this.id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getMajorVersion() {
		return this.majorVersion;
	}

	public void setMajorVersion(final int majorVersion) {
		this.majorVersion = majorVersion;
	}

	public int getMinorVersion() {
		return this.minorVersion;
	}

	public void setMinorVersion(final int minorVersion) {
		this.minorVersion = minorVersion;
	}

}
