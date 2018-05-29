package packages;

import java.util.*;
import java.lang.*;
import java.net.*;
import java.nio.*;
import java.io.*;
import java.text.DateFormat;
import packages.config.*; 

class ConnectThread extends Thread{
  Socket cs;
  InputStream is; //входной поток
  DataInputStream dis; //типизированный входной поток
  OutputStream os;  //выходной поток
  DataOutputStream dos; //типизтрованый выходной поток
  QueryData headers; //заголовки
  String requestHeaders[];
  String home;
  CgiConf cgi;
  public ConnectThread(Socket cs, String home, CgiConf cgi){
    this.cs = cs;
    this.home = home;
    this.cgi = cgi;
  }
  public void run(){
		try{
		   work();
		}catch(Exception e){
			try{
				cs.close();
		    }catch(Exception e2){}
			//e.printStackTrace();
		}
  }
  public void initStreams() throws Exception {
	   is = cs.getInputStream();
       os = cs.getOutputStream();
	   dos = new DataOutputStream(os);
	   dis = new DataInputStream(is);
  }
  String postParams;
  public void getHttpHeaders() throws Exception{
	    byte buf[] = new byte[64*1024];
	    
	    int r = is.read(buf);
	    String t = new String(buf, 0, r);	    
	    
        requestHeaders = t.split("\n");
        headers = new QueryData(requestHeaders[0]);
      
        if (getRequestType().equals("POST")){
		   headers.setPost(requestHeaders[requestHeaders.length - 1]);
		}
        for (int i = 1; i < requestHeaders.length-1; i++){
		   if (requestHeaders[i] != "" && requestHeaders[i] != null){  
			   String t1[] = requestHeaders[i].split(":",2);
			   if ( t1.length >= 2 && t1[0] != null && t1[1] != null)
			      headers.push(t1[0].trim(),t1[1].trim());
	       }
		}
  }
  public String createSession(){
      String symbols = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
      StringBuilder randString = new StringBuilder();
      int count = (int)(Math.random()*20+5);
      for(int i=0;i<count;i++)
         randString.append(symbols.charAt((int)(Math.random()*symbols.length())));
      return randString.toString();
  }
  public String getPath(){
	    String path[] = headers.getFirstQueryString().split(" +");
        String pathT = path[1];	 
        String pathS = pathT;
        if (pathT.contains("?")){
           String t[] = pathT.split("\\?",2);
           pathS = t[0];
        }
	    if (pathS.charAt(0) == '/')
          return this.home + pathS;
        else return this.home + "/" + pathS;        			           
  }
  public String getContentType(String path){
	  int i = path.lastIndexOf('.');
	  String extension = "";
      if (i > 0) {
         extension = path.substring(i+1);
      }
      if (extension.equals("html")){
		  return "text/html";
	  }else if (extension.equals("css")){
	      return "text/css";
	  }else if (extension.equals("js")){
	      return "text/javascript";
	  }else if(extension.equals("jpeg")){
	      return "image/jpeg";
	  }else if(extension.equals("png")){
	      return "image/png";
	  }else if(extension.equals("jpg")){
	      return "image/jpg";
	  }else if(extension.equals("ico")){
	      return "image/x-icon";
	  }else if(extension.equals("php")){
	      return "php";
	  }else if(extension.equals("py")){
	      return "py";
	  }else if(extension.equals("rb")){
	      return "rb";
	  }else if(extension.equals("pl")){
	      return "pl";
	  }else if(extension.equals("cxx")){
	      return "cxx";
	  }else return "text/plain";
  }
  public boolean isImage(String path){
     String t = getContentType(path);
     if (t.equals("image/jpeg")) return true;
     if (t.equals("image/png")) return true;
     if (t.equals("image/jpg")) return true;
     return false;
  }
  public String getGetParams(){
	  String path[] = headers.getFirstQueryString().split(" +");
      String pathT = path[1];	
      if (pathT.contains("?")){ 
        String t[] = pathT.split("\\?",2);
        String pathS = t[1];
        return pathS;
	  }else{
	    return "";
	  }
  }
  public void responseProcessing() throws Exception {
	        String path = getPath();
	        //System.out.print(path);
	        File f = new File(path);
	        String responseCode = "200";
	        if (f.isDirectory()){
				if (path.charAt(path.length()-1) == '/')
				   path = getPath() + "index.php";
				else path = getPath() + "/index.php";
			  	  f = new File(path);
			    if (!f.exists()){
				  if (path.charAt(path.length()-1) == '/')
				    path = getPath() + "index.html";
				  else path = getPath() + "/index.html";
				  f = new File(path);
			    }  
			}
			
			if (!f.exists()){
			    responseCode = "404";
			    path = "errors/404.html";
				f = new File("errors/404.html");
			} 
			String mime = getContentType(path);
			if (mime.equals("php")){
				mime = "text/html";
			}
			if (mime.equals("py")){
				mime = "text/html";
			}
			if (mime.equals("rb")){
				mime = "text/html";
			}
			if (mime.equals("pl")){
				mime = "text/html";
			}
			if (mime.equals("cxx")){
				mime = "text/html";
			}
           // первая строка ответа
            String response = "HTTP/1.1 " + responseCode + " OK\n";
            // дата создания в GMT
            DateFormat df = DateFormat.getTimeInstance();
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            // время последней модификации файла в GMT
            response = response + "Date: " + df.format(new Date(f.lastModified())) + "\n";
            // длина файла
            if (isImage(path)){
               response = response + "Content-Length: " + f.length() + "\n";
               response = response + "Cache-control: max-age=3600" + "\n";
		    }
            // строка с MIME кодировкой
            response = response + "Content-Type: " + mime + "\n";
            if (headers.get("Cookie") == null){
			  String session = createSession();
			  response = response + "Set-Cookie: PHPSESSID=" + session + "\n";
			  headers.push("Cookie",session);
			}
            // остальные заголовки
            response = response
            + "Connection: Keep-Alive\n"
            + "Server: Photon\n\n";
            
            // выводим заголовок:
            os.write(response.getBytes());
            
            int r = 1;
            byte buf[] = new byte[1024*1024];
            if (!checkCompilers(getContentType(path),path)){
               //файл:
               FileInputStream fis = new FileInputStream(f);
            
               while(r > 0){
                 r = fis.read(buf);
                 if(r > 0) os.write(buf, 0, r);
               }
               fis.close();
		    }
  }
  public boolean checkCompilers(String contentType, String path) throws Exception{
             for (int i = 0; i < this.cgi.count; i++){
				 if (contentType.equals(this.cgi.shalls[i].fileType)) {
					  runInterpritator(this.cgi.shalls[i].command, path, this.cgi.shalls[i].flag);
					  return true;
				 }
		     }
		     return false;
  }
  public void runInterpritator(String compiler,String path, int f) throws Exception{
			 int r = 1;
             byte buf[] = new byte[1024*1024];
             Process proc;
             if (f == 1)
                   proc = Runtime.getRuntime().exec(compiler + " " + this.home +" " + path + " " + headers.get("Cookie") + " " 
														+ getGetParams() + " " + headers.post); 
			    else proc = Runtime.getRuntime().exec(compiler + " "+ path + " " + headers.get("Cookie") + " " 
														+ getGetParams() + " " + headers.post); 
             InputStream stout = proc.getInputStream();
                 while(r > 0){
                   r = stout.read(buf);
                   if(r >= 0) os.write(buf, 0, r);
                 }
                 proc.waitFor();
                 proc.destroy();
  }
  public String getRequestType(){
     String path[] = headers.getFirstQueryString().split(" +");
     return path[0].trim();
  }
  public void work() throws Exception{
	    initStreams();
	    getHttpHeaders();
	    responseProcessing();
		cs.close();    
  }
}
