package org.piraso.accesslog.parser;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Test;

import java.io.File;

public class ParseLogUrlMojoTest {

    @Test
    public void testExecute() throws Exception {
        ParseLogUrlMojo mojo = new ParseLogUrlMojo();

        mojo.setLog(new SystemStreamLog());

        mojo.setLogDirectory(new File("/adchemy/alps/alps-cs04.prodsv4.adchemy.colo"));
        mojo.setOutputFile(new File("/adchemy/alps/alps-cs04-urls.csv"));

        mojo.execute();
    }


}