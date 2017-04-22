package remq.handlers;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class REMQHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		boolean choose = MessageDialog.openConfirm(window.getShell(),"REMQ","Hello, REMQ");
		
		try {
			writeFileToJsonFormat();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (choose){

			try {
				FileClient();
				openWebsite();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else{
			System.out.println("bye");
		}
		

		return null;
	}
	
	public void readInfo(){
		try {
			FileReader fr = new FileReader("C://data/data/data.txt");
			BufferedReader br = new BufferedReader(fr);
			String str = "";
			while (br.ready()) {
//				System.out.println(br.readLine());
				str+= br.readLine();
			}
			System.out.println(str);
			fr.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openWebsite() throws IOException{
		String url = "http://140.115.87.144:8080/REMQ/homePage.jsp";
		
		if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
	}
	
	public String getInfo() throws IOException{
		FileReader fr = new FileReader("C://data/data1.txt");
		BufferedReader br = new BufferedReader(fr);
		String str ="";
		while (br.ready()) {
			str += br.readLine();
		}
		str= str.substring(0, str.length()-1);
		fr.close();
		System.out.println(str);
		return str;
	}
	

	
	public void FileClient() throws IOException, Exception {
		int filesize=1022386; 
		int bytesRead; 
		int currentTot = 0; 
		Socket socket = new Socket("140.115.87.144",21501); 

		File transferFile = new File ("C://Users/asus/Desktop/data.txt"); 
		byte [] bytearray = new byte [(int)transferFile.length()]; 
		FileInputStream fin = new FileInputStream(transferFile); 
		BufferedInputStream bin = new BufferedInputStream(fin); 
		bin.read(bytearray,0,bytearray.length); 
		OutputStream os = socket.getOutputStream(); 
		System.out.println("Sending Files..."); 
		os.write(bytearray,0,bytearray.length); 
		os.flush(); 
		socket.close(); 
		System.out.println("File transfer complete");

    } 	
	
	public void writeFileToJsonFormat() throws IOException{
		FileWriter file = new FileWriter("C://Users/asus/Desktop/data.txt",true); 
		file.write("["+getInfo()+"]");
		file.flush();
		file.close();
		
	}
	

}
