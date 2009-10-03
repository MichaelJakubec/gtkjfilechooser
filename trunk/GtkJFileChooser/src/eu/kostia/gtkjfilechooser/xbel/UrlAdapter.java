package eu.kostia.gtkjfilechooser.xbel;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import eu.kostia.gtkjfilechooser.UrlUtil;

public class UrlAdapter extends XmlAdapter<String, String> {

	@Override
	public String marshal(String str) throws Exception {
		return UrlUtil.encode(str);
	}

	@Override
	public String unmarshal(String str) throws Exception {
		return UrlUtil.decode(str);
	}

}
