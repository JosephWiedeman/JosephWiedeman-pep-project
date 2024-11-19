package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {
    
/**
     * Inserts a message into the database, given a message to add
     * @param message The message to be added to the message table
     * @return The message that was added to the database, with the updated message_id made
     *         by the sql table of auto_increment phrase
     */
    public Message insertMessage (Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
//          Inserting with the message data, so that the database may
//          automatically generate a primary key.
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            //Execute Update and get the generated keys
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), 
                message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieve all messages from the message table.
     * @return all message.
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Gets the only message for given message_id 
     * @param messageId The message_id for a given message
     * @return Return the message with the given message_id, or return null if it is not there
     */
    public Message getMessageByMessageId(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Deletes the only message for a given message_id 
     * @param messageId The message_id for a given message
     */
    public void deleteMessageByMessageId(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            preparedStatement.executeQuery();
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
    }

    /**
     * Updates the only message for a given message_id with the updated message_text, then finds the
     * updated message and returns the now updated message
     * @param messageId The message_id for a given message
     * @param messageText The updated message_text used to update the table for given message
     * @return Returns the updated message with the updated message_text
     */
    public Message updateMessageTextByMessageId(int messageId, String messageText){
        Connection connection = ConnectionUtil.getConnection();
        try{
            //Updates the message with message_text for message with given message_id
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, messageText);
            preparedStatement.setInt(2, messageId);
            preparedStatement.executeUpdate();

            //Finds updated message using message_id
            sql = "SELECT * FROM message WHERE message_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                return new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
            }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * Gets messages that were posted by a certain user, using the posted_by column data
     * @param postedBy The user's account_id used as data for determining who posted the message
     * @return Return the list of messages with the given posted_by data, or an empty list
     */
    public List<Message> getMessageByPostedBy(int postedBy){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, postedBy);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
