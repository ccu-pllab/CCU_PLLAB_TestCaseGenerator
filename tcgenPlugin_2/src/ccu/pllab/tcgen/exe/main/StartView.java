package ccu.pllab.tcgen.exe.main;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import ccu.pllab.tcgen.AbstractCLG.CLGCriterionTransformer;
import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.transform.AST2CLG;
import ccu.pllab.tcgen.transform.CLG2Path;
import ccu.pllab.tcgen.transform.OCL2AST;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class StartView{
	private JFrame startView;
	private JButton okButton,resetButton,backButton,oclButton,umlButton, sdButton;
	private static JComboBox testType;
	private static JComboBox criterionBox;
	private JLabel testLabel,oclLabel,criterionLabel,boundaryLabel;
	private ActionListener start_actionListener,ocl_actionListener,boundary_actionListener;
	private ActionListener noBoundary_actionListener,reset_actionListener,back_actionListener;
	private ActionListener output_actionListener,uml_actionListener, sd_actionListener;
	private ItemListener test_itemListener,criterion_itemListener;
	private WindowAdapter windowAdapter;
	private JLabel outputLabel,umlLabel, sdLabel;
	private static JRadioButton[] boundary;
	private static JTextField textField,output_textField,umlTextField, sdTextField;
	private static String text,umlText;
	private static String type="<請選擇>";
	private static File ocl,classUml;
	private static boolean selectedBoundary=false;
	public StartView()
	{
		this.startView=new JFrame("Setting");
		this.startView.setBounds(500,500,500,380);
		this.startView.setVisible(true);
		this.startView.setLayout(null); 
		this.setListener();
		
		this.testLabel=new JLabel();
		this.testLabel.setText("Test Category");
		this.testLabel.setBounds(0, 0, 200, 30);
		this.startView.add(this.testLabel);
		
		this.testType=new JComboBox(); 
		this.testType.setBounds(200,0,250,30); 
		String test[]={"<Please Select Item>","Black Box Testing","White Box Testing","Class-Level Testing"} ;
		for(int type=0 ;type<test.length ;type++)
		{
			this.testType.addItem(test[type]);
		} 
		this.testType.addItemListener(this.test_itemListener);
		this.startView.add(this.testType);
				
		this.criterionLabel=new JLabel();
		this.criterionLabel.setText("Criterion");
		this.criterionLabel.setBounds(0, 40, 120, 30);
		this.startView.add(this.criterionLabel);
		
		this.criterionBox=new JComboBox(); 
		this.criterionBox.setBounds(200,40,250,30); 
		String criterion[]={"<Please Select Item>","dc","dcc","mcc","dcdup","dccdup","mccdup"} ;
		for(int type=0 ;type<criterion.length ;type++)
		{
			this.criterionBox.addItem(criterion[type]);
		} 
		this.criterionBox.addItemListener(this.criterion_itemListener);
		this.startView.add(this.criterionBox);
		
		this.oclLabel=new JLabel();
		this.oclLabel.setText("OCL File");
		this.oclLabel.setBounds(0,80,120,30);
		this.startView.add(this.oclLabel);
		
		this.textField=new JTextField("<Please Select File>");
		this.textField.setBounds(200,80,231,30);
		this.startView.add(this.textField);
		
		this.oclButton=new JButton("...");
		this.oclButton.setBounds(430,80,20,30);
		this.oclButton.addActionListener(this.ocl_actionListener);	
		this.startView.add(this.oclButton);
		
		this.umlLabel=new JLabel();
		this.umlLabel.setText("Class diagram UML File");
		this.umlLabel.setBounds(0,120,150,30);
		this.startView.add(this.umlLabel);
		
		this.umlTextField=new JTextField("<Please Select File>");
		this.umlTextField.setBounds(200,120,231,30);
		this.startView.add(this.umlTextField);
		
		this.umlButton=new JButton("...");
		this.umlButton.setBounds(430,120,20,30);
		this.umlButton.addActionListener(this.uml_actionListener);	
		this.startView.add(this.umlButton);
		
//		新加入State diagram的輸入欄位，目前先不具有實際作用
		this.sdLabel=new JLabel();
		this.sdLabel.setText("State diagram UML File");
		this.sdLabel.setBounds(0,160,150,30);
		this.startView.add(this.sdLabel);
		
		this.sdTextField=new JTextField("<Please Select File>");
		this.sdTextField.setBounds(200,160,231,30);
		this.startView.add(this.sdTextField);
		
		this.sdButton=new JButton("...");
		this.sdButton.setBounds(430,160,20,30);
		this.sdButton.addActionListener(this.sd_actionListener);	
		this.startView.add(this.sdButton);
//		到這
		
		this.outputLabel=new JLabel();
		this.outputLabel.setText("Output Folder");
		this.outputLabel.setBounds(0,200,150,30);
		this.startView.add(this.outputLabel);
		
		this.output_textField=new JTextField("<Please Select Folder>");
		this.output_textField.setBounds(200,200,231,30);
		this.startView.add(this.output_textField);
		
		this.oclButton=new JButton("...");
		this.oclButton.setBounds(430,200,20,30);
		this.oclButton.addActionListener(this.output_actionListener);	
		this.startView.add(this.oclButton);
		
		boundaryLabel=new JLabel();
		this.boundaryLabel.setText("Boundary Analysis");
		this.boundaryLabel.setBounds(0,240,200,30);
		this.startView.add(this.boundaryLabel);
		
		this.boundary=new JRadioButton[2];
		this.boundary[0] = new JRadioButton("Yes",false);
		this.boundary[0].setBounds(200, 240, 80, 30);
		this.boundary[0].addActionListener(this.boundary_actionListener);
		this.startView.add(this.boundary[0]);
		this.boundary[1] = new JRadioButton("No",false);
		this.boundary[1].setBounds(300, 240, 80, 30);	
		this.boundary[1].addActionListener(this.noBoundary_actionListener);	
		this.startView.add(this.boundary[1]);
		
		this.okButton=new JButton("Start");
		this.okButton.setBounds(0,280,100,40);
		this.okButton.addActionListener(this.start_actionListener);	
		this.startView.add(this.okButton);
		
		this.resetButton=new JButton("Reset");
		this.resetButton.setBounds(150,280,110,40);
		this.resetButton.addActionListener(this.reset_actionListener);	
		this.startView.add(this.resetButton);
		
		this.backButton=new JButton("Back");
		this.backButton.setBounds(310,280,120,40);
		this.backButton.addActionListener(this.back_actionListener);	
		this.startView.add(this.backButton);
				
		this.startView.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.startView.addWindowListener(this.windowAdapter);
	}
	
	public void setListener()
	{
		this.ocl_actionListener=new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser=new JFileChooser();
				if( fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
				{
					File selectedFile=fileChooser.getSelectedFile();;
					if(selectedFile.getName().contains("ocl"))
					{
						StartView.text=selectedFile.getName();
						StartView.textField.setText(StartView.text);
						StartView.ocl=selectedFile;
					}
					else
					{
						JOptionPane.showMessageDialog(null, selectedFile.getName()+"Error Import", "Please Select File", JOptionPane.ERROR_MESSAGE );
					}
				}
			}
      };
      
      this.uml_actionListener=new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser=new JFileChooser();
				if( fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
				{
					File selectedFile=fileChooser.getSelectedFile();;
					if(selectedFile.getName().contains("uml"))
					{
						StartView.umlText=selectedFile.getName();
						StartView.umlTextField.setText(StartView.umlText);
						StartView.classUml=selectedFile;
					}
					else
					{
						JOptionPane.showMessageDialog(null, selectedFile.getName()+"Error Import", "Please Select File", JOptionPane.ERROR_MESSAGE );
					}
				}
			}
    };
      
      this.output_actionListener=new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser=new JFileChooser();
				 fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if( fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
				{
					File selectedFile=fileChooser.getSelectedFile();
					Main.output_folder_path=selectedFile.getAbsolutePath();
					StartView.output_textField.setText(Main.output_folder_path);
				}
			}
    };  
			
      this.start_actionListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					startView.dispose();
					AST2CLG clg=null;
					OCL2AST ast;
					try {
						ast = new OCL2AST(StartView.ocl,StartView.classUml);
						ast.getAbstractSyntaxTree().toGraphViz();
						Main.ast=ast.getAbstractSyntaxTree();
						clg=new AST2CLG(Main.ast);
						CLG2Path path=new CLG2Path(clg.getCLGGraph(),clg.getInvCLG(),ast.getSymbolTable(),classUml);
						JOptionPane.showMessageDialog(null, "Execution Succeed", "Result", JOptionPane.INFORMATION_MESSAGE );
						Main.invCLP="";
					//	MainFrame mainFrame=new MainFrame();
					} catch (Exception e1) {
						e1.printStackTrace();
					}	
			}
      };
      
      this.boundary_actionListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(StartView.boundary[0].isSelected()) {
					Main.boundary_analysis=true;
					StartView.boundary[1].setSelected(false);
					StartView.selectedBoundary=true;
				}
				
			}
    };
      
    this.noBoundary_actionListener=new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			 if(StartView.boundary[1].isSelected()) 
			{
				Main.boundary_analysis=false;
				StartView.boundary[0].setSelected(false);
				StartView.selectedBoundary=true;
			}
		}
    };
    
    this.reset_actionListener=new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) {
			StartView.ocl=null;
			StartView.testType.setSelectedIndex(0);
			StartView.criterionBox.setSelectedIndex(0);
			StartView.textField.setText("<No File Selected>");
			StartView.selectedBoundary=false;
			Main.criterion=null;
			StartView.boundary[0].setSelected(false);
			StartView.boundary[1].setSelected(false);
		}
  };
  this.back_actionListener=new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) {
			MainFrame mainFrame=new MainFrame();
			startView.dispose();
		}
	};
    
      this.test_itemListener = new ItemListener() {
	      public void itemStateChanged(ItemEvent itemEvent) {
	        StartView.type=""+itemEvent.getItem();
	      }
	    };
      
      this.criterion_itemListener = new ItemListener() {
	      public void itemStateChanged(ItemEvent itemEvent) {
	        switch(""+itemEvent.getItem())
	        {
	        	case "dc":
        		Main.criterion=Criterion.dc;
        			break;
	        	case "dcc":
	        		Main.criterion=Criterion.dcc;
	        		break;
	        	case "mcc":
	        		Main.criterion=Criterion.mcc;
	        		break;
	        	case "dcdup":
	        		Main.criterion=Criterion.dcdup;
	        		break;
	        	case "dccdup":
	        		Main.criterion=Criterion.dccdup;
	        		break;
	        	case "mccdup":
	        		Main.criterion=Criterion.mccdup;
	        		break;
	        }
	      }
	    };
	    
	    this.windowAdapter= new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		        int result=JOptionPane.showConfirmDialog(startView,
		                   "Are you sure to end the program?",
		                   "Exit",
		                   JOptionPane.YES_NO_OPTION,
		                   JOptionPane.WARNING_MESSAGE);
		        if (result==JOptionPane.YES_OPTION) {startView.dispose();}
		        }    
		      };
	}
}
