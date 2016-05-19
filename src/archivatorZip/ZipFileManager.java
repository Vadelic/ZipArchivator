package archivatorZip;


import archivatorZip.exception.PathIsNotFoundException;
import archivatorZip.exception.WrongZipFileException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class ZipFileManager {
    private Path zipFile; //полный путь к архиву,с которым будем работать

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile.toAbsolutePath();
    }

    /**
     * Записывает файлы и директории в архив
     *
     * @param source путь к файлу или директории с файлами которые необходимо упаковать
     * @throws Exception
     * @throws PathIsNotFoundException если путь неверный
     */
    public void createZip(Path source) throws Exception {
        source = source.toAbsolutePath();
        try (
                ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile));
        ) {
            if (Files.isDirectory(source)) {
                FileManager fileManager = new FileManager(source);
                List<Path> fileNames = fileManager.getFileList();
                for (Path eachPath : fileNames) {
                    addNewZipEntry(zipOutputStream, eachPath.getParent(), eachPath.getFileName());
                }
            } else {
                if (Files.isRegularFile(source)) {
                    addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
                } else {
                    throw new PathIsNotFoundException();
                }
            }
        }
    }

    /**
     * Записывает и закрывает ZipEntry
     *
     * @param zipOutputStream поток к архивному файлу
     * @param filePath        путь к упаковываемому файлу
     * @param fileName        имя ZipEntry
     * @throws Exception
     */
    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception {
        try (
                InputStream inputStream = Files.newInputStream(Paths.get(filePath.toString(), fileName.toString()))
        ) {
            zipOutputStream.putNextEntry(new ZipEntry(fileName.toString()));
            copyData(zipOutputStream, inputStream);
            zipOutputStream.closeEntry();

        }
    }

    /**
     * Копируем из потока в архив через буфер.
     *
     * @param OutputStream
     * @param inputStream
     * @throws IOException
     */
    private void copyData(OutputStream OutputStream, InputStream inputStream) throws IOException {
        int marker = 0;
        byte[] buffer = new byte[1024];
        while ((marker = inputStream.read(buffer)) > 0) {
            OutputStream.write(buffer, 0, marker);
        }
    }

    /**
     * Метод получает список всех файлов в архиве и их свойства
     *
     * @return
     * @throws WrongZipFileException
     * @throws IOException
     */
    public List<FileProperties> getFilesList() throws WrongZipFileException, IOException {
        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }
        List<FileProperties> propertiesList = new ArrayList<>();
        try (
                ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile), Charset.forName("CP866"));
        ) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                copyData(buffer, zipInputStream);

                String name = zipEntry.getName();
                long size = zipEntry.getSize();
                long compressedSize = zipEntry.getCompressedSize();
                int compressionMethod = zipEntry.getMethod();
                propertiesList.add(new FileProperties(name, size, compressedSize, compressionMethod));
            }

        }
        return propertiesList;
    }

    /**
     * Метод распаковывает архив в указанную папку
     *
     * @param outputFolder папка для распаковки
     * @throws WrongZipFileException
     * @throws IOException
     */
    public void extractAll(Path outputFolder) throws WrongZipFileException, IOException {
        outputFolder = outputFolder.toAbsolutePath();
        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }

        try (
                ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile), Charset.forName("CP866"));
        ) {
            if (Files.notExists(outputFolder)) {
                Files.createDirectories(outputFolder);
            }
            ZipEntry entry;
            Path tempPath;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    Files.createDirectory(Paths.get(outputFolder.toString(), entry.getName()));
                } else {
                    tempPath = Paths.get(outputFolder.toString(), entry.getName().toString());
                    Files.copy(zipInputStream, tempPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    /**
     * запускает метод removeFiles()
     *
     * @param path
     * @throws Exception
     */
    public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(path));
    }

    /**
     * Сей метод удаляет только файлы, папки не трогает
     *
     * @param pathList
     * @throws WrongZipFileException
     * @throws IOException
     */
    public void removeFiles(List<Path> pathList) throws WrongZipFileException, IOException {
        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }
        Path tempFile = Files.createTempFile(null, null);
        try (
                ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile), Charset.forName("CP866"));
                ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempFile))
        ) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (pathList.contains(Paths.get(entry.getName()))) {
                    ConsoleHelper.writeMessage(entry.getName() + " deleted");
                } else {
                    zipOutputStream.putNextEntry(new ZipEntry(entry.getName()));
                    copyData(zipOutputStream, zipInputStream);
                    zipOutputStream.closeEntry();
                    zipInputStream.closeEntry();
                }
            }
        }
        Files.move(tempFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }


    public void addFile(Path path) throws Exception {
        //  path= path.toAbsolutePath();
        addFiles(Collections.singletonList(path));
    }

    /**
     * @param absolutePathList список абсолютных путей добавляемых файлов
     */
    public void addFiles(List<Path> absolutePathList) throws Exception {
        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }
        Path tempFile = Files.createTempFile(null, null);

        try (
                ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile), Charset.forName("CP866"));
                ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempFile))
        ) {
            List<Path> pathsInArchive = new ArrayList<>();
            ZipEntry entry;
            //переписываем в новый архив
            while ((entry = zipInputStream.getNextEntry()) != null) {
                zipOutputStream.putNextEntry(new ZipEntry(entry.getName()));

                copyData(zipOutputStream, zipInputStream);
                zipOutputStream.closeEntry();
                zipInputStream.closeEntry();

                pathsInArchive.add(Paths.get(entry.getName()));

            }
            //проверяем на наличие новые файлы и записываем
            for (Path each : absolutePathList) {

                if (pathsInArchive.contains(each)) {
                    ConsoleHelper.writeMessage(String.format("* %s is available in archive", each.toString()));
                } else {
                    if (Files.isRegularFile(each)) {
                        addNewZipEntry(zipOutputStream, each.toAbsolutePath().getParent(), each.getFileName());
                        zipOutputStream.closeEntry();
                        zipInputStream.closeEntry();
                    }
                }
            }
        }
        Files.move(tempFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

}
