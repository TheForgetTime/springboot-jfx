package top.lhx.springbootjfx.util

import javafx.fxml.FXMLLoader
import javafx.fxml.LoadException
import javafx.scene.Node
import top.lhx.springbootjfx.GUIEnv
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class SpringFXMlLoader : FXMLLoader() {
    fun <T : Node> load(url: String, resourceBundle: ResourceBundle?): T = try {
        val fxmlStream = this.javaClass.getResourceAsStream(url)
        super.setLocation(this.javaClass.classLoader.getResource("/"))
        super.setResources(resourceBundle)
        super.setControllerFactory { param -> GUIEnv.applicationContext.getBean(param) }
        this.load(fxmlStream)
    } catch (loadE: LoadException) {
        throw RuntimeException("布局加载失败", loadE)
    } catch (ioe: IOException) {
        throw RuntimeException(FileNotFoundException(url + "未找到,请重写getRoot()方法或创建该文件"))
    } catch (npe: NullPointerException) {
        throw RuntimeException(FileNotFoundException(url + "未找到,请重写getRoot()方法或创建该文件"))
    }
}
