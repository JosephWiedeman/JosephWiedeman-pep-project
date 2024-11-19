package Service;

import DAO.MessageDao;
import Model.Message;
import DAO.AccountDao;
import java.util.List;

public class MessageService {
    private MessageDao messageDao;
    private AccountDao accountDao;

    public MessageService(){
        messageDao = new MessageDao();
        accountDao = new AccountDao();
    }

    /**
     *  Adds message to the database from the given endpoint, or don't if message length is greater than 255,
     *  blank message, or if the posted_by, or an user's account_id, is not present in the account database
     * @param message The message to be added to the database
     * @return Return the message that was added, or null if it violates certain conditions
     */
    public Message addMessage(Message message){
        //SQL does help with the message size being over 255, 
        //but this can be done faster here instead of going through SQL and JDBC
        if(message.getMessage_text().length() <= 0 || message.getMessage_text().length() > 255){
            return null;
        }
        if(accountDao.getAccountByAccountId(message.getPosted_by()) == null){
            return null;
        }
        
        return messageDao.insertMessage(message);
    }

    /**
     * Uses the messageDAO to retrieve all messages.
     * @return all messages.
     */
    public List<Message> getAllMessages(){
        return messageDao.getAllMessages();
    }

    /**
     * Uses the messageDAO to retrieve message with the specific message_id.
     * @param messageId The message_id to be used to search for in the database
     * @return message with the specific message_id.
     */
    public Message getMessageByMessageId(int messageId){
        return messageDao.getMessageByMessageId(messageId);
    }

    /**
     * Uses the messageDAO to retrieve message with the specific message_id, and if it is there, delete the message data.
     * @param messageId The message_id to be used to search for in the database and to be possibly deleted
     * @return message with the specific message_id that was deleted, or null if it doesn't exist in the table.
     */
    public Message deleteMessageByMessageId(int messageId){
        //Determines if the message is present in the database first
        Message deletedMessage = messageDao.getMessageByMessageId(messageId);
        if(deletedMessage != null){
            //If the message is in the database, delete the message and return the message with the message_id
            messageDao.deleteMessageByMessageId(messageId);
            return deletedMessage;
        }
        //If the message is not in the database, then just return null 
        return null;
    }


}
