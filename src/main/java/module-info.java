module top.lhx.springbootjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires kotlin.stdlib;
    requires spring.beans;
    requires spring.boot;
    requires spring.context;
    requires spring.boot.autoconfigure;

    exports top.lhx.springbootjfx;
    exports top.lhx.springbootjfx.annotation;
    exports top.lhx.springbootjfx.config;
    exports top.lhx.springbootjfx.util;

    opens top.lhx.springbootjfx;
    opens top.lhx.springbootjfx.annotation;
    opens top.lhx.springbootjfx.config;
    opens top.lhx.springbootjfx.util;
}
