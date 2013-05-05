// $Header$
// copyright © 1997 - 2002 tangro software components gmbh - all rights reserved.

package dz;

/**
  * @todo class docu
  */
public class BadChar {
     /** CVS $Revision$ */
     public static final String versId = "$Revision$";

     public static void main(String[] args) {
         for (char ch = 126; ch < 256; ch++) {
             if (Character.isJavaIdentifierStart(ch)) {
                 System.out.println(((int) ch) + " " + ch);
             }
         }

         ªall = 47;
         ¤VALUE = 99;
         System.out.println(ªall + ¤VALUE);
         BadChar.¤VALUE = 1;
     }

     private int ºinst = 0;

     private static int ªall, ¤VALUE, ¢CLASS, ¢next;

     private void nix() {
         System.out.println(ºinst);
     }
}

//--- end of file ----------------------------------------------------------------------------------
