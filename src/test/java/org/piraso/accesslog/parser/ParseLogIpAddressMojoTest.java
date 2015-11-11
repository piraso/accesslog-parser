package org.piraso.accesslog.parser;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Test;

import java.io.File;

public class ParseLogIpAddressMojoTest {
    @Test
    public void testExecute() throws Exception {
        ParseLogIpAddressMojo mojo = new ParseLogIpAddressMojo();

        mojo.setLog(new SystemStreamLog());

        mojo.setLogDirectory(new File("/adchemy/alps/alps-ss02.prodsv4.adchemy.colo"));
        mojo.setOutputFile(new File("/adchemy/alps/alps-cs04-ips.csv"));

        mojo.execute();
    }
}