package archivatorZip.command;

import archivatorZip.ConsoleHelper;
import archivatorZip.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Вадим on 29.03.2016.
 */
public abstract class ZipCommand implements Command{
    protected ZipFileManager getZipFileManager() {
        System.out.println("Enter the full path of the archive:");
        Path pathArch = Paths.get(ConsoleHelper.readString());
        return new ZipFileManager(pathArch);

    }
}
