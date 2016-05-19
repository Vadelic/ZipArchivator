package archivatorZip;


import archivatorZip.exception.PathIsNotFoundException;
import archivatorZip.exception.WrongZipFileException;

public class Archiver {
    public static void main(String[] args) throws Exception {
        Operation operation = null;

        do {
            try {
                operation = askOperation();
                CommandExecutor.execute(operation);

            } catch (WrongZipFileException e) {
                ConsoleHelper.writeMessage("You have not selected archive file or selected the wrong file.");
            } catch (PathIsNotFoundException e) {
                ConsoleHelper.writeMessage("An error has occurred. Please check your entry.");
            }
        } while (operation != Operation.EXIT);
/*
4
testZip.zip
ZipArchivator.iml
*/

    }

    public static Operation askOperation() {
        int chose;
        do {
            ConsoleHelper.writeMessage("Chose operation:");
            ConsoleHelper.writeMessage(String.format("%d - create archive", Operation.CREATE.ordinal()));
            ConsoleHelper.writeMessage(String.format("%d - add file to archive", Operation.ADD.ordinal()));
            ConsoleHelper.writeMessage(String.format("%d - remove file from archive ", Operation.REMOVE.ordinal()));
            ConsoleHelper.writeMessage(String.format("%d - extract archive", Operation.EXTRACT.ordinal()));
            ConsoleHelper.writeMessage(String.format("%d - browse content of archive", Operation.CONTENT.ordinal()));
            ConsoleHelper.writeMessage(String.format("%d - exit", Operation.EXIT.ordinal()));
            chose = ConsoleHelper.readInt();
        } while ((chose < 0) || (chose > Operation.values().length - 1));
        return Operation.values()[chose];
    }
}
