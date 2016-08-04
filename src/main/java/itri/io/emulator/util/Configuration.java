package itri.io.emulator.util;

import java.io.File;
import java.util.List;

import javax.naming.InvalidNameException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Configuration {
  private List<Element> properties;

  @SuppressWarnings("unchecked")
  public Configuration(String path) throws DocumentException {
    SAXReader reader = new SAXReader();
    Document document = reader.read(new File(path));
    Element root = document.getRootElement();
    properties = root.elements("property");
  }

  public String get(String name) throws InvalidNameException {
    Element element = searchPropertyName(name);
    return element.element("value").getText();
  }

  public String[] getStrings(String name) throws InvalidNameException {
    Element element = searchPropertyName(name);
    String[] values = element.element("value").getText().split(",");
    return values;
  }

  public int getInt(String name, int defaultValue) throws InvalidNameException {
    Element element = searchPropertyName(name);
    String strValue = element.element("value").getText();
    return (strValue == null || strValue.length() == 0) ? defaultValue : Integer.valueOf(strValue);
  }

  private Element searchPropertyName(String name) throws InvalidNameException {
    for (Element element : properties) {
      if (element.element("name").getText().equals(name)) {
        return element;
      }
    }
    throw new InvalidNameException("Can't find the property with name: " + name
        + ". Please check your spell in configuration file.");
  }
}
