package packages.config;

import java.io.*; 
import java.util.*;
public class ServersConfig{
  String path;
  public Server servers[];
  public int count;
  public ServersConfig(String path){
    this.path = path;
  }
  public void read() throws Exception{
    Scanner sc = new Scanner(new File(path));
    servers = new Server[4000];
    count = 0;
    while (sc.hasNext()){
	   String t = sc.nextLine();
	   if (t.charAt(0) != '#'){
	      String a[] = t.split(" +");
	      servers[count++] = new Server(a[0],a[1],a[2],a[3]);
	   }
	}
    sc.close();
  }
}
