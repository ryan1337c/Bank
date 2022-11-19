package eecs1022.lab7.bank.model;

public class Client {
    public String name;
    public double balance;
    private String[] statement;
    private int statement_size;
    private int count_trans;
    private Transaction [] listTrans = new Transaction[10];
    public Client (String name, double balance){
        this.name = name;
        this.balance = balance;
        this.statement_size = 1;
        this.statement = new String [11];
        this.listTrans = new Transaction[10];
        this.count_trans = -1;
    }
    public String getStatus(){
        return this.name + ": $" + String.format("%.2f", this.balance);
    }

    public String[] getStatement(){
        String [] temp = new String[this.statement_size];
        for (int i=0; i<this.statement_size; i++){
            if (i == 0){
                this.statement[i] = this.getStatus();
                temp[i] = this.statement[i];
            }
            else if (this.statement[i] == null) {
                this.statement[i] = this.listTrans[i-1].getStatus();
                temp[i] = this.statement[i];
            }
            else {
                temp[i] = this.statement[i];
            }
        }
        return temp;
    }
    public void deposit(double num){
        this.statement_size ++;
        Transaction transaction = new Transaction ("DEPOSIT", num);
        this.count_trans++;
        this.listTrans[this.count_trans] = transaction;
        this.balance += num;
    }
    public void withdraw(double num){
        this.statement_size ++;
        Transaction transaction = new Transaction ("WITHDRAW", num);
        this.count_trans++;
        this.listTrans[this.count_trans] = transaction;
        this.balance -= num;
    }

}
