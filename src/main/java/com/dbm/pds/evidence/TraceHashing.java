package com.dbm.pds.evidence;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public final class TraceHashing {

    private TraceHashing() {
    }

    public static <Y> String sha256(String input,
                                    String state,
                                    List<Y> rawCandidates,
                                    List<Y> allowedCandidates,
                                    Y chosen,
                                    List<String> notes) {
        StringBuilder sb = new StringBuilder();
        sb.append("input=").append(input).append('\n');
        sb.append("state=").append(state).append('\n');
        sb.append("raw=").append(rawCandidates).append('\n');
        sb.append("allowed=").append(allowedCandidates).append('\n');
        sb.append("chosen=").append(chosen).append('\n');
        sb.append("notes=").append(notes).append('\n');
        return sha256(sb.toString());
    }

    public static String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return toHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}