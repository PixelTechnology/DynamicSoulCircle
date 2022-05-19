package top.wcpe.dynamicsoulcircle

import eos.moe.dragoncore.api.CoreAPI
import eos.moe.dragoncore.api.KeyPressEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerArmorStandManipulateEvent
import org.bukkit.event.vehicle.VehicleExitEvent
import top.wcpe.dynamicsoulcircle.entity.SoulCircle
import top.wcpe.wcpelib.bukkit.utils.StringActionUtil
import top.wcpe.wcpelib.common.utils.collector.ListUtil
import top.wcpe.wcpelib.common.utils.datatime.OtherUtil
import top.wcpe.wcpelib.common.utils.datatime.OtherUtil.CountDownPerSecondTask
import top.wcpe.wcpelib.common.utils.math.RandomUtil
import java.util.*


/**
 * 由 WCPE 在 2022/4/25 20:54 创建
 *
 * Created by WCPE on 2022/4/25 20:54
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
class DynamicSoulCircleListener : Listener {


    @EventHandler(
        ignoreCancelled = true,
        priority = EventPriority.HIGHEST
    )
    fun listenerItemSpawnEvent(e: ItemSpawnEvent) {

        val item = e.entity
        val itemStack = item.itemStack

        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.itemMeta.hasDisplayName()) {
            return
        }
        val itemName = itemStack.itemMeta.displayName
        val config = DynamicSoulCircle.instance.config.getConfigurationSection("soul-circle")
        for (key in config.getKeys(false)) {
            if (!itemName.contains(key)) continue
            val keySection = config.getConfigurationSection(key)
            val uuid = UUID.randomUUID().toString()
            DynamicSoulCircle.dataManager.putSoulCircle(
                SoulCircle(
                    uuid,
                    item.location,
                    key,
                    keySection.getInt("view-time")
                )
            )
            val worldTexture = DynamicSoulCircleFunction.getConfigurationWorldTexture(item.location, keySection)
            for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                CoreAPI.setPlayerWorldTextureItem(
                    onlinePlayer,
                    uuid,
                    worldTexture
                )
            }
            Bukkit.getScheduler().runTask(DynamicSoulCircle.instance) {
                item.remove()
            }
            return
        }
    }

    @EventHandler
    fun listenerPlayerArmorStandManipulateEvent(e: PlayerArmorStandManipulateEvent) {
        e.isCancelled = e.rightClicked.hasMetadata("SoulCircle")
    }


    @EventHandler
    fun listenerPlayerDeathEvent(e: PlayerDeathEvent) {
        absorbPlayer.remove(e.entity.name)?.stopAbsorb(e.entity)
    }

    @EventHandler
    fun listenerVehicleExitEvent(e: VehicleExitEvent) {
        if (e.vehicle.hasMetadata("SoulCircle")) {
            absorbPlayer[e.exited.name] ?: return
            e.isCancelled = true
        }
    }


    private val absorbPlayer: MutableMap<String, SoulCircle> = mutableMapOf()

    @EventHandler
    fun listenerKeyPressEvent(e: KeyPressEvent) {
        if (!e.key.equals(DynamicSoulCircle.instance.config.getString("absorb-key"), ignoreCase = true)) return

        val player = e.player
        DynamicSoulCircle.dataManager.getChunkSoulCircle(player.location).minByOrNull {
            it.location.distance(player.location)
        }?.let { soulCircle ->
            if (soulCircle.absorb) return
            if (soulCircle.location.distance(player.location) > 2) {
                return
            }
            val config = DynamicSoulCircle.instance.config.getConfigurationSection("soul-circle.${soulCircle.soulKey}")
                ?: return

            config.getInt("absorb-sure-level").let {
                if (player.level < it) {
                    player.sendMessage(DynamicSoulCircle.messageManager.getMessage("level-not-enough", "level:$it"))
                    return
                }
            }

            absorbPlayer[e.player.name] = soulCircle
            soulCircle.absorb(player)
            OtherUtil.putCountDownTask(CountDownPerSecondTask {
                Bukkit.getScheduler().runTask(DynamicSoulCircle.instance) {
                    StringActionUtil.executionCommands(
                        ListUtil.replaceString(
                            config.getStringList("count-down-task-string-action"),
                            "time:$it"
                        ), false, player
                    )
                }
            }, OtherUtil.CountDownFinishTask {
                absorbPlayer.remove(player.name)?.also {
                    if (!RandomUtil.probability(config.getDouble("probability"))) {
                        Bukkit.getScheduler().runTask(DynamicSoulCircle.instance) {
                            StringActionUtil.executionCommands(
                                config.getStringList("fail-string-action"),
                                false,
                                player
                            )
                        }
                    } else {
                        Bukkit.getScheduler().runTask(DynamicSoulCircle.instance) {
                            StringActionUtil.executionCommands(
                                config.getStringList("success-string-action"),
                                false,
                                player
                            )
                        }
                    }
                }?.run { absorbFinish(player) }
            }, config.getInt("absorb-time"))

        }

    }
}