package com.java2html.plugin;


import com.java2html.Java2HTML;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;

// http://maven.apache.org/plugin-tools/maven-plugin-tools-annotations/index.html

/**
 * Says "Hi" to the user.
 */
@Mojo(name = "sayhi", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class Java2HTMLMojo extends AbstractMojo {

    @Component
    private org.apache.maven.project.MavenProject project;

    public void execute() throws MojoExecutionException {

        getLog().info("Java2HTML Mojo Running");

        Java2HTML java2HTML = new Java2HTML();

        try {
            // todo have 1 set sourceMethod which can  work out dir/file/zips
            java2HTML.setJavaDirectorySource(project.getCompileSourceRoots());
            java2HTML.setDestination(project.getBuild().getDirectory() + File.separator + "java2html.output");
            java2HTML.buildJava2HTML();
        }
        catch (Exception e) {
            throw new MojoExecutionException("Java2HTML encountered a problem", e);
        }

        getLog().info("Java2HTML Mojo Completed");


    }
}
