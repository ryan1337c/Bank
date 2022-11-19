package eecs1022.lab7.bank;

import org.junit.Test;

import static org.junit.Assert.*;

import eecs1022.lab7.bank.model.*; /* import all classes from the model package */

public class Tests {

    @Test
    public void test_01() {
        /* Assumption: types of transactions are "DEPOSIT" and "WITHDRAW".
         *
         * A transfer service (say from client X to client Y) is split into two transactions:
         *  1. WITHDRAW from X (meaning that a transaction of type WITHDRAW is added to X's history)
         *  2. DEPOSIT to Y (meaning that a transaction of type DEPOSIT is added to Y's history)
         *
         * The amount of transaction should be displayed with two digits after the decimal.
         */
        Transaction t1 = new Transaction("DEPOSIT", 100.5);
        assertEquals("Transaction DEPOSIT: $100.50", t1.getStatus());

        Transaction t2 = new Transaction("WITHDRAW", 250);
        assertEquals("Transaction WITHDRAW: $250.00", t2.getStatus());
    }

    @Test
    public void test_02a() {
        Client heeyeon = new Client("Heeyeon", 100.5);

        /*
         * A client's status displays their name and current account balance.
         */
        String status = heeyeon.getStatus();
        assertEquals("Heeyeon: $100.50", status);

        /*
         * A client's statement summarizes
         * their current status followed by their history list of transactions.
         */
        String[] stmt = heeyeon.getStatement();
        String[] expectedStmt1 = {heeyeon.getStatus()};
        assertTrue(expectedStmt1.length == 1); /* just the status */
        assertArrayEquals(expectedStmt1, stmt);

        /* Assume: deposit amount always positive. No error checking needed.  */
        heeyeon.deposit(20.3);
        assertEquals("Heeyeon: $120.80", heeyeon.getStatus());
        String[] expectedStmt2 = {heeyeon.getStatus(), "Transaction DEPOSIT: $20.30"};
        assertTrue(expectedStmt2.length == 2); /* status and one transaction */
        assertArrayEquals(expectedStmt2, heeyeon.getStatement());

        /* Assume: withdraw amount always positive and not too large. No error checking needed. */
        heeyeon.withdraw(40.78);
        assertEquals("Heeyeon: $80.02", heeyeon.getStatus());
        String[] expectedStmt3 = {heeyeon.getStatus(), "Transaction DEPOSIT: $20.30", "Transaction WITHDRAW: $40.78"};
        assertTrue(expectedStmt3.length == 3); /* status and two transactions */
        assertArrayEquals(expectedStmt3, heeyeon.getStatement());
    }

    @Test
    public void test_02b() {
        Client jiyoon = new Client("Jiyoon", 89.5);
        String status = jiyoon.getStatus();
        assertEquals("Jiyoon: $89.50", status);

        String[] stmt = jiyoon.getStatement();
        String[] expectedStmt1 = {jiyoon.getStatus()};
        assertTrue(expectedStmt1.length == 1); /* just the status */
        assertArrayEquals(expectedStmt1, stmt);

        /* Assume: withdraw amount always positive and not too large. No error checking needed. */
        jiyoon.withdraw(40.78);
        assertEquals("Jiyoon: $48.72", jiyoon.getStatus());
        String[] expectedStmt2 = {jiyoon.getStatus(), "Transaction WITHDRAW: $40.78"};
        assertTrue(expectedStmt2.length == 2); /* status and one transaction */
        assertArrayEquals(expectedStmt2, jiyoon.getStatement());

        /* Assume: deposit amount always positive. No error checking needed.  */
        jiyoon.deposit(20.3);
        assertEquals("Jiyoon: $69.02", jiyoon.getStatus());
        String[] expectedStmt3 = {jiyoon.getStatus(), "Transaction WITHDRAW: $40.78", "Transaction DEPOSIT: $20.30"};
        assertTrue(expectedStmt3.length == 3); /* status and two transactions */
        assertArrayEquals(expectedStmt3, jiyoon.getStatement());
    }

    @Test
    public void test_03a() {
        Bank b = new Bank();

        /*
         * A bank's status is defined in the "Status of Bank" section of your lab PDF instructions.
         */
        String status = b.getStatus();
        /* status of an empty bank */
        assertEquals("Accounts: {}", status);

        /* Print statement from a non-existing account. */
        String[] heeyeonStmt = b.getStatement("Heeyeon");
        assertNull(heeyeonStmt);
        assertEquals("Error: From-Account Heeyeon does not exist", b.getStatus());

        /* deposit to a non-existing account */
        b.deposit("Heeyeon", 300.05);
        assertEquals("Error: To-Account Heeyeon does not exist", b.getStatus());
        b.deposit("Jiyoon", -300.05); /* error of non-existing to-account takes priority (see error tables in PDF instructions) */
        assertEquals("Error: To-Account Jiyoon does not exist", b.getStatus());

        /* withdraw from a non-existing account */
        b.withdraw("Heeyeon", 10.5);
        assertEquals("Error: From-Account Heeyeon does not exist", b.getStatus());
        b.withdraw("Jiyoon", -300.05); /* error of non-existing from-account takes priority (see error tables in PDF instructions) */
        assertEquals("Error: From-Account Jiyoon does not exist", b.getStatus());

        /* transfer between two non-existing accounts: error of from-account takes priority (see error tables in PDF instructions) */
        b.transfer("Heeyeon", "Jiyoon", 100.24);
        assertEquals("Error: From-Account Heeyeon does not exist", b.getStatus());
        b.transfer("Jiyoon", "Heeyeon", -100.24);
        assertEquals("Error: From-Account Jiyoon does not exist", b.getStatus());
    }

    @Test
    public void test_03b() {
        Bank b = new Bank();

        b.addClient("Heeyeon", -23.5);
        assertEquals("Error: Non-Positive Initial Balance", b.getStatus());
        b.addClient("Heeyeon", 0);
        assertEquals("Error: Non-Positive Initial Balance", b.getStatus());

        b.addClient("Heeyeon", 213.4);

        String expectedStatus = "Accounts: {Heeyeon: $213.40}";
        assertEquals(expectedStatus, b.getStatus());
        /*
         * Recall: A client's statement summarizes
         * their current status followed by their history list of transactions.
         */
        String[] expectedHeeyeonStmt1 = {"Heeyeon: $213.40"};
        /* The added account Heeyeon has no transactions yet. */
        assertArrayEquals(expectedHeeyeonStmt1, b.getStatement("Heeyeon"));

        /* Names of clients are case-sensitive. */
        b.addClient("Heeyeon", 134.56);
        assertEquals("Error: Client Heeyeon already exists", b.getStatus());
        assertArrayEquals(expectedHeeyeonStmt1, b.getStatement("Heeyeon")); /* after an error, account statement stays unchanged */

        /* deposit amount should be positive */
        b.deposit("Heeyeon", -238.29);
        assertEquals("Error: Non-Positive Amount", b.getStatus());
        assertArrayEquals(expectedHeeyeonStmt1, b.getStatement("Heeyeon")); /* after an error, account statement stays unchanged */

        b.deposit("Heeyeon", 0);
        assertEquals("Error: Non-Positive Amount", b.getStatus());
        assertArrayEquals(expectedHeeyeonStmt1, b.getStatement("Heeyeon")); /* after an error, account statement stays unchanged */

        b.addClient("Jiyoon", 239.4);
        b.addClient("Sunhye", 332.6);
        b.addClient("Jihye", 428.8);

        /* at this point, the bank is not full yet */

        b.addClient("Jihye", 81.72); /* here Jihye is a duplicate name, and the max capacity has not reached */
        assertEquals("Error: Client Jihye already exists", b.getStatus());
        String[] expectedJihyeStmt1 = {"Jihye: $428.80"};
        assertArrayEquals(expectedJihyeStmt1, b.getStatement("Jihye")); /* after an error, account statement stays unchanged */

        /* Add clients to gradually reach the maximum capacity of the bank. */
        b.addClient("Suyeon", 590.10);
        b.addClient("Yuna", 640.12);
        expectedStatus = "Accounts: {Heeyeon: $213.40, Jiyoon: $239.40, Sunhye: $332.60, Jihye: $428.80, Suyeon: $590.10, Yuna: $640.12}";
        assertEquals(expectedStatus, b.getStatus());

        b.getStatement("Alan");
        assertEquals("Error: From-Account Alan does not exist", b.getStatus());

        /* Too bad, the bank is full. */
        b.addClient("Jackie", 768.29); /* here Jackie is a non-existing name */
        expectedStatus = "Error: Maximum Number of Accounts Reached";
        /* new account not added */
        assertEquals(expectedStatus, b.getStatus()); /* status of bank after the unsuccessful addition of a new account */
        assertNull(b.getStatement("Jackie"));
        expectedStatus = "Error: From-Account Jackie does not exist";
        assertEquals(expectedStatus, b.getStatus()); /* status of bank after the unsuccessful print of a client's statement */

        b.addClient("Suyeon", 181.72); /* here Suyeon is a duplicated name, but the max capacity error takes priority (see error tables in PDF instructions) */
        assertEquals("Error: Maximum Number of Accounts Reached", b.getStatus());
        String[] expectedSuyeonStmt1 = {"Suyeon: $590.10"}; /* the error does not change suyeon's status */
        assertArrayEquals(expectedSuyeonStmt1, b.getStatement("Suyeon")); /* after an error, account statement stays unchanged */

        b.addClient("Sunhye", 0); /* here Sunhye is a duplicated name, and the balance is non-positive, but the max capacity error takes priority (see error tables in PDF instructions) */
        assertEquals("Error: Maximum Number of Accounts Reached", b.getStatus());
        String[] expectedSunhyeStmt1 = {"Sunhye: $332.60"}; /* the error does not change sunhye's status */
        assertArrayEquals(expectedSunhyeStmt1, b.getStatement("Sunhye")); /* after an error, account statement stays unchanged */
    }

        @Test
        public void test_03c () {
            Bank b = new Bank();
            b.addClient("Heeyeon", 213.4);
            b.addClient("Jiyoon", 239.4);
            b.addClient("Sunhye", 332.6);
            b.addClient("Jihye", 428.8);
            b.addClient("Suyeon", 590.10);
            b.addClient("Yuna", 640.12);

            /* at this point, Heeyeon's account balance is $213.40 */

            b.deposit("Heeyeon", 238.29);
            assertEquals("Accounts: {Heeyeon: $451.69, Jiyoon: $239.40, Sunhye: $332.60, Jihye: $428.80, Suyeon: $590.10, Yuna: $640.12}", b.getStatus());
            String[] expectedHeeyeonStmt2 = {"Heeyeon: $451.69", "Transaction DEPOSIT: $238.29"};
            assertArrayEquals(expectedHeeyeonStmt2, b.getStatement("Heeyeon"));

            b.deposit("Heeyeon", -489.74);
            assertEquals("Error: Non-Positive Amount", b.getStatus());
            assertArrayEquals(expectedHeeyeonStmt2, b.getStatement("Heeyeon")); /* Heeyeon's statement remains the same from before the error */

            /* at this point, Heeyeon's account balance is 451.69 */

            b.withdraw("Heeyeon", -89.74);
            assertEquals("Error: Non-Positive Amount", b.getStatus());
            assertArrayEquals(expectedHeeyeonStmt2, b.getStatement("Heeyeon")); /* Heeyeon's statement remains the same from before the error */

            b.withdraw("Heeyeon", 453.74);
            assertEquals("Error: Amount too large to withdraw", b.getStatus());
            assertArrayEquals(expectedHeeyeonStmt2, b.getStatement("Heeyeon")); /* Heeyeon's statement remains the same from before the error */

            /* at this point, Heeyeon's account balance is 451.69 */

            b.withdraw("Heeyeon", 139.37);
            assertEquals("Accounts: {Heeyeon: $312.32, Jiyoon: $239.40, Sunhye: $332.60, Jihye: $428.80, Suyeon: $590.10, Yuna: $640.12}", b.getStatus());
            String[] expectedHeeyeonStmt3 = {"Heeyeon: $312.32", "Transaction DEPOSIT: $238.29", "Transaction WITHDRAW: $139.37"};
            assertArrayEquals(expectedHeeyeonStmt3, b.getStatement("Heeyeon"));

            /* at this point, Heeyeon's account balance is 312.32 */

            b.transfer("Alan", "Mark", -234.23); /* non-existing from-account name takes priority (see error tables in PDF instructions) */
            assertEquals("Error: From-Account Alan does not exist", b.getStatus());
            b.transfer("Heeyeon", "Mark", -234.23); /* non-existing to-account name takes priority (see error tables in PDF instructions) */
            assertEquals("Error: To-Account Mark does not exist", b.getStatus());
            b.transfer("Heeyeon", "Yuna", 0);
            assertEquals("Error: Non-Positive Amount", b.getStatus());
            b.transfer("Heeyeon", "Yuna", -234.23);
            assertEquals("Error: Non-Positive Amount", b.getStatus());
            b.transfer("Heeyeon", "Yuna", 313.48);
            assertEquals("Error: Amount too large to transfer", b.getStatus());
            /* after each of the above transfer errors, the bank's status is unchanged from the last withdrawal */

            /* at this point, Heeyeon's account balance remains 312.32 and Yuna's account balance remains 640.12  */

            b.transfer("Heeyeon", "Yuna", 50); /* a WITHDRAW transaction added to Heeyeon and a DEPOSIT transaction added to Yuna */
            assertEquals("Accounts: {Heeyeon: $262.32, Jiyoon: $239.40, Sunhye: $332.60, Jihye: $428.80, Suyeon: $590.10, Yuna: $690.12}", b.getStatus());
            String[] expectedHeeyeonStmt4 = {"Heeyeon: $262.32", "Transaction DEPOSIT: $238.29", "Transaction WITHDRAW: $139.37", "Transaction WITHDRAW: $50.00"};
            assertArrayEquals(expectedHeeyeonStmt4, b.getStatement("Heeyeon"));
            String[] expectedYunaStmt2 = {"Yuna: $690.12", "Transaction DEPOSIT: $50.00"};
            assertArrayEquals(expectedYunaStmt2, b.getStatement("Yuna"));

            /* at this point, Heeyeon's account balance is 262.32 and Yuna's account balance is 690.12  */

            b.transfer("Yuna", "Heeyeon", 30); /* a WITHDRAW transaction added to Yuna and a DEPOSIT transaction added to Heeyeon */
            assertEquals("Accounts: {Heeyeon: $292.32, Jiyoon: $239.40, Sunhye: $332.60, Jihye: $428.80, Suyeon: $590.10, Yuna: $660.12}", b.getStatus());
            String[] expectedHeeyeonStmt5 = {"Heeyeon: $292.32", "Transaction DEPOSIT: $238.29", "Transaction WITHDRAW: $139.37", "Transaction WITHDRAW: $50.00", "Transaction DEPOSIT: $30.00"};
            assertArrayEquals(expectedHeeyeonStmt5, b.getStatement("Heeyeon"));
            String[] expectedYunaStmt3 = {"Yuna: $660.12", "Transaction DEPOSIT: $50.00", "Transaction WITHDRAW: $30.00"};
            assertArrayEquals(expectedYunaStmt3, b.getStatement("Yuna"));
        }
    }