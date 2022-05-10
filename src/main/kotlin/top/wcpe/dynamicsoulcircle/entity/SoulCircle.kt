package top.wcpe.dynamicsoulcircle.entity

import eos.moe.dragoncore.api.CoreAPI
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitTask
import top.wcpe.dynamicsoulcircle.DynamicSoulCircle
import top.wcpe.dynamicsoulcircle.entities.ArmorStandSoulCircle

/**
 * 由 WCPE 在 2022/5/5 20:34 创建
 *
 * Created by WCPE on 2022/5/5 20:34
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
data class SoulCircle(
    val uuid: String,
    val location: Location,
    val soulKey: String,
    val viewTime: Int,
) {
    private val startTime = System.currentTimeMillis()
    private val endViewTask: BukkitTask
    private val armorStand = ArmorStandSoulCircle.spawn(
        location.clone().subtract(0.0, 1.7, 0.0)
    )
    var absorb = false

    init {
        endViewTask = Bukkit.getScheduler()
            .runTaskLater(DynamicSoulCircle.instance, {
                remove()
            }, viewTime * 20L)
        armorStand.setGravity(false)
        armorStand.isVisible = false
        armorStand.setMetadata("SoulCircle", FixedMetadataValue(DynamicSoulCircle.instance, uuid))
    }

    fun absorb(player: Player) {
        absorb = true
        armorStand.addPassenger(player)
    }

    fun stopAbsorb(player: Player) {
        absorb = false
        armorStand.removePassenger(player)
    }

    fun absorbFinish(player: Player) {
        player.teleport(armorStand.location.clone().add(0.0, 1.7, 0.0))
        remove()
    }

    fun isExits(): Boolean {
        return System.currentTimeMillis() <= startTime + viewTime * 1000
    }

    private fun remove() {
        armorStand.remove()
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            CoreAPI.removePlayerWorldTexture(onlinePlayer, uuid)
        }
        endViewTask.cancel()
    }
}