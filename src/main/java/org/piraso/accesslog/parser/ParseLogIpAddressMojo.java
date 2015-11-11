package org.piraso.accesslog.parser;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

@Mojo(name = "parselogs-url", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class ParseLogIpAddressMojo extends AbstractMojo {

    /**
     * The directory where the test cases are located.
     */
    @Parameter(defaultValue = "${project.basedir}/src/logs/")
    private File logDirectory;

    @Parameter(defaultValue = "${project.basedir}/target/ips.csv")
    private File outputFile;

    void setLogDirectory(File logDirectory) {
        this.logDirectory = logDirectory;
    }

    void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Set<String> ips = new HashSet<String>();
        File[] files = logDirectory.listFiles();
        for (File file : files) {
            processFile(ips, file);
        }

        getLog().info("Ip Address: \n" + StringUtils.join(ips, "\n"));
    }

    private void processFile(Set<String> ips, File file) {
        getLog().info("Processing file: " + file.getAbsolutePath());

        CSVReader reader = null;
        FileInputStream in = null;
        GZIPInputStream gzin = null;

        try {
            in = new FileInputStream(file);
            gzin = new GZIPInputStream(in);
            reader = new CSVReader(new InputStreamReader(gzin), ' ');

            String[] line;
            while((line = reader.readNext()) != null) {
                if(line.length > 6) {
                    ips.add(line[6]);
                }
            }
        } catch(Exception e) {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(gzin);
            IOUtils.closeQuietly(in);
        }
    }
}
