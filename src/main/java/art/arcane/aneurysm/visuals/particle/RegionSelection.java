package art.arcane.aneurysm.visuals.particle;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public class RegionSelection {
    public static List<Location> getHollowCuboid(Location loc, double particleDistance) {
        List<Location> result = Lists.newArrayList();
        World world = loc.getWorld();
        double minX = loc.getBlockX();
        double minY = loc.getBlockY();
        double minZ = loc.getBlockZ();
        double maxX = loc.getBlockX() + 1;
        double maxY = loc.getBlockY() + 1;
        double maxZ = loc.getBlockZ() + 1;

        for (double x = minX; x <= maxX; x += particleDistance) {
            for (double y = minY; y <= maxY; y += particleDistance) {
                for (double z = minZ; z <= maxZ; z += particleDistance) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        result.add(new Location(world, x, y, z));
                    }
                }
            }
        }
        return result;
    }

    public static List<Location> getHollowCuboid(Location loc, Location loc2, double particleDistance) {
        List<Location> result = Lists.newArrayList();
        World world = loc.getWorld();

        double minX = loc.getBlockX();
        double minY = loc.getBlockY();
        double minZ = loc.getBlockZ();
        double maxX = loc2.getBlockX() + 1;
        double maxY = loc2.getBlockY() + 1;
        double maxZ = loc2.getBlockZ() + 1;

        for (double x = minX; x <= maxX; x += particleDistance) {
            for (double y = minY; y <= maxY; y += particleDistance) {
                for (double z = minZ; z <= maxZ; z += particleDistance) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        result.add(new Location(world, x, y, z));
                    }
                }
            }
        }
        return result;
    }

}
