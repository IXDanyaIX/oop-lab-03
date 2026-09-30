package org.laba;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DictionaryFileReader {
    public List<String> read(Path path)
            throws FileReadException, InvalidFileFormatException {

        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileReadException("Не удалось прочитать файл: " + path, e);
        }

        // Проверяем формат каждой непустой строки
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\|");
            if (parts.length != 2) {
                throw new InvalidFileFormatException(
                        "Строка не соответствует формату 'слово | перевод': " + line,
                        i + 1
                );
            }

            if (parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                throw new InvalidFileFormatException(
                        "Пустое поле в строке: " + line,
                        i + 1
                );
            }
        }

        return lines;
    }
}
