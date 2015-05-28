package vandy.mooc.utils;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;

/**
 * A thin facade around an Android Message that defines the schema of
 * a request from the Activity to the Service.
 */
public class RequestMessage extends RequestReplyMessageBase {
    /**
     * Constructor is private to ensure the makeRequestMessage()
     * factory method is used.
     */
    private RequestMessage(Message message) {
        super(message);
    }

    /**
     * Convert a Message into a RequestMessage.
     */
    public static RequestMessage makeRequestMessage(Message message) {
        // Make a copy of @a message since it may be recycled.
        return new RequestMessage(Message.obtain(message));
    }

    /**
     * Factory method creates a RequestMessage to return to the
     * Activity with information necessary to download an image.
     */
    public static RequestMessage makeRequestMessage(int requestCode, 
                                                    Uri url,
                                                    String directoryPathname,
                                                    Messenger replyMessenger) {
        // Create a RequestMessage that holds a reference to a Message
        // created via the Message.obtain() factory method.
        RequestMessage requestMessage =
            new RequestMessage(Message.obtain());

        // Store replyMessenger into the Message's replyTo field.
        requestMessage.setMessenger(replyMessenger);

        // Create a new Bundle to handle the result.
        Bundle data = new Bundle();

        // Set the Bundle as the "data" for the Message.
        requestMessage.setData(data);

        // Put the URL to the image file into the Bundle
        data.putString(IMAGE_URL, url.toString());

        // Put the pathname to the directory into the Bundle
        data.putString(DIRECTORY_PATHNAME, directoryPathname);

        // Put the request code into the Bundle
        data.putInt(REQUEST_CODE, requestCode);

        // Return the message to the caller.
        return requestMessage;
    }
}
