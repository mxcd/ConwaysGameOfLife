package de.mxcd.conways.ui;

import de.mxcd.conways.game.Board;
import de.mxcd.conways.game.EvolutionThread;
import de.mxcd.conways.game.ForecastThread;
import de.mxcd.conways.game.Rules;
import de.mxcd.conways.util.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Max Partenfelder on 09.01.2016.
 */

/**
 * FXML Controller class for the @EvolutionWindow.fxml file
 */
public class EvolutionWindow implements Initializable, ForecastListener, StepListener
{
    private Board board;
    private Grid grid;
    private SimpleIntegerProperty stepCount = new SimpleIntegerProperty(0);

    @FXML
    private Pane gridContainer;

    @FXML
    private Slider speedSlider;
    @FXML
    private Text speedText;

    @FXML
    private Text forecastText;
    @FXML
    private ImageView forecastImage;

    @FXML
    private Text stepCounter;

    @FXML
    private MenuBar menuBar;

    @FXML
    private GridPane rulesDisplay;


    public void initialize(URL location, ResourceBundle resources)
    {
        Program.INSTANCE.setEvolutionWindow(this);
        this.board = Program.INSTANCE.getCurrentBoard();

        // If the board is not set yet, create a new empty board with default 10x10 dimensions
        if(this.board == null)
        {
            this.board = new Board(10);
            Program.INSTANCE.setCurrentBoard(board);
        }

        this.grid = new GridCanvas(this.board);

        this.gridContainer.getChildren().add((Node)this.grid);
        this.gridContainer.widthProperty().addListener(((observable, oldValue, newValue) ->
        {
            grid.setContainerWidth(newValue.doubleValue());
            grid.sizeChanged();
            grid.repaint();
        }));
        this.gridContainer.heightProperty().addListener(((observable, oldValue, newValue) ->
        {
            grid.setContainerHeight(newValue.doubleValue());
            grid.sizeChanged();
            grid.repaint();
        }));

        speedSlider.setMin(0);
        speedSlider.setMax(EvolutionThread.DELAYS.length-1);
        speedSlider.setValue(Program.INSTANCE.getCurrentEvolutionDelayIndex());
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            if(Program.INSTANCE.getEvolutionThread() != null)
            {
                Program.INSTANCE.setCurrentEvolutionDelayIndex(newValue.intValue());
                this.speedText.setText((1000.0d / EvolutionThread.DELAYS[newValue.intValue()]) + " EPS");
            }
        });

        Program.INSTANCE.setForecastThread(new ForecastThread());
        Program.INSTANCE.getForecastThread().addForecastListener(this);

        this.forecastText.setWrappingWidth(180);
        this.forecastImage.setFitWidth(50);
        this.forecastImage.setPreserveRatio(true);

        this.stepCount.addListener(((observable, oldValue, newValue) ->
        {
            stepCounter.setText(newValue.toString());
        }));

        Program.INSTANCE.addStepListener(this);

        this.menuBar.getMenus().add(2, ExampleLoader.loadExamples());

        this.updateRulesDisplay();

        this.repaint();
    }

    @FXML
    public void onStart(ActionEvent event)
    {
        if(Program.INSTANCE.getEvolutionThread() == null)
        {
            EvolutionThread evolutionThread = new EvolutionThread();
            Program.INSTANCE.setEvolutionThread(evolutionThread);
            evolutionThread.start();
        }
    }

    public void onStop(ActionEvent event)
    {
        if(Program.INSTANCE.getEvolutionThread() != null)
        {
            Program.INSTANCE.getEvolutionThread().setRunning(false);
            Program.INSTANCE.setEvolutionThread(null);
        }
    }

    @Override
    public void pushForecastResult(ForecastResult result)
    {
        Platform.runLater(()->
        {
            this.forecastText.setText(result.toString());
            if(result.getType() == ForecastResultType.DEAD)
            {
                this.forecastImage.setImage(new Image(EvolutionWindow.class.getResourceAsStream("images/dead.png")));
            }
            else if(result.getType() == ForecastResultType.LOOP)
            {
                this.forecastImage.setImage(new Image(EvolutionWindow.class.getResourceAsStream("images/loop.png")));
            }
            else if(result.getType() == ForecastResultType.STEADY)
            {
                this.forecastImage.setImage(new Image(EvolutionWindow.class.getResourceAsStream("images/steady.png")));
            }
        });
    }

    @Override
    public void calculatingForecast()
    {
        Platform.runLater(()->
        {
            this.forecastImage.setImage(new Image(EvolutionWindow.class.getResourceAsStream("images/loading.gif")));
            this.forecastText.setText("Calculating Forecast...");
            this.stepCount.set(0);
        });
    }

    @Override
    public void stepComplete()
    {
        Platform.runLater(()->
        {
            this.stepCount.set(this.stepCount.get() +1);
        });
    }

    @FXML
    public void onLoad()
    {
        Loader.loadFileDialog();
    }

    @FXML
    public void onSave()
    {
        Saver.saveGame();
    }

    @FXML
    public void onClose()
    {
        Platform.exit();
    }

    public void repaint()
    {
        this.grid.repaint();
    }

    /**
     * Resetting the game board and the rules
     */
    @FXML
    public void onClear()
    {
        Program.INSTANCE.setCurrentBoard(new Board(this.board.getWidth(), this.board.getHeight()));
        Program.INSTANCE.setScene(SceneDef.EVOLUTION);
    }

    @FXML
    public void onSetBoardSize()
    {
        BoardSizeDialog.showDialog();
    }

    public void onChangeRules()
    {
        Program.INSTANCE.stopEvolutionThread();
        Optional<Rules> newRules = new RulesDialog(Program.INSTANCE.getCurrentRules()).showAndWait();
        if(newRules.isPresent())
        {
            Program.INSTANCE.stopForecastThread();
            Program.INSTANCE.setCurrentRules(newRules.get());
            Program.INSTANCE.getForecastThread().restart();
            this.updateRulesDisplay();
        }
    }

    public void onReset(ActionEvent event)
    {
        Program.INSTANCE.setCurrentBoard(new Board(20, 20));
        Program.INSTANCE.setCurrentRules(new Rules());
        Program.INSTANCE.setScene(SceneDef.EVOLUTION);
    }

    public void onAbout(ActionEvent event)
    {
        new AboutDialog().show();
    }

    public void updateRulesDisplay()
    {
        rulesDisplay.getChildren().clear();
        for(int line = 0; line < 4; ++ line)
        {
            for (int i = 0; i < 10; ++i)
            {
                if(i == 0)
                {
                    Text t = new Text();
                    switch(line)
                    {
                        case 1:
                            t.setText("Birth");
                            break;
                        case 2:
                            t.setText("Life");
                            break;
                        case 3:
                            t.setText("Death");
                            break;
                    }
                    rulesDisplay.add(t, i, line);
                }
                else if(line == 0)
                {
                    Text t = new Text("" + i);
                    rulesDisplay.add(t, i, line);
                }
                else
                {
                    Label rectangle = new Label();
                    String styleClass = "neutralIndicator";
                    switch(line)
                    {
                        case 1:
                            if(Program.INSTANCE.getCurrentRules().getFate(i-1) == Rules.Fate.BIRTH)
                                styleClass = "birthIndicator";
                            break;
                        case 2:
                            if(Program.INSTANCE.getCurrentRules().getFate(i-1) == Rules.Fate.LIFE)
                                styleClass = "lifeIndicator";
                            break;
                        case 3:
                            if(Program.INSTANCE.getCurrentRules().getFate(i-1) == Rules.Fate.DEATH)
                                styleClass = "deathIndicator";
                            break;
                    }
                    rectangle.getStyleClass().add(styleClass);
                    rulesDisplay.add(rectangle, i, line);
                }
            }
        }
    }
}