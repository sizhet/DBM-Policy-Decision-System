package com.dbm.pds.policy;

public class PolicyNote {

    private final String policyType;
    private final String policyName;
    private final String message;

    public PolicyNote(String policyType, String policyName, String message) {
        this.policyType = policyType;
        this.policyName = policyName;
        this.message = message;
    }

    public String getPolicyType() {
        return policyType;
    }

    public String getPolicyName() {
        return policyName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "[" + policyType + "] " + policyName + " - " + message;
    }
}