package archivatorZip.command;

import archivatorZip.ConsoleHelper;
import archivatorZip.ZipFileManager;
import archivatorZip.exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * Команда добавления файла в архив
 */
public class ZipAddCommand extends  ZipCommand{
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("\t******************");
            ConsoleHelper.writeMessage("\t* Add to archive *");
            ConsoleHelper.writeMessage("\t******************");

            ZipFileManager zipFileManager = getZipFileManager();

            ConsoleHelper.writeMessage("\t* Enter the added file *");
            Path pathObj = Paths.get(ConsoleHelper.readString());
            zipFileManager.addFile(pathObj);

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("An error has occurred. The file doesn't exist.");
        }
    }
}
