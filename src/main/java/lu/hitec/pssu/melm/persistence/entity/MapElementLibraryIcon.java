package lu.hitec.pssu.melm.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "map_element_library_icon")
@SequenceGenerator(name = "map_element_library_icon_seq", sequenceName = "map_element_library_icon_seq")
public class MapElementLibraryIcon {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "map_element_library_icon_seq")
	@Column(name = "id", nullable = false)
	private long id;

	@ManyToOne
	@ForeignKey(name = "fk_meli_to_ibrary")
	@JoinColumn(name = "library_id", nullable = false)
	private MapElementLibrary library;

	@ManyToOne
	@ForeignKey(name = "fk_meli_to_icon")
	@JoinColumn(name = "icon_id", nullable = false)
	private MapElementIcon icon;

	@Column(name = "icon_name_in_library", nullable = false, updatable = true)
	private String iconNameInLibrary;

	@Column(name = "icon_name_in_library", nullable = false, updatable = true)
	private String iconDescriptionInLibrary;

	public long getId() {
		return this.id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public MapElementLibrary getLibrary() {
		return this.library;
	}

	public void setLibrary(final MapElementLibrary library) {
		this.library = library;
	}

	public MapElementIcon getIcon() {
		return this.icon;
	}

	public void setIcon(final MapElementIcon icon) {
		this.icon = icon;
	}

	public String getIconNameInLibrary() {
		return this.iconNameInLibrary;
	}

	public void setIconNameInLibrary(final String iconNameInLibrary) {
		this.iconNameInLibrary = iconNameInLibrary;
	}

	public String getIconDescriptionInLibrary() {
		return this.iconDescriptionInLibrary;
	}

	public void setIconDescriptionInLibrary(final String iconDescriptionInLibrary) {
		this.iconDescriptionInLibrary = iconDescriptionInLibrary;
	}

}
