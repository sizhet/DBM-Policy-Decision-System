package com.dbm.pds.demo.trajectory.audit;

import com.dbm.pds.demo.trajectory.Trajectory;
import com.dbm.pds.policy.PolicyCandidate;
import com.dbm.pds.policy.PolicyNote;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrajectoryScoreBreakdownMarkdownPrinterTest {

    @Test
    public void shouldPrintTrajectoryAuditTableWithBreakdownColumns() {
        Map<String, Double> routeABreakdown = new HashMap<>();
        routeABreakdown.put("Goal", -1.0);
        routeABreakdown.put("Risk", -2.7);

        Map<String, Double> routeBBreakdown = new HashMap<>();
        routeBBreakdown.put("Goal", -2.0);
        routeBBreakdown.put("Risk", -0.3);

        List<PolicyCandidate<Trajectory>> candidates = Arrays.asList(
                new PolicyCandidate<>(
                        new Trajectory("Route-A", 5, 0.9),
                        true,
                        -3.7,
                        Arrays.asList(
                                new PolicyNote("Goal", "TrajectoryGoalPolicy", "Prefer shorter route"),
                                new PolicyNote("Risk", "TrajectoryRiskPolicy", "Risk penalty")
                        ),
                        routeABreakdown
                ),
                new PolicyCandidate<>(
                        new Trajectory("Route-B", 10, 0.1),
                        true,
                        -2.3,
                        Arrays.asList(
                                new PolicyNote("Goal", "TrajectoryGoalPolicy", "Prefer shorter route"),
                                new PolicyNote("Risk", "TrajectoryRiskPolicy", "Risk penalty")
                        ),
                        routeBBreakdown
                )
        );

        String markdown = TrajectoryScoreBreakdownMarkdownPrinter.print(candidates);

        Assert.assertTrue(markdown.contains("| Route | Length | Risk | Allowed | Goal | RiskScore | Strategy | Final Score | Notes |"));
        Assert.assertTrue(markdown.contains("| Route-A | 5 | 0.9 | true | -1.0 | -2.7 | 0.0 | -3.7 |"));
        Assert.assertTrue(markdown.contains("| Route-B | 10 | 0.1 | true | -2.0 | -0.3 | 0.0 | -2.3 |"));
        Assert.assertTrue(markdown.contains("TrajectoryGoalPolicy"));
        Assert.assertTrue(markdown.contains("TrajectoryRiskPolicy"));
    }

    @Test
    public void shouldHandleEmptyBreakdownValuesAsZero() {
        List<PolicyCandidate<Trajectory>> candidates = Collections.singletonList(
                new PolicyCandidate<>(
                        new Trajectory("Route-C", 7, 0.3),
                        true,
                        -1.5,
                        Collections.<PolicyNote>emptyList(),
                        Collections.<String, Double>emptyMap()
                )
        );

        String markdown = TrajectoryScoreBreakdownMarkdownPrinter.print(candidates);

        Assert.assertTrue(markdown.contains("| Route-C | 7 | 0.3 | true | 0.0 | 0.0 | 0.0 | -1.5 |  |"));
    }
}