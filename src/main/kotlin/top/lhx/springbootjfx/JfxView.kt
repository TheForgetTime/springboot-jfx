package top.lhx.springbootjfx

import javafx.application.Platform
import javafx.scene.Parent
import javafx.scene.layout.BorderPane
import org.springframework.beans.factory.annotation.Autowired
import top.lhx.springbootjfx.annotation.FxmlView
import top.lhx.springbootjfx.util.SpringFXMlLoader
import kotlin.reflect.KClass

abstract class JfxView : IView {
    @Autowired
    private lateinit var springFXMlLoader: SpringFXMlLoader
    private var fxmlView: FxmlView = this.javaClass.getAnnotation(FxmlView::class.java)
    private var fxmlResPath: String
    private var isLoaded = false

    init {
        val tmp = fxmlView.value
        fxmlResPath = when {
            tmp == "" -> {
                "/${this.javaClass.name.replace(".", "/")}"
            }
            tmp.startsWith("/") -> {
                tmp
            }
            else -> {
                "/${this.javaClass.packageName.replace(".", "/")}/" + tmp
            }
        }
    }

    override fun getRoot(): Parent {
        return if (isLoaded) {
            springFXMlLoader.getRoot()
        } else {
            isLoaded = true
            springFXMlLoader.load("$fxmlResPath.fxml", null)
        }
    }

    fun back() {
        Platform.runLater {
            if (!GUIEnv.preViewStack.isEmpty()) {
                GUIEnv.nextViewStack.push(GUIEnv.currentView)
                GUIEnv.currentView = GUIEnv.preViewStack.pop()
                (GUIEnv.stage.scene.root as BorderPane).center = GUIEnv.currentView.getRoot()
            }
        }
    }

    fun forward() {
        Platform.runLater {
            if (!GUIEnv.nextViewStack.isEmpty()) {
                GUIEnv.preViewStack.push(GUIEnv.currentView)
                GUIEnv.currentView = GUIEnv.nextViewStack.pop()
                (GUIEnv.stage.scene.root as BorderPane).center = GUIEnv.currentView.getRoot()
            }
        }
    }

    fun showNew(viewClass: KClass<out JfxView>) {
        Platform.runLater {
            GUIEnv.nextViewStack.empty()
            GUIEnv.preViewStack.push(GUIEnv.currentView)
            val view: IView = GUIEnv.applicationContext.getBean(viewClass.java) as IView
            GUIEnv.currentView = view
            (GUIEnv.stage.scene.root as BorderPane).center = GUIEnv.currentView.getRoot()
        }
    }
}
