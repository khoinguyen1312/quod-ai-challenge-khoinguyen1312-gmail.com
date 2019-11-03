package ai.quod.challenge;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GZipReaderTest {
    GZipReader reader = new GZipReader();

    @Before
    public void init() {

    }

    @Test
    public void unzipFileToTempFile_should_unzipAFileToATempFolderSuccess() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource("testFile.gz").getFile());

        GZipReader gZipReader = new GZipReader();
        Optional<Path> path = gZipReader.unzipFileToTempFile(file.getAbsolutePath());

        List<String> strings = Files.readAllLines(path.get());

        Assert.assertEquals("ThisLineIsForTesting", strings.get(0));
    }
}
