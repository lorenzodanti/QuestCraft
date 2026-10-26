package it.unicam.cs.mpgc.rpg126280;

import it.unicam.cs.mpgc.rpg126280.controller.GameController;
import it.unicam.cs.mpgc.rpg126280.model.entity.Monster;
import it.unicam.cs.mpgc.rpg126280.model.entity.Player;
import it.unicam.cs.mpgc.rpg126280.model.map.Direction;
import it.unicam.cs.mpgc.rpg126280.model.map.Position;
import it.unicam.cs.mpgc.rpg126280.persistence.LeaderboardManager;
import it.unicam.cs.mpgc.rpg126280.persistence.ScoreEntry;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;

import java.util.List;

public class App extends Application {

    private GameController controller;
    private GridPane mapGrid;
    private Label statsLabel;
    private ProgressBar healthBar;
    private TextArea logArea;
    private Button potionButton;
    private Stage mainStage;

    @Override
    public void start(Stage stage) {
        this.mainStage = stage;
        showStartMenu();
    }

    private void showStartMenu() {
        VBox root = new VBox(12);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #1e1e2e;");

        Label title = new Label("⚔️ QUEST CRAFT ⚔️");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #f5c2e7;");

        TextField nameInput = new TextField();
        nameInput.setPromptText("Inserisci il nome del tuo Eroe");
        nameInput.setMaxWidth(230);
        nameInput.setStyle("-fx-font-size: 14px;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #f38ba8; -fx-font-weight: bold;");

        Button btnStart = new Button("🎮 Nuova Partita");
        btnStart.setStyle("-fx-background-color: #a6e3a1; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
        btnStart.setPrefWidth(230);

        Button btnLeaderboard = new Button("🏆 Classifica");
        btnLeaderboard.setStyle("-fx-background-color: #89b4fa; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
        btnLeaderboard.setPrefWidth(230);

        btnStart.setOnAction(e -> {
            String name = nameInput.getText().trim();
            if (name.isEmpty()) {
                errorLabel.setText("⚠️ Devi inserire un nome per giocare!");
                return;
            }
            errorLabel.setText("");
            this.controller = new GameController(name, 5, 5);
            showGameScene();
        });

        btnLeaderboard.setOnAction(e -> showLeaderboardDialog());

        root.getChildren().addAll(title, nameInput, errorLabel, btnStart, btnLeaderboard);

        // Evita che il TextField prenda subito il focus per mostrare il testo di suggerimento
        Platform.runLater(root::requestFocus);

        Scene scene = new Scene(root, 650, 450);
        mainStage.setTitle("QuestCraft RPG - Menu Principale");
        mainStage.setScene(scene);
        mainStage.show();
    }

    private void showGameScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1e1e2e;");

        mapGrid = new GridPane();
        mapGrid.setAlignment(Pos.CENTER);
        mapGrid.setHgap(8);
        mapGrid.setVgap(8);
        root.setCenter(mapGrid);

        VBox rightPanel = new VBox(12);
        rightPanel.setPadding(new Insets(15));
        rightPanel.setPrefWidth(260);
        rightPanel.setStyle("-fx-background-color: #2b2b3b; -fx-background-radius: 10px;");

        Label titleLabel = new Label("⚔️ QuestCraft");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #f5c2e7;");

        statsLabel = new Label();
        statsLabel.setStyle("-fx-text-fill: #cdd6f4;");

        healthBar = new ProgressBar(1.0);
        healthBar.setMaxWidth(Double.MAX_VALUE);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefHeight(140);
        logArea.setStyle("-fx-control-inner-background: #181825; -fx-text-fill: #a6adc8;");

        potionButton = new Button();
        potionButton.setMaxWidth(Double.MAX_VALUE);
        potionButton.setStyle("-fx-background-color: #fab387; -fx-font-weight: bold; -fx-cursor: hand;");
        potionButton.setOnAction(e -> handleUsePotion());

        Button menuButton = new Button("🏠 Torna al Menu");
        menuButton.setMaxWidth(Double.MAX_VALUE);
        menuButton.setStyle("-fx-background-color: #f38ba8; -fx-font-weight: bold;");
        menuButton.setOnAction(e -> showStartMenu());

        rightPanel.getChildren().addAll(titleLabel, statsLabel, healthBar, new Label("Registro:"), logArea, potionButton, menuButton);
        root.setRight(rightPanel);

        updateUI();

        Scene scene = new Scene(root, 700, 500);

        // Intercetta i tasti della tastiera
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);

        mainStage.setTitle("QuestCraft RPG - Partita in corso");
        mainStage.setScene(scene);
    }

    private void handleKeyPress(KeyEvent event) {
        Direction dir = null;
        switch (event.getCode()) {
            case UP, W -> dir = Direction.UP;
            case DOWN, S -> dir = Direction.DOWN;
            case LEFT, A -> dir = Direction.LEFT;
            case RIGHT, D -> dir = Direction.RIGHT;
            default -> { return; }
        }
        if (dir != null) {
            handleMove(dir);
            event.consume();
        }
    }

    private void handleMove(Direction direction) {
        String result = controller.movePlayer(direction);

        if ("COMBAT_START".equals(result)) {
            showBattleDialog();
        } else if ("VICTORY".equals(result)) {
            showEndGameDialog(true);
        } else {
            logArea.appendText(result + "\n");
        }
        updateUI();
    }

    private void showBattleDialog() {
        Monster monster = controller.getCurrentEncounter();
        Stage battleStage = new Stage();
        battleStage.initModality(Modality.APPLICATION_MODAL);
        battleStage.initOwner(mainStage);
        battleStage.setTitle("⚔️ COMBATTIMENTO!");

        VBox layout = new VBox(12);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #181825;");

        Label battleInfo = new Label("Scontro con: " + monster.getName() + " (PV: " + monster.getHealth() + ")");
        battleInfo.setStyle("-fx-text-fill: #f38ba8; -fx-font-weight: bold; -fx-font-size: 15px;");

        Label battleLog = new Label("Scegli la tua mossa...");
        battleLog.setStyle("-fx-text-fill: #cdd6f4;");

        Button btnAttack = new Button("⚔️ Attacca");
        btnAttack.setStyle("-fx-background-color: #f38ba8; -fx-font-weight: bold;");
        btnAttack.setPrefWidth(190);

        Button btnDefend = new Button("🛡️ Difenditi (-50% Danno)");
        btnDefend.setStyle("-fx-background-color: #89b4fa; -fx-font-weight: bold;");
        btnDefend.setPrefWidth(190);

        btnAttack.setOnAction(e -> {
            String res = controller.playerAttack();
            if (res.contains("sconfitto")) {
                logArea.appendText(res + "\n");
                battleStage.close();
            } else if ("GAME_OVER".equals(res)) {
                battleStage.close();
                showEndGameDialog(false);
            } else {
                battleLog.setText(res);
                battleInfo.setText(monster.getName() + " PV: " + Math.max(0, monster.getHealth()));
            }
            updateUI();
        });

        btnDefend.setOnAction(e -> {
            String res = controller.playerDefend();
            if ("GAME_OVER".equals(res)) {
                battleStage.close();
                showEndGameDialog(false);
            } else {
                battleLog.setText(res);
            }
            updateUI();
        });

        layout.getChildren().addAll(battleInfo, battleLog, btnAttack, btnDefend);
        Scene battleScene = new Scene(layout, 380, 260);
        battleStage.setScene(battleScene);
        battleStage.showAndWait();
    }

    private void showEndGameDialog(boolean victory) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(victory ? "🎉 VITTORIA!" : "☠️ SCONFITTA!");
        alert.setHeaderText(victory ? "Complimenti! Hai raggiunto la fine del Dungeon!" : "Sei stato sconfitto nel Dungeon!");
        alert.setContentText("Punteggio registrato in Classifica! Cosa vuoi fare?");

        ButtonType btnReplay = new ButtonType("🔄 Rigioca");
        ButtonType btnMenu = new ButtonType("🏆 Classifica / Menu");
        ButtonType btnExit = new ButtonType("🚪 Esci", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnReplay, btnMenu, btnExit);

        alert.showAndWait().ifPresent(type -> {
            if (type == btnReplay) {
                controller.initGame(controller.getPlayer().getName(), 5, 5);
                showGameScene();
            } else if (type == btnMenu) {
                showStartMenu();
                showLeaderboardDialog();
            } else {
                mainStage.close();
            }
        });
    }

    private void showLeaderboardDialog() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("🏆 CLASSIFICA GIOCATORI");

        VBox root = new VBox(12);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1e1e2e;");

        Label title = new Label("🏆 Classifica dei Punteggi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #f9e2af;");

        ListView<String> listView = new ListView<>();
        List<ScoreEntry> scores = controller != null ? controller.getLeaderboardManager().loadScores() : new LeaderboardManager().loadScores();

        if (scores.isEmpty()) {
            listView.getItems().add("Nessun punteggio salvato ancora.");
        } else {
            int rank = 1;
            for (ScoreEntry entry : scores) {
                listView.getItems().add(String.format(
                        "#%d %s | Punti: %d (Mostri sconfitti: %d, Livello: %d)",
                        rank, entry.getPlayerName(), entry.getScore(), entry.getMonstersDefeated(), entry.getLevelReached()
                ));
                rank++;
            }
        }

        Button btnClose = new Button("Chiudi");
        btnClose.setOnAction(e -> stage.close());

        root.getChildren().addAll(title, listView, btnClose);
        stage.setScene(new Scene(root, 420, 360));
        stage.showAndWait();
    }

    private void handleUsePotion() {
        String res = controller.usePotion();
        logArea.appendText(res + "\n");
        updateUI();
    }

    private ImageView createTileImage(String imagePath) {
        try {
            InputStream stream = getClass().getResourceAsStream(imagePath);
            if (stream != null) {
                Image img = new Image(stream, 50, 50, true, true);
                return new ImageView(img);
            }
        } catch (Exception e) {
            // Fallback in caso l'immagine non venga trovata
        }
        return null;
    }

    private void updateUI() {
        Player p = controller.getPlayer();
        statsLabel.setText(String.format("👤 %s  | 🎖️ Liv: %d\n⚔️ ATK: %d | ⭐ EXP: %d",
                p.getName(), p.getLevel(), p.getAttackPower(), p.getExperience()));
        healthBar.setProgress(Math.max(0, (double) p.getHealth() / p.getMaxHealth()));

        // Aggiorna lo stato del pulsante pozioni
        int count = controller.getPotionCount();
        potionButton.setText("🧪 Usa Pozione (" + count + " rimaste)");
        if (count == 0) {
            potionButton.setDisable(true);
            potionButton.setStyle("-fx-background-color: #585b70; -fx-text-fill: #a6adc8;");
        } else {
            potionButton.setDisable(false);
            potionButton.setStyle("-fx-background-color: #fab387; -fx-font-weight: bold; -fx-cursor: hand;");
        }

        mapGrid.getChildren().clear();
        for (int r = 0; r < controller.getMap().getRows(); r++) {
            for (int c = 0; c < controller.getMap().getCols(); c++) {
                Position pos = new Position(r, c);
                VBox cell = new VBox();
                cell.setAlignment(Pos.CENTER);
                cell.setPrefSize(70, 70);

                var entity = controller.getMap().getEntityAt(pos);

                if (pos.equals(controller.getPlayerPosition())) {
                    // Celle Giocatore
                    cell.setStyle("-fx-background-color: #2e4438; -fx-background-radius: 8px; -fx-border-color: #a6e3a1; -fx-border-radius: 8px;");
                    ImageView imgView = createTileImage("/images/player.png");
                    if (imgView != null) {
                        cell.getChildren().add(imgView);
                    } else {
                        cell.getChildren().add(new Label("🧙‍♂️\nTU"));
                    }
                } else if (r == controller.getMap().getRows() - 1 && c == controller.getMap().getCols() - 1) {
                    // Cella Meta / Vittoria
                    cell.setStyle("-fx-background-color: #453a24; -fx-background-radius: 8px; -fx-border-color: #f9e2af; -fx-border-radius: 8px;");
                    ImageView imgView = createTileImage("/images/winner.png");
                    if (imgView != null) {
                        cell.getChildren().add(imgView);
                    } else {
                        cell.getChildren().add(new Label("🏆\nMETA"));
                    }
                } else if (entity instanceof Monster monster) {
                    // Celle Mostri
                    cell.setStyle("-fx-background-color: #442a35; -fx-background-radius: 8px; -fx-border-color: #f38ba8; -fx-border-radius: 8px;");

                    String imgFileName;
                    if (monster.getName().equalsIgnoreCase("Drago")) {
                        imgFileName = "/images/dragon.png";
                    } else if (monster.getName().equalsIgnoreCase("Orco")) {
                        imgFileName = "/images/orco.png";
                    } else {
                        imgFileName = "/images/goblin.png";
                    }

                    ImageView imgView = createTileImage(imgFileName);
                    if (imgView != null) {
                        cell.getChildren().add(imgView);
                    } else {
                        cell.getChildren().add(new Label("👹\n" + monster.getName()));
                    }
                } else {
                    // Cella Vuota
                    cell.setStyle("-fx-background-color: #313244; -fx-background-radius: 8px;");
                    cell.getChildren().add(new Label("▪"));
                }

                mapGrid.add(cell, c, r);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}