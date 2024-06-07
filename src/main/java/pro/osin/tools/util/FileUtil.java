package pro.osin.tools.util;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class FileUtil {

    private FileUtil() {
        throw new UnsupportedOperationException();
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T readFile(String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (T) ois.readObject();
        }
    }

    @SneakyThrows
    public static void writeFile(String path, Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(obj);
        }
    }

    public static void createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @SneakyThrows
    public static void deletePath(String path) {
        Files.walk(Paths.get(path))
                .map(Path::toFile)
                .sorted(Comparator.comparing(File::isDirectory))
                .forEach(File::delete);
    }

    @SneakyThrows
    public static void deleteFile(String path) {
        Files.delete(Paths.get(path));
    }

}
