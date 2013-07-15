package com.akavrt.csp._1d.tester;

import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;

/**
 * User: akavrt
 * Date: 07.07.13
 * Time: 23:18
 */
public class InversionCounter {
    private final String path;
    private long inversionsCount;
    private int inputSize;

    public static void main(String[] args) {
        String path = "/Users/akavrt/Sandbox/IntegerArray.txt";

        InversionCounter counter = new InversionCounter(path);
        counter.count();

        System.out.println(counter.getInversionsCount() + " inversions found in array of size " +
                            counter.getInputSize() + ".");
    }

    public InversionCounter(String path) {
        this.path = path;
    }

    public long count() {
        List<Integer> numbers = read();

        inputSize = numbers == null ? 0 : numbers.size();
        inversionsCount = numbers == null ? 0 : process(numbers);

        return inversionsCount;
    }

    public long getInversionsCount() {
        return inversionsCount;
    }

    public int getInputSize() {
        return inputSize;
    }

    private List<Integer> read() {
        File file = new File(path);

        if (!file.exists() || !file.isFile()) {
            return null;
        }

        BufferedReader br = null;
        List<Integer> input = Lists.newArrayList();
        try {
            br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                input.add(Integer.parseInt(line));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        return input;
    }

    private long process(List<Integer> numbers) {
        long inversions = 0;
        for (int i = 0; i < numbers.size() - 1; i++) {
            int current = numbers.get(i);
            for (int j = i + 1; j < numbers.size(); j++) {
                if (current > numbers.get(j)) {
                    inversions++;
                }
            }
        }

        return inversions;
    }

}
