package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) throws IOException {

        String inputFile = args[0];
        String outputFile = args[1];
        String delimiter = args[2];

        try (Stream<String> inputFileStream = Files.lines(Path.of(args[0]))) {
            Optional<String> firstLine = inputFileStream.findFirst();
            if (firstLine.isPresent()) {
                String[] headers = firstLine.get().split("[" + delimiter + "]");
                process(inputFile, outputFile, delimiter, headers);
            }
        }
    }
    private static void process(String inputFile, String outputFile, String delimiter, String[] headers) throws IOException {

        FileWriter fileWriter = new FileWriter(outputFile, StandardCharsets.UTF_8);

        try (Stream<String> inputFileStream = Files.lines(Path.of(inputFile))) {
            inputFileStream
                    .skip(1)
                    .forEach(record -> {
                        String[] values = Arrays.stream(record.split("[" + delimiter + "](?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1))
                                .map(i -> i.replace("\"", "")).toArray(String[]::new);
                        Map<String, Object> json = IntStream.range(0, headers.length)
                                .boxed()
                                .filter(i -> !values[i].isEmpty())
                                .collect(Collectors.toMap(
                                        i -> headers[i],
                                        i -> {
                                            try {
                                                return deserialize(values[i]);
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                ));
                        ObjectMapper objectMapper = new ObjectMapper();
                        try{
                            fileWriter.write(objectMapper.writeValueAsString(json));
                            fileWriter.write("\n");
                            fileWriter.flush();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        fileWriter.close();
    }
    static Object deserialize(String value) throws ParseException {

        String isNumeric = "-?\\d+(\\.\\d+)?";
        String isDate = "\\b\\d{1,4}[-/]\\d{1,2}[-/]\\d{1,4}\\b";

        if (Pattern.matches(isNumeric, value)) {
            return Integer.parseInt(value);
        } else {
            if (Pattern.matches(isDate, value)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                value = value.replaceAll("/", "-");
                if (isValidDateFormat(value)) {
                    return simpleDateFormat.format(simpleDateFormat.parse(value));
                } else {
                    return simpleDateFormat.format(new SimpleDateFormat("dd-MM-yyyy").parse(value));
                }
            } else {
                return value;
            }
        }
    }
    static boolean isValidDateFormat(String date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setLenient(false);

        try {
            simpleDateFormat.parse(date);
        } catch (ParseException parseException) {
            return false;
        }
        return true;
    }
}
