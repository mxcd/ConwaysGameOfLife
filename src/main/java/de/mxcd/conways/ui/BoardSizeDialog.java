package de.mxcd.conways.ui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

/**
 * Created by Max Partenfelder on 27.01.2016.
 * Dialog for selecting a new width and height of the board
 */
public class BoardSizeDialog extends Dialog<Pair<Integer, Integer>>
{
    // Default width and height
    private SimpleIntegerProperty width = new SimpleIntegerProperty(10);
    private SimpleIntegerProperty height = new SimpleIntegerProperty(10);

    public static final int FAST_CLICK = 300;
    private int fastClickCount = 0;
    private long lastClick = 0;

    private HBox warning = new HBox();
    private GridPane grid;
    public static final int WARNING_THRESHOLD = 200;
    private boolean warningDisplayed = false;
    private Text warningText;

    public BoardSizeDialog(int currentWidth, int currentHeight)
    {
        this.width.set(currentWidth);
        this.height.set(currentHeight);

        this.setTitle("Select new size");
        this.setHeaderText("Select a new size for the Board");

        ImageView icon = new ImageView(BoardSizeDialog.class.getResource("images/resize.png").toString());
        icon.setFitWidth(50);
        icon.setPreserveRatio(true);
        this.setGraphic(icon);

        // Set the button types.
        this.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        grid = new GridPane();
        grid.setHgap(50);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 50, 20, 50));

        String widthString = "" + this.width.get();
        int stringLength = widthString.length();
        for(int i = 1; i <= (4 - stringLength); ++i)
            widthString = "0" + widthString;
        Text width = new Text(widthString);
        width.getStyleClass().add("controlHeadline");
        width.setWrappingWidth(80);
        width.setStyle("-fx-text-alignment: center;\n" +
                "    -fx-font-family: monospace;\n" +
                "    -fx-font-size: 30px;");
        this.width.addListener(((observable, oldValue, newValue) ->
        {
            String text = newValue.toString();
            int length = text.length();
            for(int i = 1; i <= (4 - length); ++i)
                text = "0" + text;
            width.setText(text);
        }));
        String heightString = "" + this.height.get();
        stringLength = heightString.length();
        for(int i = 1; i <= (4 - stringLength); ++i)
            heightString = "0" + heightString;
        Text height = new Text(heightString);
        height.setWrappingWidth(80);
        height.setStyle("-fx-text-alignment: center;\n" +
                        "    -fx-font-family: monospace;\n" +
                        "    -fx-font-size: 30px;");
        this.height.addListener(((observable, oldValue, newValue) ->
        {
            String text = newValue.toString();
            int length = text.length();
            for(int i = 1; i <= (4 - length); ++i)
                text = "0" + text;
            height.setText(text);
        }));
        ImageView widthUp = new ImageView(BoardSizeDialog.class.getResource("images/upArrow.png").toString());
        widthUp.setFitWidth(40);
        widthUp.setPreserveRatio(true);
        widthUp.setStyle("-fx-cursor: hand");
        widthUp.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
        {
            this.width.set(this.width.get() + getIncrement());
            if(this.width.get() > 1000)
                this.width.set(1000);
            if(this.width.get() > WARNING_THRESHOLD && !this.warningDisplayed)
            {
                this.warningDisplayed = true;
                this.warning.getChildren().add(this.warningText);
            }
        });
        ImageView widthDown = new ImageView(BoardSizeDialog.class.getResource("images/downArrow.png").toString());
        widthDown.setFitWidth(40);
        widthDown.setPreserveRatio(true);
        widthDown.setStyle("-fx-cursor: hand");
        widthDown.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
        {
            this.width.set(this.width.get() - getIncrement());
            if(this.width.get() <= 0)
                this.width.set(1);
            if(this.width.get() < WARNING_THRESHOLD && this.warningDisplayed)
            {
                this.warningDisplayed = false;
                this.warning.getChildren().remove(this.warningText);
            }

        });
        ImageView heightUp = new ImageView(BoardSizeDialog.class.getResource("images/upArrow.png").toString());
        heightUp.setFitWidth(40);
        heightUp.setPreserveRatio(true);
        heightUp.setStyle("-fx-cursor: hand");
        heightUp.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
        {
            this.height.set(this.height.get() + getIncrement());
            if(this.height.get() > 1000)
                this.height.set(1000);
            if(this.height.get() > WARNING_THRESHOLD && !this.warningDisplayed)
            {
                this.warningDisplayed = true;
                this.warning.getChildren().add(this.warningText);
            }
        });
        ImageView heightDown = new ImageView(BoardSizeDialog.class.getResource("images/downArrow.png").toString());
        heightDown.setFitWidth(40);
        heightDown.setPreserveRatio(true);
        heightDown.setStyle("-fx-cursor: hand");
        heightDown.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
        {
            this.height.set(this.height.get() - getIncrement());
            if(this.height.get() <= 0)
                this.height.set(1);
            if(this.height.get() < WARNING_THRESHOLD && this.warningDisplayed)
            {
                this.warningDisplayed = false;
                this.warning.getChildren().remove(this.warningText);
            }
        });

        Text widthLabel = new Text("Width");
        widthLabel.getStyleClass().add("controlHeadline");
        Text heightLabel = new Text("Height");
        heightLabel.getStyleClass().add("controlHeadline");

        grid.add(widthLabel, 0, 0);
        grid.add(widthUp, 0, 1);
        grid.add(width, 0, 2);
        grid.add(widthDown, 0, 3);

        grid.add(heightLabel, 1, 0);
        grid.add(heightUp, 1, 1);
        grid.add(height, 1, 2);
        grid.add(heightDown, 1, 3);

        warningText = new Text("WARNING: Boards with a size of " + WARNING_THRESHOLD + " and greater are ony recommended" +
                " to use on very fast machines. Otherwise drawbacks in performance will be noticeable.");
        warningText.setWrappingWidth(250);
        warningText.setStyle("-fx-font-size: 15px; -fx-font-family: monospace; -fx-fill: red");
        warning.setMinHeight(80);
        GridPane.setColumnSpan(warning, 3);
        this.grid.add(this.warning, 0,4);

        this.getDialogPane().setContent(grid);

        this.setResultConverter(dialogButton ->
        {
            if (dialogButton == ButtonType.FINISH)
            {
                return new Pair<>(this.width.get(), this.height.get());
            }
            return null;
        });
    }

    private int getIncrement()
    {
        if(System.currentTimeMillis() - this.lastClick < FAST_CLICK)
        {
            this.fastClickCount++;
        }
        else
        {
            this.fastClickCount = 0;
        }
        this.lastClick = System.currentTimeMillis();

        if(this.fastClickCount > 20)
            return 100;
        else if(this.fastClickCount > 15)
            return 50;
        else if(this.fastClickCount > 10)
            return 10;
        else if(this.fastClickCount > 5)
            return 5;
        else
            return 1;
    }
}