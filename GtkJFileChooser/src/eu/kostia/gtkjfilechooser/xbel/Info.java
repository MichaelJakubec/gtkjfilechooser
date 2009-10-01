package eu.kostia.gtkjfilechooser.xbel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"metadata"
})
@XmlRootElement(name = "info")
public class Info {

	@XmlElement(required = true)
	protected List<Metadata> metadata;

	/**
	 * Gets the value of the metadata property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the metadata property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getMetadata().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link Metadata }
	 * 
	 * 
	 */
	public List<Metadata> getMetadata() {
		if (metadata == null) {
			metadata = new ArrayList<Metadata>();
		}
		return this.metadata;
	}

}
