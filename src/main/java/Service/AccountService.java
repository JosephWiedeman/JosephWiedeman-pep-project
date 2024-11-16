package Service;

import DAO.AccountDao;
import Model.Account;

public class AccountService {
    private AccountDao accountDao;

    public AccountService(){
        accountDao = new AccountDao();
    }

    public Account addAccount(Account account){
        if(account.getPassword().length() < 4 ){
            return null;
        }
        if(account.getUsername().equals("")){
            return null;
        }
        return accountDao.insertAccount(account);
    }
}
