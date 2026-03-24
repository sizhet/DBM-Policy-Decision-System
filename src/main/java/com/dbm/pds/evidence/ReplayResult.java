package com.dbm.pds.evidence;

public class ReplayResult<Y> {

    private final Y originalChosen;
    private final Y replayChosen;
    private final boolean matched;

    public ReplayResult(Y originalChosen, Y replayChosen, boolean matched) {
        this.originalChosen = originalChosen;
        this.replayChosen = replayChosen;
        this.matched = matched;
    }

    public Y getOriginalChosen() {
        return originalChosen;
    }

    public Y getReplayChosen() {
        return replayChosen;
    }

    public boolean isMatched() {
        return matched;
    }
}