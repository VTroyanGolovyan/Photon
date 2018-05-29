
import java.lang.*;
import packages.*;
import packages.config.*;

class Main{
  public static void main(String [] args) throws Exception {
	ServersConfig conf = new ServersConfig("conf/photon.conf");
	CgiConf cgi = new CgiConf("conf/cgi.conf");
	conf.read();
	cgi.read();
	for (int i = 0; i < conf.count; i++){
		new VirtualServer(conf.servers[i].port, conf.servers[i].home,cgi).start();
	}
  }
}
class VirtualServer extends Thread{
   int port;
   String home;
   CgiConf cgi;
   public VirtualServer(int port, String home, CgiConf cgi){
     this.port = port;
     this.home = home;
     this.cgi = cgi;
   }
   public void run(){
	   try{
         new VHServer(this.port, this.home,cgi).run();
       }catch(Exception e){
	     e.printStackTrace();
	   }
   }
}

