package top.lhx.springbootjfx.config

import javafx.stage.StageStyle
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import top.lhx.springbootjfx.util.SpringFXMlLoader

@Configuration
@ConfigurationProperties(prefix = "springjfx")
class JavaFxConfig {
    var title: String = "This is title"
    var width: Double = 800.0
    var height: Double = 600.0
    var stageStyle: StageStyle = StageStyle.DECORATED

    @Bean
    @Scope("prototype")
    fun springFxmlLoader(): SpringFXMlLoader {
        return SpringFXMlLoader()
    }
}