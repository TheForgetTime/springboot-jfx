package top.lhx.springbootjfx

import javafx.application.HostServices
import javafx.application.Platform
import javafx.scene.Node
import javafx.stage.Stage
import org.springframework.context.ConfigurableApplicationContext
import java.util.*
import java.util.concurrent.Executors

object GUIEnv {
    val executorService = Executors.newFixedThreadPool(3)
    val preViewStack = Stack<IView>()
    lateinit var currentView: IView
    val nextViewStack = Stack<IView>()
    lateinit var applicationContext: ConfigurableApplicationContext
    lateinit var stage: Stage
    lateinit var hostServices: HostServices
    fun quitApp() {
        executorService.shutdown()
        executorService.shutdownNow()
        nextViewStack.empty()
        preViewStack.empty()
        applicationContext.close()
        Platform.exit()
    }
}
