package com.abims.esg.calculator.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Component
public class StringCalculatorServiceTest {
    final private StringCalculatorService  stringCalculatorService;

    @Autowired
    public StringCalculatorServiceTest(StringCalculatorService stringCalculatorService) {
        this.stringCalculatorService = stringCalculatorService;
    }

    //Test that passing an empty String returns 0
    @Test
    public void testNoNumberSpecified() {

        final int sum = stringCalculatorService.add("");

        assertThat("Sum is incorrect", sum, is(0));
    }

    //Test adding single number
    @Test
    public void testSingleNumber() {
        final int sum = stringCalculatorService.add("2");
        assertThat("Sum is incorrect", sum, is(2));
    }

    //Test adding multiple numbers
    @Test
    public void testMultipleNumbers() {
        final int sum = stringCalculatorService.add("1,2");
        assertThat("Sum is incorrect", sum, is(3));
    }

    //Test adding multiple numbers which includes 0
    @Test
    public void testMultipleNumbers_WithZero() {
        final int sum = stringCalculatorService.add("1,2,0");

        assertThat("Sum is incorrect", sum, is(3));
    }

    //Test adding multiple numbers which includes a newLine
    @Test
    public void testMultipleNumbers_WithNewLine() {
        final int sum = stringCalculatorService.add("1\n,2,\n0");

        assertThat("Sum is incorrect", sum, is(3));
    }

    //Test adding multiple numbers with different delimiter
    @Test
    public void testMultipleNumbers_WithDelimiter() {
        final int sum = stringCalculatorService.add("//;\n1,2");

        assertThat("Sum is incorrect", sum, is(3));
    }

    //Test adding multiple numbers with different delimiter
    @Test
    public void testMultipleNumbers_WithDifferentDelimiter2() {
        final int sum = stringCalculatorService.add("//[|||]\n1|||2|||3" );

        assertThat("Sum is incorrect", sum, is(6));
    }

    //Test adding multiple numbers with different delimiter
    @Test
    public void testMultipleNumbers_WithDifferentDelimiter3() {
        final int sum = stringCalculatorService.add("//[|][%]\n1|2%3"  );

        assertThat("Sum is incorrect", sum, is(6));
    }

    //Test adding multiple numbers with different delimiter any length
    @Test
    public void testMultipleNumbersAnyLength() {
        final int sum = stringCalculatorService.add("//[|][%]\n1|2%3//[|][%]\n1|2%3//[|][%]\n1|2%3");

        assertThat("Sum is incorrect", sum, is(18));
    }

    //Test adding numbers which includes a negative number throws an exception
    @Test
    public void testNegativeNumbers() {

        final RuntimeException exception = assertThrows(RuntimeException.class,
                () -> stringCalculatorService.add("-1, -6, 3"));

        assertThat("Message is incorrect", exception.getMessage(), is("Negatives not allowed: -6,-1"));
    }

    //Test adding numbers which includes a negative with different delimiter number throws an exception
    @Test
    public void testNegativeNumbersWithDifferentDelimiter() {

        final RuntimeException exception = assertThrows(RuntimeException.class,
                () -> stringCalculatorService.add("//[|][%]\n1|-2%3//[|][%]\n1|2%-3//[|][%]\n1|2%3"));

        assertThat("Message is incorrect", exception.getMessage(), is("Negatives not allowed: -3,-2"));
    }

    //Test numbers greater than 1000 is excluded from the sum
    @Test
    public void testNumbersGreaterThan1000() {
        final int sum = stringCalculatorService.add("1001, 6, 3");

        assertThat("Sum is incorrect", sum, is(9));
    }

    //Test number 1000 is included in the sum
    @Test
    public void testNumbersIs1000() {
        final int sum = stringCalculatorService.add("1000, 6, 3");

        assertThat("Sum is incorrect", sum, is(1009));
    }
}
