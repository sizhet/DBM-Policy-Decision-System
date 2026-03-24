package com.dbm.pds.trace;

public class DecisionTracePrinter {

    public static <Y> void print(DecisionTrace<Y> trace) {

        System.out.println("=== PDS Decision Trace ===");

        System.out.println("State:");
        System.out.println("  " + trace.getState());

        System.out.println("\nCandidates:");
        trace.getCandidates().forEach(c -> System.out.println("  - " + c));

        System.out.println("\nAfter Policy:");
        trace.getFilteredCandidates().forEach(c -> System.out.println("  - " + c));

        System.out.println("\nChosen:");
        System.out.println("  " + trace.getChosen());

        System.out.println("\nPolicy Note:");
        System.out.println("  " + trace.getPolicyNote());

        System.out.println("===========================");
    }
}