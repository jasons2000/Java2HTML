package com.src2html.plugin;


import com.src2html.Src2Html;
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
 * generate Src2Html
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class Src2HtmlMojo extends AbstractMojo {

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

        getLog().info("Src2Html Running");

        Src2Html src2Html = new Src2Html();

        try {
            if (javaSources == null || javaSources.isEmpty()) {
                src2Html.setJavaDirectorySource(project.getCompileSourceRoots());
            }
            else {
                src2Html.setJavaDirectorySource(javaSources);
            }

            src2Html.setDestinationDir(destination != null ? destination: project.getBuild().getDirectory() + File.separator + "src2html-output");

            src2Html.setTitle(title);
            src2Html.setQuiet(quite);
            src2Html.setShowLineNumbers(showLineNumbers);
            if (tabSize != null) src2Html.setTabSize(tabSize);
            src2Html.setJavaDocLinks(links);

            src2Html.generateHtml();
        }
        catch (Exception e) {
            throw new MojoExecutionException("Src2Html encountered a problem", e);
        }

        getLog().info("Src2Html Completed");
    }



}
