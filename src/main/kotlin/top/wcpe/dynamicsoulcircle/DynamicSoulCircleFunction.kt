package top.wcpe.dynamicsoulcircle

import eos.moe.dragoncore.api.worldtexture.WorldTexture
import eos.moe.dragoncore.api.worldtexture.animation.RotateAnimation
import eos.moe.dragoncore.api.worldtexture.animation.TranslateAnimation
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

/**
 * 由 WCPE 在 2022/4/28 21:45 创建
 *
 * Created by WCPE on 2022/4/28 21:45
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
object DynamicSoulCircleFunction {

    @JvmStatic
    fun getConfigurationWorldTexture(
        location: Location,
        config: ConfigurationSection
    ): WorldTexture {
        val worldTexture = WorldTexture()
        val worldTextureConfig =
            DynamicSoulCircle.instance.config.getConfigurationSection("world-texture.${config.getString("world-texture")}")
                ?: return worldTexture
        worldTexture.world = location.world.name
        worldTexture.path = config.getString("url")
        worldTexture.glow = worldTextureConfig.getBoolean("glow")
        worldTexture.width = worldTextureConfig.getDouble("width").toFloat()
        worldTexture.height = worldTextureConfig.getDouble("height").toFloat()
        worldTexture.translateX = location.x + worldTextureConfig.getDouble("translate-x")
        worldTexture.translateY = location.y + worldTextureConfig.getDouble("translate-y")
        worldTexture.translateZ = location.z + worldTextureConfig.getDouble("translate-z")
        worldTexture.rotateX = worldTextureConfig.getDouble("rotate-x").toFloat()
        worldTexture.rotateY = worldTextureConfig.getDouble("rotate-y").toFloat()
        worldTexture.rotateZ = worldTextureConfig.getDouble("rotate-z").toFloat()
        worldTexture.followEntityDirection = worldTextureConfig.getBoolean("follow-entity-direction")
        val animationConfig =
            DynamicSoulCircle.instance.config.getConfigurationSection("animations") ?: return worldTexture

        for (key in config.getStringList("animation")) {
            if (!animationConfig.contains(key)) {
                continue
            }
            val keySection = animationConfig.getConfigurationSection(key) ?: continue

            worldTexture.animationList.add(
                when (keySection.getString("type") ?: continue) {
                    "rotate" -> {
                        RotateAnimation().also {
                            it.direction = keySection.getString("direction")
                            it.angle = keySection.getDouble("angle").toFloat()
                            it.duration = keySection.getInt("duration")
                            it.cycleCount = keySection.getInt("cycle-count")
                            it.fixed = keySection.getBoolean("fixed")
                        }
                    }
                    "translate" -> {
                        TranslateAnimation().also {
                            it.direction = keySection.getString("direction")
                            it.distance = keySection.getDouble("distance").toFloat()
                            it.duration = keySection.getInt("duration")
                            it.cycleCount = keySection.getInt("cycle-count")
                            it.fixed = keySection.getBoolean("fixed")
                        }
                    }
                    else -> continue
                }
            )
        }




        return worldTexture
    }



}