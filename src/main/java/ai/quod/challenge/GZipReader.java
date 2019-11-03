package ai.quod.challenge;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class GZipReader {

    public Optional<Path> unzipFileToTempFile(String fileLocation) {
        System.out.println("Unzipping " + fileLocation + " to temporary file");

        Path tempFile = null;
        try {
            tempFile = buildTempFileWithReadWritePermission();
        } catch (IOException e) {
            throw new RuntimeException("Unable to create temporary file", e);
        }

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

        return Optional.ofNullable(tempFile);
    }

    private Path buildTempFileWithReadWritePermission() throws IOException {
        Set<PosixFilePermission> permissions = new HashSet<>();
        permissions.add(PosixFilePermission.OWNER_READ);
        permissions.add(PosixFilePermission.OWNER_WRITE);

        FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);

        return Files.createTempFile(this.getClass().getSimpleName(), "", fileAttributes);
    }
}
