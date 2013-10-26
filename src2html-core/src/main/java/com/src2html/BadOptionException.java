/*

 */

package com.src2html;

/**
 * Exception Indicating a bad Src2Html option
 *
 **/

public class BadOptionException
    extends Exception {

    /**
     * Create a BadOptionException
     *
     * @param errorText Error Description
     */
    public BadOptionException(String errorText) {
        super(errorText);
    }
}