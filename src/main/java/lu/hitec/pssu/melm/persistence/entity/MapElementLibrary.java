package lu.hitec.pssu.melm.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "map_element_library")
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

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public void setMajorVersion(final int majorVersion) {
		this.majorVersion = majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public void setMinorVersion(final int minorVersion) {
		this.minorVersion = minorVersion;
	}

}
