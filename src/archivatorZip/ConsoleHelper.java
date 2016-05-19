package archivatorZip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ����� on 29.03.2016.
 */
public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }
    public static String readString() {
        while (true) {
            try {
                return reader.readLine();
            } catch (IOException e) {
                writeMessage("��������� ������ ��� ������� ����� ������. ���������� ��� ���.");
            }
        }
    }

    public static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(readString());
            } catch (NumberFormatException e) {
                writeMessage("��������� ������ ��� ������� ����� �����. ���������� ��� ���.");
            }
        }
    }


}
