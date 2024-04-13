package minesweeper;

import polyglot.Pair;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;


public class GUI extends JFrame {

    private static final long serialVersionUID = -6218820567019985015L;
    private final Map<JButton,Pair<Integer,Integer>> buttons = new HashMap<>();
    private final Logics logics;

    public GUI(int size, int bombNumber) {
        this.logics = new LogicsImpl(size, bombNumber);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(100*size, 100*size);

        JPanel panel = new JPanel(new GridLayout(size,size));
        this.getContentPane().add(BorderLayout.CENTER,panel);

        ActionListener onClick = (e)->{
            final JButton bt = (JButton)e.getSource();
            final Pair<Integer,Integer> pos = buttons.get(bt);
            logics.click(pos);
            boolean aMineWasFound = logics.isBombSelected(pos); // call the logic here to tell it that cell at 'pos' has been seleced
            if (aMineWasFound) {
                quitGame();
                JOptionPane.showMessageDialog(this, "You lost!!");
            } else {
                drawBoard();
            }
            boolean isThereVictory = logics.isWin(); // call the logic here to ask if there is victory
            if (isThereVictory){
                quitGame();
                JOptionPane.showMessageDialog(this, "You won!!");
                System.exit(0);
            }
        };

        MouseInputListener onRightClick = new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    return;
                }
                final JButton bt = (JButton)e.getSource();
                if (bt.isEnabled()){
                    final Pair<Integer,Integer> pos = buttons.get(bt);
                    // call the logic here to put/remove a flag
                    logics.setFlag(pos);
                }
                drawBoard();
            }
        };

        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                final JButton jb = new JButton(" ");
                jb.addActionListener(onClick);
                jb.addMouseListener(onRightClick);
                this.buttons.put(jb,new Pair<>(i,j));
                panel.add(jb);
            }
        }
        this.drawBoard();
        this.setVisible(true);
    }

    private void quitGame() {
        this.drawBoard();
        for (var entry: this.buttons.entrySet()) {
            if(this.logics.isBombSelected(entry.getValue())){
                entry.getKey().setText("*");
            }
            entry.getKey().setEnabled(false);
        }
    }

    private void drawBoard() {
        for (var entry: this.buttons.entrySet()) {
            if(logics.isDiscovered(entry.getValue())){
                entry.getKey().setText(logics.getCellStamp(entry.getValue()));
                entry.getKey().setEnabled(false);
            } else if(logics.getGrid().getCell(entry.getValue()).isFlagged()){
                entry.getKey().setText("F");
            } else {
                entry.getKey().setText("");
            }
        }
    }

}


