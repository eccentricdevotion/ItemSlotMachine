package com.darkblade12.itemslotmachine.slotmachine.combo;

import com.darkblade12.itemslotmachine.slotmachine.combo.types.ItemPotCombo;
import com.darkblade12.itemslotmachine.slotmachine.combo.types.MoneyPotCombo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Action {
    MULTIPLY_POT_AND_DISTRIBUTE(true, MoneyPotCombo.class),
    ADD_TO_POT_AND_DISTRIBUTE(true, MoneyPotCombo.class, ItemPotCombo.class),
    DISTRIBUTE_POT(false, MoneyPotCombo.class, ItemPotCombo.class),
    DISTRIBUTE_INDEPENDENT_MONEY(true, MoneyPotCombo.class),
    DOUBLE_POT_ITEMS_AND_DISTRIBUTE(false, ItemPotCombo.class),
    DISTRIBUTE_INDEPENDENT_ITEMS(true, ItemPotCombo.class);

    private final boolean requiresInput;
    private final List<Class<? extends Combo>> applicable;
    private static final Map<String, Action> NAME_MAP = new HashMap<>();

    static {
        for (Action a : values()) {
            NAME_MAP.put(a.name(), a);
        }
    }

    @SafeVarargs
    Action(boolean requiresInput, Class<? extends Combo>... applicable) {
        this.requiresInput = requiresInput;
        this.applicable = Arrays.asList(applicable);
    }

    public boolean requiresInput() {
        return requiresInput;
    }

    public boolean isApplicable(Class<? extends Combo> clazz) {
        return applicable.contains(clazz);
    }

    public static Action fromName(String name) {
        return name == null ? null : NAME_MAP.get(name.toUpperCase());
    }
}
