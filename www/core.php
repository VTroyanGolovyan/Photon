<?php
   $path = $argv[1];
   $get = $argv[3];
   $post = $argv[4];
   $sessid =  explode("=",$argv[2],2);
   @session_id($sessid[1]);
   $arr = explode("/",$argv[1],2);
   $getarr = explode("&",$get);
   $postarr = explode("&",$post);
   foreach ($getarr as $value){
      $t = explode("=",$value,2);
      $_GET[$t[0]] = $t[1];
   }
   foreach ($postarr as $value){
      $t = explode("=",$value,2);
      $_POST[$t[0]] = $t[1];
   }
   
   //print $arr[1];
   if ($arr[1] != 'core.php')
      include($path);
   else print 'Security error';
?>
