package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AppTest {

    @Test
    public void testInput1() throws IOException {

        String[] args = {"src/test/resources/input1.txt", "src/test/resources/result1.jsonl", ","};
        App.main(args);

        assert(new File("src/test/resources/result1.jsonl").exists());

        FileReader resultFileReader = new FileReader("src/test/resources/result1.jsonl", StandardCharsets.UTF_8);
        BufferedReader resultBufferedReader = new BufferedReader(resultFileReader);

        FileReader outputFileReader = new FileReader("src/test/resources/output.jsonl", StandardCharsets.UTF_8);
        BufferedReader outputBufferedReader = new BufferedReader(outputFileReader);

        ObjectMapper mapper = new ObjectMapper();

        String resultLine;

        while ((resultLine = resultBufferedReader.readLine()) != null) {

            JsonNode resultRecord = mapper.readTree(resultLine);
            String outputLine = outputBufferedReader.readLine();
            JsonNode outputRecord = mapper.readTree(outputLine);
            assert(resultRecord.get("firstName").asText().contentEquals(outputRecord.get("firstName").asText()));
            assert !resultRecord.has("middleName") || (resultRecord.get("middleName").asText().contentEquals(outputRecord.get("middleName").asText()));
            assert(resultRecord.get("lastName").asText().contentEquals(outputRecord.get("lastName").asText()));
            assert(resultRecord.get("gender").asText().contentEquals(outputRecord.get("gender").asText()));
            assert(resultRecord.get("dateOfBirth").asText().contentEquals(outputRecord.get("dateOfBirth").asText()));
            assert(resultRecord.get("salary").asText().contentEquals(outputRecord.get("salary").asText()));
        }
    }

    @Test
    public void testInput2() throws IOException {

        String[] args = {"src/test/resources/input2.txt", "src/test/resources/result2.jsonl", "|"};
        App.main(args);

        assert (new File("src/test/resources/result2.jsonl").exists());

        FileReader resultFileReader = new FileReader("src/test/resources/result2.jsonl", StandardCharsets.UTF_8);
        BufferedReader resultBufferedReader = new BufferedReader(resultFileReader);

        FileReader outputFileReader = new FileReader("src/test/resources/output.jsonl", StandardCharsets.UTF_8);
        BufferedReader outputBufferedReader = new BufferedReader(outputFileReader);

        ObjectMapper mapper = new ObjectMapper();

        String resultLine;

        while ((resultLine = resultBufferedReader.readLine()) != null) {

            JsonNode resultRecord = mapper.readTree(resultLine);
            String outputLine = outputBufferedReader.readLine();
            JsonNode outputRecord = mapper.readTree(outputLine);
            assert (resultRecord.get("firstName").asText().contentEquals(outputRecord.get("firstName").asText()));
            assert !resultRecord.has("middleName") || (resultRecord.get("middleName").asText().contentEquals(outputRecord.get("middleName").asText()));
            assert (resultRecord.get("lastName").asText().contentEquals(outputRecord.get("lastName").asText()));
            assert (resultRecord.get("gender").asText().contentEquals(outputRecord.get("gender").asText()));
            assert (resultRecord.get("dateOfBirth").asText().contentEquals(outputRecord.get("dateOfBirth").asText()));
            assert (resultRecord.get("salary").asText().contentEquals(outputRecord.get("salary").asText()));
        }
    }
}
