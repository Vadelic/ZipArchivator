package archivatorZip.command;

import archivatorZip.ConsoleHelper;
import archivatorZip.ZipFileManager;
import archivatorZip.exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Команда удаления файла из архива
 */
public class ZipRemoveCommand extends  ZipCommand{
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("\t***********************");
            ConsoleHelper.writeMessage("\t* Remove from archive *");
            ConsoleHelper.writeMessage("\t***********************");

            ZipFileManager zipFileManager = getZipFileManager();

            ConsoleHelper.writeMessage("\t* Enter removed file *");
            Path pathObj = Paths.get(ConsoleHelper.readString());
            zipFileManager.removeFile(pathObj);

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("An error has occurred. The file doesn't exist.");
        }
    }
}
