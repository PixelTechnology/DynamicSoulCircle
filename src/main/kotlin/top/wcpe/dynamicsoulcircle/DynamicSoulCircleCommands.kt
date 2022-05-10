package top.wcpe.dynamicsoulcircle

import top.wcpe.wcpelib.bukkit.command.CommandPlus
import top.wcpe.wcpelib.bukkit.command.entity.Command

/**
 * 由 WCPE 在 2022/5/8 13:37 创建
 *
 * Created by WCPE on 2022/5/8 13:37
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.0
 */
class DynamicSoulCircleCommands {
    init {
        val cp = CommandPlus.Builder("DynamicSoulCircle", DynamicSoulCircle.instance).aliases("dsc").build()
        cp.registerSubCommand(
            Command.Builder("reload", "重载配置文件").onlyPlayerUse(false).executeComponent { sender, _ ->
                DynamicSoulCircle.instance.reloadConfig()
                DynamicSoulCircle.messageManager.reload()
                sender.sendMessage("§e重载配置文件完成~")
            }.build()
        )
        cp.registerThis()
    }
}