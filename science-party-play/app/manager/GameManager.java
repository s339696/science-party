package manager;

import exception.games.*;
import models.ebean.*;
import models.enums.PerkType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * The GameManger manages the whole Gameplay of a Match.
 */
public class GameManager {

    /**
     * Creates a new Game object and the required database inserts.
     *
     * @param topic
     * @param users
     * @return
     */
    public static Game createGame(Topic topic, String name, List<User> users) throws GameException {
        if (topic == null) {
            throw new CreateGameException("Das ausgewählte Thema ist ungültig.");
        }

        // Create actual game
        Game game = new Game();
        game.setGameStatus(Game.GameStatus.PENDING);
        game.setTopic(topic);
        game.setName(name);
        game.insert();

        // Create Players for the Games, each user represent a player
        if (users.size() < 1 || users.size() > 4) {
            throw new CreateGameException("Die Anzahl der Spieler ist ungültig.");
        }
        List<Player> players = new ArrayList<Player>();
        for (User user : users) {
            // Create player for this game
            Player player = new Player();
            player.setFieldPosition(0);
            player.setPlayerStatus(Player.PlayerStatus.INVITED);
            player.setUser(user);
            player.setGame(game);

            //If user is host, set PlayStatus to Accepted
            if (LoginManager.getLoggedInUser().getId() == user.getId()) {
                player.setPlayerStatus(Player.PlayerStatus.ACCEPTED);
            }
            player.insert();

            // Create perks for this player
            List<PerkPerUser> perksPerUser = user.getPerksPerUserAndTopic(topic);
            for (PerkPerUser perkPerUser : perksPerUser) {
                PerkPerPlayer perkPerPlayer = new PerkPerPlayer();
                perkPerPlayer.setPlayer(player);
                perkPerPlayer.setPerkPerUser(perkPerUser);
                perkPerPlayer.setUsed(false);
                perkPerPlayer.insert();
            }

            // Send notification
            Notification.createNotification(user, "Du wurdest zum Spiel mit der Nummer #" + game.getId() + " eingeladen.","");
        }

        //Try to start game directly if it is a one player game
        game.refresh();
        startGame(game);

        return game;
    }

    /**
     * Accept the invitation and try to start the game
     *
     * @param game
     * @param user
     * @throws Exception
     */
    public static void respondGameInvite(Game game, User user, boolean accept) throws Exception {
        Player player = Player.getPlayerOfGameAndUser(game, user);

        // Accept if player found
        if (player == null || player.getPlayerStatus() != Player.PlayerStatus.INVITED) {
            throw new GameException("Der angegebene User wurde nicht zu diesem Spiel eingeladen oder hat die Anfrage bereits beantwortet.");
        } else {
            if (accept) {
                player.setPlayerStatus(Player.PlayerStatus.ACCEPTED);
            } else {
                player.setPlayerStatus(Player.PlayerStatus.DECLINE);
            }
            player.update();
        }

        // Try to start the game
        startGame(game);
    }

    /**
     * Leave a running game
     *
     * @param game
     * @param user
     */
    public static void leaveGame(Game game, User user) throws GameException {
        // Leave game
        Player player = Player.getPlayerOfGameAndUser(game, user);

        if (player == null || player.getPlayerStatus() != Player.PlayerStatus.PLAYING) {
            throw new GameException("Der angegebene User ist kein aktives Mitglied dieses Spieles.");
        }

        if (player.getPlayerStatus() == Player.PlayerStatus.PLAYING) {
            player.setPlayerStatus(Player.PlayerStatus.LEFT);
        }
        player.update();

        // Check if there are still people who play, otherwise stop!
        if (!game.hasPlayingPlayer()) {
            if (stopGame(game)) {
                throw new StopGameException("Das Spiel wurde verlassen und beendet.");
            }
        }
    }

    /**
     * Starts a game if it is possible.
     *
     * @param game
     * @throws StartGameException
     */
    public static void startGame(Game game) throws StartGameException {
        List<Player> players = game.getPlayers();

        // Check if game is already running or finished
        if (game.getGameStatus() != Game.GameStatus.PENDING) {
            throw new StartGameException("Das Spiel wurde bereits gestartet.", game);
        }

        // Check if all player responded the invitation
        for (Player player : players) {
            if (player.getPlayerStatus() != Player.PlayerStatus.ACCEPTED &&
                    player.getPlayerStatus() != Player.PlayerStatus.DECLINE) {
                throw new StartGameException("Das Spiel konnte nicht gestartet werden, da noch nicht alle Spieler ihre Einladung beantwortet haben.", game);
            }
        }

        // All players responded, let's set them to PLAYING:
        for (Player player : players) {
            if (player.getPlayerStatus() == Player.PlayerStatus.ACCEPTED) {
                player.setPlayerStatus(Player.PlayerStatus.PLAYING);
            }
            player.update();
        }

        // Random start: Generate starting player id as long a starting player has status PLAYING
        System.out.println("Wähle zufällig Startspieler...");
        List<Player> activePlayers = game.getPlayingPlayer();
        Random random = new Random();
        int startNumb = random.nextInt(activePlayers.size());

        // Set starting player, starting question and start game
        System.out.println("Aktiven Spieler setzen...");
        game.setActivePlayer(activePlayers.get(startNumb));
        System.out.println("Aktive Frage setzen...");
        game.setActiveQuestion(Question.getRandomQuestion(game.getTopic()));
        game.setGameStatus(Game.GameStatus.ACTIVE);
        game.update();

        // Send a notification to the player that the game has started
        String playerString = "";
        for (Player player : activePlayers) {
            if (activePlayers.get(activePlayers.size() - 1).equals(player)) {
                // Last player
                playerString += player.getUser();
                Notification.createNotification(player.getUser(),
                        "Das Spiel mit der Nummer #" + game.getId() + " hat begonnen.",
                        playerString + " haben begonnen ein Spiel zu spielen.");
            } else {
                // Any player
                playerString += player.getUser() + ", ";
                Notification.createNotification(player.getUser(),
                        "Das Spiel mit der Nummer #" + game.getId() + " hat begonnen.",
                        "");
            }
        }
    }

    /**
     * Stops a game independent to its state
     *
     * @param game
     */
    public static boolean stopGame(Game game) throws GameException {
        List<Player> activePlayers = game.getPlayingPlayer();

        // Set game and player to state FINISHED
        if (game.getGameStatus() == Game.GameStatus.ACTIVE) {
            Player winner = game.getActivePlayer();
            game.setGameStatus(Game.GameStatus.FINISHED);
            game.setActivePlayer(null);
            game.setActiveQuestion(null);
            game.update();

            // Send a notification to the player that the game has started
            String playerString = "";
            for (Player player : activePlayers) {
                // Change player status for playing players
                player.setPlayerStatus(Player.PlayerStatus.FINISHED);
                player.update();

                // Update global ranking
                User userOfPlayer = player.getUser();
                userOfPlayer.setPoints(userOfPlayer.getPoints() + player.getFieldPosition());
                userOfPlayer.update();

                // Send notification
                if (activePlayers.get(activePlayers.size() - 1).equals(player)) {
                    // Last player
                    playerString += player.getUser();
                    Notification.createNotification(player.getUser(),
                            winner.getUser() + " hat das Spiel mit der Nummer #" + game.getId() + " gewonnen. " +
                                    "Du hast in diesem Spiel " + player.getFieldPosition() + " Punkte erreicht.",
                            playerString + " haben ein spiel beendet. Gewonnen hat " + winner.getUser() + ".");
                } else {
                    // Any player
                    playerString += player.getUser() + ", ";
                    Notification.createNotification(player.getUser(),
                            winner.getUser() + " hat das Spiel mit der Nummer #" + game.getId() + " gewonnen. " +
                                    "Du hast in diesem Spiel " + player.getFieldPosition() + " Punkte erreicht.",
                            "");
                }
            }
        } else {
            throw new GameException("Das Spiel kann nicht gestoppt werden.");
        }

        return true;
    }

    /**
     * Answer the Question and process the turn
     *
     * @param game
     * @param answer
     */
    public static boolean answerActiveQuestion(Game game, Answer answer) throws GameException {
        boolean result = false;
        Answer correctAnswer = game.getActiveQuestion().getCorrectAnswer();
        Player activePlayer = game.getActivePlayer();

        if (correctAnswer == null) {
            throw new GameException("Es ist ein Fehler aufgetreten. Auf diese Frage gibt es leider keine korrekte Antwort.");
        }

        if (game.getGameStatus() != Game.GameStatus.ACTIVE) {
            throw new GameException("Es handelt sich um kein aktives Spiel.");
        }

        // Check if answer is correct.
        if (correctAnswer.getId() == answer.getId()) {
            // Step forward and update points of active player
            Question activeQuestion = game.getActiveQuestion();
            activePlayer.setFieldPosition(activePlayer.getFieldPosition() + activeQuestion.getDifficulty());
            activePlayer.update();

            result = true;
        }

        // Check if active player reached 40points
        if (activePlayer.getFieldPosition() >= 40) {
            // Set points to 40 to avoid more then 40 points per game and a invalid map position
            activePlayer.setFieldPosition(40);
            activePlayer.update();
            if (stopGame(game)) {
                throw new StopGameException("Du hast das Ziel erreicht und das Spiel gewonnen.");
            }
        }

        // Pass turn to next player
        game.nextTurn();

        return result;
    }

    /**
     * Uses a perk for a given game.
     *
     * @param game
     * @param perk
     * @return
     */
    public static void usePerk(Game game, PerkPerPlayer perk) throws Exception {
        if (perk.isUsed()) {
            throw new UsePerkGameException("Diese Fähigkeit wurde bereits eingesetzt.");
        }
        PerkType perkType = PerkType.fromInt(perk.getPerkPerUser().getPerkPerTopic().getId().intValue());
        switch (perkType) {
            case GIVE_ANSWER:
                usePerkGiveAnswer(game);
                break;
            default:
                throw new UsePerkGameException("Die eingesetzte Fähigkeit ist ungültig.");
        }
        perk.setUsed(true);
        perk.save();
    }

    /**
     * Gives the correct answer to the active question.
     *
     * @param game
     * @throws Exception
     */
    private static void usePerkGiveAnswer(Game game) throws Exception {
        answerActiveQuestion(game, game.getActiveQuestion().getCorrectAnswer());
    }
}
