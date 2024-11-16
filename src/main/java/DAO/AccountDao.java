package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {


    public Account insertAccount (Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
//          Inserting with the username and password columns, so that the database may
//          automatically generate a primary key.
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            //Execute Update and get the generated keys
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

    public Account geAccountByUsername(String userName){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return new Account(rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
