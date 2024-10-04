package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {

    private AccountDAO accountDAO;

    /**
     * No-args constructor
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor that creates an AccountService with a specified AccountDAO object.
     * 
     * @param accountDAO The DAO layer that is being used.
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /**
     * Service layer implementation for creating a new account if it meets requirements.
     * 
     * @param acc Account object with no Account ID.
     * @return Account object with ID if successful, null if not.
     */
    public Account addAccount(Account acc){        
        acc = validateAddAccount(acc);
        if(acc != null){
            return accountDAO.insertAccount(acc);
        }
        return null;
        
    }

    /**
     * Validates that the Account meets username and password requirements.
     * 
     * @param acc Account object to be verified.
     * @return Account object if successfull, null if not.
     */
    private Account validateAddAccount(Account acc){
        if((acc.getPassword()).length() < 4){
            return null;
        }
        if(acc.getUsername() == null || acc.getUsername().equals("")){
            return null;
        }
        if(accountDAO.getAccount(acc) != null){
            return null;
        }

        return acc;
    }

    /**
     * Service layer implementation for getting an existing Account by its username and password.
     * 
     * @param acc Account object without Account ID.
     * @return Account object with ID if successful, null if not.
     */
    public Account loginAccount(Account acc){       
        return accountDAO.getAccount(acc);
    }
    
}
