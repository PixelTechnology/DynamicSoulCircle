package top.wcpe.dynamicsoulcircle

import eos.moe.dragoncore.api.CoreAPI
import org.bukkit.plugin.java.JavaPlugin
import top.wcpe.dynamicsoulcircle.data.DataManagerImpl
import top.wcpe.dynamicsoulcircle.data.IDataManager
import top.wcpe.wcpelib.bukkit.manager.MessageManager

/**
 * 由 WCPE 在 2022/4/25 19:18 创建
 *
 * Created by WCPE on 2022/4/25 19:18
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
class DynamicSoulCircle : JavaPlugin() {

    companion object {
        @JvmStatic
        lateinit var instance: DynamicSoulCircle

        @JvmStatic
        val dataManager: IDataManager = DataManagerImpl()

        @JvmStatic
        lateinit var messageManager: MessageManager
    }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        messageManager = MessageManager(this, "CN")
        CoreAPI.registerKey(config.getString("absorb-key"))
        DynamicSoulCircleCommands()
        server.pluginManager.registerEvents(DynamicSoulCircleListener(), this)
    }

}