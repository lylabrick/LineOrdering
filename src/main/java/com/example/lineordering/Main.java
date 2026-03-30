package com.example.lineordering;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        int[] numbers = {5, 3, 9, 1, 6};
        int[] sortedNumbers = sortArray(numbers);

        System.out.println("Original array: " + Arrays.stream(numbers).mapToObj(String::valueOf).collect(Collectors.joining(", ")));
        System.out.println("Sorted array: " + Arrays.stream(sortedNumbers).mapToObj(String::valueOf).collect(Collectors.joining(", ")));
    }

    public static int[] sortArray(int[] array) {
        return Arrays.stream(array).sorted().toArray();
    }
}
