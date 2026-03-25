package com.dbm.pds.demo.trajectory.audit;

import com.dbm.pds.demo.trajectory.Trajectory;
import com.dbm.pds.policy.PolicyCandidate;
import com.dbm.pds.policy.PolicyNote;

import java.util.List;

public final class TrajectoryScoreBreakdownMarkdownPrinter {

    private TrajectoryScoreBreakdownMarkdownPrinter() {
    }

    public static String print(List<PolicyCandidate<Trajectory>> candidates) {
        StringBuilder sb = new StringBuilder();

        sb.append("| Route | Length | Risk | Allowed | Goal | RiskScore | Strategy | Final Score | Notes |\n");
        sb.append("|---|---:|---:|---:|---:|---:|---:|---:|---|\n");

        for (PolicyCandidate<Trajectory> c : candidates) {
            Trajectory t = c.getCandidate();

            sb.append("| ")
              .append(escape(t.getName()))
              .append(" | ")
              .append(t.getLength())
              .append(" | ")
              .append(t.getRisk())
              .append(" | ")
              .append(c.isAllowed())
              .append(" | ")
              .append(c.getBreakdownValue("Goal"))
              .append(" | ")
              .append(c.getBreakdownValue("Risk"))
              .append(" | ")
              .append(c.getBreakdownValue("Strategy"))
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