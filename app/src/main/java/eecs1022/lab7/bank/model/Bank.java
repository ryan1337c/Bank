package eecs1022.lab7.bank.model;

public class Bank {
    private Client client;
    private Client[] listClients = new Client[6];
    private String error;
    private int clientCount = -1;
    private String name_client = "";
    public Bank() {
        this.error  = "";
    }

    public String getStatus() {
        if (this.error.equals("Max reached")){
            this.error = "";
            return "Error: Maximum Number of Accounts Reached";
        }
        else if (this.error.equals("Name exist")){
            this.error = "";
            return "Error: Client " + this.name_client + " already exists";
        }
        else if (this.error.equals("Non-positive Bal")){
            this.error = "";
            return "Error: Non-Positive Initial Balance";
        }
        else if (this.error.equals("From-account n")){
            this.error = "";
            return "Error: From-Account " + this.name_client + " does not exist";
        }
        else if (this.error.equals("To-account n")){
            this.error = "";
            return "Error: To-Account " + this.name_client + " does not exist";
        }
        else if(this.error.equals("Non-Positive")){
            this.error = "";
            return "Error: Non-Positive Amount";
        }
        else if (this.error.equals("Withdrawing too much")){
            this.error = "";
            return "Error: Amount too large to withdraw";
        }
        else if (this.error.equals("Too large to transfer")){
            this.error = "";
            return "Error: Amount too large to transfer";
        }
        if (this.clientCount >= 0){
            String b_Status = "Accounts: {";
            for (int i=0; i<this.listClients.length; i++){
                if (!(this.listClients[i] == null)){
                    b_Status += this.listClients[i].getStatus();
                    if (i!=this.listClients.length-1 && this.listClients[i+1]!=null){
                        b_Status += ", ";
                    }
                }
            }
            b_Status += "}";
            return b_Status;
        }
            return "Accounts: {}";
    }
    public String[] getStatement(String name) {
        String [] temp = new String[0];
        this.name_client = name;
        for (int i = 0; i < this.listClients.length; i++) {
            if (this.listClients[i] == null){
                temp = null;
                this.error = "From-account n";
            }
            else if (this.listClients[i].name.equals(name)) {
                temp = this.listClients[i].getStatement();
                this.error = "";
                break;
            }
            else{
                temp = null;
                this.error = "From-account n";
            }
        }
        return temp;
    }
    public void addClient(String name, double bal) {
        this.clientCount++;
        boolean error = false;
        if (this.clientCount > 5){
            this.error = "Max reached";
        }
        else{
            if (bal<=0){
                this.error = "Non-positive Bal";
                error = true;
            }
            for (int i = 0; i < this.listClients.length; i++) {
                if (this.listClients[i] == null){
                    continue;
                }
                else if (this.listClients[i].name.equals(name)) {
                    this.error = "Name exist";
                    this.name_client= name;
                    error = true;
                    break;
                }
            }
            if (error == false){
                this.client = new Client(name, bal);
                this.listClients[this.clientCount] = this.client;
            }
            else{
                this.clientCount--;
            }
        }
    }
    public void deposit(String name, double num) {
        this.name_client = name;
            for (int i = 0; i < this.listClients.length; i++) {
                if (this.listClients[i]==null){
                    this.error = "To-account n";
                }
                else if (this.listClients[i].name.equals(name)) {
                    if (num<=0){
                        this.error = "Non-Positive";
                    }
                    else{
                        this.listClients[i].deposit(num);
                        this.error = "";
                    }
                    break;
                }
                else {
                    this.name_client = name;
                    this.error = "To-account n";
                }
            }
        }
    public void withdraw (String name, double num){
        this.name_client = name;
        for (int i=0; i<this.listClients.length; i++){
            if (this.listClients[i] == null){
                this.name_client = name;
                this.error = "From-account n";
            }
            else if (this.listClients[i].name.equals(name)){
                if (this.listClients[i].balance < num){
                    this.error = "Withdrawing too much";
                }
                else if (num <=0){
                    this.error = "Non-Positive";
                }
                else{
                    this.listClients[i].withdraw(num);
                }
                break;
            }
            else{
                this.name_client = name;
                this.error = "From-account n";
            }
        }
    }
    public void transfer (String name1, String name2, double transfer){
        for (int i=0; i<this.listClients.length; i++){
            if (this.listClients[i] == null){
                this.name_client = name1;
                this.error = "From-account n";
            }
            else if (this.listClients[i].name.equals(name1)) {
                for (int j = 0; j < this.listClients.length; j++){
                    if(this.listClients[j] == null){
                        this.name_client = name2;
                        this.error = "To-account n";
                    }
                    else if (this.listClients[j].name.equals(name2)){
                        if (transfer <= 0) {
                            this.error = "Non-Positive";
                         }
                        else if (this.listClients[i].balance < transfer){
                            this.error = "Too large to transfer";
                        }
                        else{
                            this.error = "";
                            this.listClients[j].deposit(transfer);
                            this.listClients[i].withdraw(transfer);
                        }
                        break;
                    }
                    else{
                        this.name_client = name2;
                        this.error = "To-account n";
                    }
                  }
                 break;
                }
            else{
                this.name_client = name1;
                this.error = "From-account n";
            }
        }
    }
}