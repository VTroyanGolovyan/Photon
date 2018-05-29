package packages;

import java.util.*;
import java.lang.*;
import java.net.*;
import java.nio.*;
import java.io.*;
import java.text.DateFormat;
import packages.config.*; 

public class VHServer{
   int port;
   String home;
   ServerSocket ss;
   CgiConf cgi;
   public VHServer(int port, String home,CgiConf cgi){
      this.port = port;
      this.home = home;
      this.cgi = cgi;
   }
   public void run() throws Exception {
      ss = new ServerSocket(this.port);
      long i = 0;
      while(true){
		 i++;
		 new ConnectThread( ss.accept(), this.home, this.cgi ).start();   //ожидание подключения и создание дочернего потока	
		// System.out.println(i);
	  }
   }
}


