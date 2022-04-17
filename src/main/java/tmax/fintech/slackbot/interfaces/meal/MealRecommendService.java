package tmax.fintech.slackbot.interfaces.meal;

import com.slack.api.Slack;
import com.slack.api.model.block.LayoutBlock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tmax.fintech.slackbot.config.SlackChannel;
import tmax.fintech.slackbot.domain.meal.Gyudon;
import tmax.fintech.slackbot.domain.meal.Joyce;
import tmax.fintech.slackbot.domain.meal.KimbabKing;
import tmax.fintech.slackbot.interfaces.alert.AlertService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.header;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.webhook.WebhookPayloads.payload;

@Service
@Slf4j
@RequiredArgsConstructor
public class MealRecommendService {


    @Value(value = "${slack.stageUrl}")
    String stageUrl;

    @Value(value = "${slack.testUrl}")
    String testUrl;


    private final AlertService alertService;
    private final Joyce joyce;
    private final Gyudon gyudon;
    private final KimbabKing kimbabKing;

    public void getMenu() {
        alertService.slackSendMessage(SlackChannel.TEST, "메뉴 추천");
    }

    public void getRecommendMenu() throws IOException {
        List<LayoutBlock> layoutBlocks = new ArrayList<>();
        layoutBlocks.add(header(h -> h.text(plainText(p -> p.emoji(true).text("식당 추천")))));
        int num = (int) (Math.random() * 3) + 1;

        if (num == 1) {
            joyce.getRestaurant(layoutBlocks);
        } else if (num == 2) {
            gyudon.getRestaurant(layoutBlocks);
        } else {
            kimbabKing.getRestaurant(layoutBlocks);
        }

        Slack.getInstance().send(testUrl,
                payload(p -> p
                        .text("슬랙에 메시지를 출력하였습니다.")
                        .blocks(layoutBlocks)
                )
        );
    }
}
