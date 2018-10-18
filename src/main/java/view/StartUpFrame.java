package view;

import controller.StartUpController;
import model.Country;
import model.GameMap;
import utility.GameStateChangeListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * GUI part of StartUp Panel
 * extends (@link JPanel)
 */
public class StartUpFrame extends JFrame {

    JComboBox numOfPlayers;
    StartUpController startUpController;
    JLabel jLabelplayers;
    JComboBox jComboBoxCountries;
    JComboBox numberOfArmies;
    JButton jButtonAssignArmy;
    private JPanel mainStartPanel, countriesPanel;
    private JLabel jlabel = new JLabel("Welcome to the RISK Game\n");
    private JLabel jlabel2 = new JLabel("Choose number of players:");
    private JButton buttonSubmit = new JButton("Submit");
    private JTextField addArmies;


    /**
     * Constructor
     * <p>
     *     To create the Panel for StartUp Phase of the Game
     * </p>
     * @param stateChangeListener observer for game state
     */
    public StartUpFrame(GameStateChangeListener stateChangeListener) {
        super("Welcome to the Game");

        startUpController = new StartUpController(this);
        startUpController.setStateChangeListener(stateChangeListener);

        mainStartPanel = new JPanel();
        jLabelplayers = new JLabel();
        mainStartPanel.setLayout(new BoxLayout(mainStartPanel, BoxLayout.Y_AXIS));
        add(mainStartPanel);

        mainStartPanel.setBorder(new LineBorder(Color.BLACK));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setVisible(true);

        buttonSubmit.setName("submit");
        buttonSubmit.addActionListener(startUpController);
        numOfPlayers = new JComboBox();
        for (int i = 1; i < Math.min(6, GameMap.getInstance().countries.size()); i++) {
            numOfPlayers.addItem(i + 1);
        }


        countriesPanel = new JPanel();
        countriesPanel.setLayout(new GridBagLayout());
        //jlabel2.setLabelFor(nuOfPlayers);
        mainStartPanel.add(jlabel);
        mainStartPanel.add(jlabel2);
        mainStartPanel.add(numOfPlayers);
        mainStartPanel.add(buttonSubmit);
        mainStartPanel.add(countriesPanel);

        mainStartPanel.revalidate();
        jButtonAssignArmy = new JButton("Assign");
        jButtonAssignArmy.setName("Assign");
        jButtonAssignArmy.addActionListener(startUpController);
    }

    /**
     * To get the number of players needed in the game
     *
     * @return
     */
    public int getNumOfPlayers() {
        return (Integer) numOfPlayers.getSelectedItem();
    }

    /**
     * To get the unique player IDs
     *
     * @return
     */
    public String getjLabelPlayerValue() {
        return jLabelplayers.getText();
    }

    /**
     * TO assign the unique player IDs to players
     *
     * @param newPlayerId
     */
    public void setjLabelPlayerValue(String newPlayerId) {
        jLabelplayers.setText(newPlayerId);
    }

    /**
     * to create a list of countries
     *
     * @return Combo Box for the countries
     */
    public int getjComboBoxCountry() {

        return jComboBoxCountries.getSelectedIndex();
    }

    /**
     * To get the number of armies
     *
     * @return number of armies which player has
     */
    public int getNumberOfArmies() {
        return (Integer) numberOfArmies.getSelectedItem();
    }

    /**
     * observer for the assign button
     */
    public void clickAssignButton() {
        jButtonAssignArmy.doClick();
    }

    /**
     * Creates {@link GridBagConstraints} with provided gridX and gridY values
     *
     * @param x value for constraints gridx (row in the grid)
     * @param y value for constraints gridY (col in the grid)
     * @return default constraints (see {@link GridBagConstraints}) with provided x,y values
     */
    private GridBagConstraints getConstraints(int x, int y) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 0;
        constraints.weightx = 0;
        return constraints;
    }

    /**
     * To update the list of countries which the player still has
     *
     * @param countries list of the countries of current player
     */
    public void updateCountries(List<Country> countries) {
        countriesPanel.removeAll();
        addArmies = new JTextField(10);
        jComboBoxCountries = new JComboBox();

        if (countries != null) {
            for (Country countryName : countries) {
                jComboBoxCountries.addItem(countryName);
            }
        }

        countriesPanel.add(new JLabel("Player"), getConstraints(0, 0));
        jLabelplayers.setText(String.valueOf(GameMap.getInstance().currentPlayer.id));
        countriesPanel.add(jLabelplayers, getConstraints(0, 1));
        countriesPanel.add(jComboBoxCountries, getConstraints(0, 2));
        countriesPanel.add(new JLabel("Select Number of armies"), getConstraints(0, 3));
        numberOfArmies = new JComboBox(startUpController.getArmies());
        countriesPanel.add(numberOfArmies, getConstraints(0, 4));


        countriesPanel.add(jButtonAssignArmy, getConstraints(0, 5));
        countriesPanel.revalidate();
        countriesPanel.repaint();
    }
}
