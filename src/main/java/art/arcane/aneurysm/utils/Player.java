package art.arcane.aneurysm.utils;

import art.arcane.aneurysm.utils.runnable.J;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public static void damageOffHand(org.bukkit.entity.Player p, int damage) {
        ItemStack is = p.getInventory().getItemInOffHand();
        ItemMeta im = is.getItemMeta();

        if (im == null) {
            return;
        }

        if (im.isUnbreakable()) {
            return;
        }

        Damageable dm = (Damageable) im;
        dm.setDamage(dm.getDamage() + damage);

        if (dm.getDamage() > is.getType().getMaxDurability()) {
            p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
            return;
        }

        is.setItemMeta(im);
        p.getInventory().setItemInOffHand(is);
    }

    public static Block getRightBlock(org.bukkit.entity.Player p, Block b) {
        Location l = p.getLocation();
        float yaw = l.getYaw();
        // Make sure yaw is in the range 0 to 360
        while (yaw < 0) {
            yaw += 360;
        }
        yaw = yaw % 360;
        // The player's yaw is their rotation in the world,
        // so, we can use that to get the right face of a block!
        BlockFace rightFace;
        // if the player is facing SE to SW
        if (yaw < 45 || yaw >= 315) {
            rightFace = BlockFace.EAST;
            return b.getRelative(rightFace);
        }
        // if the player is facing SW to NW
        else if (yaw < 135) {
            rightFace = BlockFace.SOUTH;
            return b.getRelative(rightFace);
        }
        // if the player is facing NW to NE
        else if (yaw < 225) {
            rightFace = BlockFace.WEST;
            return b.getRelative(rightFace);
        }
        // if the player is facing NE to SE
        else if (yaw < 315) {
            rightFace = BlockFace.NORTH;
            return b.getRelative(rightFace);
        } else {
            return null;
        }
    }

    public static Block getLeftBlock(org.bukkit.entity.Player p, Block b) {
        Location l = p.getLocation();
        float yaw = l.getYaw();

        // Make sure yaw is in the range 0 to 360
        while (yaw < 0) {
            yaw += 360;
        }
        yaw = yaw % 360;
        // The player's yaw is their rotation in the world,
        // so, we can use that to get the right face of a block!
        BlockFace leftFace;
        // if the player is facing SE to SW
        if (yaw < 45 || yaw >= 315) {
            leftFace = BlockFace.WEST;
            return b.getRelative(leftFace);
        }
        // if the player is facing SW to NW
        else if (yaw < 135) {
            leftFace = BlockFace.NORTH;
            return b.getRelative(leftFace);
        }
        // if the player is facing NW to NE
        else if (yaw < 225) {
            leftFace = BlockFace.EAST;
            return b.getRelative(leftFace);
        }
        // if the player is facing NE to SE
        else if (yaw < 315) {
            leftFace = BlockFace.SOUTH;
            return b.getRelative(leftFace);
        } else {
            return null;
        }
    }


    public static void safeGiveItem(org.bukkit.entity.Player player, Entity itemEntity, ItemStack is) {
        EntityPickupItemEvent e = new EntityPickupItemEvent(player, (Item) itemEntity, 0);
        Bukkit.getPluginManager().callEvent(e);
        if (!e.isCancelled()) {
            itemEntity.remove();
            if (!player.getInventory().addItem(is).isEmpty()) {
                player.getWorld().dropItem(player.getLocation(), is);
            }
        }
    }


    public static void safeGiveItem(org.bukkit.entity.Player player, ItemStack item) {
        if (!player.getInventory().addItem(item).isEmpty()) {
            player.getWorld().dropItem(player.getLocation(), item);
        }
    }


    public static void damageHand(org.bukkit.entity.Player p, int damage) {
        ItemStack is = p.getInventory().getItemInMainHand();
        ItemMeta im = is.getItemMeta();

        if (im == null) {
            return;
        }

        if (im.isUnbreakable()) {
            return;
        }

        Damageable dm = (Damageable) im;
        dm.setDamage(dm.getDamage() + damage);

        if (dm.getDamage() > is.getType().getMaxDurability()) {
            p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
            return;
        }

        is.setItemMeta(im);
        p.getInventory().setItemInMainHand(is);
    }

    /**
     * Attempts to "damage" an item.
     * 1. If the item is null, null is returned
     * 2. If the item doesnt have durability, (damage) amount will be consumed from the stack, null will be returned if
     * more consumed than amount
     * 3. If the item has durability, the damage will be consuemd and return the item affected, OR null if it broke
     *
     * @param item   the item (tool)
     * @param damage the damage to cause
     * @return the damaged item or null if destroyed
     */
    public static ItemStack damage(ItemStack item, int damage) {
        if (item == null) {
            return null;
        }

        if (item.getItemMeta() == null) {
            if (item.getAmount() == 1) {
                return null;
            }

            item = item.clone();
            item.setAmount(item.getAmount() - 1);
            return item;
        }

        if (item.getItemMeta() instanceof Damageable d) {
            if (d.getDamage() + 1 > item.getType().getMaxDurability()) {
                return null;
            }

            d.setDamage(d.getDamage() + 1);
            item = item.clone();
            item.setItemMeta(d);
            return item;
        } else {
            if (item.getAmount() == 1) {
                return null;
            }

            item = item.clone();
            item.setAmount(item.getAmount() - 1);

            return item;
        }
    }

    public static void decrementItemstack(ItemStack hand, org.bukkit.entity.Player p) {
        if (hand.getAmount() > 1) {
            hand.setAmount(hand.getAmount() - 1);
        } else {
            p.getInventory().setItemInMainHand(null);
        }
    }


    public static void setExp(org.bukkit.entity.Player p, int exp) {
        p.setExp(0);
        p.setLevel(0);
        p.setTotalExperience(0);

        if (exp <= 0) {
            return;
        }

        giveExp(p, exp);
    }

    public static void giveExp(org.bukkit.entity.Player p, int exp) {
        while (exp > 0) {
            int xp = getExpToLevel(p) - getExp(p);
            if (xp > exp) {
                xp = exp;
            }
            p.giveExp(xp);
            exp -= xp;
        }
    }

    public static void takeExp(org.bukkit.entity.Player p, int exp) {
        takeExp(p, exp, true);
    }

    public static void takeExp(org.bukkit.entity.Player p, int exp, boolean fromTotal) {
        int xp = getTotalExp(p);

        if (fromTotal) {
            xp -= exp;
        } else {
            int m = getExp(p) - exp;
            if (m < 0) {
                m = 0;
            }
            xp -= getExp(p) + m;
        }

        setExp(p, xp);
    }

    public static int getExp(org.bukkit.entity.Player p) {
        return (int) (getExpToLevel(p) * p.getExp());
    }

    public static int getTotalExp(org.bukkit.entity.Player p) {
        return getTotalExp(p, false);
    }

    public static int getTotalExp(org.bukkit.entity.Player p, boolean recalc) {
        if (recalc) {
            recalcTotalExp(p);
        }
        return p.getTotalExperience();
    }

    public static int getLevel(org.bukkit.entity.Player p) {
        return p.getLevel();
    }

    public static int getExpToLevel(org.bukkit.entity.Player p) {
        return p.getExpToLevel();
    }

    public static int getExpToLevel(int level) {
        return level >= 30 ? 62 + (level - 30) * 7 : (level >= 15 ? 17 + (level - 15) * 3 : 17);
    }

    public static void recalcTotalExp(org.bukkit.entity.Player p) {
        int total = getExp(p);
        for (int i = 0; i < p.getLevel(); i++) {
            total += getExpToLevel(i);
        }
        p.setTotalExperience(total);
    }


    /**
     * Takes a custom amount of the item stack exact type (Ignores the item amount)
     *
     * @param inv    the inv
     * @param is     the item ignore the amount
     * @param amount the amount to use
     * @return true if taken, false if not (missing)
     */
    public static boolean takeAll(Inventory inv, ItemStack is, int amount) {
        ItemStack isf = is.clone();
        isf.setAmount(amount);
        return takeAll(inv, is);
    }

    /**
     * Take one of an exact type ignoring the item stack amount
     *
     * @param inv the inv
     * @param is  the item ignoring the amount
     * @return true if taken, false if diddnt
     */
    public static boolean takeOne(Inventory inv, ItemStack is, int amount) {
        return takeAll(inv, is, 1);
    }

    /**
     * Take a specific amount of an EXACT META TYPE from an inventory
     *
     * @param inv the inv
     * @param is  uses the amount
     * @return returns false if it couldnt get enough (and none was taken)
     */
    public static boolean takeAll(Inventory inv, ItemStack is) {
        ItemStack[] items = inv.getStorageContents();

        int take = is.getAmount();

        for (int ii = 0; ii < items.length; ii++) {
            ItemStack i = items[ii];

            if (i == null) {
                continue;
            }

            if (i.isSimilar(is)) {
                if (take > i.getAmount()) {
                    i.setAmount(i.getAmount() - take);
                    items[ii] = i;
                    take = 0;
                    break;
                } else {
                    items[ii] = null;
                    take -= i.getAmount();
                }
            }
        }

        if (take > 0) {
            return false;
        }

        inv.setStorageContents(items);
        return true;
    }

    public static void addPotionStacks(org.bukkit.entity.Player p, PotionEffectType potionEffect, int amplifier, int duration, boolean overlap, Plugin plugin) {
        List<PotionEffect> activeEffects = new ArrayList<>(p.getActivePotionEffects());

        for (PotionEffect activeEffect : activeEffects) {
            if (activeEffect.getType() == potionEffect) {
                if (!overlap) {
                    return; // don't modify the effect if overlap is false
                }
                // modify the effect if overlap is true
                int newDuration = activeEffect.getDuration() + duration;
                int newAmplifier = Math.max(activeEffect.getAmplifier(), amplifier);
                p.removePotionEffect(potionEffect);
                p.addPotionEffect(new PotionEffect(potionEffect, newDuration, newAmplifier));
                p.playSound(p.getLocation(), Sound.ENTITY_IRON_GOLEM_STEP, 0.25f, 0.25f);
                return;
            }
        }
        // if we didn't find an existing effect, add a new one
        J.a(() -> {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            J.s(() -> {
                p.addPotionEffect(new PotionEffect(potionEffect, duration, amplifier));
                p.playSound(p.getLocation(), Sound.ENTITY_IRON_GOLEM_STEP, 0.25f, 0.25f);
            }, plugin);
        });

    }


    public static void potion(org.bukkit.entity.Player p, PotionEffectType type, int power, int duration) {
        p.addPotionEffect(new PotionEffect(type, power, duration, true, false, false));
    }


    public static double getArmorValue(org.bukkit.entity.Player player) {
        org.bukkit.inventory.PlayerInventory inv = player.getInventory();
        ItemStack boots = inv.getBoots();
        ItemStack helmet = inv.getHelmet();
        ItemStack chest = inv.getChestplate();
        ItemStack pants = inv.getLeggings();
        double armorValue = 0.0;
        if (helmet == null) armorValue = armorValue + 0.0;
        else if (helmet.getType() == Material.LEATHER_HELMET) armorValue = armorValue + 0.04;
        else if (helmet.getType() == Material.GOLDEN_HELMET) armorValue = armorValue + 0.08;
        else if (helmet.getType() == Material.TURTLE_HELMET) armorValue = armorValue + 0.08;
        else if (helmet.getType() == Material.CHAINMAIL_HELMET) armorValue = armorValue + 0.08;
        else if (helmet.getType() == Material.IRON_HELMET) armorValue = armorValue + 0.08;
        else if (helmet.getType() == Material.DIAMOND_HELMET) armorValue = armorValue + 0.12;
        else if (helmet.getType() == Material.NETHERITE_HELMET) armorValue = armorValue + 0.12;
        //
        if (boots == null) armorValue = armorValue + 0.0;
        else if (boots.getType() == Material.LEATHER_BOOTS) armorValue = armorValue + 0.04;
        else if (boots.getType() == Material.GOLDEN_BOOTS) armorValue = armorValue + 0.04;
        else if (boots.getType() == Material.CHAINMAIL_BOOTS) armorValue = armorValue + 0.04;
        else if (boots.getType() == Material.IRON_BOOTS) armorValue = armorValue + 0.08;
        else if (boots.getType() == Material.DIAMOND_BOOTS) armorValue = armorValue + 0.12;
        else if (boots.getType() == Material.NETHERITE_BOOTS) armorValue = armorValue + 0.12;
        //
        if (pants == null) armorValue = armorValue + 0.0;
        else if (pants.getType() == Material.LEATHER_LEGGINGS) armorValue = armorValue + 0.08;
        else if (pants.getType() == Material.GOLDEN_LEGGINGS) armorValue = armorValue + 0.12;
        else if (pants.getType() == Material.CHAINMAIL_LEGGINGS) armorValue = armorValue + 0.16;
        else if (pants.getType() == Material.IRON_LEGGINGS) armorValue = armorValue + 0.20;
        else if (pants.getType() == Material.DIAMOND_LEGGINGS) armorValue = armorValue + 0.24;
        else if (pants.getType() == Material.NETHERITE_LEGGINGS) armorValue = armorValue + 0.24;
        //
        if (chest == null) armorValue = armorValue + 0.0;
        else if (chest.getType() == Material.LEATHER_CHESTPLATE) armorValue = armorValue + 0.12;
        else if (chest.getType() == Material.GOLDEN_CHESTPLATE) armorValue = armorValue + 0.20;
        else if (chest.getType() == Material.CHAINMAIL_CHESTPLATE) armorValue = armorValue + 0.20;
        else if (chest.getType() == Material.IRON_CHESTPLATE) armorValue = armorValue + 0.24;
        else if (chest.getType() == Material.DIAMOND_CHESTPLATE) armorValue = armorValue + 0.32;
        else if (chest.getType() == Material.NETHERITE_CHESTPLATE) armorValue = armorValue + 0.32;
        return armorValue;
    }


}
