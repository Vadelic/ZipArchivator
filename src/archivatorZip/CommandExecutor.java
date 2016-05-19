package archivatorZip;

import archivatorZip.command.*;

import java.util.EnumMap;

/**
 * Created by Вадим on 29.03.2016.
 */
public class CommandExecutor {
    private static EnumMap<Operation, Command> allKnownCommandsMap = new EnumMap<>(Operation.class);

    static {

        allKnownCommandsMap.put(Operation.ADD, new ZipAddCommand());
        allKnownCommandsMap.put(Operation.CONTENT, new ZipContentCommand());
        allKnownCommandsMap.put(Operation.CREATE, new ZipCreateCommand());
        allKnownCommandsMap.put(Operation.EXTRACT, new ZipExtractCommand());
        allKnownCommandsMap.put(Operation.REMOVE, new ZipRemoveCommand());
        allKnownCommandsMap.put(Operation.EXIT, new ExitCommand());
    }

    public static void execute(Operation operation) throws Exception {

        allKnownCommandsMap.get(operation).execute();
    }

    private CommandExecutor() {
    }
}
