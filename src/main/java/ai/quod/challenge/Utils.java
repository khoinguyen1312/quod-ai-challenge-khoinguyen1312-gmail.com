package ai.quod.challenge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Consumer;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class Utils {

    public static Path buildTempFileWithReadWritePermission() {
        Set<PosixFilePermission> permissions = new HashSet<>();
        permissions.add(PosixFilePermission.OWNER_READ);
        permissions.add(PosixFilePermission.OWNER_WRITE);

        FileAttribute<Set<PosixFilePermission>> fileAttributes = PosixFilePermissions.asFileAttribute(permissions);

        try {
            return Files.createTempFile("", "", fileAttributes);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create temporary file");
        }
    }

    public static void activateCerificateForDownloadFromHttps() {
        // Create a new trust manager that trust all certificates
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        // Activate the new trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }

        System.setProperty("http.agent", "Chrome");
    }

    public static void forEachLineInFile(File file, Consumer<String> consumer) throws IOException {
        BufferedReader reader;

        reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
            consumer.accept(line);
            line = reader.readLine();
        }
        reader.close();
    }

    public static void createCSVFile(File output, List<List<String>> rows) throws IOException {
        FileWriter out = new FileWriter(output);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {
            for (List<String> row : rows) {
                printer.printRecord(row);
            }
        }
    }

    public static Calendar parseIso8601DateFormat(String datetimeStart) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ssX");
        cal.setTime(simpleDateFormat.parse(datetimeStart));
        return cal;
    }

    public static String formatToUtc(Calendar time) {
        SimpleDateFormat gitHubDateFormat = new SimpleDateFormat("yyyy-MM-dd-H");
        gitHubDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return gitHubDateFormat.format(time.getTime());
    }


    public static List<Calendar> buildGitHubTimeRange(Calendar start, Calendar end) {
        List<Calendar> result = new ArrayList<>();

        Calendar counter = Calendar.getInstance();
        counter.setTime(start.getTime());

        while (counter.compareTo(end) <= 0) {
            Calendar cloned = Calendar.getInstance();
            cloned.setTime(counter.getTime());
            result.add(cloned);

            counter.add(Calendar.HOUR, 1);
        }

        return result;
    }
}
