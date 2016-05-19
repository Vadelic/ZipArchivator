package archivatorZip.command;

import archivatorZip.ConsoleHelper;
import archivatorZip.FileProperties;
import archivatorZip.ZipFileManager;

/**
 *  оманда просмотра содержимого архива
 */
public class ZipContentCommand extends ZipCommand {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("View archive content.");
        ZipFileManager zipFileManager = getZipFileManager();
        ConsoleHelper.writeMessage("\t-Archive content:");
        for (FileProperties each : zipFileManager.getFilesList()) {
            ConsoleHelper.writeMessage(each.toString());
        }
        ConsoleHelper.writeMessage("\t-The content of the archive is read.");
    }
}
