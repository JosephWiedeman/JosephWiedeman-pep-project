package Service;

import DAO.MessageDao;
import Model.Message;
import DAO.AccountDao;

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
     * @param account The account to be added to the database
     * @return Return the account that was added, or null if it violates certain condition
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

}
