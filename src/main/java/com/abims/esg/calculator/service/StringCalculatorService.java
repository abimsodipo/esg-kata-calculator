package com.abims.esg.calculator.service;


import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class StringCalculatorService {

    public int add(final String numbers) {
        // If an empty string is passed in, return 0
        if (!StringUtils.hasLength(numbers)) {
            return 0;
        }

        //Retrieve all numbers from String
        List<Integer> integers = getNumbersFromString(numbers);

        // Calculate the sum of numbers derived above
        return integers.stream()
                .mapToInt(Integer::intValue)
                .sum();

    }

    /** This extracts all the numbers from the list of String that meets the requirement
     * 1.Extracts all numbers between 0-9 from the list
     * 2.Handles negative numbers - throws exception
     * 3.Excludes numbers greater than 1000
     * */
    private List<Integer> getNumbersFromString(final String numbers) {

        final List<Integer> integers = new ArrayList<>();

        // The regex below finds any digit from 0-9 including negative numbers (multiple occurrence allowed)
        final Pattern pattern = Pattern.compile("-?\\d+");
        Matcher matcher = pattern.matcher((numbers));

        while (matcher.find()) {
            integers.add(Integer.parseInt(matcher.group()));
        }

        // Find all negative numbers, throw exception if any found
        excludeNegativeNumbers(integers);

        // Exclude numbers greater than 1000 from the list
        return excludeNumbersGreaterThan1000(integers);
    }

    /** Excludes number greater than 1000 from the list */
    private static List<Integer> excludeNumbersGreaterThan1000(List<Integer> integers) {
        return integers.stream()
                .filter(integer -> integer <= 1000)
                .toList();
    }

    /** Excludes negative numbers from the list */
    private static void excludeNegativeNumbers(final List<Integer> integers) {
        //Filter out negative numbers
        //Sort the filtered numbers
        //Convert list of integers to list of string
        //Convert list of String to a String joined by ","
        final String numbers = integers.stream()
                .filter(in -> in < 0)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        if (!numbers.isEmpty()) {
            throw new IllegalArgumentException(String.format("Negatives not allowed: %s", numbers));
        }
    }

}


