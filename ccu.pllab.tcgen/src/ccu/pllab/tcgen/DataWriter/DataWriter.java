package ccu.pllab.tcgen.DataWriter;

 


import java.io.*;
import java.util.ArrayList;
import java.util.List;

import ccu.pllab.tcgen.exe.main.Main;


public class DataWriter {
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;

	public static String output_folder_path = Main.output_folder_path;
	
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
			System.out.println("fileName  " + fileDes + fileName);
			// fw = new FileWriter(outputPath+fileName+"."+fileType, false); //
			// 預設是fals~如資料夾中有資料會刪除原資料印新的
			fw = new FileWriter(fileDes + fileName + "." + fileType, false);
			bw = new BufferedWriter(fw);
			// for(int i = 0; i < data.length ; i++){
			bw.write(data + "\n"); // 加上"\n"讓文字換行"
			// bw.newLine(); //若要設定成整行新的資料將上方"\n"刪除使用這行
			// }
		} catch (IOException e) {
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
			fw = new FileWriter(fileDes + fileName + "." + fileType, false); // 預設是fals~如資料夾中有資料會刪除原資料印新的
			// fw = new FileWriter(outputPath+fileName+"."+fileType, false);
			bw = new BufferedWriter(fw);
			System.out.println("ArrayList String");
			for (int i = 0; i < data.size(); i++) {
				bw.write(data.get(i) + "\n"); // 加上"\n"讓文字換行"
				// bw.newLine(); //若要設定成整行新的資料將上方"\n"刪除使用這行
			}
		} catch (IOException e) {
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
			}
		}
	}

}