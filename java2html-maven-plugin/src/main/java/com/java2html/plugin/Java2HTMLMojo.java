package com.java2html.plugin;


import com.java2html.Java2HTML;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

// http://maven.apache.org/plugin-tools/maven-plugin-tools-annotations/index.html

/**
 * Says "Hi" to the user.
 */
@Mojo(name = "sayhi", defaultPhase = LifecyclePhase.CLEAN)
public class Java2HTMLMojo extends AbstractMojo {

    @Component
    private org.apache.maven.project.MavenProject project;

    public void execute() throws MojoExecutionException {

        getLog().info("Hello, world. X: " + project.getCompileSourceRoots() );

        getLog().info("Hello, world. Y: " + project.getBuild().getOutputDirectory() );

        Java2HTML java2HTML =new Java2HTML();
        java2HTML.setJavaFileSource(project.getCompileSourceRoots());
        java2HTML.setDestination(project.getBuild().getOutputDirectory());
        try {
            java2HTML.buildJava2HTML();
        }
        catch (Exception e) {
            throw new MojoExecutionException("Java2HTML encountered a problem" , e);
        }


    }
}
