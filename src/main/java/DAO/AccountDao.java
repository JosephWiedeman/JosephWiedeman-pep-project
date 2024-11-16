package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    private AccountDao(){

    }
    private static AccountDao accountDao = null; 

    static AccountDao instance() {
        if (accountDao == null) {
            accountDao = new AccountDao();
        }
        return accountDao;
    }

    public Account insertAuthor(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
//          Write SQL logic here. You should only be inserting with the name column, so that the database may
//          automatically generate a primary key.
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            //write preparedStatement's setString method here.
            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
