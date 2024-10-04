package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
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
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserId);

        return app;
    }

    /**
     * Controller for registering a new account. Provides corresponding service level with Account object with all parameters except an account ID.
     * Receives a full Account message if new account conditions are met, sets 400 error status if not.
     * 
     * @param ctx Represents POST request and response handler at endpoint "/register".
     * @throws JsonProcessingException if method encounters an error processing JSON POST request.
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account acc = om.readValue(ctx.body(), Account.class);
        Account addedAcc = accountService.addAccount(acc);
        if(addedAcc != null){
            ctx.json(addedAcc);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Controller for logging in to existing account. Provides corresponding service level with Account object with all parameters except an Account ID.
     * Receives a full Account object if successful, sets 401 error status if not.
     * 
     * @param ctx Represents POST request and response handler at endpoint "/login".
     * @throws JsonProcessingException if method encounters an error processing JSON POST request.
     */
    private void postLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account acc = om.readValue(ctx.body(), Account.class);
        Account loginAcc = accountService.loginAccount(acc);
        if(loginAcc != null){
            ctx.json(loginAcc);
        } else {
            ctx.status(401);
        }
    }

    /**
     * Controller for creating a new message. Provides corresponding service level with Message object with all parameters except a Message ID.
     * Receives a full Message object if successful, sets 400 error status if not.
     * 
     * @param ctx Represents POST request and response handler at endpoint "/messages".
     * @throws JsonProcessingException if method encounters an error processing JSON POST request.
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message msg = om.readValue(ctx.body(), Message.class);
        Message submitMsg = messageService.postMessage(msg);
        if(submitMsg != null){
            ctx.json(submitMsg);
        } else {
            ctx.status(400);
        }
    }
    
    /**
     * Controller for getting all messages in the database.
     * Receives a List of full Message objects if successful, empty if there are no messages.
     * 
     * @param ctx Represents GET request and response handler at endpoint "/messages".
     */
    private void getAllMessagesHandler(Context ctx){
        ctx.json(messageService.getAllMessages());
    }


    /**
     * Controller for getting a specific message. Provides corresponding service level with a Message ID.
     * Receives a full Message object if successful, empty if there is no Message at Message ID.
     * 
     * @param ctx Represents GET request and response handler at endpoint "/messages/{message_id}".
     * @throws JsonProcessingException if method encounters an error processing JSON GET request.
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException{
        int msgId = Integer.parseInt(ctx.pathParam("message_id"));
        Message getMsg = messageService.getMessageById(msgId);
        if(getMsg != null){
            ctx.json(getMsg);
        }
    }

    /**
     * Controller for removing a specific message. Provides corresponding service level with a Message ID.
     * Receives a full Message object if successful, empty if there is no Message at Message ID.
     * 
     * @param ctx Represents DELETE request and response handler at endpoint "/messages/{message_id}".
     * @throws JsonProcessingException if method encounters an error processing JSON DELETE request.
     */
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
        int msgId = Integer.parseInt(ctx.pathParam("message_id"));
        Message delMsg = messageService.deleteMessage(msgId);
        if(delMsg != null){
            ctx.json(delMsg);
        }
    }

    /**
     * Controller for replacing message text for a specific Message. Provides corresponding service level with a Message containing Message ID and Text only.
     * Receives a full updated Message object if successful, sets 400 error code if not.
     * 
     * @param ctx Represents PATCH request and response handler at endpoint "/messages/{message_id}".
     * @throws JsonProcessingException if method encounters an error processing JSON PATCH request.
     */
    private void patchMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message msg = om.readValue(ctx.body(), Message.class);
        msg.setMessage_id(Integer.parseInt(ctx.pathParam("message_id")));
        Message patchMsg = messageService.updateMessage(msg);

        if(patchMsg != null){
            ctx.json(patchMsg);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Controller for getting all messages in the database at a specific Account ID.
     * Receives a List of full Message objects if successful, empty if there are no messages.
     * 
     * @param ctx Represents GET request and response handler at endpoint "/accounts/{account_id}/messages".
     */
    private void getAllMessagesByUserId(Context ctx){
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getAllMessagesByAccountId(accountId));
    }

}