package com.dbm.pds.policy;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PolicyEvaluationMarkdownPrinterTest {

    @Test
    public void shouldPrintMarkdownTableHeaderAndRows() {
        List<PolicyCandidate<String>> candidates = Arrays.asList(
                new PolicyCandidate<>(
                        "HELLO",
                        true,
                        0.5,
                        Arrays.asList(
                                new PolicyNote("Goal", "PreferShortGoalPolicy", "Prefer shorter candidates"),
                                new PolicyNote("Strategy", "PreferFirstStrategyPolicy", "Prefer direct state-preserving candidate")
                        )
                ),
                new PolicyCandidate<>(
                        "HELLO!!!",
                        true,
                        -2.8,
                        Arrays.asList(
                                new PolicyNote("Risk", "SimpleSuffixRiskPolicy", "Penalize high-volatility suffix")
                        )
                ),
                new PolicyCandidate<>(
                        "HELLO LONG_SUFFIX",
                        false,
                        0.0,
                        Arrays.asList(
                                new PolicyNote("Constraint", "MaxLengthConstraintPolicy", "Candidate length exceeds maxLength=15")
                        )
                )
        );

        String markdown = PolicyEvaluationMarkdownPrinter.print(candidates);

        Assert.assertTrue(markdown.contains("| Candidate | Allowed | Policy Score Adjustment | Notes |"));
        Assert.assertTrue(markdown.contains("|---|---:|---:|---|"));

        Assert.assertTrue(markdown.contains("| HELLO | true | 0.5 |"));
        Assert.assertTrue(markdown.contains("| HELLO!!! | true | -2.8 |"));
        Assert.assertTrue(markdown.contains("| HELLO LONG_SUFFIX | false | 0.0 |"));
    }

    @Test
    public void shouldJoinMultipleNotesWithHtmlBreak() {
        List<PolicyCandidate<String>> candidates = Collections.singletonList(
                new PolicyCandidate<>(
                        "HELLO",
                        true,
                        0.5,
                        Arrays.asList(
                                new PolicyNote("Goal", "PreferShortGoalPolicy", "Prefer shorter candidates"),
                                new PolicyNote("Strategy", "PreferFirstStrategyPolicy", "Prefer direct state-preserving candidate")
                        )
                )
        );

        String markdown = PolicyEvaluationMarkdownPrinter.print(candidates);

        Assert.assertTrue(markdown.contains(" <br> "));
        Assert.assertTrue(markdown.contains("[Goal] PreferShortGoalPolicy - Prefer shorter candidates"));
        Assert.assertTrue(markdown.contains("[Strategy] PreferFirstStrategyPolicy - Prefer direct state-preserving candidate"));
    }

    @Test
    public void shouldEscapePipeCharactersInCandidateAndNotes() {
        List<PolicyCandidate<String>> candidates = Collections.singletonList(
                new PolicyCandidate<>(
                        "A|B",
                        true,
                        1.0,
                        Arrays.asList(
                                new PolicyNote("Goal", "PipePolicy", "contains | symbol")
                        )
                )
        );

        String markdown = PolicyEvaluationMarkdownPrinter.print(candidates);

        Assert.assertTrue(markdown.contains("A\\|B"));
        Assert.assertTrue(markdown.contains("contains \\| symbol"));
    }

    @Test
    public void shouldHandleEmptyNotes() {
        List<PolicyCandidate<String>> candidates = Collections.singletonList(
                new PolicyCandidate<>(
                        "HELLO",
                        true,
                        0.0,
                        Collections.<PolicyNote>emptyList()
                )
        );

        String markdown = PolicyEvaluationMarkdownPrinter.print(candidates);

        Assert.assertTrue(markdown.contains("| HELLO | true | 0.0 |  |"));
    }

    @Test
    public void shouldPrintFalseAllowedStateCorrectly() {
        List<PolicyCandidate<String>> candidates = Collections.singletonList(
                new PolicyCandidate<>(
                        "TOO_LONG",
                        false,
                        0.0,
                        Arrays.asList(
                                new PolicyNote("Constraint", "MaxLengthConstraintPolicy", "Rejected by max length")
                        )
                )
        );

        String markdown = PolicyEvaluationMarkdownPrinter.print(candidates);

        Assert.assertTrue(markdown.contains("| TOO_LONG | false | 0.0 |"));
        Assert.assertTrue(markdown.contains("[Constraint] MaxLengthConstraintPolicy - Rejected by max length"));
    }
}