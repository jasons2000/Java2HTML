package com.src2html.plugin;


import com.src2html.Java2HTML;
import com.src2html.internal.Link;
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
 * generate Src2HTML
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class Src2HTMLMojo extends AbstractMojo {

    @Component
    private org.apache.maven.project.MavenProject project;

    @Parameter
    private String title;

    @Parameter
    private boolean simple;

    @Parameter
    private boolean quite;

    @Parameter
    private boolean showLineNumbers;

    @Parameter
    private Integer tabSize;

    @Parameter
    private List<Link> links;

    @Parameter
    private List<String> javaSources;

    @Parameter
    private String destination;


    public void execute() throws MojoExecutionException {

        getLog().info("Java2HTML Running");

        Java2HTML java2HTML = new Java2HTML();

        try {
            if (javaSources == null || javaSources.isEmpty()) {
                java2HTML.setJavaDirectorySource(project.getCompileSourceRoots());
            }
            else {
                java2HTML.setJavaDirectorySource(javaSources);
            }

            java2HTML.setDestinationDir(destination != null ? destination: project.getBuild().getDirectory() + File.separator + "src2html-output");

            java2HTML.setTitle(title);
            java2HTML.setQuiet(quite);
            java2HTML.setShowLineNumbers(showLineNumbers);
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
