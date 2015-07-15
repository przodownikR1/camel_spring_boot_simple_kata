package pl.java.scalatech;

import java.io.File;
import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static pl.java.scalatech.FileUtils.*;

public class SimpleFileTest {

    private final CamelContext context = new DefaultCamelContext();

    @Rule
    public final TemporaryFolder testFolder = new TemporaryFolder();

    private File inFolder;
    private File outFolder;

    @Before
    public void setUpContext() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file://" + inFolder.getPath()).to("file://" + outFolder.getPath());
            }
        });

        context.start();
    }

    @Before
    public void createFolders() throws IOException {
        inFolder = testFolder.newFolder();
        outFolder = testFolder.newFolder();
    }

    @After
    public void tearDownContext() throws Exception {
        context.stop();
    }

    @After
    public void deleteFolders() {
        inFolder.delete();
        outFolder.delete();
    }

    @Test
    public void copyFileFromSourceToTarget() throws Exception {
        createNewFile(inFolder, "file.txt");

        FileUtils.assertThatFileExistsInFolder(outFolder);
    }

}