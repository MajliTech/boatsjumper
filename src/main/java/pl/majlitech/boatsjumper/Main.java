package pl.majlitech.boatsjumper;

import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.List;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent ev) {
        if (ev.getVehicle().getType() != null && ev.getVehicle().getType() == EntityType.BOAT) {
            Boat boat = (Boat) ev.getVehicle();
                // determine which direction the boat is moving: x, z, -x, -z
                double dx = ev.getTo().getX() - ev.getFrom().getX();
                double dz = ev.getTo().getZ() - ev.getFrom().getZ();
                if (Math.abs(dx) > Math.abs(dz)) {
                    // Boat is moving in the x direction
                    if (dx > 0) {
                        dx = 1;
                    } else {
                        dx = -1;
                    }
                    dz = 0;
                } else {
                    dx = 0;
                    // Boat is moving in the z direction
                    if (dz > 0) {
                        dz = 1;
                    } else {
                        dz = -1;
                    }
                }
                if (ev.getTo().getBlock().getRelative((int) dx, 0, (int) dz).getType().isSolid()) {
                    // check if block +1 direction and +1 height is also solid
                    if (!ev.getTo().getBlock().getRelative((int) dx, 1, (int) dz).getType().isSolid()) {
                        List<Entity> toPutBack = boat.getPassengers();
                        toPutBack.forEach(boat::removePassenger);
                        boat.teleport(ev.getTo().add(new Vector(dx*1.5, 2, dz*1.5)));
                        // delay half a second before putting player back
                        getServer().getScheduler().runTaskLater(this, () -> {
                                toPutBack.forEach(boat::addPassenger);

                        }, 5L);
                    }
                }


        }
    }
}
