package eu.kostia.gtkjfilechooser.xbel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "metadata")
public class Metadata {

	@XmlAttribute(required = true)
	protected String owner;

	/**
	 * Gets the value of the owner property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Sets the value of the owner property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setOwner(String value) {
		this.owner = value;
	}

}
