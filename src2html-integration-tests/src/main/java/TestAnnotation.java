


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

@An_Annotation2(1)
@interface An_Annotation {
    int[] id();
    String name() default "not assigned";
    Class aClass();
    An_Annotation2[] aNannon();

}

@interface An_Annotation2 {
    int value();
}

class B {

    String[] test = new String[]{};

}


public class TestAnnotation {

    public static void main(String[] args) {
        
    }


    @An_Annotation (
        id = {1,2},
        name = "some vlaue" ,
        aNannon = {@An_Annotation2(1), @An_Annotation2(2)},
        aClass = B.class
    )
    public void somefn() {

    }
}



