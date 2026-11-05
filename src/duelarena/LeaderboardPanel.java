
package duelarena;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {

    private final JFrame frame;

    private JTable leaderboardTable;
    private DefaultTableModel tableModel;

    private final LeaderboardDAO leaderboardDAO =
            new LeaderboardDAO();

    public LeaderboardPanel(JFrame frame) {

        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(new Color(12, 17, 35));

        // MAIN BACKGROUND
        JPanel background = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(10, 15, 35),
                        getWidth(), getHeight(),
                        new Color(45, 20, 65)
                );

                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(0, 220, 255, 18));
                g2.fillOval(30, 100, 180, 180);

                g2.setColor(new Color(255, 190, 50, 15));
                g2.fillOval(
                        getWidth() - 220, 300, 190, 190
                );

                g2.dispose();
            }
        };

        background.setLayout(new BorderLayout());
        background.setOpaque(false);

        // HEADING
        JPanel headingPanel = new JPanel();

        headingPanel.setOpaque(false);
        headingPanel.setLayout(
                new BoxLayout(headingPanel, BoxLayout.Y_AXIS)
        );

        JLabel heading = new JLabel(
                "DUEL ARENA",
                SwingConstants.CENTER
        );

        heading.setFont(
                new Font("Arial", Font.BOLD, 34)
        );

        heading.setForeground(new Color(255, 210, 75));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subHeading = new JLabel(
                "GLOBAL LEADERBOARD",
                SwingConstants.CENTER
        );

        subHeading.setFont(
                new Font("Arial", Font.BOLD, 17)
        );

        subHeading.setForeground(new Color(90, 220, 255));
        subHeading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel description = new JLabel(
                "FIGHT • WIN • DOMINATE",
                SwingConstants.CENTER
        );

        description.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );

        description.setForeground(Color.LIGHT_GRAY);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        headingPanel.add(Box.createVerticalStrut(20));
        headingPanel.add(heading);
        headingPanel.add(Box.createVerticalStrut(5));
        headingPanel.add(subHeading);
        headingPanel.add(Box.createVerticalStrut(5));
        headingPanel.add(description);
        headingPanel.add(Box.createVerticalStrut(20));

        background.add(headingPanel, BorderLayout.NORTH);

        // TABLE COLUMNS
        String[] columns = {
                "RANK",
                "AVATAR",
                "PLAYER",
                "WINS",
                "MATCHES",
                "POINTS"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        leaderboardTable = new JTable(tableModel);

        leaderboardTable.setRowHeight(65);

        leaderboardTable.setFont(
                new Font("Arial", Font.BOLD, 15)
        );

        leaderboardTable.setBackground(
                new Color(25, 32, 55)
        );

        leaderboardTable.setForeground(Color.WHITE);

        leaderboardTable.setGridColor(
                new Color(65, 75, 105)
        );

        leaderboardTable.setSelectionBackground(
                new Color(50, 100, 145)
        );

        leaderboardTable.setSelectionForeground(Color.WHITE);

        leaderboardTable.setShowVerticalLines(false);
        leaderboardTable.setShowHorizontalLines(true);

        leaderboardTable.setIntercellSpacing(
                new Dimension(0, 1)
        );

        // TABLE HEADER
        leaderboardTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        leaderboardTable.getTableHeader().setBackground(
                new Color(20, 35, 65)
        );

        leaderboardTable.getTableHeader().setForeground(
                new Color(100, 225, 255)
        );

        leaderboardTable.getTableHeader().setPreferredSize(
                new Dimension(100, 42)
        );

        leaderboardTable.getTableHeader().setReorderingAllowed(false);

        // ACTUAL GAME CHARACTER AVATAR COLUMN
        leaderboardTable.getColumnModel()
                .getColumn(1)
                .setCellRenderer(new AvatarRenderer());

        leaderboardTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(85);

        leaderboardTable.getColumnModel()
                .getColumn(1)
                .setMaxWidth(85);

        // COLUMN WIDTHS
        leaderboardTable.getColumnModel()
                .getColumn(0).setPreferredWidth(65);

        leaderboardTable.getColumnModel()
                .getColumn(2).setPreferredWidth(190);

        leaderboardTable.getColumnModel()
                .getColumn(3).setPreferredWidth(90);

        leaderboardTable.getColumnModel()
                .getColumn(4).setPreferredWidth(110);

        leaderboardTable.getColumnModel()
                .getColumn(5).setPreferredWidth(110);

        // CUSTOM TABLE STYLING
        for (int i = 0; i < leaderboardTable.getColumnCount(); i++) {

            if (i != 1) {

                leaderboardTable.getColumnModel()
                        .getColumn(i)
                        .setCellRenderer(new RankingRenderer());
            }
        }

        JScrollPane scrollPane =
                new JScrollPane(leaderboardTable);

        scrollPane.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(70, 150, 190), 2
                        ),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                )
        );

        scrollPane.getViewport().setBackground(
                new Color(25, 32, 55)
        );

        JPanel tableContainer = new JPanel(new BorderLayout());

        tableContainer.setOpaque(false);

        tableContainer.setBorder(
                BorderFactory.createEmptyBorder(10, 45, 15, 45)
        );

        tableContainer.add(
                scrollPane,
                BorderLayout.CENTER
        );

        background.add(tableContainer, BorderLayout.CENTER);

        // BOTTOM BUTTONS
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 25, 15)
        );

        buttonPanel.setOpaque(false);

        JButton refreshButton =
                new JButton("REFRESH SCORES");

        JButton backButton =
                new JButton("BACK TO MENU");

        styleButton(
                refreshButton,
                new Color(0, 145, 175)
        );

        styleButton(
                backButton,
                new Color(180, 115, 35)
        );

        refreshButton.addActionListener(e -> loadLeaderboard());

        backButton.addActionListener(e -> {

            MenuMusic.play();

            MainMenuPanel menuPanel =
                    new MainMenuPanel(frame);

            frame.setContentPane(menuPanel);
            frame.revalidate();
            frame.repaint();
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        background.add(buttonPanel, BorderLayout.SOUTH);

        add(background, BorderLayout.CENTER);

        loadLeaderboard();
    }

    // LOAD LEADERBOARD FROM MYSQL
    private void loadLeaderboard() {

        tableModel.setRowCount(0);

        List<Object[]> players =
                leaderboardDAO.getLeaderboard();

        int rank = 1;

        for (Object[] player : players) {

            tableModel.addRow(new Object[]{

                    rank,

                    player[4],

                    player[0],

                    player[1],

                    player[2],

                    player[3]

            });

            rank++;
        }

        if (players.isEmpty()) {

            System.out.println(
                    "No player records found in leaderboard."
            );
        }
    }

    // ACTUAL GAME CHARACTER AVATAR RENDERER
    private class AvatarRenderer extends JLabel
            implements TableCellRenderer {

        private int characterType = 0;

        public AvatarRenderer() {

            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            try {

                characterType = value == null
                        ? 0
                        : Integer.parseInt(value.toString());

            } catch (NumberFormatException e) {

                characterType = 0;
            }

            if (isSelected) {

                setBackground(new Color(50, 100, 145));

            } else {

                setBackground(getRowColor(row));
            }

            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            double scale = 0.65;

            int avatarWidth = (int) (52 * scale);
            int avatarHeight = (int) (69 * scale);

            int x = (getWidth() - avatarWidth) / 2;
            int y = (getHeight() - avatarHeight) / 2;

            Player avatar =
                    Player.createLeaderboardAvatar(characterType);

            avatar.drawAvatar(g2, x, y, scale);

            g2.dispose();
        }
    }

    // RANKING TABLE RENDERER
    private class RankingRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column
            );

            setHorizontalAlignment(SwingConstants.CENTER);

            setFont(
                    new Font("Arial", Font.BOLD, 15)
            );

            if (isSelected) {

                setBackground(new Color(50, 100, 145));
                setForeground(Color.WHITE);

                return this;
            }

            setBackground(getRowColor(row));

            if (row == 0) {

                setForeground(new Color(255, 215, 70));

            } else if (row == 1) {

                setForeground(new Color(210, 220, 235));

            } else if (row == 2) {

                setForeground(new Color(220, 155, 100));

            } else {

                setForeground(Color.WHITE);
            }

            return this;
        }
    }

    // ROW COLORS
    private Color getRowColor(int row) {

        if (row == 0) {
            return new Color(75, 60, 25);
        }

        if (row == 1) {
            return new Color(50, 55, 75);
        }

        if (row == 2) {
            return new Color(75, 45, 35);
        }

        return new Color(25, 32, 55);
    }

    // BUTTON STYLING
    private void styleButton(JButton button, Color color) {

        button.setBackground(color);
        button.setForeground(Color.WHITE);

        button.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setPreferredSize(
                new Dimension(175, 42)
        );

        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
    }
}
