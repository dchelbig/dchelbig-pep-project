package DAO;

import Util.ConnectionUtil;
import Model.Account;
import java.sql.*;

public class AccountDAO{

    /**
     * DAO implementation to create a new Account in database.
     * 
     * @param acc Account to be inserted in database.
     * @return Account if insertion successful, null if not.
     */
    public Account insertAccount(Account acc){
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, acc.getUsername());
            ps.setString(2, acc.getPassword());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if(rs.next()){
                return new Account(rs.getInt(1), 
                acc.getUsername(), 
                acc.getPassword());
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * DAO implementation to get an Account in database by its username and password.
     * @param acc Account with username and password fields only.
     * @return Account with Account ID if successful, null if not.
     */
    public Account getAccount(Account acc){
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, acc.getUsername());
            ps.setString(2, acc.getPassword());

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Account(rs.getInt(1), 
                rs.getString(2), 
                rs.getString(3));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    

    
}