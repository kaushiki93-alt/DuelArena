
package duelarena;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ArenaSelectionPanel extends JPanel {

    private final String[] arenas = {
            "bg1.png", "Bg2.png", "Bg3.png",
            "Bg4.png", "Bg5.png", "Bg6.png",
            "bg7.png", "bg8.png", "bg9.png"
    };

    private final ArrayList<JButton> arenaButtons = new ArrayList<>();

    private JTextField player1NameField;
    private JTextField player2NameField;

    private JComboBox<String> player1CharacterBox;
    private JComboBox<String> player2CharacterBox;

    private CharacterPreviewPanel player1Preview;
    private CharacterPreviewPanel player2Preview;

    // Separate player accounts
    private PlayerAccount player1Account;
    private PlayerAccount player2Account;

    private final PlayerAccountDAO accountDAO =
            new PlayerAccountDAO();

    private int player1ArenaIndex = 0;
    private int player2ArenaIndex = 1;
    private int currentPlayer = 1;

    private JLabel selectionLabel;
    private JButton nextPlayerButton;

    private final Color backgroundColor = new Color(5, 8, 25);
    private final Color panelColor = new Color(10, 15, 35);
    private final Color fieldColor = new Color(20, 25, 45);
    private final Color cyanColor = new Color(0, 225, 255);
    private final Color pinkColor = new Color(255, 100, 170);
    private final Color lightTextBackground = Color.WHITE;

    public ArenaSelectionPanel() {

        setLayout(new BorderLayout(10, 10));
        setBackground(backgroundColor);
        setOpaque(true);

        MenuMusic.play();

        // =========================
        // TOP PANEL
        // =========================

        JPanel topPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        topPanel.setBackground(backgroundColor);
        topPanel.setOpaque(true);
        topPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 20, 5, 20)
        );

        JLabel title = new JLabel(
                "DUEL ARENA",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(cyanColor);

        JLabel subtitle = new JLabel(
                "CHOOSE YOUR CHARACTERS AND BATTLE ARENAS",
                SwingConstants.CENTER
        );

        subtitle.setFont(new Font("Arial", Font.BOLD, 13));
        subtitle.setForeground(Color.LIGHT_GRAY);

        // =========================
        // PLAYER NAME AND ACCOUNT PANEL
        // =========================

        JPanel namePanel = new JPanel(new GridLayout(1, 2, 20, 5));
        namePanel.setBackground(backgroundColor);
        namePanel.setOpaque(true);

        // PLAYER 1

        JPanel p1NamePanel = new JPanel(new BorderLayout(5, 5));
        p1NamePanel.setBackground(backgroundColor);
        p1NamePanel.setOpaque(true);

        JLabel p1NameLabel = new JLabel("PLAYER 1 - BLUE NAME");
        p1NameLabel.setForeground(cyanColor);
        p1NameLabel.setFont(new Font("Arial", Font.BOLD, 13));

        player1NameField = new JTextField("Player 1");
        styleTextField(player1NameField);

        JButton player1AccountButton =
                new JButton("PLAYER 1 LOGIN / REGISTER");

        styleButton(player1AccountButton, cyanColor);

        player1AccountButton.addActionListener(e ->
                authenticatePlayer(1)
        );

        p1NamePanel.add(p1NameLabel, BorderLayout.NORTH);
        p1NamePanel.add(player1NameField, BorderLayout.CENTER);
        p1NamePanel.add(player1AccountButton, BorderLayout.SOUTH);

        // PLAYER 2

        JPanel p2NamePanel = new JPanel(new BorderLayout(5, 5));
        p2NamePanel.setBackground(backgroundColor);
        p2NamePanel.setOpaque(true);

        JLabel p2NameLabel = new JLabel("PLAYER 2 - RED NAME");
        p2NameLabel.setForeground(pinkColor);
        p2NameLabel.setFont(new Font("Arial", Font.BOLD, 13));

        player2NameField = new JTextField("Player 2");
        styleTextField(player2NameField);

        JButton player2AccountButton =
                new JButton("PLAYER 2 LOGIN / REGISTER");

        styleButton(player2AccountButton, pinkColor);

        player2AccountButton.addActionListener(e ->
                authenticatePlayer(2)
        );

        p2NamePanel.add(p2NameLabel, BorderLayout.NORTH);
        p2NamePanel.add(player2NameField, BorderLayout.CENTER);
        p2NamePanel.add(player2AccountButton, BorderLayout.SOUTH);

        namePanel.add(p1NamePanel);
        namePanel.add(p2NamePanel);

        topPanel.add(title);
        topPanel.add(subtitle);
        topPanel.add(namePanel);

        // =========================
        // CHARACTER SELECTION
        // =========================

        JPanel characterPanel = new JPanel(
                new GridLayout(1, 2, 15, 5)
        );

        characterPanel.setBackground(backgroundColor);
        characterPanel.setOpaque(true);

        characterPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 20, 5, 20)
        );

        String[] characterNames = Player.CHARACTER_NAMES;

        // PLAYER 1 CHARACTER

        JPanel p1CharacterPanel =
                new JPanel(new BorderLayout(5, 5));

        p1CharacterPanel.setBackground(panelColor);
        p1CharacterPanel.setOpaque(true);

        p1CharacterPanel.setBorder(
                BorderFactory.createLineBorder(cyanColor, 2)
        );

        JLabel p1CharacterLabel = new JLabel(
                "PLAYER 1 - BLUE",
                SwingConstants.CENTER
        );

        p1CharacterLabel.setForeground(cyanColor);
        p1CharacterLabel.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        player1CharacterBox = new JComboBox<>(characterNames);
        player1CharacterBox.setSelectedIndex(0);

        styleComboBox(player1CharacterBox);

        player1Preview = new CharacterPreviewPanel(Color.BLUE, 0);

        player1CharacterBox.addActionListener(e ->
                player1Preview.setCharacterType(
                        player1CharacterBox.getSelectedIndex()
                )
        );

        p1CharacterPanel.add(p1CharacterLabel, BorderLayout.NORTH);
        p1CharacterPanel.add(player1Preview, BorderLayout.CENTER);
        p1CharacterPanel.add(player1CharacterBox, BorderLayout.SOUTH);

        // PLAYER 2 CHARACTER

        JPanel p2CharacterPanel =
                new JPanel(new BorderLayout(5, 5));

        p2CharacterPanel.setBackground(panelColor);
        p2CharacterPanel.setOpaque(true);

        p2CharacterPanel.setBorder(
                BorderFactory.createLineBorder(pinkColor, 2)
        );

        JLabel p2CharacterLabel = new JLabel(
                "PLAYER 2 - RED",
                SwingConstants.CENTER
        );

        p2CharacterLabel.setForeground(pinkColor);
        p2CharacterLabel.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        player2CharacterBox = new JComboBox<>(characterNames);
        player2CharacterBox.setSelectedIndex(2);

        styleComboBox(player2CharacterBox);

        player2Preview = new CharacterPreviewPanel(Color.RED, 2);

        player2CharacterBox.addActionListener(e ->
                player2Preview.setCharacterType(
                        player2CharacterBox.getSelectedIndex()
                )
        );

        p2CharacterPanel.add(p2CharacterLabel, BorderLayout.NORTH);
        p2CharacterPanel.add(player2Preview, BorderLayout.CENTER);
        p2CharacterPanel.add(player2CharacterBox, BorderLayout.SOUTH);

        characterPanel.add(p1CharacterPanel);
        characterPanel.add(p2CharacterPanel);

        // COMBINE UPPER SECTIONS

        JPanel upperPanel = new JPanel(new BorderLayout(5, 5));
        upperPanel.setBackground(backgroundColor);
        upperPanel.setOpaque(true);

        upperPanel.add(topPanel, BorderLayout.NORTH);
        upperPanel.add(characterPanel, BorderLayout.CENTER);

        add(upperPanel, BorderLayout.NORTH);

        // =========================
        // ARENA GALLERY
        // =========================

        JPanel galleryPanel = new JPanel(
                new GridLayout(3, 3, 10, 10)
        );

        galleryPanel.setBackground(backgroundColor);
        galleryPanel.setOpaque(true);

        galleryPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 20, 5, 20)
        );

        for (int i = 0; i < arenas.length; i++) {

            final int index = i;

            JButton arenaButton = new JButton();
            arenaButton.setLayout(new BorderLayout());
            arenaButton.setBackground(fieldColor);
            arenaButton.setOpaque(true);
            arenaButton.setContentAreaFilled(true);
            arenaButton.setFocusPainted(false);

            arenaButton.setBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 2)
            );

            java.net.URL imageURL =
                    getClass().getResource("/arenas/" + arenas[i]);

            if (imageURL != null) {

                ImageIcon originalIcon = new ImageIcon(imageURL);

                Image scaledImage = originalIcon.getImage()
                        .getScaledInstance(
                                220, 85, Image.SCALE_SMOOTH
                        );

                JLabel imageLabel = new JLabel(
                        new ImageIcon(scaledImage),
                        SwingConstants.CENTER
                );

                imageLabel.setOpaque(false);

                arenaButton.add(imageLabel, BorderLayout.CENTER);

            } else {

                JLabel missingLabel = new JLabel(
                        arenas[i],
                        SwingConstants.CENTER
                );

                missingLabel.setForeground(Color.BLACK);
                missingLabel.setBackground(lightTextBackground);
                missingLabel.setOpaque(true);

                missingLabel.setFont(
                        new Font("Arial", Font.BOLD, 12)
                );

                arenaButton.add(missingLabel, BorderLayout.CENTER);

                System.out.println(
                        "Arena preview not found: /arenas/" + arenas[i]
                );
            }

            JLabel nameLabel = new JLabel(
                    "ARENA " + (i + 1),
                    SwingConstants.CENTER
            );

            nameLabel.setForeground(Color.BLACK);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
            nameLabel.setOpaque(true);
            nameLabel.setBackground(lightTextBackground);

            arenaButton.add(nameLabel, BorderLayout.SOUTH);

            arenaButton.setCursor(
                    Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            );

            arenaButton.addActionListener(e -> {

                if (currentPlayer == 1) {
                    player1ArenaIndex = index;
                } else {
                    player2ArenaIndex = index;
                }

                updateHighlights();
            });

            arenaButtons.add(arenaButton);
            galleryPanel.add(arenaButton);
        }

        JScrollPane scrollPane = new JScrollPane(galleryPanel);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(true);

        add(scrollPane, BorderLayout.CENTER);

        // =========================
        // BOTTOM PANEL
        // =========================

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        bottomPanel.setBackground(backgroundColor);
        bottomPanel.setOpaque(true);

        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(5, 20, 10, 20)
        );

        selectionLabel = new JLabel(
                "PLAYER 1 (BLUE): Select your arena",
                SwingConstants.CENTER
        );

        selectionLabel.setForeground(cyanColor);
        selectionLabel.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 12, 4)
        );

        buttonPanel.setBackground(backgroundColor);
        buttonPanel.setOpaque(true);

        JButton backButton = new JButton("BACK TO MENU");
        styleButton(backButton, new Color(60, 60, 75));

        nextPlayerButton = new JButton("NEXT PLAYER");
        styleButton(nextPlayerButton, new Color(15, 100, 140));

        JButton startButton = new JButton("START BATTLE");
        styleButton(startButton, new Color(140, 30, 65));

        // NEXT PLAYER

        nextPlayerButton.addActionListener(e -> {

            if (currentPlayer == 1) {

                currentPlayer = 2;

                selectionLabel.setText(
                        "PLAYER 2 (RED): Select your arena"
                );

                selectionLabel.setForeground(pinkColor);

                nextPlayerButton.setEnabled(false);

                updateHighlights();
            }
        });

        // START BATTLE

        startButton.addActionListener(e -> {

            // Check both accounts
            if (player1Account == null || player2Account == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Both players must login or register first!",
                        "Account Required",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String p1Name = player1Account.getUsername();
            String p2Name = player2Account.getUsername();

            // Ensure different accounts
            if (player1Account.getPlayerId()
                    == player2Account.getPlayerId()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Player 1 and Player 2 must use different accounts.",
                        "Different Accounts Required",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            Window window = SwingUtilities.getWindowAncestor(this);

            if (!(window instanceof JFrame)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to open the game window.",
                        "Duel Arena",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            JFrame frame = (JFrame) window;

            String p1Arena = arenas[player1ArenaIndex];
            String p2Arena = arenas[player2ArenaIndex];

            int p1Character =
                    player1CharacterBox.getSelectedIndex();

            int p2Character =
                    player2CharacterBox.getSelectedIndex();

            MenuMusic.stop();

            // Pass both accounts to GamePanel
            GamePanel gamePanel = new GamePanel(
                    p1Arena,
                    p2Arena,
                    p1Name,
                    p2Name,
                    p1Character,
                    p2Character,
                    player1Account,
                    player2Account
            );

            frame.setContentPane(gamePanel);
            frame.revalidate();
            frame.repaint();

            SwingUtilities.invokeLater(
                    gamePanel::requestFocusInWindow
            );
        });

        // BACK TO MENU

        backButton.addActionListener(e -> {

            Window window = SwingUtilities.getWindowAncestor(this);

            if (window instanceof JFrame) {

                JFrame frame = (JFrame) window;

                MenuMusic.stop();

                MainMenuPanel menuPanel =
                        new MainMenuPanel(frame);

                frame.setContentPane(menuPanel);
                frame.revalidate();
                frame.repaint();
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(nextPlayerButton);
        buttonPanel.add(startButton);

        bottomPanel.add(selectionLabel);
        bottomPanel.add(buttonPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        updateHighlights();
    }

    // =========================
    // ACCOUNT AUTHENTICATION
    // =========================

    private void authenticatePlayer(int playerNumber) {

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        if (playerNumber == 1) {
            usernameField.setText(
                    player1NameField.getText().trim()
            );
        } else {
            usernameField.setText(
                    player2NameField.getText().trim()
            );
        }

        Object[] options = {
                "Login",
                "Register",
                "Cancel"
        };

        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose an account option for Player " + playerNumber,
                "Duel Arena Account",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 2 || choice == JOptionPane.CLOSED_OPTION) {
            return;
        }

        JPanel loginPanel = new JPanel(
                new GridLayout(2, 2, 8, 8)
        );

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);

        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(
                this,
                loginPanel,
                choice == 0 ? "Player Login" : "Player Registration",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String username = usernameField.getText().trim();

        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        Arrays.fill(passwordChars, '\0');

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both username and password."
            );

            return;
        }

        PlayerAccount account;

        // REGISTER

        if (choice == 1) {

            boolean registered =
                    accountDAO.registerPlayer(username, password);

            if (!registered) {

                JOptionPane.showMessageDialog(
                        this,
                        "Registration failed. This username may already exist.",
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Account registered successfully!"
            );
        }

        // LOGIN

        account = accountDAO.loginPlayer(username, password);

        if (account == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Login failed. Please check your username and password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // STORE PLAYER ACCOUNT

        if (playerNumber == 1) {

            player1Account = account;

            player1NameField.setText(
                    account.getUsername()
            );

        } else {

            player2Account = account;

            player2NameField.setText(
                    account.getUsername()
            );
        }

        JOptionPane.showMessageDialog(
                this,
                "Player " + playerNumber
                        + " logged in successfully!\nUsername: "
                        + account.getUsername()
        );
    }

    // =========================
    // CHARACTER PREVIEW
    // =========================

    private static class CharacterPreviewPanel extends JPanel {

        private final Color playerColor;
        private int characterType;

        public CharacterPreviewPanel(
                Color playerColor,
                int characterType) {

            this.playerColor = playerColor;
            this.characterType = characterType;

            setBackground(new Color(10, 15, 35));
            setPreferredSize(new Dimension(100, 90));
            setOpaque(true);
        }

        public void setCharacterType(int characterType) {

            this.characterType = characterType;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            int previewX = (getWidth() - 52) / 2;
            int previewY = (getHeight() - 70) / 2;

            Player preview = new Player(
                    previewX,
                    previewY,
                    playerColor,
                    characterType
            );

            preview.draw(g2);

            g2.dispose();
        }
    }

    // =========================
    // TEXT FIELD STYLE
    // =========================

    private void styleTextField(JTextField field) {

        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(Color.WHITE);
        field.setCaretColor(cyanColor);
        field.setBackground(fieldColor);
        field.setOpaque(true);

        field.setSelectionColor(new Color(0, 100, 150));
        field.setSelectedTextColor(Color.WHITE);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(cyanColor, 1),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)
                )
        );
    }

    // =========================
    // COMBO BOX STYLE
    // =========================

    private void styleComboBox(JComboBox<String> comboBox) {

        comboBox.setFont(new Font("Arial", Font.BOLD, 12));
        comboBox.setForeground(Color.BLACK);
        comboBox.setBackground(Color.WHITE);
        comboBox.setOpaque(true);

        comboBox.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                JLabel label = (JLabel)
                        super.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus
                        );

                label.setFont(new Font("Arial", Font.BOLD, 12));
                label.setOpaque(true);

                if (isSelected) {
                    label.setBackground(new Color(210, 235, 245));
                    label.setForeground(Color.BLACK);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(Color.BLACK);
                }

                return label;
            }
        });
    }

    // =========================
    // BUTTON STYLE
    // =========================

    private void styleButton(JButton button, Color color) {

        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);

        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(cyanColor, 2),
                        BorderFactory.createEmptyBorder(6, 12, 6, 12)
                )
        );

        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
    }

    // =========================
    // UPDATE ARENA HIGHLIGHTS
    // =========================

    private void updateHighlights() {

        for (int i = 0; i < arenaButtons.size(); i++) {

            if (i == player1ArenaIndex
                    && i == player2ArenaIndex) {

                arenaButtons.get(i).setBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 4)
                );

            } else if (i == player1ArenaIndex) {

                arenaButtons.get(i).setBorder(
                        BorderFactory.createLineBorder(cyanColor, 4)
                );

            } else if (i == player2ArenaIndex) {

                arenaButtons.get(i).setBorder(
                        BorderFactory.createLineBorder(pinkColor, 4)
                );

            } else {

                arenaButtons.get(i).setBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 2)
                );
            }
        }
    }
}
