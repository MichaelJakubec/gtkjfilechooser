package eu.kostia.gtkjfilechooser.xbel;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"title",
		"info",
		"desc"
})
@XmlRootElement(name = "bookmark")
public class Bookmark {

	@XmlAttribute
	@XmlID
	protected String id;

	@XmlAttribute
	@XmlJavaTypeAdapter(ISO8601XmlAdapter.class)
	protected Date added;

	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(UrlAdapter.class)
	protected String href;

	@XmlAttribute
	@XmlJavaTypeAdapter(ISO8601XmlAdapter.class)
	protected Date visited;

	@XmlAttribute
	@XmlJavaTypeAdapter(ISO8601XmlAdapter.class)
	protected Date modified;

	protected String title;

	protected Info info;

	protected String desc;

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
	public Date getAdded() {
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
	public void setAdded(Date value) {
		this.added = value;
	}

	/**
	 * Gets the value of the href property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getHref() {
		return href;
	}

	/**
	 * Sets the value of the href property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setHref(String value) {
		this.href = value;
	}

	/**
	 * Gets the value of the visited property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public Date getVisited() {
		return visited;
	}

	/**
	 * Sets the value of the visited property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setVisited(Date value) {
		this.visited = value;
	}

	/**
	 * Gets the value of the modified property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * Sets the value of the modified property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setModified(Date value) {
		this.modified = value;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Bookmark [href=");
		builder.append(href);
		builder.append(", modified=");
		builder.append(modified);
		builder.append("]");
		return builder.toString();
	}

}
