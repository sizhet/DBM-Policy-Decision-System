package com.dbm.pds.policy.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryPolicyRegistry implements PolicyRegistry {

    private final Map<String, PolicyProfile> profiles = new HashMap<>();
    private String activeKey;

    @Override
    public void register(PolicyProfile profile) {
        profiles.put(profile.getQualifiedName(), profile);
        if (activeKey == null) {
            activeKey = profile.getQualifiedName();
        }
    }

    @Override
    public PolicyProfile get(String profileName, String version) {
        return profiles.get(key(profileName, version));
    }

    @Override
    public PolicyProfile getActive() {
        return activeKey == null ? null : profiles.get(activeKey);
    }

    @Override
    public void setActive(String profileName, String version) {
        String key = key(profileName, version);
        if (!profiles.containsKey(key)) {
            throw new IllegalArgumentException("Profile not found: " + key);
        }
        activeKey = key;
    }

    @Override
    public List<PolicyProfile> listProfiles() {
        return new ArrayList<>(profiles.values());
    }

    private String key(String profileName, String version) {
        return profileName + ":" + version;
    }
}