package org.reborn.FeatherDisguise.enums;

public enum ViewType {

    // disguise (& all related entities) visible to player(s). the default playerEntity is hidden to player(s)
    CAN_SEE_DISGUISE,

    // disguise (& all related entities) are hidden. the default playerEntity is visible to player(s)
    CANNOT_SEE_DISGUISE,

    // disguise (& all related entities) are hidden as well as the default playerEntity. clients cannot see anything.
    HARD_HIDDEN_DISGUISE;
}
