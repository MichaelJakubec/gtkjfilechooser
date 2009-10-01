package eu.kostia.gtkjfilechooser.xbel;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ISO8601XmlAdapter extends XmlAdapter<String, Date> {

	private ISO8601DateFormat fmt = new ISO8601DateFormat();

	@Override
	public String marshal(Date date) throws Exception {
		return fmt.format(date);
	}

	@Override
	public Date unmarshal(String v) throws Exception {
		return fmt.parse(v);
	}

}
