package org.piraso.accesslog.parser;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
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
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

@Mojo(name = "parselogs-url", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class ParseLogUrlMojo extends AbstractMojo {

    /**
     * The directory where the test cases are located.
     */
    @Parameter(defaultValue = "${project.basedir}/src/logs/")
    private File logDirectory;

    @Parameter(defaultValue = "${project.basedir}/target/output.csv")
    private File outputFile;

    void setLogDirectory(File logDirectory) {
        this.logDirectory = logDirectory;
    }

    void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        FileWriter fileWriter = null;
        CSVWriter writer = null;

        try {
            fileWriter = new FileWriter(outputFile);
            writer = new CSVWriter(fileWriter);

            File[] files = logDirectory.listFiles();
            for (File file : files) {
                processFile(writer, file);
            }

            writer.close();
            fileWriter.close();
        } catch(Exception e) {
            IOUtils.closeQuietly(fileWriter);
            IOUtils.closeQuietly(writer);
        }
    }

    private void processFile(CSVWriter writer, File file) {
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
                if(line.length > 5) {
                    String url = line[5].split(" ")[1];
                    if(!StringUtils.startsWith(url, "/alps-cs/status")) {
                        writer.writeNext(new String[]{url});
                    }
                }
            }
        } catch(Exception e) {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(gzin);
            IOUtils.closeQuietly(in);
        }
    }
}
