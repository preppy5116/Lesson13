package sbrt.concurrent;

import com.fasterxml.jackson.databind.ObjectMapper;
import sbrt.concurrent.data.JSONController;
import sbrt.concurrent.ui.MainForm;

public class Main {
    public static void main(String[] args) {
        JSONController chunkController = new JSONController(new ObjectMapper());
        MainForm ui = new MainForm(chunkController);
        ui.setVisible(true);
    }
}
