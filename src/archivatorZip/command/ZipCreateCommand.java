package archivatorZip.command;

import archivatorZip.ConsoleHelper;
import archivatorZip.exception.PathIsNotFoundException;
import archivatorZip.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Команда создания архива (упаковки файлов в архив)
 */
public class ZipCreateCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Create archive.");
            ZipFileManager zipFileManager = getZipFileManager();

            System.out.println("Enter archived object:");
            Path pathObj = Paths.get(ConsoleHelper.readString());
            zipFileManager.createZip(pathObj);
            ConsoleHelper.writeMessage("Archive created.");
        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("An error has occurred. The file doesn't exist.");
        }
    }


}
