package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class MessageDAO {

    /**
     * DAO implementation to create a new Message in database.
     * 
     * @param msg Message object to be created
     * @return Message if insertion successful, null if not.
     */
    public Message createMessage(Message msg){
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, msg.getPosted_by());
            ps.setString(2, msg.getMessage_text());
            ps.setLong(3, msg.getTime_posted_epoch());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if(rs.next()){
                return new Message(rs.getInt("message_id"), 
                msg.getPosted_by(), 
                msg.getMessage_text(), 
                msg.getTime_posted_epoch());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * DAO implementation to get all Message objects in database.
     * 
     * @return List of Message objects, blank if no Message objects are in database.
     */
    public List<Message> getAllMessages(){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messageList = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message msg = new Message(rs.getInt(1), 
                rs.getInt(2), 
                rs.getString(3), 
                rs.getLong(4));

                messageList.add(msg);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return messageList;
    }

    /**
     * DAO implementation to get a Message by its Message ID.
     * 
     * @param id ID no. for the Message to be retrieved.
     * @return Message if retrieval is successful, null if not.
     */
    public Message getMessageById(int id){
        Connection conn = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message msg = new Message(rs.getInt(1), 
                rs.getInt(2), 
                rs.getString(3), 
                rs.getLong(4));
                return msg;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * DAO implementation to delete a Message by its Message ID.
     * 
     * @param id ID no. of the Message to be deleted.
     * @return Message if deletion was successful, null if not.
     */
    public Message deleteMessage(int id){
        Connection conn = ConnectionUtil.getConnection();
        try{
            Message msgExists = getMessageById(id);
            if(msgExists != null){
                String sql = "DELETE FROM message WHERE message_id = ?;";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setInt(1, id);

                int result = ps.executeUpdate();
                if(result > 0){
                    return msgExists;
                }
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }


        return null;
    }

    /**
     * DAO implementation to update a Message by its Message ID.
     * 
     * @param msg Message object with Message ID and Message Text fields only.
     * @return Message object if successful, null if not.
     */
    public Message updateMessage(Message msg){
        Connection conn = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, msg.getMessage_text());
            ps.setInt(2, msg.getMessage_id());

            int result = ps.executeUpdate();
            if(result > 0){
                Message patchedMsg = getMessageById(msg.getMessage_id());
                return patchedMsg;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * DAO implementation to get List of Message objects from database by their Account ID.
     * 
     * @param accountId Account ID no. of Message objects to be retrieved.
     * @return List of Messages if successful, null if not.
     */
    public List<Message> getAllMessagesByAccountId(int accountId){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messageList = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message msg = new Message(rs.getInt(1), 
                rs.getInt(2), 
                rs.getString(3), 
                rs.getLong(4));

                messageList.add(msg);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return messageList;
    }
    
}
