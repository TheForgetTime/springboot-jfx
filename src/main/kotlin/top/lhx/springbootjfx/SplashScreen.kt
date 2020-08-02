package top.lhx.springbootjfx

import javafx.scene.Parent
import javafx.scene.control.ProgressBar
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane

open class SplashScreen : IView {

    open val visible: Boolean = true

    override fun getRoot(): Parent {
        val imageView = ImageView(Image(javaClass.getResourceAsStream("/splash/javafx.png")))
        val progressBar = ProgressBar()
        progressBar.prefWidth = imageView.image.width
        val root = BorderPane()
        root.center = imageView
        root.bottom = progressBar
        return root
    }
}