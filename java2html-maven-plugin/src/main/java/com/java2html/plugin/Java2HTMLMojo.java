package com.java2html.plugin;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * Goal which touches a timestamp file.
 *
 * @goal generate
 *
 * @phase process-sources
 */
public class Java2HTMLMojo extends AbstractMojo
{
    public void execute() throws MojoExecutionException
    {
        getLog().info( "Hello, world." );
    }
}