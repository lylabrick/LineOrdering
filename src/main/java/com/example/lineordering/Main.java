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

        System.out.println("\n=== Entrando a countTopologicalOrders ===");
        System.out.println("InDegree actual: " + inDegree);

        List<Character> available = new ArrayList();

        for (Map.Entry<Character, Integer> e : inDegree.entrySet()) {
            if (e.getValue() == 0) {
                available.add(e.getKey());
            }
        }

        System.out.println("Nodos disponibles (inDegree = 0): " + available);

        if (available.isEmpty()) {
            System.out.println("❌ Lista disponible vacía. Verificando si todos los inDegree son 0...");
            int result = inDegree.
                    values().
                    stream().
                    allMatch(v -> v == 0) ? 1 : 0;
            System.out.println("Resultado: " + result + " (1=válido, 0=inválido)");
            return result;
        }

        int count = 0;

        for (char node : available) {

            System.out.println("\n>>> Procesando nodo: " + node);
            inDegree.remove(node);
            System.out.println("    Nodo eliminado de inDegree: " + node);

            List<Character> reduced = new ArrayList<>();

            //graph.get(node) obtiene los nodos vecinos del nodo que estoy eliminando en inDegree
            System.out.println("    Por cada vecino del nodo " + node + " le resto en 1 a inDegree");
            for (char neighbor : graph.get(node)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                reduced.add(neighbor);
            }

            System.out.println("    Vecinos procesados: " + reduced);
            System.out.println("    InDegree después de reducir: " + inDegree);
            System.out.println("Node: " + node + ", Available: " + available + ", InDegree: " + inDegree);

            int count2 = countTopologicalOrders(graph, inDegree);

            System.out.println("    Este es el resultado del  countTopologicalOrders en nodo " + node + ", count acumulado: " + count2);

            count += count2;
            System.out.println("    Retornando de recursión para nodo " + node + ", count acumulado: " + count);
            System.out.println("    Valor de inDegree luego de volver de la recursion " + inDegree);


            System.out.println("    Agrego a inDegree en la posicion del nodo " + node + " con el valor 0");
            inDegree.put(node, 0);

            System.out.println("    Por cada vecino en reduced, aumento su inDegree en 1");
            System.out.println("    Contenido de reduced: " + reduced);

            for (char neighbor : reduced) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
            System.out.println("    Estado restaurado para backtracking");
        }

        System.out.println("\n=== Saliendo de countTopologicalOrders, count total: " + count);

        return count;
    }
}
