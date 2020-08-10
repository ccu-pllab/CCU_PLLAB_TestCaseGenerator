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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UmlTransformer {
	
	public UmlTransformer() {
		
	}
	
	public File transform(File uml) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		File umlOld = new File(uml.getParent()+"\\"+uml.getName().substring(0,uml.getName().length()-4)+"(old).uml");
		System.out.println(umlOld.getAbsolutePath());
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		FileInputStream  xmlInputStream = new FileInputStream(uml);
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document document = builder.parse(xmlInputStream);
		document.setXmlStandalone(true);
		//更改root為之前版本的uml:Package標籤，設定rootNode的attribute
		Element root = document.getDocumentElement();
		Node rootNew = document.renameNode(root, "http://www.eclipse.org/uml2/5.0.0/UML", "uml:Package");
		NodeList rootElement = document.getElementsByTagName("uml:Package");
		rootElement.item(0).getAttributes().getNamedItem("name").setNodeValue("tcgen");
		root.removeAttribute("viewpoint");
		//rootElement.item(0).getAttributes().getNamedItem("xmlns:xmi").setNodeValue("http://www.omg.org/spec/XMI/2.1");
		//rootElement.item(0).getAttributes().getNamedItem("xmi:version").setNodeValue("2.1");
		//刪除ownedAttribute中的upperValue和lowerValue子節點
		NodeList rootsChild = rootNew.getChildNodes();
		int len = rootsChild.getLength();
		Element newChild = document.createElement("type");
		newChild.setAttribute("href", "pathmap://UML_LIBRARIES/JavaPrimitiveTypes.library.uml#int");
		newChild.setAttribute("xmi:type", "uml:PrimitiveType");
		System.out.println(rootsChild.getLength());
		for (int i = len - 1; i >= 0; i--) {
			if (rootsChild.item(i).getNodeName() == "packagedElement") {
				for (int j = 0; j < rootsChild.item(i).getAttributes().getLength(); j++) {
					if (rootsChild.item(i).getAttributes().item(j).getNodeValue().matches("ArrayList")) {
//						rootNew.removeChild(rootsChild.item(i));
//						rootNew.removeChild(rootsChild.item(i-1));
//						i--;
						document.renameNode(rootsChild.item(i), "","type");
						break;
					}
				}
			}
		}
//		len = rootsChild.getLength();
//		for(int k = len - 1; k >= 0; k--) {
//			if ((rootsChild.item(k).getNodeName() == "packagedElement")&&(rootsChild.item(k).getAttributes().getNamedItem("name").getNodeValue().equals("UnboundedStack"))) {
//				for (int s = 0; s < rootsChild.item(k).getChildNodes().getLength(); s++) {
//					if (!rootsChild.item(k).getChildNodes().item(s).getNodeName().equals("#text")) {
//						System.out.println(rootsChild.item(k).getChildNodes().item(s).getNodeName());
//						if (rootsChild.item(k).getChildNodes().item(s).getAttributes().getNamedItem("name")
//								.getNodeValue().equals("data")) {
//							System.out.println("in");
//							rootsChild.item(k).getChildNodes().item(s).getAttributes().removeNamedItem("type");
//							rootsChild.item(k).getChildNodes().item(s).appendChild(newChild);
//							break;
//						}
//					}
//				}
//			}
//		}
		NodeList ownedAttribute = document.getElementsByTagName("ownedAttribute");
		for(int i=0;i<ownedAttribute.getLength();i++) {
			Element element = (Element) ownedAttribute.item(i);
			if(element.hasAttribute("xmi:type")) {
				 element.removeAttribute("xmi:type");
			}
			NodeList ownedAttributeChild = ownedAttribute.item(i).getChildNodes();
			try {
				for(int j=0;j<ownedAttributeChild.getLength();j++) {
					if(ownedAttributeChild.item(j).getNodeName().equals("upperValue")||
						ownedAttributeChild.item(j).getNodeName().equals("lowerValue")	
					) {
						ownedAttribute.item(i).removeChild(ownedAttributeChild.item(j));
					}
				}
			} catch (Exception e ) {
				continue;
			}
		}
		//刪除ownedOperation節點
		NodeList umlChildNodes = rootNew.getChildNodes();
		NodeList uml2LayerChildNodes;
		System.out.println("------------------------------------");
		for(int i=0;i<umlChildNodes.getLength();i++) {
			Node umlInforNode = umlChildNodes.item(i);
			if(umlInforNode.getNodeType() == Node.ELEMENT_NODE) {
				uml2LayerChildNodes = umlInforNode.getChildNodes();
				if(umlInforNode.getNodeName().equals("elementImport")||
				umlInforNode.getNodeName().equals("packageImport")) {
					//System.out.println("xml檔案的子節點為："+umlInforNode.getNodeName());
					rootNew.removeChild(umlInforNode);
				}
				for(int j=0;j<uml2LayerChildNodes.getLength();j++) {
					Node uml2LayerInforNode = uml2LayerChildNodes.item(j);
					if(uml2LayerInforNode.getNodeName().equals("ownedOperation")) {
						umlInforNode.removeChild(uml2LayerInforNode);
					}						
				}
			}
		}
		
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(umlOld);
		transformer.transform(domSource, streamResult);
		return umlOld;
	}
}
