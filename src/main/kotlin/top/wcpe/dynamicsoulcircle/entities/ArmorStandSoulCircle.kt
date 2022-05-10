package top.wcpe.dynamicsoulcircle.entities

import net.minecraft.server.v1_12_R1.EntityArmorStand
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_12_R1.CraftServer
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Vehicle


/**
 * 由 WCPE 在 2022/5/8 20:27 创建
 *
 * Created by WCPE on 2022/5/8 20:27
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
class ArmorStandSoulCircle(server: CraftServer, entity: EntityArmorStand) :
    CraftArmorStand(server, entity), Vehicle {

    companion object {
        @JvmStatic
        fun spawn(location: Location): ArmorStand {
            val world = location.world as CraftWorld
            return EntityArmorStandSoulCircle(world.handle, location).bukkitEntity as ArmorStand
        }
    }
}