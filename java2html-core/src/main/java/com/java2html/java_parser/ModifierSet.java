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

/**
 * Class to hold modifiers.
 */
public final class ModifierSet
{
  /* Definitions of the bits in the modifiers field.  */
  public static final int PUBLIC = 0x0001;
  public static final int PROTECTED = 0x0002;
  public static final int PRIVATE = 0x0004;
  public static final int ABSTRACT = 0x0008;
  public static final int STATIC = 0x0010;
  public static final int FINAL = 0x0020;
  public static final int SYNCHRONIZED = 0x0040;
  public static final int NATIVE = 0x0080;
  public static final int TRANSIENT = 0x0100;
  public static final int VOLATILE = 0x0200;
  public static final int STRICTFP = 0x1000;

  /** A set of accessors that indicate whether the specified modifier
      is in the set. */

  public static boolean isPublic( final int modifiers )
  {
    return (modifiers & PUBLIC) != 0;
  } // isPublic

  public static boolean isProtected( final int modifiers )
  {
    return (modifiers & PROTECTED) != 0;
  } // isProtected

  public static boolean isPrivate( final int modifiers )
  {
    return (modifiers & PRIVATE) != 0;
  } // isPrivate

  public static boolean isStatic( final int modifiers )
  {
    return (modifiers & STATIC) != 0;
  } // isStatic

  public static boolean isAbstract( final int modifiers )
  {
    return (modifiers & ABSTRACT) != 0;
  } // isAbstract

  public static boolean isFinal( final int modifiers )
  {
    return (modifiers & FINAL) != 0;
  } // isFinal

  public static boolean isNative( final int modifiers )
  {
    return (modifiers & NATIVE) != 0;
  } // isNative

  public static boolean isStrictfp( final int modifiers )
  {
    return (modifiers & STRICTFP) != 0;
  } // isStrictfp

  public static boolean isSynchronized( final int modifiers )
  {
    return (modifiers & SYNCHRONIZED) != 0;
  } // isSynchronized

  public static boolean isTransient( final int modifiers )
  {
    return (modifiers & TRANSIENT) != 0;
  } // isTransient

  public static boolean isVolatile( final int modifiers )
  {
    return (modifiers & VOLATILE) != 0;
  } // isVolatile
} // class ModifierSet
