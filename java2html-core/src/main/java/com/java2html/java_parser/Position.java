/*
 * Copyright (c) 1999-2007, Enterprise Solution Consultants Limited, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package com.java2html.java_parser;

public class Position {

    public Position(int column, int line) {
        this.column = column;
        this.line = line;
    }

    public boolean equals(Object o) {
        Position p = (Position) o;
        return ( (p.column == column) && (p.line == line));
    }

    public int hashCode() {
        //TODO improve hash code
        return line * 128 + column;
    }

    public int column = 0;
    public int line = 0;

    /*public static void main(String[] args)
      {
     Position p = new Position(5,5);
     System.out.println ( p.equals(new Position(5,5)));
     System.out.println ( new Position(5,4).equals(new Position(5,5)));
     Hashtable s = new Hashtable();
     s.put(p,"Got IT");
     //System.out.println(s.get(new Position(5,5)));
     System.out.println(s.get(p));
      }*/
}
