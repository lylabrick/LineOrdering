package com.example.lineordering;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        String[] rules = {"A>B", "A>C", "C>J", "C>E", "J>F"};
        int sortedRules = lineOrdering(rules);

        System.out.println("Original array: " + String.join(", ", rules));
        System.out.println("Sorted array valid combinations: " + sortedRules);
    }

    private static int lineOrdering(String[] rules) {

        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();

        for (String rel : rules) {
            char a = rel.charAt(0);
            char b = rel.charAt(2);

            graph.putIfAbsent(a, new HashSet<>());
            graph.putIfAbsent(b, new HashSet<>());

            inDegree.putIfAbsent(a, 0);
            inDegree.putIfAbsent(b, 0);

        }

        for (String rel : rules) {
            char a = rel.charAt(0);
            char op = rel.charAt(1);
            char b = rel.charAt(2);

            char from, to;

            if (op == '>') {
                from = a;
                to = b;
            } else {
                from = b;
                to = a;
            }

            if (!graph.get(from).contains(to)) {
                graph.get(from).add(to);
                inDegree.put(to, inDegree.get(to) + 1);
            }
        }

        return countTopologicalOrders(graph, inDegree);

    }

    private static int countTopologicalOrders(
            Map<Character, Set<Character>> graph,
            Map<Character, Integer> inDegree) {

        List<Character> available = new ArrayList();

        for (Map.Entry<Character, Integer> e : inDegree.entrySet()) {
            if (e.getValue() == 0) {
                available.add(e.getKey());
            }
        }

        if (available.isEmpty()) {
            return inDegree.
                    values().
                    stream().
                    allMatch(v -> v == 0) ? 1 : 0;
        }

        int count = 0;

        for (char node : available) {
            inDegree.remove(node);
            List<Character> reduced = new ArrayList<>();

            //graph.get(node) obtiene los nodos vecinos del nodo que estoy eliminando en inDegree
            for (char neighbor : graph.get(node)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                reduced.add(neighbor);
            }

            System.out.println("Node: " + node + ", Available: " + available + ", InDegree: " + inDegree);
            count += countTopologicalOrders(graph, inDegree);

            inDegree.put(node, 0);
            for (char neighbor : reduced) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }

        }

        return count;
    }
}
