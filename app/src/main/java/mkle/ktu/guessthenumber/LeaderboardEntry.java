package mkle.ktu.guessthenumber;

public class LeaderboardEntry {

    private String name;
    private int result;
    private int number;
    private int totalTurns;

    public LeaderboardEntry(String name, int result, int number, int totalTurns) {
        this.name = name;
        this.result = result;
        this.number = number;
        this.totalTurns = totalTurns;
    }

    public LeaderboardEntry() {
        this.name = "Player";
        this.result = 0;
        this.number = -1;
        this.totalTurns = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int newResult) {
        this.result = newResult;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int newNumber) {
        this.number = newNumber;
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public void setTotalTurns(int newTotalTurns) {
        this.totalTurns = newTotalTurns;
    }
}
