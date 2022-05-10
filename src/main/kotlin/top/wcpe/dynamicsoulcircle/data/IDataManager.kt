package top.wcpe.dynamicsoulcircle.data

import org.bukkit.Location
import top.wcpe.dynamicsoulcircle.entity.SoulCircle

/**
 * 由 WCPE 在 2022/5/5 18:38 创建
 *
 * Created by WCPE on 2022/5/5 18:38
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
interface IDataManager {

    fun getChunkSoulCircle(location: Location): List<SoulCircle>

    fun putSoulCircle(soulCircle: SoulCircle)
}
