package packages;

import java.util.*;
import java.lang.*;
import java.net.*;
import java.nio.*;
import java.io.*;
import java.text.DateFormat;

public class QueryData{
   Header a[];
   String post;
   int n;
   String firstQueryString;
   public QueryData(String firstQueryString){
	  this.firstQueryString = firstQueryString;
      a = new Header[4000];
      n = 0;
      this.post = "";
   }
   public void setPost(String post){
      this.post = post;
   }
   public void push(String name, String content){
      a[n++] = new Header(name, content);
   }
   public String get(String name){
      for (int i = 0; i < n; i++){
         if(a[i].name.equals(name))
            return a[i].content;
		}
      return null;
   }
   public String getFirstQueryString(){
     return this.firstQueryString;
   }
}
