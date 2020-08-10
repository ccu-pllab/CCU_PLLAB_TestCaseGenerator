package ccu.pllab.tcgen.DataWriter;

 


import java.io.*;
import java.util.ArrayList;
import java.util.List;

import ccu.pllab.tcgen.exe.main.Main;
import tcgenplugin_2.handlers.BlackBoxHandler;

public class DataWriter {
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;

	public static String output_folder_path = Main.output_folder_path;
	public static String Clg_output_path ;
	public static String testPath_output_path ;
	public static String testCons_output_path ;
	public static String testData_output_path ;
	
	public static void writeInfo(Object data, String fileName, String fileType, String fileDes, String folderName) {
		
		
		try {			
			File f = new File(fileDes+"\\"+folderName);
			
			if(!f.exists()){
				f.mkdirs();
			}
			
			fw = new FileWriter(fileDes + "\\" + folderName + "\\" + fileName + "." + fileType, false);
			bw = new BufferedWriter(fw);
			if (data instanceof List) {
				for (int i = 0; i < ((List) data).size(); i++) {
					bw.write(data + "\n");
				}
			}else{
				bw.write(data + "\n");
			}

		} catch (IOException e) {
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
			}
		}
	}

	public static void writeInfo(String data, String fileName, String fileType, String fileDes) {
		try {
			//System.out.println("fileName  " + fileDes + fileName);
			// fw = new FileWriter(outputPath+fileName+"."+fileType, false); //
			// �w�]�Ofals~�p��Ƨ�������Ʒ|�R�����ƦL�s��
			File folder = new File(fileDes);
			
			if(!folder.exists()){
				folder.mkdirs();
			}
			File f= new File(fileDes + fileName + "." + fileType);
			fw = new FileWriter(f, false);
			bw = new BufferedWriter(fw);
			// for(int i = 0; i < data.length ; i++){
			bw.write(data + "\n"); // �[�W"\n"����r����"
			// bw.newLine(); //�Y�n�]�w�����s����ƱN�W��"\n"�R���ϥγo��
			// }
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
			}
		}
	}

	public static void writeInfo(ArrayList<String> data, String fileName, String fileType, String fileDes) {
		try {
			// System.out.println("ArrayList String");
			fw = new FileWriter(fileDes + fileName + "." + fileType, false); // �w�]�Ofals~�p��Ƨ�������Ʒ|�R�����ƦL�s��
			// fw = new FileWriter(outputPath+fileName+"."+fileType, false);
			bw = new BufferedWriter(fw);
			System.out.println("ArrayList String");
			for (int i = 0; i < data.size(); i++) {
				bw.write(data.get(i) + "\n"); // �[�W"\n"����r����"
				// bw.newLine(); //�Y�n�]�w�����s����ƱN�W��"\n"�R���ϥγo��
			}
		} catch (IOException e) {
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
			}
		}
	}
	
	public static void initOutputPath() {
		String[] paths = new String[]{DataWriter.Clg_output_path, DataWriter.testPath_output_path, DataWriter.testCons_output_path, DataWriter.testData_output_path};
		for(String p : paths) {
			File f = new File(p);
			if (!f.exists()) {
				f.mkdirs();
			}
		}	
	}

}