package de.mxcd.conways.ui;

import de.mxcd.conways.game.Rules;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Created by Max Partenfelder on 27.01.2016.
 * Dialog for selecting a new width and height of the board
 */
public class RulesDialog extends Dialog<Rules>
{

    private Rules rules = new Rules();

    public RulesDialog(Rules rules)
    {
        this.rules = rules.deepCopy();
        init();
    }

    public RulesDialog()
    {
        init();
    }

    public void init()
    {
        this.setTitle("Change the rules");
        this.setHeaderText("Since this is your game of life, you can play God change it's rules:");

        ImageView icon = new ImageView(RulesDialog.class.getResource("images/god.png").toString());
        icon.setFitWidth(50);
        icon.setPreserveRatio(true);
        this.setGraphic(icon);

        // Set the button types.
        this.getDialogPane().getButtonTypes().addAll(ButtonType.FINISH, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        for(int i = 0; i <= 9; ++i)
        {
            ToggleGroup group = null;
            if(i != 0)
                 group = new ToggleGroup();

            for(int j = 0; j <= 3; ++j)
            {
                if(i == 0 && j == 0)
                {
                    // Do nothing
                }
                else if(j == 0)
                {
                    Text t = new Text("" + (i-1));
                    grid.add(t, i, j);
                }
                else if(i == 0 && j != 0)
                {
                    Text t = new Text();
                    switch(j)
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
                    grid.add(t, i, j);
                }
                else
                {
                    if(group != null)
                    {
                        RadioButton radioBtn = new RadioButton();
                        radioBtn.setToggleGroup(group);
                        grid.add(radioBtn, i, j);

                        if(rules.getFate(i-1) == Rules.Fate.BIRTH && j == 1)
                            radioBtn.setSelected(true);
                        else if(rules.getFate(i-1) == Rules.Fate.LIFE && j == 2)
                            radioBtn.setSelected(true);
                        else if(rules.getFate(i-1) == Rules.Fate.DEATH && j == 3)
                            radioBtn.setSelected(true);

                        final int finalI = i;
                        final int finalJ = j;
                        radioBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
                        {
                            switch(finalJ)
                            {
                                case 1:
                                    this.rules.setRule(finalI - 1, Rules.Fate.BIRTH);
                                    break;
                                case 2:
                                    this.rules.setRule(finalI - 1, Rules.Fate.LIFE);
                                    break;
                                case 3:
                                    this.rules.setRule(finalI - 1, Rules.Fate.DEATH);
                                    break;
                            }
                        });
                    }
                }
            }
        }

        this.getDialogPane().setContent(grid);

        this.setResultConverter(dialogButton ->
        {
            if (dialogButton == ButtonType.FINISH)
            {
                return this.rules;
            }
            return null;
        });
    }
}