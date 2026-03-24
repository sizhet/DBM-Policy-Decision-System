package com.dbm.pds.policy.profile;

import java.util.List;

public interface PolicyRegistry {

    void register(PolicyProfile profile);

    PolicyProfile get(String profileName, String version);

    PolicyProfile getActive();

    void setActive(String profileName, String version);

    List<PolicyProfile> listProfiles();
}