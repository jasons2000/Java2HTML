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

package com.java2html;

import java.io.*;

public class JavaDoc {

    private String httpRef;

    /**
     * Required by Ant Task
     */
    public JavaDoc() {
    }

    /**
     * Used by Java2HTML to communicate a JavaDoc option
     *
     * @param localRef Local Reference to a JavaDoc Source, this reference will be used in the
     *                       generated HTML output.
     */
    @Deprecated
    public JavaDoc(File localRef) {

//        this.localRef = localRef;
    }

    /**
     * Used by Java2HTML to communicate a JavaDoc option
     *
     * @param localRef Local Reference to a JavaDoc Source
     * @param httpRef The Http Reference that will be used in the generated HTML
     */
    public JavaDoc(File localRef, String httpRef) {

//        this.localRef = localRef;
        this.httpRef = httpRef;
    }

    /**
     * Set the Local Reference
     */
    public void setLocalRef(File localRef) {
//        this.localRef = localRef;
    }


    /**
     * Set the Http Reference
     */
    public void setHttpRef(String httpRef) {
        this.httpRef = httpRef;
    }

    /**
     * Return the Http Reference
     */
    public String getHttpRef() {
        return httpRef;
    }

    /**
     * Retrn the Local Reference
     */
    public File getLocalRef() {
        return null;
    }

    /**
     * Validate the this object is in a consistent state
     */
    public void validate() throws BadOptionException {
//        if (localRef == null) throw new BadOptionException("localRef must be set");
//        if (!localRef.isDirectory()) throw new BadOptionException("localRef must be a Directory");
        if (httpRef != null && !httpRef.toLowerCase().startsWith("http://")) throw new BadOptionException("httpRef must begin with http://");
    }

}