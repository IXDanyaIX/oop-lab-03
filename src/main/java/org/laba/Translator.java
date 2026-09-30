package org.laba;

import java.util.*;

public class Translator {

    private final SortedMap<String, String> dict = new TreeMap<>(
            Comparator.comparingInt(String::length).reversed()
                    .thenComparing(Comparator.naturalOrder())
    );

    public void loadDictionary(List<String> lines)
            throws InvalidFileFormatException {

        dict.clear();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\|", -1);

            if (parts.length != 2) {
                throw new InvalidFileFormatException(
                        "Строка не соответствует формату " +
                                "'слово | перевод': " + line,
                        i + 1
                );
            }

            String word = parts[0].trim().toLowerCase();
            String translation = parts[1].trim();

            if (word.isEmpty() || translation.isEmpty()) {
                throw new InvalidFileFormatException(
                        "Пустое поле в строке: " + line,
                        i + 1
                );
            }

            dict.put(word, translation);
        }
    }

    public String translate(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder result = new StringBuilder();

        int i = 0;

        while (i < text.length()) {

            // Если текущий символ не является буквой,
            // просто копируем его.
            if (!Character.isLetter(text.charAt(i))) {
                result.append(text.charAt(i));
                i++;
                continue;
            }

            String bestKey = null;
            String bestTranslation = null;

            // Ищем самое длинное выражение,
            // которое начинается в позиции i.
            for (Map.Entry<String, String> entry : dict.entrySet()) {

                String key = entry.getKey();

                if (matches(text, i, key)) {
                    bestKey = key;
                    bestTranslation = entry.getValue();

                    // dict отсортирован по длине ключа
                    // от большего к меньшему.
                    // Поэтому первое совпадение уже самое длинное.
                    break;
                }
            }

            if (bestKey != null) {
                result.append(bestTranslation);
                i += bestKey.length();
            } else {
                result.append(text.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    private boolean matches(String text, int position, String key) {

        if (position + key.length() > text.length()) {
            return false;
        }

        String part = text.substring(
                position,
                position + key.length()
        );

        if (!part.equalsIgnoreCase(key)) {
            return false;
        }

        // Проверяем границу слева.
        // Если перед выражением находится буква,
        // значит это не отдельное слово/выражение.
        if (position > 0 &&
                Character.isLetter(text.charAt(position - 1))) {
            return false;
        }

        int end = position + key.length();

        if (end < text.length() &&
                Character.isLetter(text.charAt(end))) {
            return false;
        }

        return true;
    }
}