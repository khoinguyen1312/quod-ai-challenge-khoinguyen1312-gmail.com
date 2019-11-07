package ai.quod.challenge;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

public class GZipReader {

    public Path unzipFileToTempFile(String fileLocation) {
        System.out.println("Unzipping " + fileLocation + " to temporary file");

        Path tempFile = Utils.buildTempFileWithReadWritePermission();

        byte[] buffer = new byte[1024];

        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(fileLocation));

            FileOutputStream fileOutputStream = new FileOutputStream(tempFile.toFile());

            int len;
            while ((len = gzipInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, len);
            }

            gzipInputStream.close();
            fileOutputStream.close();
        } catch(IOException e){
            throw new RuntimeException("Unable unzip file " + fileLocation, e);
        }

        System.out.println("Finish Unzipping " + fileLocation + " to " + tempFile.toAbsolutePath());

        return tempFile;
    }
}
