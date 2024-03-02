package sbrt.concurrent.ui;

import reactor.core.Disposable;
import sbrt.concurrent.data.JSONController;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {
    private final JSONController controller;
    Disposable disposable;

    public MainForm(JSONController chunkController) {
        this.controller = chunkController;
        runUI();
    }

    private void runUI() {
        setTitle("JSON");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton receiveDataButton = new JButton("Получить данные");
        JButton abort = new JButton("Прервать");
        JTextArea displayArea = new JTextArea(10, 30);

        displayArea.setEditable(false);

        buttonPanel.add(receiveDataButton);
        buttonPanel.add(abort);
//        abort.setVisible(false);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        receiveDataButton.addActionListener(e -> receiveDataAndUpdateForm(displayArea));
        abort.addActionListener(e -> abortData());
        getContentPane().add(mainPanel);
    }

    private void receiveDataAndUpdateForm(JTextArea displayArea) {
        displayArea.selectAll();
        displayArea.replaceSelection("");
        disposable = controller.getJSONChunks().subscribe(
                json -> {
                    displayArea.append(json + "\n");
                    displayArea.setCaretPosition(displayArea.getDocument().getLength());
                },
                error -> displayArea.append("Ошибка получения данных: " + error.getMessage() + "\n"),
                () -> displayArea.append("Получение данных завершено\n")
        );
    }

    private void abortData() {
        if(disposable!= null) {
            disposable.dispose();
        }
    }
}

