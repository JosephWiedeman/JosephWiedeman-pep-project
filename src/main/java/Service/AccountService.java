package Service;

import DAO.AccountDao;
import Model.Account;

public class AccountService {
    private AccountDao accountDao;

    public AccountService(){
        accountDao = new AccountDao();
    }

    /**
     *  Adds account to the database from the given endpoint, or don't if password length is less than 4,
     *  blank username, or it is already present in the present in the database
     * @param account The account to be added to the database
     * @return Return the account that was added, or null if it violates certain condition
     */
    public Account addAccount(Account account){
        if(account.getPassword().length() < 4 ){
            return null;
        }
        if(account.getUsername().equals("")){
            return null;
        }
        if(accountDao.getAccountByUsername(account.username) != null){
            return null;
        }
        return accountDao.insertAccount(account);
    }

    /**
     * Finds the account with the given username and password from the given account
     * @param account The account's username and password to check to see if account is present
     * @return The account that has the given username and password, or null if not
     */
    public Account findAccountByUsernameAndPassword(Account account){
        return accountDao.getAccountByUsernameAndPassword(account);
    }
}
