package top.lhx.springbootjfx

import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import top.lhx.springbootjfx.config.JavaFxConfig
import top.lhx.springbootjfx.util.DrawUtil
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.reflect.KClass

@ComponentScan("top.lhx.springbootjfx.util")
@EnableConfigurationProperties(JavaFxConfig::class)
@ConfigurationPropertiesScan("top.lhx.springbootjfx.config")
abstract class JfxApp<T : JfxView> : Application() {
    private val splashShowing = CompletableFuture<Runnable>()
    private val errorConsumer: Consumer<Throwable> = defaultErrorConsumer()

    open fun defaultErrorConsumer(): Consumer<Throwable> {
        return Consumer {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.contentText = it.message
            alert.showAndWait()
            Platform.exit()
        }
    }

    companion object {
        lateinit var _appClass: Class<out Any>
        lateinit var _initialView: Class<out JfxView>
        lateinit var _splashScreen: IView
    }

    fun runApp(
            springAppClass: KClass<out Any>,
            initialMainView: KClass<out JfxView>,
            args: Array<out String>,
            splashScreen: IView = SplashScreen()
    ) {
        runApp(springAppClass.java,initialMainView.java,args,splashScreen)
    }

    fun runApp(
            springAppClass: Class<out Any>,
            initialMainView: Class<out JfxView>,
            args: Array<out String>
    ) {
        runApp(springAppClass,initialMainView,args,SplashScreen())
    }

    fun runApp(
        springAppClass: Class<out Any>,
        initialMainView: Class<out JfxView>,
        args: Array<out String>,
        splashScreen: IView
    ) {
        _appClass = springAppClass
        _initialView = initialMainView
        _splashScreen = splashScreen
        launch(this::class.java, *args)
    }

    final override fun init() {
        CompletableFuture
            .supplyAsync(
                Supplier { SpringApplicationBuilder(_appClass).run(*parameters.raw.toTypedArray()) },
                GUIEnv.executorService
            )
            .whenCompleteAsync(BiConsumer { ctx, thr: Throwable? ->
                if (thr != null) {
                    Platform.runLater {
                        errorConsumer.accept(thr)
                    }
                } else {
                    GUIEnv.applicationContext = ctx
                }
            }, GUIEnv.executorService)
            .thenAcceptBothAsync(splashShowing, BiConsumer { _, closeSplash ->
                Platform.runLater(closeSplash)
            }, GUIEnv.executorService)
    }

    final override fun start(primaryStage: Stage) {
        GUIEnv.stage = primaryStage
        GUIEnv.hostServices = this.hostServices
        val splashStage = Stage(StageStyle.TRANSPARENT)
        splashStage.scene = Scene(_splashScreen.getRoot())
        splashStage.initStyle(StageStyle.TRANSPARENT)
        splashStage.show()

        splashShowing.complete(Runnable {
            showInitView()
            splashStage.close()
            splashStage.scene = null
        })
    }

    final override fun stop() {
        GUIEnv.quitApp()
    }

    private fun showInitView() {
        try {
            val view = GUIEnv.applicationContext.getBean(_initialView) as IView
            val javaFxConfig = GUIEnv.applicationContext.getBean(JavaFxConfig::class.java)
            val root = BorderPane()
            val stageStyle = javaFxConfig.stageStyle
            if (stageStyle == StageStyle.TRANSPARENT || stageStyle == StageStyle.UTILITY) {
                root.border = Border(
                    BorderStroke(
                        null,
                        null,
                        Color.TRANSPARENT,
                        null,
                        null,
                        null,
                        BorderStrokeStyle.SOLID,
                        null,
                        null,
                        BorderWidths.DEFAULT,
                        Insets(2.0)
                    )
                )
                DrawUtil.addDrawFunc(GUIEnv.stage, root)
            }
            root.center = view.getRoot()
            GUIEnv.currentView = view
            val scene = Scene(root)
            GUIEnv.stage.scene = scene
            GUIEnv.stage.initStyle(stageStyle)
            GUIEnv.stage.title = javaFxConfig.title
            GUIEnv.stage.width = javaFxConfig.width
            GUIEnv.stage.height = javaFxConfig.height

            GUIEnv.stage.show()
        } catch (e: Exception) {
            errorConsumer.accept(e)
        }
    }
}
