import static spark.Spark.*;


import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class SMSBackend {
    public static void main(String[] args) {
        get("/", (req, res) -> "Hello, World! now");

        TwilioRestClient client = new TwilioRestClient.Builder(System.getenv("TWILIO_ACCOUNT_SID"), System.getenv("TWILIO_AUTH_TOKEN")).build();
        post("/sms", (req, res) -> {
            String body = req.queryParams("Body");
            String to = req.queryParams("To");
            String from = "+12672140841";

            Message message = new MessageCreator(
                    new PhoneNumber(to),
                    new PhoneNumber(from),
                    body).create(client);

            return message.getSid();
        });

        post("/mms", (req, res) -> {

            String body = req.queryParams("Body");
            String to = req.queryParams("To");

            String[] str = req.queryParams("imageUrls").split("TWILIO_URL");
            List<URI> imageUrls = new ArrayList<URI>();
            for(String string : str)
                imageUrls.add(new URI(string));

            String from = "+12672140841";

            Message message = new MessageCreator(
                    new PhoneNumber(to),
                    new PhoneNumber(from),
                    body).setMediaUrl(imageUrls).create(client);

            return message.getSid();
        });
    }
}