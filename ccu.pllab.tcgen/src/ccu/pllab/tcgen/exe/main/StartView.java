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
	private JButton okButton,resetButton,backButton,oclButton,umlButton;
	private static JComboBox testType;
	private static JComboBox criterionBox;
	private JLabel testLabel,oclLabel,criterionLabel,boundaryLabel;
	private ActionListener start_actionListener,ocl_actionListener,boundary_actionListener;
	private ActionListener noBoundary_actionListener,reset_actionListener,back_actionListener;
	private ActionListener output_actionListener,uml_actionListener;
	private ItemListener test_itemListener,criterion_itemListener;
	private WindowAdapter windowAdapter;
	private JLabel outputLabel,umlLabel;
	private static JRadioButton[] boundary;
	private static JTextField textField,output_textField,umlTextField;
	private static String text,umlText;
	private static String type="<請選擇>";
	private static File ocl,classUml;
	private static boolean selectedBoundary=false;
	public StartView()
	{
		this.startView=new JFrame("設定(Setting)");
		this.startView.setBounds(500,500,500,350);
		this.startView.setVisible(true);
		this.startView.setLayout(null); 
		this.setListener();
		
		this.testLabel=new JLabel();
		this.testLabel.setText("測試種類(Test Category)");
		this.testLabel.setBounds(0, 0, 200, 30);
		this.startView.add(this.testLabel);
		
		this.testType=new JComboBox(); 
		this.testType.setBounds(200,0,250,30); 
		String test[]={"<請選擇 select item>","黑箱測試(Black Testing)","白箱測試(white Testing)","類別層級測試(Class-Level Testing)"} ;
		for(int type=0 ;type<test.length ;type++)
		{
			this.testType.addItem(test[type]);
		} 
		this.testType.addItemListener(this.test_itemListener);
		this.startView.add(this.testType);
				
		this.criterionLabel=new JLabel();
		this.criterionLabel.setText("覆蓋標準(Criterion)");
		this.criterionLabel.setBounds(0, 40, 120, 30);
		this.startView.add(this.criterionLabel);
		
		this.criterionBox=new JComboBox(); 
		this.criterionBox.setBounds(200,40,250,30); 
		String criterion[]={"<請選擇 Select Item>","dc","dcc","mcc","dcdup","dccdup","mccdup"} ;
		for(int type=0 ;type<criterion.length ;type++)
		{
			this.criterionBox.addItem(criterion[type]);
		} 
		this.criterionBox.addItemListener(this.criterion_itemListener);
		this.startView.add(this.criterionBox);
		
		this.oclLabel=new JLabel();
		this.oclLabel.setText("OCL檔案(OCL File)");
		this.oclLabel.setBounds(0,80,120,30);
		this.startView.add(this.oclLabel);
		
		this.textField=new JTextField("<選取檔案 Select File>");
		this.textField.setBounds(200,80,231,30);
		this.startView.add(this.textField);
		
		this.oclButton=new JButton("...");
		this.oclButton.setBounds(430,80,20,30);
		this.oclButton.addActionListener(this.ocl_actionListener);	
		this.startView.add(this.oclButton);
		
		this.umlLabel=new JLabel();
		this.umlLabel.setText("類別圖(Class UML File)");
		this.umlLabel.setBounds(0,120,150,30);
		this.startView.add(this.umlLabel);
		
		this.umlTextField=new JTextField("<選取檔案 Select File>");
		this.umlTextField.setBounds(200,120,231,30);
		this.startView.add(this.umlTextField);
		
		this.umlButton=new JButton("...");
		this.umlButton.setBounds(430,120,20,30);
		this.umlButton.addActionListener(this.uml_actionListener);	
		this.startView.add(this.umlButton);
		
		this.outputLabel=new JLabel();
		this.outputLabel.setText("輸出資料夾(Output Folder)");
		this.outputLabel.setBounds(0,160,150,30);
		this.startView.add(this.outputLabel);
		
		this.output_textField=new JTextField("<選取資料夾 Select Folder>");
		this.output_textField.setBounds(200,160,231,30);
		this.startView.add(this.output_textField);
		
		this.oclButton=new JButton("...");
		this.oclButton.setBounds(430,160,20,30);
		this.oclButton.addActionListener(this.output_actionListener);	
		this.startView.add(this.oclButton);
		
		boundaryLabel=new JLabel();
		this.boundaryLabel.setText("界限分析(Boundary Analysis)");
		this.boundaryLabel.setBounds(0,200,200,30);
		this.startView.add(this.boundaryLabel);
		
		this.boundary=new JRadioButton[2];
		this.boundary[0] = new JRadioButton("是(Yes)",false);
		this.boundary[0].setBounds(200, 200, 80, 30);
		this.boundary[0].addActionListener(this.boundary_actionListener);
		this.startView.add(this.boundary[0]);
		this.boundary[1] = new JRadioButton("否(No)",false);
		this.boundary[1].setBounds(300, 200, 80, 30);	
		this.boundary[1].addActionListener(this.noBoundary_actionListener);	
		this.startView.add(this.boundary[1]);
		
		this.okButton=new JButton("確認(Start)");
		this.okButton.setBounds(0,240,100,40);
		this.okButton.addActionListener(this.start_actionListener);	
		this.startView.add(this.okButton);
		
		this.resetButton=new JButton("重設(Reset)");
		this.resetButton.setBounds(150,240,110,40);
		this.resetButton.addActionListener(this.reset_actionListener);	
		this.startView.add(this.resetButton);
		
		this.backButton=new JButton("上一頁(Back)");
		this.backButton.setBounds(310,240,120,40);
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
						JOptionPane.showMessageDialog(null, selectedFile.getName()+"匯入錯誤", "選取檔案", JOptionPane.ERROR_MESSAGE );
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
						JOptionPane.showMessageDialog(null, selectedFile.getName()+"匯入錯誤", "選取檔案", JOptionPane.ERROR_MESSAGE );
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
						CLG2Path path=new CLG2Path(clg.getCLGGraph(),clg.getInvCLG(),ast.getSymbolTable());
						JOptionPane.showMessageDialog(null, "執行成功", "結果", JOptionPane.INFORMATION_MESSAGE );
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
			StartView.textField.setText("<尚未選擇檔案>");
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
		                   "確定要結束程式嗎?",
		                   "確認訊息",
		                   JOptionPane.YES_NO_OPTION,
		                   JOptionPane.WARNING_MESSAGE);
		        if (result==JOptionPane.YES_OPTION) {startView.dispose();}
		        }    
		      };
	}
}
