package com.dbm.pds.evidence;

public final class EvidenceTraceMarkdownPrinter {

    private EvidenceTraceMarkdownPrinter() {
    }

    public static <Y> String print(EvidenceTrace<Y> trace) {
        StringBuilder sb = new StringBuilder();

        sb.append("# Evidence Trace\n\n");
        sb.append("- **Input**: `").append(escape(trace.getInput())).append("`\n");
        sb.append("- **State**: `").append(escape(trace.getState())).append("`\n");
        sb.append("- **Chosen**: `").append(escape(trace.getChosen())).append("`\n");
        sb.append("- **Trace Hash**: `").append(trace.getTraceHash()).append("`\n");
        sb.append("- **Valid**: `").append(trace.isValid()).append("`\n\n");

        sb.append("## Allowed Candidates\n\n");
        for (Y c : trace.getAllowedCandidates()) {
            sb.append("- `").append(escape(c)).append("`\n");
        }

        sb.append("\n## Notes\n\n");
        for (String note : trace.getNotes()) {
            sb.append("- ").append(note).append("\n");
        }

        if (!trace.getInvariantViolations().isEmpty()) {
            sb.append("\n## Invariant Violations\n\n");
            for (String v : trace.getInvariantViolations()) {
                sb.append("- ").append(v).append("\n");
            }
        }

        return sb.toString();
    }

    private static String escape(Object v) {
        return v == null ? "null" : String.valueOf(v).replace("`", "\\`");
    }
}