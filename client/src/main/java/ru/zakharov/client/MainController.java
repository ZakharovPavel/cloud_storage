package ru.zakharov.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.zakharov.common.AbstractMessage;
import ru.zakharov.common.FileMessage;
import ru.zakharov.common.FileRequest;
import ru.zakharov.common.TextRequest;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    VBox rootNode;

    @FXML
    VBox authPanel;

    @FXML
    HBox uiPanel;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    TextField tfClientFileName;

    @FXML
    TextField tfServerFileName;

    @FXML
    ListView<String> clientFilesList;

    @FXML
    ListView<String> serverFilesList;

    private boolean isAuthorised;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof TextRequest) {

                    }
                    if (am instanceof FileMessage) {
                        FileMessage fm = (FileMessage) am;
                        Files.write(Paths.get("client_storage/" + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                        refreshLocalFilesList();
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                Network.stop();
            }
        });
        t.setDaemon(true);
        t.start();
        refreshLocalFilesList();
    }

    public void setAuthorised(boolean isAuthorised) {
        this.isAuthorised = isAuthorised;
        if(!isAuthorised) {
            authPanel.setVisible(true);
            authPanel.setManaged(true);
            uiPanel.setVisible(false);
            uiPanel.setManaged(false);
        } else {
            authPanel.setVisible(false);
            authPanel.setManaged(false);
            uiPanel.setVisible(true);
            uiPanel.setManaged(true);
        }
    }

    public void tryToAuth(ActionEvent actionEvent) {
        isAuthorised = true;
        setAuthorised(isAuthorised);
    }

    public void pressOnUploadBtn(ActionEvent actionEvent) {
        if (tfClientFileName.getLength() > 0) {
            try {
                Network.sendMsg(new FileMessage(Paths.get("client_storage/" + tfClientFileName.getText())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            tfClientFileName.clear();
        }
    }

    public void pressOnDownloadBtn(ActionEvent actionEvent) {
        if (tfServerFileName.getLength() > 0) {
            Network.sendMsg(new FileRequest(tfServerFileName.getText()));
            tfServerFileName.clear();
        }
    }

    public void refreshLocalFilesList() {
        updateUI(() -> {
            try {
                clientFilesList.getItems().clear();
                serverFilesList.getItems().clear();
                Files.list(Paths.get("client_storage")).map(p -> p.getFileName().toString()).forEach(o -> clientFilesList.getItems().add(o));
                Files.list(Paths.get("server_storage")).map(p -> p.getFileName().toString()).forEach(o -> serverFilesList.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void updateUI(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }

    public void pressOnRefreshBtn(ActionEvent actionEvent) {
        refreshLocalFilesList();
    }

    public void pressOnDeleteBtnClient(ActionEvent actionEvent) {
        try {
            Files.deleteIfExists(Paths.get("client_storage/" + tfClientFileName.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        tfClientFileName.clear();
    }

    public void pressOnDeleteBtnServer(ActionEvent actionEvent) {
        try {
            Files.deleteIfExists(Paths.get("server_storage/" + tfServerFileName.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        tfServerFileName.clear();
    }
}
