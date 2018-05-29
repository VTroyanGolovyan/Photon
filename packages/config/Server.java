package  packages.config;
public class Server{
  public String host;
  public int port;
  public String home;
  public String name;
  public Server(String host, String port, String home,String name){
     this.host = host;
     this.port = Integer.parseInt(port);
     this.home = home;
     this.name = name;
  }
}

