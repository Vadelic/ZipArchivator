package archivatorZip.command;

import archivatorZip.ConsoleHelper;

/**
 * Created by Вадим on 29.03.2016.
 */
public class ExitCommand implements Command {
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("До встречи!");
    }
}
