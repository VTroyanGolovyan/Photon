package packages.config;

import java.io.*; 
import java.util.*;
public class CgiConf{
  public String path;
  public Shall shalls[];
  public int count;
  public CgiConf(String path){
    this.path = path;
  }
  public void read() throws Exception{
    Scanner sc = new Scanner(new File(path));
    shalls = new Shall[4000];
    count = 0;
    while (sc.hasNext()){
	   String t = sc.nextLine();
	   if (t.charAt(0) != '#'){
	      String a[] = t.split(" +");
	      shalls[count++] = new Shall(a[0],a[1],a[2]);
	   }
	}
    sc.close();
  }
}
