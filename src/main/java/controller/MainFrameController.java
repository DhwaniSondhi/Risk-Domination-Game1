package controller;

import model.GameMap;
import utility.FileHelper;
import utility.GameStateChangeListener;
import utility.MapHelper;
import view.MainFrame;
import view.MapCreatorFrame;
import view.StartUpFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * This is the Controller for the MainFrame. see {@link BaseController}
 * implements {@link ActionListener} for MenuItems
 * implements {@link GameStateChangeListener} to observe state change
 */
public class MainFrameController extends BaseController<MainFrame> implements
        ActionListener,
        GameStateChangeListener,
        Observer {

    /**
     * Controller for MainFrame
     *
     * @param view MainFrame view
     */
    public MainFrameController(MainFrame view) {
        super(view);
        model.addObserver(view);
        model.addObserver(this);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        if (model.currentPhase != model.previousPhase && model.stateHasChanged) {
            model.stateHasChanged = false;
            switch (model.currentPhase) {
                case REINFORCE:
                    if (model.previousPhase == GameMap.Phase.STARTUP) {
                        view.setUpGamePanels();
                        model.resetCurrentPlayer();
                    } else if (model.previousPhase == GameMap.Phase.FORTIFY) {
                        model.changeToNextPlayer(true);
                        view.fortifyPanel.setVisible(false);
                        view.reinforcementPanel.setVisible(true);
                        view.reinforcementPanel.update();
                    }
                    if (!model.currentPlayer.isHuman()) {
                        model.currentPlayer.reinforce(null, 0);
                    }
                    break;
                case ATTACK:
                    view.reinforcementPanel.setVisible(false);
                    view.attackPanel.setVisible(true);
                    view.attackPanel.revalidate();
                    view.attackPanel.update();
                    if (!model.currentPlayer.isHuman()) {
                        model.currentPlayer.attack(null, null, false);
                    }
                    break;
                case FORTIFY:
                    view.attackPanel.setVisible(false);
                    view.fortifyPanel.setVisible(true);
                    view.fortifyPanel.update();
                    if (!model.currentPlayer.isHuman()) {
                        model.currentPlayer.fortify(0, null, null);
                    }
                    break;
            }
        }
    }

    /**
     * Invoked when an game map is loaded
     */
    @Override
    public void onMapLoaded() {
        new StartUpFrame(this);
    }

    /**
     * Invoked when an startup is completed is loaded
     */
    @Override
    public void onStartUpCompleted() {
        /*model.changePhase(GameMap.Phase.REINFORCE);
        view.setUpGamePanels();
        model.resetCurrentPlayer();
        if (!model.currentPlayer.isHuman()) {
            model.currentPlayer.reinforce(null, 0);
        }*/
    }

    /**
     * Invoked when reinforcement is done
     */
    @Override
    public void onReinforcementCompleted() {
        /*model.changePhase(GameMap.Phase.ATTACK);
        view.reinforcementPanel.setVisible(false);
        view.attackPanel.setVisible(true);
        view.attackPanel.revalidate();
        view.attackPanel.update();
        if (!model.currentPlayer.isHuman()) {
            model.currentPlayer.attack(null, null, false);
        }*/
    }

    /**
     * Invoked when attack is done
     */
    @Override
    public void onAttackCompleted() {
       /* model.changePhase(GameMap.Phase.FORTIFY);
        view.attackPanel.setVisible(false);
        view.fortifyPanel.setVisible(true);
        view.fortifyPanel.update();
        if (!model.currentPlayer.isHuman()) {
            model.currentPlayer.fortify(0, null, null);
        }*/
    }

    /**
     * Invoked when fortification is done
     */
    @Override
    public void onFortificationCompleted() {
        /*model.changePhase(GameMap.Phase.REINFORCE);
        model.changeToNextPlayer(true);
        view.fortifyPanel.setVisible(false);
        view.reinforcementPanel.setVisible(true);
        view.reinforcementPanel.update();

        if (!model.currentPlayer.isHuman()) {
            model.currentPlayer.reinforce(null, 0);
        }*/
    }

    /**
     * This function is checking if the action is
     * Load countryGraph : loads the countryGraph using {@link JFileChooser}
     * Create countryGraph : creates the countryGraph and does validation
     * Exit : exits the game
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean isLoadMap, isEditMap;
        isLoadMap = e.getActionCommand().equalsIgnoreCase("Load GameMap");
        isEditMap = e.getActionCommand().equalsIgnoreCase("Edit GameMap");
        if (isEditMap || isLoadMap) {
            File dir = new File("maps");
            dir.mkdir();
            JFileChooser file = new JFileChooser(dir);
            int confirmValue = file.showOpenDialog(null);

            if (confirmValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = file.getSelectedFile();
                if (isLoadMap) {
                    try {

                        FileHelper.loadToConfig(selectedFile);
                        if (MapHelper.validateContinentGraph() && MapHelper.validateMap()) {
                            onMapLoaded();
                        } else {
                            FileHelper.emptyConfig();
                            JOptionPane.showMessageDialog(null, "File Validation Failed", "Error Message", JOptionPane.ERROR_MESSAGE);
                            System.out.println("File validation failed");
                        }
                    } catch (IllegalStateException exception) {

                        FileHelper.emptyConfig();
                        JOptionPane.showMessageDialog(null, exception.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
                        System.out.println("File validation failed : " + exception.getMessage());
                    }
                } else {
                    new MapCreatorFrame("Edit Map", selectedFile);
                }
            }
        } else if (e.getActionCommand().equalsIgnoreCase("Create GameMap")) {
            new MapCreatorFrame("Create Map");
        } else if (e.getActionCommand().equalsIgnoreCase("exit")) {
            System.exit(0);
        }
    }
}
