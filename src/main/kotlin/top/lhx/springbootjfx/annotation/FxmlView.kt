package top.lhx.springbootjfx.annotation

import org.springframework.stereotype.Component

@Component
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class FxmlView(val value: String = "")