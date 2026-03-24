package com.dbm.pds.learning;

public class PolicyFeedback {

    private final boolean success;
    private final double reward;

    public PolicyFeedback(boolean success, double reward) {
        this.success = success;
        this.reward = reward;
    }

    public boolean isSuccess() {
        return success;
    }

    public double getReward() {
        return reward;
    }
}