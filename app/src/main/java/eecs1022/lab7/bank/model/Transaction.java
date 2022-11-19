package eecs1022.lab7.bank.model;

public class Transaction {
    private String action;
    private double num;
    public Transaction (String action, double num){
        this.action = action;
        this.num = num;
    }
    public String getStatus(){
        String status = "";
        if (this.action.equals("DEPOSIT")){
            status = "Transaction DEPOSIT: $" + String.format("%.2f",this.num);
            return status;
        }
        else if (this.action.equals("WITHDRAW")){
            status = "Transaction WITHDRAW: $" + String.format("%.2f",this.num);
            return status;
        }
        else{
            return status;
        }
    }
}
