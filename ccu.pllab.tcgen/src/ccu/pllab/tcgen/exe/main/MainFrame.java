package ccu.pllab.tcgen.exe.main;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
public class MainFrame{
	private JFrame window;
	private JButton clg;
	private JButton ast;
	private JButton test;
	//private JButton exit;
	private ActionListener clg_actionListener,ast_actionListener, tst_actionListener;//,exit_actionListener;
	private WindowAdapter windowAdapter;
	  
	public MainFrame()
	{
		this.window= new JFrame("Tcgen");
		this.window.setBounds(500,500,300,300);
		this.window.setVisible(true);
		this.window.setLayout(null); 
		this.setListener();
		
		this.clg =new JButton("CLG2CLP");
		this.clg.setBounds(90,30,100,40);
		this.clg.addActionListener(this.clg_actionListener);
		this.window.add(clg);
		
		this.ast = new JButton("OCL2CLP");
		this.ast.setBounds(90,80,100,40);
		this.ast.addActionListener(this.ast_actionListener);
		this.window.add(ast);
		
		this.test = new JButton("test");
		this.test.setBounds(90,150,100,40);
		this.test.addActionListener(this.tst_actionListener);
		this.window.add(test);
		/*this.exit = new JButton("End");
		this.exit.setBounds(90,130,100,40);
		this.exit.addActionListener(this.exit_actionListener);
		this.window.add(exit);*/
	
		
		this.window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.window.addWindowListener(this.windowAdapter);
	}
	
	public void setListener()
	{
		this.clg_actionListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StartView view=new StartView();
				window.dispose();
			}};
			
			this.ast_actionListener=new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					OCL2CLPFrame view=new OCL2CLPFrame();
					window.dispose();
				}};
		
				this.tst_actionListener=new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						StartView2 view=new StartView2();
						window.dispose();
					}};
		/*this.exit_actionListener=new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result=JOptionPane.showConfirmDialog(window,
		                "確定要結束程式嗎?",
		                "確認訊息",
		                JOptionPane.YES_NO_OPTION,
		                JOptionPane.WARNING_MESSAGE);
						if (result==JOptionPane.YES_OPTION) {window.dispose();}
			}
      };*/
      this.windowAdapter=new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	        int result=JOptionPane.showConfirmDialog(window,
	                   "確定要結束程式嗎?",
	                   "確認訊息",
	                   JOptionPane.YES_NO_OPTION,
	                   JOptionPane.WARNING_MESSAGE);
	        if (result==JOptionPane.YES_OPTION) {window.dispose();}
	        }    
	      };
	}

}
