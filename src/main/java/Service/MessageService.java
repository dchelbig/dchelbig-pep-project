package Service;

import Model.Message;
import DAO.MessageDAO;
import java.util.List;

public class MessageService {   
    private MessageDAO messageDAO;

    /**
     * No-args constructor
     */
    public MessageService(){
        messageDAO = new MessageDAO();
    }

    /**
     * Constructor that creates a MessageService with a specified MessageDAO object.
     * 
     * @param messageDAO The DAO layer that is being used.
     */
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    /**
     * Service layer implementation for posting a new message if it meets requirements.
     * 
     * @param msg Message object with missing Message ID parameter.
     * @return A Message object of the new post if successful, null if not.
     */
    public Message postMessage(Message msg){
        msg = validatePostMessage(msg);
        return msg == null ? null : messageDAO.createMessage(msg);
    }

    /**
     * Validates that the message text being posted meets the requirements.
     * 
     * @param msg Message object to be validated
     * @return Same message object if requirements are met, null if not.
     */
    private Message validatePostMessage(Message msg){
        return (msg.getMessage_text() == null || msg.getMessage_text().length() == 0) ? null : msg;
    }

    /**
     * Service layer implementation for getting all Messages in database.
     * 
     * @return List of Message objects (blank list if no messages are in database).
     */
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    /**
     * Service layer implementation for getting a message by its Message ID.
     * 
     * @param msgId ID no. of the Message to be retrieved.
     * @return Message identified by Message ID, null if ID is not present in database.
     */
    public Message getMessageById(int msgId){
        return messageDAO.getMessageById(msgId);
    }

    /**
     * Service layer implementation for deleting a message by its Message ID.
     * 
     * @param msgId ID no. of the message to be deleted.
     * @return Deleted Message if Message ID exists, null if ID is not present in database.
     */
    public Message deleteMessage(int msgId){
        return messageDAO.deleteMessage(msgId);
    }

    /**
     * Service layer implementation for updating message text of a Message by its Message ID.
     * 
     * @param msg Message object that will have its text updated.
     * @return Message with updated messageText field if Message ID exists and requirements met,
     *         null if not.
     */
    public Message updateMessage(Message msg){
        String patchMsg = validateUpdateMessage(msg.getMessage_text());
        return patchMsg == null ? null : messageDAO.updateMessage(msg);
    }

    /**
     * Validates that the message text for updating the message meets message requirements.
     * 
     * @param msg Message text to be evaluated.
     * @return Message text if requirements are met, null if not.
     */
    private String validateUpdateMessage(String msg){
        return (msg == null || msg.isEmpty() || msg.length() > 255) ? null : msg;
    }

    /**
     * Service layer implementation for getting all Message object that match a specific Account ID.
     * @param accountId Account ID no. that is a foerign key of Message table in database.
     * @return List of Message objects if Account ID exists, null if not.
     */
    public List<Message> getAllMessagesByAccountId(int accountId){
        return messageDAO.getAllMessagesByAccountId(accountId);
    }
}
