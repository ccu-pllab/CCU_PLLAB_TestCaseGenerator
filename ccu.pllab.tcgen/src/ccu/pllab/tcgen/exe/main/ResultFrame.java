package ccu.pllab.tcgen.exe.main;
  
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ResultFrame {
	JFrame result;
	JButton main;
	public ResultFrame(String photo) 
	{
		result=new JFrame("result");
		result. setBounds(0, 0, 1000, 1000);  
	    result.setVisible(true);
		result.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		result.addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	       result.dispose();
	        }    
	      });
		
		
	}
}
