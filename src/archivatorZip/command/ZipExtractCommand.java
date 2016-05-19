package archivatorZip.command;

import archivatorZip.ConsoleHelper;
import archivatorZip.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Команда распаковки архива
 */
public class ZipExtractCommand extends  ZipCommand{
    @Override
    public void execute()  {
        try {
            ConsoleHelper.writeMessage("Extract archive.");
            ZipFileManager zipFileManager = getZipFileManager();
            ConsoleHelper.writeMessage("Enter target folder:");
            Path pathObj = Paths.get(ConsoleHelper.readString());
            zipFileManager.extractAll(pathObj);
            ConsoleHelper.writeMessage("Archive extracted.");
        } catch (Exception e) {
            ConsoleHelper.writeMessage("An error has occurred. The file doesn't exist.");
        }
    }
}
