package remq.handlers;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.json.simple.JSONObject;


/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class REMQHandler extends AbstractHandler {
	String path ="C://data/data.txt";
	String new_path ="C://data/data1.txt";
	String jsonContent ="";
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		boolean choose = MessageDialog.openConfirm(window.getShell(),"REMQ","Hello, REMQ");
						
		try {
			writeFileToJsonFormat(path, new_path);
			readInfo(new_path);
			System.out.println("content: "+jsonContent);
			
			 String urlParameters  = "content="+jsonContent;
		        byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
		        int postDataLength = postData.length;
		        String checkurl = "http://140.115.87.144:8080/REMQ/CountResult.jsp";
		        System.out.println("local: "+jsonContent);
		        try
		        {
		            URL connectto = new URL(checkurl);
		            HttpURLConnection conn = (HttpURLConnection) connectto.openConnection();
		            conn.setRequestMethod( "POST" );
		            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
		            conn.setRequestProperty( "charset", "utf-8");
		            conn.setUseCaches(false);
		            conn.setAllowUserInteraction(false);
		            conn.setDoOutput( true );
		            
		            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		            wr.writeBytes(urlParameters);
		            wr.flush();
		            wr.close();
		            
		            int responseCode = conn.getResponseCode();
		            
		            System.out.println("\nSending 'POST' request to URL : " + checkurl);
		            System.out.println("Post parameters : " + urlParameters);
		            System.out.println("Response Code : " + responseCode);
		            jsonContent="";
		            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		            StringBuilder sb = new StringBuilder();
		            String line;
		            
		            while ((line = br.readLine()) != null) {
		                sb.append(line+"\n"); 
		            }
		            
		            br.close();
		            
		            System.out.println("WEB return value is : " + sb);
		        }
		        catch (IOException e)
		        {
		            e.printStackTrace();
		        }

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (choose){

			try {
				//FileClient();
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
	
	public String readInfo(String path){
		try {			 
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				jsonContent+= br.readLine();
			}
			fr.close();
			deleteFile(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonContent;
	}
	
	public void openWebsite() throws IOException{
		String url = "http://140.115.87.144:8080/REMQ/CountResult.jsp";
		
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
	
	public String getInfo(String path) throws IOException{
		FileReader fr = new FileReader(path);
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

	public void writeFileToJsonFormat(String path, String newPath) throws IOException{
		FileWriter file = new FileWriter(newPath,true); 
		file.write("["+getInfo(path)+"]");
		file.flush();
		file.close();	
		deleteFile(path);
	}
	
	public void deleteFile(String path){
		File file = new File(path);
		file.delete();
	}
	
	public String postData (String url, String contentType, String data) throws MalformedURLException, IOException {
		URL endpoint = new URL(url);
		HttpURLConnection httpConnection = (HttpURLConnection) endpoint.openConnection();
	    httpConnection.setRequestMethod("POST");
		httpConnection.setDoInput(true);
		httpConnection.setDoOutput(true);
		httpConnection.setRequestProperty("Content-Type", contentType);
		if(data != null && data.length() > 0) {
			httpConnection.setRequestProperty("Content-Length", String.valueOf(data.length()));
			DataOutputStream dos = null;
			try {
			dos = new DataOutputStream(httpConnection.getOutputStream());
			// Use utf-8 encoding for the post data.
				dos.write(data.getBytes(Charset.forName("utf-8")));
				dos.flush();
			} finally {
			if(dos != null) dos.close();
				}
				}

			// Read the response from server
			InputStreamReader isr = null;
			BufferedReader br = null;
			String line = null;
			StringBuilder sb = new StringBuilder();
			try {
			isr = new InputStreamReader(httpConnection.getInputStream());
				br = new BufferedReader(isr);
			while( (line = br.readLine()) != null ) {
				sb.append(line);
				}		
			} finally {
			if(br != null) br.close();
			if(isr != null) isr.close();
			}
			return sb.toString();
	}
}
