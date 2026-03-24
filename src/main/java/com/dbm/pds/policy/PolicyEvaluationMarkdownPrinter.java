package com.dbm.pds.policy;

import java.util.List;

public class PolicyEvaluationMarkdownPrinter {

    public static <Y> String print(List<PolicyCandidate<Y>> candidates) {
        StringBuilder sb = new StringBuilder();

        sb.append("| Candidate | Allowed | Policy Score Adjustment | Notes |\n");
        sb.append("|---|---:|---:|---|\n");

        for (PolicyCandidate<Y> c : candidates) {
            sb.append("| ")
              .append(escape(c.getCandidate()))
              .append(" | ")
              .append(c.isAllowed())
              .append(" | ")
              .append(c.getPolicyScoreAdjustment())
              .append(" | ")
              .append(escape(joinNotes(c.getNotes())))
              .append(" |\n");
        }

        return sb.toString();
    }

    private static String joinNotes(List<PolicyNote> notes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < notes.size(); i++) {
            if (i > 0) {
                sb.append(" <br> ");
            }
            sb.append(notes.get(i).toString());
        }
        return sb.toString();
    }

    private static String escape(Object value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value).replace("|", "\\|");
    }
}