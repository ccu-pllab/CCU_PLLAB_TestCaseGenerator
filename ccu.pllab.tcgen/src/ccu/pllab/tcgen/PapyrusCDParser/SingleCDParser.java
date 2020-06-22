/*
 * 20180331 黎怡伶
 * 取得用Papyrus繪製成的類別圖資訊
 */


package ccu.pllab.tcgen.PapyrusCDParser;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class SingleCDParser {

	private File cd ;  // class diagram uml file
	private Document doc ;
	private ArrayList<Document> ref = null;  // 用來放其自訂型别需參考的其他類別圖
	private ArrayList<ClassInfo> classList ; // 用來放主要
	 
	
	// constructor ===========================================================================
	// 單一圖
	public SingleCDParser(File cd) {
		this.cd = cd ;
		init();
	} 
	// constructor ===========================================================================
	
	
	
	// 外部拿取資料 method =======================================================================
	// Get package name
	public String getPkgName() {
		return doc.getDocumentElement().getAttribute("name");   // XML根節點
	}
	
	// Get Class List
	public ArrayList<ClassInfo> getClassList() {
		return classList;
	}
	// 外部拿取資料 method =======================================================================
	
	
	
	/*
	 * 以下為Parse主要及所需method
	 * 
	 * 1. 將類別圖轉成dom樹狀結構 : init(), initRef()
	 * 2. 分析並取得類別圖裡的資訊 : Parse()
	 */ 
	
	// NO.1 =====================================================================================
	// 把主要要測試的類別的類別圖轉成dom
	private void init() {
		// TODO Auto-generated method stub
		try {
			// 定義XML DOM parser解析器
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			// 建立DOM document
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			// 引入xml
			doc = dBuilder.parse(this.cd);
			
			// 針對xml文檔的元素做normalize
		    doc.getDocumentElement().normalize();
		    
		} catch (Exception e) {
			 e.printStackTrace();
	    }
	} // init()
	
	
	// 把參考用的uml圖list 轉成dom
	private void initRef( ArrayList<File> rList ) {
		try {
			this.ref = new ArrayList<Document>();
			
			// 定義XML DOM parser解析器
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			// 建立DOM document
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			for(int i = 0 ; i < rList.size() ; i++ ) {
			    doc = dBuilder.parse(rList.get(i));       // 引入xml
			    doc.getDocumentElement().normalize();   // 針對xml文檔的元素做normalize
			    this.ref.add(doc) ;
			} // for
		    
		} catch (Exception e) {
			 e.printStackTrace();
	    }
	} // initRef()
	// NO.1 =====================================================================================
	
	
	
	// NO.2 =====================================================================================
	// 取得類別圖的所有class的資訊
	public void Parse() {
		classList = new ArrayList<ClassInfo>() ;
		
		// 取得所有class
		NodeList nList = doc.getElementsByTagName("packagedElement");
		
		for (int i = 0; i < nList.getLength(); i++) {
			ArrayList<VariableInfo> property_List = null ;
			ArrayList<OperationInfo> operation_List = null ;
			ClassInfo c = new ClassInfo();
			
            Node node = nList.item(i);                // 第i個class
            if (node.getNodeType() == Node.ELEMENT_NODE) {  
                Element e = (Element) node;
                c.setName(e.getAttribute("name"));    // class name
                c.setID(e.getAttribute("xmi:id"));    // class id
                
                // ---------- Property ----------
                NodeList pList = e.getElementsByTagName("ownedAttribute");   // 取得此class的所有property
                if( pList.getLength() != 0 ) property_List = new ArrayList<VariableInfo>() ;
                
                // 取得此class裡的各個property的type & name
                for( int j = 0 ; j < pList.getLength() ; j++ ) {
                	Element child = (Element) pList.item(j);
                	VariableInfo p = new VariableInfo( getVarType(child), child.getAttribute("name"), 
                			                           child.getAttribute("xmi:id"), getAttrVisibility(child), 
                			                           c.getName() );
                	if (child.getElementsByTagName("lowerValue").item(0) != null) {
                		Element temp = (Element) child.getElementsByTagName("lowerValue").item(0);
                		if( temp.hasAttribute("value") ) p.setLowerValue(temp.getAttribute("value"));
                		else p.setLowerValue("0");
                	} // if
                	
                	if (child.getElementsByTagName("upperValue").item(0) != null) {
                		Element temp = (Element) child.getElementsByTagName("upperValue").item(0);
                		p.setUpperValue(temp.getAttribute("value"));
                	} // if
                	
                	property_List.add( p ) ;    // 把property加進list
                } // for
                // ---------- Property ----------
                
                
                // ---------- Operation ----------
                NodeList oList = e.getElementsByTagName("ownedOperation");   // 取得此class的所有operation
                if( oList.getLength() != 0 ) operation_List = new ArrayList<OperationInfo>() ;
                
                // 取得此class裡的各個operation的name, parameter, return type 
                for( int k = 0 ; k < oList.getLength() ; k++ ) {
                	OperationInfo method = new OperationInfo();
                	Element child = (Element) oList.item(k);
                	method.setName(child.getAttribute("name"));     // 取得method name
                	method.setID(child.getAttribute("xmi:id"));     // 取得method id
                	method.setVisibility(getAttrVisibility(child)); // 取得method visibility
                	method.setClassName(c.getName());               // 設定所屬類別
                	
                	NodeList parameter = child.getElementsByTagName("ownedParameter");  // 取得所有參數(包括return的變數)
                	
                	// method 沒有參數 , return type為void
                	if(parameter.getLength()== 0 ) {
                		VariableInfo rt = new VariableInfo("void", "", "", "", c.getName());
                		method.setReturnType(rt);
                	} // if
                	
                	// 取得method所有參數的name, type 以及 return type
                	else {
                	  ArrayList<VariableInfo> parameter_List = new ArrayList<VariableInfo>();
                      for( int p_i = 0 ; p_i < parameter.getLength() ; p_i++ ) {
                    	  Element eParameter = (Element) parameter.item(p_i);
                    	  
                          VariableInfo p = new VariableInfo( getVarType(eParameter), eParameter.getAttribute("name"), 
                          			                         eParameter.getAttribute("xmi:id"), "", c.getName() );
                          	
                          if (eParameter.getElementsByTagName("lowerValue").item(0) != null) {
                         	  Element temp = (Element) child.getElementsByTagName("lowerValue").item(0);
                       		  if( temp.hasAttribute("value") ) p.setLowerValue(temp.getAttribute("value"));
                       		  else p.setLowerValue("0");
                       	  } // if
                        	
                          if (eParameter.getElementsByTagName("upperValue").item(0) != null) {
                              Element temp = (Element) child.getElementsByTagName("upperValue").item(0);
                        	  p.setUpperValue(temp.getAttribute("value"));
                          } // if
                    	  // System.out.println(p.GetType()+" " + p.GetName());
                        	
                      	  // 參數
                      	  if ( ! eParameter.getAttribute("direction").equals("return"))
                      		  parameter_List.add( p ) ;    // 把parameter加進list
                    	  
                    	  // return variable
                    	  else method.setReturnType( p );
                      	  
                      } // for
                      
                      
                      if( parameter_List.size()!=0 )    // 表示method有參數
                    	  method.setParameter(parameter_List);
                	} // else
                	
                	operation_List.add( method ) ;    // 把operation加進list
                } // for
                // ---------- Operation ----------
                
                c.setProperties(property_List);     // class property
                c.setOperations(operation_List);    // class operation
            } // if
            
            classList.add(c);   // 把一個取得資訊後的classInfo加進class list
		} // for
		
	} // Parse()
	// NO.2 =====================================================================================
	
	
	
	// 於Parse()中所用到的method =================================================================
	
	// 取得property的type: 自建立的型別, 基本型別, 陣列
	private String getVarType(Element e) {
    	if(e.hasAttribute("type")) return e.getAttribute("type");
    	else {
    		Element et = (Element)e.getElementsByTagName("type").item(0);
    		String s = et.getAttribute("href");
    		s = s.substring(s.indexOf("#")+1, s.length());
    		return s;
    	}
    	
	}
	
	// 取得visibility, 若無設定此屬性, 則設為預設的"public"
	private String getAttrVisibility(Element e) {
    	if(e.hasAttribute("visibility")) return e.getAttribute("visibility");
    	else return "public" ;
	}
	

	// 判斷是否有return值
	private boolean hasReturn(NodeList list) {
		boolean result = false ;
		for(int i = 0 ; i < list.getLength() ; i++) {
			Element e = (Element) list.item(i);
			if ( e.hasAttribute("direction") && e.getAttribute("direction").equals("return") )
				return true;
		} // for
		
		return result;
	}
	
	
	// 取得type為array的property的size(lowerValue & upperValue)
	private String getSize(Element e) {
    	if(e.hasAttribute("type")) return e.getAttribute("type");
    	else {
    		Element et = (Element)e.getElementsByTagName("type").item(0);
    		String s = et.getAttribute("href");
    		s = s.substring(s.indexOf("#")+1, s.length());
    		return s;
    	}
    	
	}
	
	
	// 將變數的型別與自己建立的class的id做對應, 把id改成class的name
	// 如果型別是陣列要把在name後面加上[]                                   ****待改
	public void changeTypeStr() {
		
		for (int  i= 0 ; i < this.classList.size();i++){
		    // class local variable
            for(int j = 0 ; this.classList.get(i).getProperties() != null && 
            		j < this.classList.get(i).getProperties().size();j++) {
            	VariableInfo p = this.classList.get(i).getProperties().get(j);
            	p.setType(typeIDtoName(p.getType()));
            	
            	// if(p.getSize() != null) p.setType(p.getType()+"[]"); // size非空即為陣列
		    } // for
            

		    // class method 
            for(int j = 0 ; this.classList.get(i).getOperations() != null &&
            		j < this.classList.get(i).getOperations().size();j++){
            	OperationInfo o=this.classList.get(i).getOperations().get(j);
            	// class method parameter
            	for(int para_i = 0 ; o.getParameter() != null && 
            			para_i < o.getParameter().size();para_i++) {
            		VariableInfo parameter = o.getParameter().get(para_i);
            		parameter.setType(typeIDtoName(parameter.getType()));
            		
            		// if(parameter.getSize() != null) parameter.setType(parameter.getType()+"[]"); // size非空即為陣列
                } // for
            	
            	// return type
            	VariableInfo rt = o.getReturnType() ;
            	if(o.getReturnType()!=null)
            	rt.setType(typeIDtoName(rt.getType()));
            	
            	// *** if(o.getReturnType().getSize() != null) o.setType(p.getType()+"[]"); // size非空即為陣列
            }//for operation
		}
	} // changeTypeStr()
	
	
	// 如果是自定義的型別, 在類別圖裡type是以class ID的形式記錄
	// 所以要去將id轉換成相對應的型別名稱
	// 被changeTypeStr()呼叫
	private String typeIDtoName( String type ) {
		// System.out.println(classList.size());
		String s = type ;
		for(int k = 0 ; k < this.classList.size();k++ ) {
			// System.out.println((k+1) + this.classList.get(k).getID());
			
			if (type.equals(this.classList.get(k).getID())) {
				// System.out.println(type + "**"+this.classList.get(k).getID() + " "+ this.classList.get(k).getName());
				s = this.classList.get(k).getName();
			}
		} //for
		
		return s;
	} // typeIDtoName()
	
	
	public void parseRef() {
		NodeList list = doc.getElementsByTagName("packageImport");
		Node node = list.item(4);                // 第i個class
         if (node.getNodeType() == Node.ELEMENT_NODE) {  
             Element e = (Element) node;
             NodeList child = e.getElementsByTagName("importedPackage");
             Element ch = (Element) child.item(0);
             System.out.println(ch.getAttribute("href"));
		File f = new File(ch.getAttribute("href"));
		if( f.exists() ) System.out.println("O");
		else System.out.println("X");
         }
		
	}
	
	// 於Parse()中所用到的method =================================================================

	
	
	
	//    拿取資料範例
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		    // File uml = new File("C:/Users/p/Downloads/coffeeMachineClass.uml") ;
			File uml = new File("D:\\Eclipse_Code\\AA\\cal\\model2.uml") ;
			
		    SingleCDParser parser = new SingleCDParser(uml) ;
		    System.out.println("Package Name :" + parser.getPkgName());   // package name

		    parser.Parse();
		    parser.changeTypeStr();
		    parser.parseRef();

		    for( int i = 0 ; i < parser.getClassList().size() ; i++ ) {
		    	ClassInfo c = parser.getClassList().get(i);
			    System.out.println("Class Name: ") ;
		    	System.out.println((i+1) + " "+c.getName() + " " + c.getID() +"\n");
		    	
		    	System.out.println("Attributes: ");
		    	for(int j= 0; c.getProperties()!= null && j < c.getProperties().size();j++ ) {
		    		VariableInfo p = c.getProperties().get(j);
		    		System.out.println(p.getType() + " " + p.getName() + " " + p.getID() + " " + p.getVisibility() + " " + p.getClassName());
		    	}
		    	
		    	System.out.println("\nOperations: ");
		    	for(int k= 0; c.getOperations()!= null && k < c.getOperations().size();k++ ) {
		    		OperationInfo o = c.getOperations().get(k);
		    		System.out.println(o.getReturnType().getType() +" " + o.getName() + " " + o.getID() + " " + o.getVisibility()+ " " + o.getClassName());
		    		System.out.println("Parameter: ");
		    	    for(int index = 0 ;o.getParameter()!= null && index < o.getParameter().size();index++) {
			    		VariableInfo p = o.getParameter().get(index);
			    		System.out.println(p.getType() + " " + p.getName() + " " + p.getID() + " " + p.getVisibility()+ " " + p.getClassName());
		    	    }
		    	    System.out.println();
		    	}
		    	System.out.println("\n================================");
		    }

		} catch (Exception e) {
			 e.printStackTrace();
	    }
	}*/
	
	

}
