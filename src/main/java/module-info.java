module sk.spu.diplomovka {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.fazecast.jSerialComm;

    opens sk.spu.diplomovka to javafx.fxml;
    exports sk.spu.diplomovka;


}

