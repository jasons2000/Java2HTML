// $Header$
// copyright � 1997 - 2002 tangro software components gmbh - all rights reserved.

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

         �all = 47;
         �VALUE = 99;
         System.out.println(�all + �VALUE);
         BadChar.�VALUE = 1;
     }

     private int �inst = 0;

     private static int �all, �VALUE, �CLASS, �next;

     private void nix() {
         System.out.println(�inst);
     }
}

//--- end of file ----------------------------------------------------------------------------------
