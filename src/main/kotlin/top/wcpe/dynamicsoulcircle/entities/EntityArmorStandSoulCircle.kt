package top.wcpe.dynamicsoulcircle.entities

import net.minecraft.server.v1_12_R1.Entity
import net.minecraft.server.v1_12_R1.EntityArmorStand
import net.minecraft.server.v1_12_R1.World
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_12_R1.CraftServer


/**
 * 由 WCPE 在 2022/5/8 22:59 创建
 *
 * Created by WCPE on 2022/5/8 22:59
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
class EntityArmorStandSoulCircle(world: World, location: Location) :
    EntityArmorStand(world, location.x, location.y, location.z) {
    init {
        setPositionRotation(location.x, location.y, location.z, location.yaw, location.pitch)
        this.world.addEntity(this as Entity)
        bukkitEntity = ArmorStandSoulCircle(Bukkit.getServer() as CraftServer, this)
    }
}