package org.laba;


import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final String DICTIONARY_PATH = "dictionary.txt";

    static void main()  {


        Translator translator = new Translator();
        DictionaryFileReader reader = new DictionaryFileReader();

        try {
            List<String> lines = reader.read(Path.of("src/main/resources/dictionary.txt"));
            translator.loadDictionary(lines);
        } catch (FileReadException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return;
        } catch (InvalidFileFormatException e) {
            System.err.println("Ошибка формата словаря: " + e.getMessage());
            return;
        }

        System.out.println("Словарь загружен. Введите текст для перевода (exit — выход).");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    break;
                }

                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }
                if (input.isEmpty()) {
                    continue;
                }

                String translated = translator.translate(input);
                System.out.println(translated);
            }
        }

        System.out.println("Выход.");
    }



}

