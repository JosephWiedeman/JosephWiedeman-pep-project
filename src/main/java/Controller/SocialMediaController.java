package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;
import java.util.Objects;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;


    //Sets up the services for the account and messages when the controller is made
    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByMessageIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByMessageIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageUpdate);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesForUserHandler);
        return app;
    }

    /**
     *  Handler for posting new accounts to the database (Requirement #1)
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));
        }else{
            ctx.status(400);
        }
    }

    /**
     *  Handler for determining if the account is present and the user is able to login and post (Requirement #2)
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.findAccountByUsernameAndPassword(account);
        if(addedAccount!=null){
            ctx.json(mapper.writeValueAsString(addedAccount));
        }else{
            ctx.status(401);
        }
    }

    /**
     *  Handler for posting new messages to the database (Requirement #3)
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage!=null){
            ctx.json(mapper.writeValueAsString(addedMessage));
        }else{
            ctx.status(400);
        }
    }

    /**
     *  Handler for getting all messages from the message database (Requirement #4)
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     */
    public void getAllMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    /**
     *  Handler for getting messages from the message database based on message_id (Requirement #5)
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    public void getMessageByMessageIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        Message message = messageService.getMessageByMessageId(messageId);
        if(message!=null){
            ctx.json(mapper.writeValueAsString(message));
        }else{
            ctx.json("");
        }
    }

     /**
     *  Handler for deleteing messages from the message database based on message_id (Requirement #6)
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    public void deleteMessageByMessageIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        Message message = messageService.deleteMessageByMessageId(messageId);
        if(message!=null){
            ctx.json(mapper.writeValueAsString(message));
        }else{
            //Empty message and status is set to 200, just in case
            ctx.status(200);
            ctx.json("");
        }
    }

    /**
     *  Handler for updating messages from the message database based on message_id and 
     * using an updated message_text(Requirement #7)
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    public void patchMessageUpdate(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        //Gets a message with updated message_text
        Message message = mapper.readValue(ctx.body(), Message.class);
        //Gets the message_id for the given message to update
        int messageId = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        Message updatedMessage = messageService.updateMessageTextByMessageId(messageId, message.getMessage_text());
        if(updatedMessage!=null){
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }else{
            ctx.status(400);
        }
    }

    /**
     *  Handler for getting all messages from the message database for a certain user (Requirement #8)
     * @param ctx The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    public void getAllMessagesForUserHandler(Context ctx) throws JsonProcessingException{
        int accountId = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("account_id")));
        List<Message> messages = messageService.getMessageByPostedBy(accountId);
        ctx.json(messages);
    }

}