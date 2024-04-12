package minesweeper;

import polyglot.Pair;

public interface Logics {
    public void click(Pair<Integer, Integer> coordinates);

    boolean isBombSelected(Pair<Integer, Integer> pos);

    boolean isWin();

    Grid getGrid();

    String getCellStamp(Pair<Integer, Integer> pos);

    boolean isDiscovered(Pair<Integer, Integer> pos);

    void setFlag(Pair<Integer, Integer> pos);
}
