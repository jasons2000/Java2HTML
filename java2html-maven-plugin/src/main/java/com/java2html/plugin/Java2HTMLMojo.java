package com.java2html.plugin;


import com.java2html.Java2HTML;
import com.java2html.internal.Link;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

// http://maven.apache.org/plugin-tools/maven-plugin-tools-annotations/index.html

/**
 * generate Java2HTML
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class Java2HTMLMojo extends AbstractMojo {

    @Component
    private org.apache.maven.project.MavenProject project;

    @Parameter
    private String title;

    @Parameter
    private boolean simple;

    @Parameter
    private boolean quite;

    @Parameter
    private Integer marginSize;

    @Parameter
    private Integer tabSize;

    @Parameter
    private List<Link> links;


    public void execute() throws MojoExecutionException {

        getLog().info("Java2HTML Running");

        Java2HTML java2HTML = new Java2HTML();

        try {
            // todo have 1 set sourceMethod which can  work out dir/file/zips
            java2HTML.setJavaDirectorySource(project.getCompileSourceRoots());
            java2HTML.setDestination(project.getBuild().getDirectory() + File.separator + "java2html-output");
            java2HTML.setTitle(title);
            java2HTML.setQuiet(quite);
            if (marginSize != null) java2HTML.setMarginSize(marginSize);
            if (tabSize != null) java2HTML.setTabSize(tabSize);
            java2HTML.setJavaDocLinks(links);

            java2HTML.buildJava2HTML();
        }
        catch (Exception e) {
            throw new MojoExecutionException("Java2HTML encountered a problem", e);
        }

        getLog().info("Java2HTML Completed");
    }



}
