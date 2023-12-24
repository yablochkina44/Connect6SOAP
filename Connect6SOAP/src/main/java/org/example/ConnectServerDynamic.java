package org.example;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.Objects;
@WebService
public class ConnectServerDynamic implements ConnectDynamic{
    public static final int port = 8080;
    String QueueStep = "white";
    private int PlayerId = 0;
    public String[][] BoardState = new String[19][19];
    //w - белый камень
    //b - черный камень
    //o - пустая клетка

    //заполняем состояния доски(изначально вся пустая)
    public void CreateBoardState(){
        for (int i=0; i<19; i++){
            for (int j=0; j<19; j++){
                BoardState[i][j] = "o";
            }
        }
    }
    @WebMethod
    @Override
    public int GetPlayerCommand(){
        CreateBoardState();
        int CurrentPlId = PlayerId;
        PlayerId += 1;
        //System.out.println(CurrentPlId);
        return CurrentPlId;
    }
    @WebMethod
    @Override
    public String GetCurrentStep(int id) {
        if (id == 0){
            return "white";
        }
        else{
            return "black";
        }
    }
    @WebMethod
    @Override
    public String GetQueueStep() {
        return QueueStep;
    }
    @WebMethod
    @Override
    public void SetQueueStep(String step) {
        QueueStep = step;
    }
    @WebMethod
    @Override
    public String GetStateBoard(int i, int j) {
        return BoardState[i][j];
    }
    @WebMethod
    @Override
    public void SetStateBoard(int i, int j, String s) {
        BoardState[i][j] = s;
    }
    @WebMethod
    @Override
    public String CheckWinner(){
        int countBlack = 0;
        int countWhite = 0;
        for (int i=0; i<19; i++){
            for (int j=0; j<19; j++){
                if (Objects.equals(BoardState[i][j], "b")){
                    countBlack += 1;
                    countWhite = 0;
                    if (countBlack >= 6){
                        return "black";
                    }
                }
                else if (Objects.equals(BoardState[i][j], "w")){
                    countWhite += 1;
                    countBlack = 0;
                    if (countWhite >= 6){
                        return "white";
                    }
                }
                else{
                    countWhite = 0;
                    countBlack = 0;
                }

            }
        }
        countBlack = 0;
        countWhite = 0;
        for (int i=0; i<19; i++){
            for (int j=0; j<19; j++){
                if (Objects.equals(BoardState[j][i], "b")){
                    countBlack += 1;
                    countWhite = 0;
                    if (countBlack >= 6){
                        return "black";
                    }
                }
                else if (Objects.equals(BoardState[j][i], "w")){
                    countWhite += 1;
                    countBlack = 0;
                    if (countWhite >= 6){
                        return "white";
                    }
                }
                else{
                    countWhite = 0;
                    countBlack = 0;
                }

            }
        }
        return "o";
    }

    public static void main(String[] argc){
        ConnectServerDynamic service = new ConnectServerDynamic();
        String url = String.format("http://localhost:%d/ConnectDynamic", port);
        Endpoint.publish(url, service);
        System.out.println("Server for Game is run");
    }

}
