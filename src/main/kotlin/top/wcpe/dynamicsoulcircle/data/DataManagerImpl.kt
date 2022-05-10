package top.wcpe.dynamicsoulcircle.data

import org.bukkit.Location
import top.wcpe.dynamicsoulcircle.entity.SoulCircle

/**
 * 由 WCPE 在 2022/5/5 19:19 创建
 *
 * Created by WCPE on 2022/5/5 19:19
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
class DataManagerImpl : IDataManager {

    private val soulCircleLocationMap: MutableMap<String, MutableList<SoulCircle>> = mutableMapOf()

    override fun getChunkSoulCircle(location: Location): List<SoulCircle> {
        val soulCircle =
            soulCircleLocationMap["${location.world.name}:${location.chunk.x}:${location.chunk.z}"] ?: return listOf()

        val iterator = soulCircle.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (!next.isExits()) {
                iterator.remove()
            }
        }
        return soulCircle
    }


    override fun putSoulCircle(soulCircle: SoulCircle) {
        val key = "${soulCircle.location.world.name}:${soulCircle.location.chunk.x}:${soulCircle.location.chunk.z}"
        var soulCirclesList = soulCircleLocationMap[key]
        if (soulCirclesList == null) {
            soulCirclesList = mutableListOf()
            soulCircleLocationMap[key] = soulCirclesList
        }
        soulCirclesList.add(soulCircle)
    }

}