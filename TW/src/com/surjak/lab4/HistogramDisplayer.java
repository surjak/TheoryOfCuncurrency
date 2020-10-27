package com.surjak.lab4;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by surja on 20.10.2020
 */
public class HistogramDisplayer implements Displayer {
    private static final String CSV_SEPARATOR = ",";

    @Override
    public void display(Map<String, Integer> map) {
        List<CSVRow> list = map
                .entrySet()
                .stream()
                .map(e -> new CSVRow(Main.M, e.getKey().split("-")[0], Integer.parseInt(e.getKey().split("-")[1]), Main.numberOfProducers, Main.fairBuffer, Main.probability, e.getValue()))
                .sorted()
                .collect(Collectors.toList());
        try {
            saveToFile(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(List<CSVRow> list) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("outputData.csv",true), StandardCharsets.UTF_8));
        for (CSVRow csvRow : list) {
            StringBuffer oneLine = new StringBuffer();
            oneLine.append(csvRow.bufferSize);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(csvRow.producerOrConsumer);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(csvRow.size);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(csvRow.PK_config);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(csvRow.isFair);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(csvRow.randomization);
            oneLine.append(CSV_SEPARATOR);
            oneLine.append(csvRow.counter);
            bw.write(oneLine.toString());
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }

    public void writeHeader() throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("outputData.csv",true), StandardCharsets.UTF_8));
        StringBuffer oneLine = new StringBuffer();
        oneLine.append("BUFFER_SIZE");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("P/C");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("SIZE");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("PK_CONFIG");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("FAIR_BUFFER");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("RANDOMIZATION_OPTION_NORMAL");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("COUNTER");
        bw.write(oneLine.toString());
        bw.newLine();
    }

}
