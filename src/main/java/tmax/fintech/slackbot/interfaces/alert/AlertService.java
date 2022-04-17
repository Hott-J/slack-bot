package tmax.fintech.slackbot.interfaces.alert;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tmax.fintech.slackbot.config.SlackChannel;

import java.io.IOException;

@Service
@Slf4j
public class AlertService {
    @Value(value = "${slack.token}")
    String token;

    @Value(value = "${slack.channel.stage}")
    String stageChannel;

    @Value(value = "${slack.channel.test}")
    String testChannel;

    public void slackSendMessage(SlackChannel slackChannel, String message){
        try{
            Slack slack = Slack.getInstance();
            String channel;
            if (slackChannel.equals(SlackChannel.STAGE)){
                channel = stageChannel;
            }
            else {
                channel = testChannel;
            }
            slack.methods(token).chatPostMessage(req -> req.channel(channel).text(message));
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }

    public void slackSendMessage(String message){
        try{
            Slack slack = Slack.getInstance();
            slack.methods(token).chatPostMessage(req -> req.channel(stageChannel).text(message));
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }

}
