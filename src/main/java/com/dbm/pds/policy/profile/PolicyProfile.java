package com.dbm.pds.policy.profile;

import com.dbm.pds.learning.PolicyWeights;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PolicyProfile {

    private final String profileName;
    private final String version;
    private final PolicyMode mode;
    private final PolicyWeights weights;
    private final Map<String, String> metadata;

    public PolicyProfile(String profileName,
                         String version,
                         PolicyMode mode,
                         PolicyWeights weights,
                         Map<String, String> metadata) {
        this.profileName = profileName;
        this.version = version;
        this.mode = mode;
        this.weights = weights;
        this.metadata = new HashMap<>(metadata);
    }

    public String getProfileName() {
        return profileName;
    }

    public String getVersion() {
        return version;
    }

    public PolicyMode getMode() {
        return mode;
    }

    public PolicyWeights getWeights() {
        return weights;
    }

    public Map<String, String> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }

    public String getQualifiedName() {
        return profileName + ":" + version;
    }
}