package eu.kostia.gtkjfilechooser.xbel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"title",
		"info",
		"desc",
		"bookmarks"
})
@XmlRootElement(name = "xbel")
public class Xbel {

	@XmlAttribute
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlID
	protected String id;
	@XmlAttribute
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String added;
	@XmlAttribute
	@XmlJavaTypeAdapter(NormalizedStringAdapter.class)
	protected String version;
	protected String title;
	protected Info info;
	protected String desc;
	@XmlElements({
		@XmlElement(name = "bookmark", type = Bookmark.class),
	})
	protected List<Bookmark> bookmarks;

	/**
	 * Gets the value of the id property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Gets the value of the added property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getAdded() {
		return added;
	}

	/**
	 * Sets the value of the added property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setAdded(String value) {
		this.added = value;
	}

	/**
	 * Gets the value of the version property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getVersion() {
		if (version == null) {
			return "1.0";
		} else {
			return version;
		}
	}

	/**
	 * Sets the value of the version property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setVersion(String value) {
		this.version = value;
	}

	/**
	 * Gets the value of the title property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the value of the title property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setTitle(String value) {
		this.title = value;
	}

	/**
	 * Gets the value of the info property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Info }
	 *     
	 */
	public Info getInfo() {
		return info;
	}

	/**
	 * Sets the value of the info property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link Info }
	 *     
	 */
	public void setInfo(Info value) {
		this.info = value;
	}

	/**
	 * Gets the value of the desc property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Sets the value of the desc property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setDesc(String value) {
		this.desc = value;
	}

	/**
	 * Gets the value of the bookmarks property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the bookmarks property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getBookmarkOrFolderOrAliasOrSeparator().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link Bookmark }
	 * 
	 * 
	 */
	public List<Bookmark> getBookmarks() {
		if (bookmarks == null) {
			bookmarks = new ArrayList<Bookmark>();
		}
		return this.bookmarks;
	}

}
