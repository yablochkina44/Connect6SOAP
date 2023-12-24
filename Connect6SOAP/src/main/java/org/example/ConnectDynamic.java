package org.example;
import javax.jws.WebMethod;
import javax.jws.WebService;
@WebService
public interface ConnectDynamic{
    @WebMethod
    int GetPlayerCommand();
    @WebMethod
    String GetCurrentStep(int id);
    @WebMethod
    String GetQueueStep();
    @WebMethod
    void SetQueueStep(String step);
    @WebMethod
    String GetStateBoard(int i, int j);
    @WebMethod
    void SetStateBoard(int i, int j, String s);
    @WebMethod
    String CheckWinner();

}
