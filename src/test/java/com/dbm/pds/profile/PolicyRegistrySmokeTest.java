package com.dbm.pds.profile;

import com.dbm.pds.policy.profile.DefaultPolicyProfiles;
import com.dbm.pds.policy.profile.InMemoryPolicyRegistry;
import com.dbm.pds.policy.profile.PolicyMode;
import org.junit.Assert;
import org.junit.Test;

public class PolicyRegistrySmokeTest {

    @Test
    public void shouldRegisterAndSwitchActiveProfiles() {
        InMemoryPolicyRegistry registry = new InMemoryPolicyRegistry();
        registry.register(DefaultPolicyProfiles.safeV1());
        registry.register(DefaultPolicyProfiles.aggressiveV1());

        Assert.assertNotNull(registry.getActive());
        Assert.assertEquals("safe", registry.getActive().getProfileName());

        registry.setActive("aggressive", "v1");
        Assert.assertEquals("aggressive", registry.getActive().getProfileName());
        Assert.assertEquals(PolicyMode.AGGRESSIVE, registry.getActive().getMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenSwitchingToMissingProfile() {
        InMemoryPolicyRegistry registry = new InMemoryPolicyRegistry();
        registry.register(DefaultPolicyProfiles.safeV1());

        registry.setActive("missing", "v1");
    }
}