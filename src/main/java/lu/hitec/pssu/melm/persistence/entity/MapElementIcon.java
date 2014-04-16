package lu.hitec.pssu.melm.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "map_element_icon")
@SequenceGenerator(name = "map_element_icon_seq", sequenceName = "map_element_icon_seq")
public class MapElementIcon {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "map_element_icon_seq")
	@Column(name = "id", nullable = false)
	private long id;

	@Column(name = "pic_100px_md5", nullable = false, updatable = true)
	private String pic100pxMd5;

	@Column(name = "size_in_bytes", nullable = false, updatable = true)
	private int sizeInBytes;

	@Column(name = "path", nullable = false, updatable = true)
	private String path;

}
