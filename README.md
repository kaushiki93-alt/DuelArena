# Duel Arena – A 2D Combat Game

## About the Project

Duel Arena is a 2D fighting game developed using Java. It is a two-player game in which both players compete against each other in a virtual arena.

The main idea behind developing this project was to create an interactive game where two players can choose their characters, select an arena and fight against each other. The game includes different features such as character movement, shooting, health management, power-ups, shields and a leaderboard.

While developing this project, we worked with Java programming, Java Swing, database connectivity and different programming concepts. We also focused on making the game easy to understand and interesting to play.

## Features of the Game

The game includes the following features:

- **Two-player gameplay:** Two players can play against each other on the same screen.
- **Character selection:** Players can choose their characters before starting the game.
- **Arena selection:** Different backgrounds are available for players to choose from.
- **Player names:** Players can enter their own names before the match.
- **Movement and shooting:** Both players can move around the arena and shoot at their opponents.
- **Health system:** Each player has a health bar that decreases when they get hit.
- **Power-ups:** Players can collect power-ups during the match.
- **Shield system:** Shields provide additional protection during gameplay.
- **Adaptive balancing:** The game includes a feature that helps balance the match when one player is significantly behind.
- **Sound and music:** Background music and shooting sound effects make the gameplay more interactive.
- **Winner screen:** After the match ends, the winner's name and match result are displayed.
- **Leaderboard:** Players can view their scores and performance.
- **Match history:** Completed match details are stored in the database.

## Technologies Used

The following technologies and tools were used to develop this project:

| Technology | Use |
|---|---|
| Java | Main programming language |
| Java Swing | Designing the game interface |
| Java AWT | Graphics and keyboard controls |
| MySQL | Storing player and match information |
| JDBC | Connecting Java with MySQL |
| IntelliJ IDEA | Writing and running the code |
| GitHub | Managing and storing project files |

## How the Game Works

The game starts with a main menu from where players can enter the game.

First, both players enter their names and select their preferred characters. After that, they choose an arena for the battle.

Once the selections are complete, the game starts with a countdown. Both players can move around the arena and attack each other using their shooting controls.

During the match, players can also collect power-ups and use shields. Their health is displayed on the screen, which helps them keep track of the match.

When the match ends, the game displays the winner's name on the winner screen. The match details are also saved in the database and used for the leaderboard.

## Game Controls

| Action | Player 1 | Player 2 |
|---|---|---|
| Move Up | W | Up Arrow |
| Move Down | S | Down Arrow |
| Move Left | A | Left Arrow |
| Move Right | D | Right Arrow |
| Shoot | F | L |

## Project Folder Structure

The project contains Java source files and resource files required to run the game.

```text
DuelArena/
│
├── src/
│   └── duelarena/
│       ├── Game.java
│       ├── GamePanel.java
│       ├── Player.java
│       ├── Bullet.java
│       ├── PowerUp.java
│       ├── ShieldPowerUp.java
│       ├── MainMenuPanel.java
│       ├── ArenaSelectionPanel.java
│       ├── WinnerPanel.java
│       ├── LeaderboardPanel.java
│       ├── LeaderboardDAO.java
│       ├── MatchHistoryDAO.java
│       └── PlayerAccount.java
│
├── resources/
│   ├── arenas/
│   ├── menu_background/
│   └── sounds/
│
├── .gitignore
└── README.md
```

*This is a basic representation of the project structure. Some additional files may also be included.*

## Database Connectivity

MySQL is used in this project to store player and match-related information.

We have used JDBC to connect our Java application with the MySQL database.

The database is named `duel_arena_db`.

It is used to manage information such as player details, match results, winners and points. The leaderboard uses the stored information to display player performance.

## How to Run the Project

To run this project on your computer, follow these steps:

1. Install Java JDK on your system.
2. Install IntelliJ IDEA.
3. Clone or download this repository from GitHub.
4. Open the project in IntelliJ IDEA.
5. Add the MySQL Connector/J library to the project.
6. Make sure MySQL is installed and running.
7. Create the database named `duel_arena_db`.
8. Check the database connection details in the Java code.
9. Make sure all required images, backgrounds and sound files are available in the resources folder.
10. Run the `Game.java` file to start the game.

## What We Learned

During the development of Duel Arena, we got the opportunity to apply the concepts that we had studied in our programming subjects.

We learned how to create a graphical interface using Java Swing, handle keyboard events, manage player movement and implement different game features.

We also got practical experience with MySQL and JDBC by connecting the game to a database. Apart from coding, this project helped us understand how different classes and features can be connected to build one complete application.

## Future Improvements

There are still many things that can be improved in the game. In the future, we would like to add more characters, introduce new arenas, improve the animations and include additional gameplay features.

We can also improve the leaderboard by adding more statistics and providing better ways to view player performance.

## Project Details

**Project Name:** Duel Arena – A 2D Combat Game

**Course:** Bachelor of Computer Applications (BCA)

**Programming Language:** Java

**Database:** MySQL

**Development Environment:** IntelliJ IDEA

## Acknowledgement

Duel Arena is an academic project developed as part of our BCA course. Working on this project gave us practical experience in Java programming, interface development, database connectivity and teamwork.

It also helped us understand how the concepts we learn in class can be used to develop a working software application.
