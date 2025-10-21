package gestionProyectos.dataAccesLayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface IFileStore<T> {
    List<T> readAll();

    void writeAll(List<T> data);

    default void ensureFile(File xmlFile) {
        try {
            File parent = xmlFile.getParentFile();

            if (parent != null) {
                parent.mkdirs();
            }

            if (!xmlFile.exists()) {
                xmlFile.createNewFile();
                writeAll(new ArrayList<>());
            }
        } catch (Exception ignored) {}
    }
}
