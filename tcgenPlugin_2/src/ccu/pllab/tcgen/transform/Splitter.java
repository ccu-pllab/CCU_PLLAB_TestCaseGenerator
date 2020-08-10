package ccu.pllab.tcgen.transform;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Splitter {	
	File uml;
	public Splitter(File uml) {
		this.uml = uml;
	}
	public File split2SDuml() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		File umlSD = new File(uml.getParent()+"\\"+uml.getName().substring(0,uml.getName().length()-4)+"SD.uml");
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		FileInputStream  xmlInputStream = new FileInputStream(uml);
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.parse(xmlInputStream);
		document.setXmlStandalone(true);
		Element root = document.getDocumentElement();
		NodeList rootsChild = root.getChildNodes();
		//找出CD&SD的uml部分並刪除
		int len = root.getChildNodes().getLength();
		for (int i = len - 1; i >= 0; i--) {
			NamedNodeMap rootsChildAttr= rootsChild.item(i).getAttributes();
			if (rootsChild.item(i).getNodeName() == "packagedElement") {
				for (int j = 0; j < rootsChildAttr.getLength(); j++) {
					//System.out.println(rootsChild.item(i).getAttributes().item(j).getNodeValue());
					if (rootsChildAttr.item(j).getNodeValue().matches("uml:Class")) {
						root.removeChild(rootsChild.item(i));
						root.removeChild(rootsChild.item(i - 1));
						i -= 1;
						break;
					}
					if (rootsChildAttr.item(j).getNodeValue().matches(uml.getName().substring(0, uml.getName().length() - 4))) {
						root.removeChild(rootsChild.item(i));
						root.removeChild(rootsChild.item(i - 1));
						i -= 1;
						break;
					}
					
				}
			} else if (rootsChild.item(i).getNodeName() == "elementImport") {
				root.removeChild(rootsChild.item(i));
				root.removeChild(rootsChild.item(i - 1));
				i -= 1;
			} else if (rootsChild.item(i).getNodeName() == "packageImport") {
				root.removeChild(rootsChild.item(i));
				root.removeChild(rootsChild.item(i - 1));
				i -= 1;
			}
		}
		len = root.getChildNodes().getLength();
		for (int i = len - 1; i >= 0; i--) {
			NamedNodeMap rootsChildAttr= rootsChild.item(i).getAttributes();
			if (rootsChild.item(i).getNodeName() == "packagedElement") {
				for (int j = 0; j < rootsChildAttr.getLength(); j++) {
					if (rootsChildAttr.item(j).getNodeValue().matches(uml.getName().substring(0, uml.getName().length() - 4)+"I")) {
						rootsChildAttr.item(j).setNodeValue(uml.getName().substring(0, uml.getName().length() - 4));
					}
				}
			}
		}
		root.setAttribute("name", "RootElement");
		root.removeAttribute("viewpoint");
		//String oldName = root.getAttribute("name")+"SD";
		//root.setAttribute("name", oldName);
		//輸出到File
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(umlSD);
		transformer.transform(domSource, streamResult);
		return umlSD;
	}
	
	public File split2CDuml() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		// File umlMix = new File(uml.getParent()+"\\BoundedQueue.uml");
		File umlCD = new File(
				uml.getParent() + "\\" + uml.getName().substring(0, uml.getName().length() - 4) + "CD.uml");
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		FileInputStream xmlInputStream = new FileInputStream(uml);
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.parse(xmlInputStream);
		document.setXmlStandalone(true);
		Element root = document.getDocumentElement();
		NodeList rootsChild = root.getChildNodes();
		// 找出SD的uml部分並刪除
		int len = root.getChildNodes().getLength();
		for (int i = len - 1; i >= 0; i--) {
			if (rootsChild.item(i).getNodeName() == "packagedElement") {
				for (int j = 0; j < rootsChild.item(i).getAttributes().getLength(); j++) {
					//System.out.println(rootsChild.item(i).getAttributes().item(j).getNodeValue());
					if (rootsChild.item(i).getAttributes().item(j).getNodeValue().matches("uml:StateMachine")) {
						root.removeChild(rootsChild.item(i));
						root.removeChild(rootsChild.item(i - 1));
						i -= 1;
						break;
					}
				}
			} else if (rootsChild.item(i).getNodeName() == "elementImport") {
				root.removeChild(rootsChild.item(i));
				root.removeChild(rootsChild.item(i - 1));
				i -= 1;
				// System.out.println(i);
			} else if (rootsChild.item(i).getNodeName() == "packageImport") {
				root.removeChild(rootsChild.item(i));
				root.removeChild(rootsChild.item(i - 1));
				i -= 1;
				// System.out.println(i);
			}
		}

		//輸出到File
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(umlCD);
		transformer.transform(domSource, streamResult);
		return umlCD;
	}
}
