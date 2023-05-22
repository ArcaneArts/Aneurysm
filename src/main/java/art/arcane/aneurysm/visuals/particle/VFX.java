package art.arcane.aneurysm.visuals.particle;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static art.arcane.aneurysm.math.Rands.RANDOM;

public final class VFX {


    public void vfxXP(Player p, Location l, int amt) {
        p.spawnParticle(Particle.ENCHANTMENT_TABLE, l, Math.min(amt / 10, 20), 0.5, 0.5, 0.5, 1);
    }

    public void vfxXP(Location l) {
        l.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, l.add(0, 1.7, 0), 3, 0.1, 0.1, 0.1, 3);
    }

    public void vfxLevelUp(Player p) {
        p.spawnParticle(Particle.REVERSE_PORTAL, p.getLocation().clone().add(0, 1.7, 0), 100, 0.1, 0.1, 0.1, 4.1);
    }

    public void vfxFastRing(Location location, double radius, Color color) {
        for (int d = 0; d <= 90; d += 1) {
            Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            particleLoc.setX(location.getX() + Math.cos(d) * radius);
            particleLoc.setZ(location.getZ() + Math.sin(d) * radius);
            location.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 1, new Particle.DustOptions(color, 1));
        }
    }

    public void vfxFastRing(Location location, double radius, Particle particle) {
        for (int d = 0; d <= 90; d += 1) {
            Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            particleLoc.setX(location.getX() + Math.cos(d) * radius);
            particleLoc.setZ(location.getZ() + Math.sin(d) * radius);
            location.getWorld().spawnParticle(particle, particleLoc, 1);
        }
    }

    public void vfxFastRing(Location location, double radius, Particle particle, int angle) {
        for (int d = 0; d <= 90; d += angle) {
            Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            particleLoc.setX(location.getX() + Math.cos(d) * radius);
            particleLoc.setZ(location.getZ() + Math.sin(d) * radius);
            location.getWorld().spawnParticle(particle, particleLoc, 1);
        }
    }

    public void vfxShootParticle(Player player, Particle particle, double velocity, int count) {
        Location location = player.getEyeLocation();
        Vector direction = location.getDirection();
        for (int i = 0; i < count; i++) {
            player.getWorld().spawnParticle(particle, location.getX(), location.getY(), location.getZ(), 0, (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(), velocity, null);
        }
    }

    public void vfxParticleSpiral(Location center, int radius, int height, Particle type) {
        double angle = 0;
        for (int i = 0; i <= height; i++) {
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            center.getWorld().spawnParticle(type, x, +center.getY(), z, 1, 0, 0, 0, 0);
            angle += 0.1;
        }
    }

    public void vfxCuboidOutline(Block block, Particle particle) {
        List<Location> hollowCube = RegionSelection.getHollowCuboid(block.getLocation(), 0.25);
        for (Location l : hollowCube) {
            block.getWorld().spawnParticle(particle, l, 1, 0F, 0F, 0F, 0.000);
        }
    }

    public void vfxCuboidOutline(Block blockStart, Block blockEnd, Particle particle) {
        List<Location> hollowCube = RegionSelection.getHollowCuboid(blockStart.getLocation(), blockEnd.getLocation(), 0.25);
        for (Location l : hollowCube) {
            blockStart.getWorld().spawnParticle(particle, l, 2, 0F, 0F, 0F, 0.000);
        }
    }

    public void vfxCuboidOutline(Block blockStart, Block blockEnd, Color color, int size) {
        List<Location> hollowCube = RegionSelection.getHollowCuboid(blockStart.getLocation(), blockEnd.getLocation(), 0.25);
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, size);
        for (Location l : hollowCube) {
            blockStart.getWorld().spawnParticle(Particle.REDSTONE, l, 2, 0F, 0F, 0F, 0.000, dustOptions);
        }
    }

    public void vfxPrismOutline(Location placer, double outset, Particle particle, int particleCount) {

        Location top = new Location(placer.getWorld(), placer.getX(), placer.getY() + outset, placer.getZ());
        Location baseCorner1 = new Location(placer.getWorld(), placer.getX() - outset, placer.getY(), placer.getZ() - outset);
        Location baseCorner2 = new Location(placer.getWorld(), placer.getX() + outset, placer.getY(), placer.getZ() - outset);
        Location baseCorner3 = new Location(placer.getWorld(), placer.getX() + outset, placer.getY(), placer.getZ() + outset);
        Location baseCorner4 = new Location(placer.getWorld(), placer.getX() - outset, placer.getY(), placer.getZ() + outset);

        vfxParticleLine(baseCorner1, baseCorner2, particle, particleCount, 1, 0.0D, 0D, 0.0D, 0D, null, true, l -> l.getBlock().isPassable());
        vfxParticleLine(baseCorner2, baseCorner3, particle, particleCount, 1, 0.0D, 0D, 0.0D, 0D, null, true, l -> l.getBlock().isPassable());
        vfxParticleLine(baseCorner3, baseCorner4, particle, particleCount, 1, 0.0D, 0D, 0.0D, 0D, null, true, l -> l.getBlock().isPassable());
        vfxParticleLine(baseCorner4, baseCorner1, particle, particleCount, 1, 0.0D, 0D, 0.0D, 0D, null, true, l -> l.getBlock().isPassable());

        for (Location location : Arrays.asList(baseCorner1, baseCorner2, baseCorner3, baseCorner4)) {
            vfxParticleLine(location, top, particle, particleCount, 1, 0.0D, 0D, 0.0D, 0D, null, true, l -> l.getBlock().isPassable());
        }
    }

    public void vfxFastSphere(Location center, double range, Color color, int particleCount) {
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
        World world = center.getWorld();

        for (int i = 0; i < particleCount; i++) {
            double x, y, z;
            do {
                x = RANDOM.nextDouble() * 2 - 1;
                y = RANDOM.nextDouble() * 2 - 1;
                z = RANDOM.nextDouble() * 2 - 1;
            } while (x * x + y * y + z * z > 1);

            double magnitude = Math.sqrt(x * x + y * y + z * z);
            x = x / magnitude * range;
            y = y / magnitude * range;
            z = z / magnitude * range;

            Location particleLocation = center.clone().add(x, y, z);
            world.spawnParticle(Particle.REDSTONE, particleLocation, 0, 0, 0, 0, dustOptions);
        }
    }

    public void vfxLoadingRing(Plugin p, Location center, double radius, Color color, int durationTicks, int particleCount) {
        World world = center.getWorld();
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.0f);

        new BukkitRunnable() {
            int tick = 0;

            public void run() {
                if (tick >= durationTicks) {
                    cancel();
                    return;
                }

                double angle = 2 * Math.PI * tick / durationTicks;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                Location particleLocation = center.clone().add(x, 0, z);
                world.spawnParticle(Particle.REDSTONE, particleLocation, particleCount, 0, 0, 0, dustOptions);

                tick++;
            }
        }.runTaskTimer(p, 0, 1);
    }

    public void vfxLoadingRing(Plugin p, Location center, double radius, Particle particle, int durationTicks, int particleCount) {
        World world = center.getWorld();

        new BukkitRunnable() {
            int tick = 0;

            public void run() {
                if (tick >= durationTicks) {
                    cancel();
                    return;
                }

                double angle = 2 * Math.PI * tick / durationTicks;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                Location particleLocation = center.clone().add(x, 0, z);
                world.spawnParticle(particle, particleLocation, particleCount, 0, 0, 0);

                tick++;
            }
        }.runTaskTimer(p, 0, 1);
    }

    public void vfxParticleLine(Location start, Location end, Particle particle, int pointsPerLine, int particleCount, double offsetX, double offsetY, double offsetZ, double extra, @Nullable Double data, boolean forceDisplay,
                                @Nullable Predicate<Location> operationPerPoint) {
        double d = start.distance(end) / pointsPerLine;
        for (int i = 0; i < pointsPerLine; i++) {
            Location l = start.clone();
            Vector direction = end.toVector().subtract(start.toVector()).normalize();
            Vector v = direction.multiply(i * d);
            l.add(v.getX(), v.getY(), v.getZ());
            if (operationPerPoint == null) {
                start.getWorld().spawnParticle(particle, l, particleCount, offsetX, offsetY, offsetZ, extra, data, forceDisplay);
                continue;
            }
            if (operationPerPoint.test(l)) {
                start.getWorld().spawnParticle(particle, l, particleCount, offsetX, offsetY, offsetZ, extra, data, forceDisplay);
            }
        }
    }

    public void vfxParticleLine(Location start, Location end, int particleCount, Particle particle) {
        World world = start.getWorld();
        double distance = start.distance(end);
        Vector direction = end.toVector().subtract(start.toVector()).normalize();
        double step = distance / (particleCount - 1);

        for (int i = 0; i < particleCount; i++) {
            Location particleLocation = start.clone().add(direction.clone().multiply(i * step));
            world.spawnParticle(particle, particleLocation, 1);
        }
    }

    public void vfxZuck(Location from, Location to) {
        Vector v = from.clone().subtract(to).toVector();
        double l = v.length();
        v.normalize();
        from.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, to, 1, 6, 6, 6, 0.6);
    }

    public void vfxZuck(Location from, Location to, Particle particle) {
        Vector v = from.clone().subtract(to).toVector();
        double l = v.length();
        v.normalize();
        from.getWorld().spawnParticle(particle, to, 1, 6, 6, 6, 0.6);
    }

    public void vfxSphereV1(Player p, Location l, double radius, Particle particle, int verticalDensity, int radialDensity) {
        for (double phi = 0; phi <= Math.PI; phi += Math.PI / verticalDensity) {
            for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / radialDensity) {
                double x = radius * Math.cos(theta) * Math.sin(phi);
                double y = radius * Math.cos(phi) + 1.5;
                double z = radius * Math.sin(theta) * Math.sin(phi);

                l.add(x, y, z);
                p.getWorld().spawnParticle(particle, l, 1, 0F, 0F, 0F, 0.001);
                l.subtract(x, y, z);
            }
        }
    }

    public void vfxMovingSphere(Plugin p, Location startLocation, Location endLocation, int ticks, Color color, double size, double density) {
        World world = startLocation.getWorld();
        double startX = startLocation.getX();
        double startY = startLocation.getY();
        double startZ = startLocation.getZ();
        double endX = endLocation.getX();
        double endY = endLocation.getY();
        double endZ = endLocation.getZ();
        double deltaX = (endX - startX) / ticks;
        double deltaY = (endY - startY) / ticks;
        double deltaZ = (endZ - startZ) / ticks;
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, (float) size);

        new BukkitRunnable() {
            int tick = 0;

            public void run() {
                if (tick >= ticks) {
                    cancel();
                    return;
                }
                double x = startX + deltaX * tick;
                double y = startY + deltaY * tick;
                double z = startZ + deltaZ * tick;
                Location particleLocation = new Location(world, x, y, z);

                for (double i = 0; i < Math.PI; i += Math.PI / density) {
                    double radius = Math.sin(i) * size;
                    double yCoord = Math.cos(i) * size;
                    for (double j = 0; j < Math.PI * 2; j += Math.PI / density) {
                        double xCoord = Math.sin(j) * radius;
                        double zCoord = Math.cos(j) * radius;

                        Location loc = particleLocation.clone().add(xCoord, yCoord, zCoord);
                        world.spawnParticle(Particle.REDSTONE, loc, 0, 0, 0, 0, dustOptions);
                    }
                }

                tick++;
            }
        }.runTaskTimer(p, 0, 1);
    }

    public void vfxMovingSwirlingSphere(Plugin p, Location startLocation, Location endLocation, int ticks, Color color, double size, double swirlRadius, double density) {
        World world = startLocation.getWorld();
        double startX = startLocation.getX();
        double startY = startLocation.getY();
        double startZ = startLocation.getZ();
        double endX = endLocation.getX();
        double endY = endLocation.getY();
        double endZ = endLocation.getZ();
        double deltaX = (endX - startX) / ticks;
        double deltaY = (endY - startY) / ticks;
        double deltaZ = (endZ - startZ) / ticks;
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, (float) size);

        new BukkitRunnable() {
            int tick = 0;

            public void run() {
                if (tick >= ticks) {
                    cancel();
                    return;
                }
                double x = startX + deltaX * tick;
                double y = startY + deltaY * tick;
                double z = startZ + deltaZ * tick;

                // Add swirling effect
                double swirlAngle = 2 * Math.PI * tick / ticks;
                x += swirlRadius * Math.cos(swirlAngle);
                z += swirlRadius * Math.sin(swirlAngle);

                Location particleLocation = new Location(world, x, y, z);

                for (double i = 0; i < Math.PI; i += Math.PI / density) {
                    double radius = Math.sin(i) * size;
                    double yCoord = Math.cos(i) * size;
                    for (double j = 0; j < Math.PI * 2; j += Math.PI / density) {
                        double xCoord = Math.sin(j) * radius;
                        double zCoord = Math.cos(j) * radius;

                        Location loc = particleLocation.clone().add(xCoord, yCoord, zCoord);
                        world.spawnParticle(Particle.REDSTONE, loc, 0, 0, 0, 0, dustOptions);
                    }
                }

                tick++;
            }
        }.runTaskTimer(p, 0, 1);
    }

    public void vfxPlayerBoundingBoxOutline(Plugin p, Player player, Color color, int ticks, int particleCount) {
        World world = player.getWorld();
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.0f);

        new BukkitRunnable() {
            int tick = 0;

            public void run() {
                if (tick >= ticks) {
                    cancel();
                    return;
                }

                BoundingBox boundingBox = player.getBoundingBox();
                double minX = boundingBox.getMinX();
                double minY = boundingBox.getMinY();
                double minZ = boundingBox.getMinZ();
                double maxX = boundingBox.getMaxX();
                double maxY = boundingBox.getMaxY();
                double maxZ = boundingBox.getMaxZ();

                for (int i = 0; i < particleCount; i++) {
                    double t = (double) i / (particleCount - 1);

                    // Edges along X-axis
                    world.spawnParticle(Particle.REDSTONE, minX + t * (maxX - minX), minY, minZ, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.REDSTONE, minX + t * (maxX - minX), maxY, minZ, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.REDSTONE, minX + t * (maxX - minX), minY, maxZ, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.REDSTONE, minX + t * (maxX - minX), maxY, maxZ, 0, 0, 0, 0, dustOptions);

                    // Edges along Y-axis
                    world.spawnParticle(Particle.REDSTONE, minX, minY + t * (maxY - minY), minZ, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.REDSTONE, maxX, minY + t * (maxY - minY), minZ, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.REDSTONE, minX, minY + t * (maxY - minY), maxZ, 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.REDSTONE, maxX, minY + t * (maxY - minY), maxZ, 0, 0, 0, 0, dustOptions);

                    // Edges along Z-axis
                    world.spawnParticle(Particle.REDSTONE, minX, minY, minZ + t * (maxZ - minZ), 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.REDSTONE, maxX, minY, minZ + t * (maxZ - minZ), 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.REDSTONE, minX, maxY, minZ + t * (maxZ - minZ), 0, 0, 0, 0, dustOptions);
                    world.spawnParticle(Particle.REDSTONE, maxX, maxY, minZ + t * (maxZ - minZ), 0, 0, 0, 0, dustOptions);
                }

                tick++;
            }
        }.runTaskTimer(p, 0, 1);
    }

    public void vfxVortexSphere(Plugin p, Location startLocation, Location endLocation, int ticks, Color color, double radius) {
        World world = startLocation.getWorld();
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.0f);

        double startX = startLocation.getX();
        double startY = startLocation.getY();
        double startZ = startLocation.getZ();
        double endX = endLocation.getX();
        double endY = endLocation.getY();
        double endZ = endLocation.getZ();
        double deltaX = (endX - startX) / ticks;
        double deltaY = (endY - startY) / ticks;
        double deltaZ = (endZ - startZ) / ticks;

        new BukkitRunnable() {
            int tick = 0;

            public void run() {
                if (tick >= ticks) {
                    cancel();
                    return;
                }

                double x = startX + deltaX * tick;
                double y = startY + deltaY * tick;
                double z = startZ + deltaZ * tick;
                Location particleLocation = new Location(world, x, y, z);

                double currentRadius = radius * (1 - (double) tick / ticks);

                for (double theta = 0; theta < 2 * Math.PI; theta += Math.PI / 10) {
                    for (double phi = 0; phi < Math.PI; phi += Math.PI / 10) {
                        double xCoord = currentRadius * Math.sin(phi) * Math.cos(theta);
                        double yCoord = currentRadius * Math.sin(phi) * Math.sin(theta);
                        double zCoord = currentRadius * Math.cos(phi);

                        Location loc = particleLocation.clone().add(xCoord, yCoord, zCoord);
                        world.spawnParticle(Particle.REDSTONE, loc, 0, 0, 0, 0, dustOptions);
                    }
                }

                tick++;
            }
        }.runTaskTimer(p, 0, 1);
    }


    public void vfxDome(Location center, double range, Color color, int particleCount) {
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
        World world = center.getWorld();

        for (int i = 0; i < particleCount; i++) {
            double theta = 2 * Math.PI * RANDOM.nextDouble();
            double phi = Math.PI / 2 * RANDOM.nextDouble(); // Adjusted range of phi to create a dome
            double x = range * Math.sin(phi) * Math.cos(theta);
            double y = range * Math.sin(phi) * Math.sin(theta);
            double z = range * Math.cos(phi);

            Location particleLocation = center.clone().add(x, y, z);
            world.spawnParticle(Particle.REDSTONE, particleLocation, 0, 0, 0, 0, dustOptions);
        }
    }


}
