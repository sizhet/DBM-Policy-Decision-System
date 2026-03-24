package com.dbm.pds.policy.profile;

import java.util.Map;

public final class PolicyProfileMarkdownPrinter {

    private PolicyProfileMarkdownPrinter() {
    }

    public static String print(PolicyProfile profile) {
        StringBuilder sb = new StringBuilder();

        sb.append("# Policy Profile\n\n");
        sb.append("- **Name**: `").append(profile.getProfileName()).append("`\n");
        sb.append("- **Version**: `").append(profile.getVersion()).append("`\n");
        sb.append("- **Mode**: `").append(profile.getMode()).append("`\n\n");

        sb.append("## Weights\n\n");
        sb.append("| Policy Type | Weight |\n");
        sb.append("|---|---:|\n");
        for (Map.Entry<String, Double> e : profile.getWeights().asMap().entrySet()) {
            sb.append("| ").append(e.getKey()).append(" | ").append(e.getValue()).append(" |\n");
        }

        sb.append("\n## Metadata\n\n");
        for (Map.Entry<String, String> e : profile.getMetadata().entrySet()) {
            sb.append("- **").append(e.getKey()).append("**: ").append(e.getValue()).append("\n");
        }

        return sb.toString();
    }
}