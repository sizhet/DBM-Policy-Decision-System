package com.dbm.pds.evidence;

import java.util.List;

public final class EvidenceTraceReplay {

    private EvidenceTraceReplay() {
    }

    public interface Selector<Y> {
        Y select(List<Y> allowedCandidates);
    }

    public static <Y> ReplayResult<Y> replay(EvidenceTrace<Y> trace, Selector<Y> selector) {
        Y replayChosen = selector.select(trace.getAllowedCandidates());
        boolean matched = equalsNullable(trace.getChosen(), replayChosen);
        return new ReplayResult<>(trace.getChosen(), replayChosen, matched);
    }

    private static boolean equalsNullable(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }
}