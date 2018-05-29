package  packages.config;

import java.util.*;

public class Shall{
   public String command;
   public int flag;
   public String fileType;
   public Shall(String command, String flag, String fileType){
      this.command = command;
      this.flag = Integer.parseInt(flag);
      this.fileType = fileType;
   }
}
