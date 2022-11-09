package com.djulb.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;

public class FileUtils {
    public static List<String> readAll(String filePath) {
        List<String> result = new ArrayList<>();
        try (Stream<String> lines = lines(Paths.get(filePath))) {
            result = lines.collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
