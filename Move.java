import javax.swing.text.Position;
import java.util.List;

public class Move {

    private Disc disc;
    private Player player;
    private Position position;
    private List<Position> flippedDiscs;

    //base constructor
    public Move(Position position) {
        this.position = position;
    }

    public Move(Position position, Disc disc, Player player, List flippedDiscs) {
        this.position = position;
        this.disc = disc;
        this.player = player;
        this.flippedDiscs = flippedDiscs;
    }

    public Disc getDisc() {
        return disc;
    }

    public List<Position> getFlippedDisc() {
        return flippedDiscs;
    }

    public Player getPlayer() {
        return player;
    }

    public Position getPosition() {
         return position;
    }

    @Override
    public String toString() {
            String playerType = player.isPlayerOne() ? "Player 1" : "Player 2"; // בדיקה ישירה לפי isPlayerOne
            String discType = disc instanceof BombDisc ? "BombDisc" :
                    disc instanceof UnflippableDisc ? "UnflippableDisc" :
                            "SimpleDisc";

            // בניית התוצאה
            StringBuilder sb = new StringBuilder();
            sb.append(playerType).append(" placed a ").append(discType)
                    .append(" at ").append(position);

            // הוספת מידע על דיסקים שהתהפכו
            if (flippedDiscs != null && !flippedDiscs.isEmpty()) {
                sb.append(". Flipped discs: ");
                for (Position pos : flippedDiscs) {
                    sb.append(pos).append(" ");
                }
            } else {
                sb.append(". No discs flipped.");
            }

            return sb.toString();
        }

}