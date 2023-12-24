package org.example;

import javax.swing.*;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Objects;

import static javax.swing.JOptionPane.showMessageDialog;
public class ConnectClient extends JFrame{
    protected ConnectDynamic game;
    public static final int port = 8080;
    public static final String url = "http://localhost:8080/ConnectDynamic?wsdl";
    protected int playerId;
    private static final JButton[][] board = new JButton[19][19];

    protected void CreateBoard(){
        for (int i=0; i<19; i++){
            for (int j=0; j<19; j++){
                board[i][j] = new JButton();
                board[i][j].setBackground(Color.GRAY);
                board[i][j].setSize(20, 20);
                board[i][j].setLocation(2+i*25, 2+j*25);
                int TotalI = i;
                int TotalJ = j;
                board[i][j].addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try{
                            if (Objects.equals(game.GetCurrentStep(playerId), "white") &&
                                    Objects.equals(game.GetQueueStep(), "white")){
                                if (Objects.equals(game.GetStateBoard(TotalI, TotalJ), "o")){
                                    board[TotalI][TotalJ].setBackground(Color.WHITE);
                                    game.SetStateBoard(TotalI, TotalJ, "w");
                                    game.SetQueueStep("black");
                                    board[TotalI][TotalJ].setEnabled(false);
                                }
                                else if (Objects.equals(game.GetStateBoard(TotalI, TotalJ), "b")){
                                    showMessageDialog(null, "Данная клетка занята, " +
                                            "обновите поле и сделайте новый ход");
                                }
                            }
                            else if (Objects.equals(game.GetCurrentStep(playerId), "black") &&
                                    Objects.equals(game.GetQueueStep(), "black")){
                                if (Objects.equals(game.GetStateBoard(TotalI, TotalJ), "o")){
                                    board[TotalI][TotalJ].setBackground(Color.BLACK);
                                    game.SetStateBoard(TotalI, TotalJ, "b");
                                    game.SetQueueStep("white");
                                    board[TotalI][TotalJ].setEnabled(false);
                                }
                                else if (Objects.equals(game.GetStateBoard(TotalI, TotalJ), "w")){
                                    showMessageDialog(null, "Данная клетка занята, " +
                                            "обновите поле и сделайте новый ход");
                                }
                            }
                        }catch (Exception ex){
                            throw new RuntimeException(ex);
                        }
                    }
                });
                this.add(board[i][j]);
            }
        }
    }

    public ConnectClient(final ConnectDynamic game) throws RemoteException{
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        CreateBoard();
        this.game = game;
        final JButton appendButton = new JButton("Присоединиться");
        appendButton.setSize(140, 40);
        appendButton.setBackground(Color.GRAY);
        appendButton.setLocation(500, 30);
        appendButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int getterPlayerId = game.GetPlayerCommand();
                    if (getterPlayerId == 0){
                        System.out.println("Ваши камни белые");
                        playerId = 0;
                        appendButton.setBackground(Color.WHITE);
                        appendButton.setText("Белые");
                        appendButton.setEnabled(false);
                    }
                    else if (getterPlayerId == 1){
                        System.out.println("Ваши камни черные");
                        playerId = 1;
                        appendButton.setBackground(Color.BLACK);
                        appendButton.setText("Черные");
                        appendButton.setEnabled(false);
                    }
                    else {
                        System.out.println("В игре уже два игрока, невозможно присоедниться");
                        appendButton.setEnabled(false);
                        for (int i=0; i<19; i++){
                            for (int j=0; j<19; j++){
                                board[i][j].setEnabled(false);
                            }
                        }
                    }
                }catch (Exception ex){
                    throw new RuntimeException(ex);
                }
            }
        });

        final JButton updateButton = new JButton("Обновить поле");
        updateButton.setSize(140, 40);
        updateButton.setBackground(Color.GRAY);
        updateButton.setLocation(500, 90);
        updateButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String winner = game.CheckWinner();
                    if (!Objects.equals(winner, "o")){
                        showMessageDialog(null, ("Победила команда: " + winner));
                    }
                    for (int i=0; i<19; i++) {
                        for (int j = 0; j < 19; j++) {
                            String TotalState = game.GetStateBoard(i,j);
                            switch (TotalState){
                                case "w":
                                    board[i][j].setBackground(Color.WHITE);
                                    board[i][j].setEnabled(false);
                                    break;
                                case "b":
                                    board[i][j].setBackground(Color.BLACK);
                                    board[i][j].setEnabled(false);
                                    break;
                                default:
                                    board[i][j].setBackground(Color.GRAY);
                                    break;
                            }
                        }
                    }
                }catch (Exception ex){
                    throw new RuntimeException(ex);
                }
            }
        });

        this.add(appendButton);
        this.add(updateButton);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setSize(680, 550);
        this.setVisible(true);
    }

    public static void main(String[] args) throws Exception{
        Service service = Service.create(new URL(String.format(url, port)),
                new QName("http://example.org/", "ConnectServerDynamicService"));
        ConnectDynamic port = service.getPort(new QName("http://example.org/",
                                        "ConnectServerDynamicPort"),
                                    ConnectDynamic.class);
        System.out.println("Подключение...");
        new ConnectClient(port);

    }

}
