package com.darkblade12.itemslotmachine.item;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public final class ItemList extends ArrayList<ItemStack> implements Cloneable {

    private static final long serialVersionUID = -4142733645445441396L;
    private static final Random RANDOM = new Random();
    //private static final String FORMAT = "(\\d+|[\\w\\s]+)(-\\d+){0,2}(, (\\d+|[\\w\\s]+)(-\\d+){0,2})*";
    private static final String FORMAT = "([0-9A-Z_]+)(-\\d+)?(, ([0-9A-Z_]+)(-\\d+)?)*";

    public ItemList() {
        super();
    }

    private ItemList(Collection<ItemStack> c) {
        super(c);
    }

    public static ItemList fromString(String s, boolean amount) throws IllegalArgumentException {
        if (!s.matches(FORMAT)) {
            throw new IllegalArgumentException("has an invalid format");
        }
        ItemList list = new ItemList();
        for (String i : s.split(", ")) {
            list.add(ItemFactory.fromString(i, amount), false);
        }
        return list;
    }

    public static ItemList fromString(String s) throws IllegalArgumentException {
        return fromString(s, true);
    }

    public static boolean hasEnoughSpace(Player p, ItemStack i) {
        int s = 0;
        for (ItemStack is : p.getInventory().getContents()) {
            if (is == null) {
                s += 64;
            } else if (is.isSimilar(i)) {
                s += 64 - is.getAmount();
            }
        }
        return s >= i.getAmount();
    }

    private boolean add(ItemStack e, boolean stack) {
        if (stack) {
            for (ItemStack s : this) {
                if (s.isSimilar(e)) {
                    s.setAmount(s.getAmount() + e.getAmount());
                    return true;
                }
            }
        }
        return super.add(e.clone());
    }

    @Override
    public boolean add(ItemStack e) {
        return add(e, true);
    }

    @Override
    public boolean addAll(Collection<? extends ItemStack> c) {
        for (ItemStack i : c) {
            add(i);
        }
        return true;
    }

    private void removeRandom() {
        int size = size();
        if (size > 0) {
            remove(RANDOM.nextInt(size));
        }
    }

    public void removeRandom(int amount) {
        while (amount > 0) {
            removeRandom();
            amount--;
        }
    }

    public void doubleAmounts() {
        for (int i = 0; i < size(); i++) {
            ItemStack s = get(i);
            s.setAmount(s.getAmount() * 2);
            set(i, s);
        }
    }

    public void distribute(Player p) {
        Location loc = p.getLocation();
        World w = loc.getWorld();
        for (int i = 0; i < size(); i++) {
            ItemStack c = get(i).clone();
            if (hasEnoughSpace(p, c)) {
                p.getInventory().addItem(c);
            } else {
                w.dropItemNaturally(loc, c);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (ItemStack itemStack : this) {
            if (s.length() > 0) {
                s.append(", ");
            }
            s.append(ItemFactory.toString(itemStack));
        }
        return s.toString();
    }

    @Override
    public ItemList clone() {
        ItemList itemStacks = (ItemList) super.clone();
        return new ItemList(this);
    }
}
