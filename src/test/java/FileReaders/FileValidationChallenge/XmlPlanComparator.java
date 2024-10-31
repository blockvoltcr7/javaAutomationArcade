package FileReaders.FileValidationChallenge;

import java.io.IOException;
import java.nio.file.*;

public class XmlPlanComparator {

    public static void compareXmlPlanDirectories(Path expectedXmlplan, Path actualXmlplan) throws IOException {
        if (!Files.exists(expectedXmlplan) || !Files.exists(actualXmlplan)) {
            throw new RuntimeException("xmlplan folder missing in " + expectedXmlplan.getParent().getFileName());
        }

        FileComparator.compareAllFiles(expectedXmlplan, actualXmlplan);
    }
}
