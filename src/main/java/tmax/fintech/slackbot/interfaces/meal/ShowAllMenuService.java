package tmax.fintech.slackbot.interfaces.meal;

import com.slack.api.Slack;
import com.slack.api.model.block.LayoutBlock;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import tmax.fintech.slackbot.domain.meal.Gyudon;
import tmax.fintech.slackbot.domain.meal.Joyce;
import tmax.fintech.slackbot.domain.meal.KimbabKing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.slack.api.model.block.Blocks.divider;
import static com.slack.api.model.block.Blocks.header;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.webhook.WebhookPayloads.payload;

@Service
@RequiredArgsConstructor
public class ShowAllMenuService {

    private final Joyce joyce;
    private final Gyudon gyudon;
    private final KimbabKing kimBabKing;



    public void getAllMenu() throws IOException {
        List<LayoutBlock> layoutBlocks = new ArrayList<>();
        layoutBlocks.add(header(h -> h.text(plainText(p -> p.emoji(true).text("오리역 페이코 식당 목록")))));

        joyce.getRestaurant(layoutBlocks);
        gyudon.getRestaurant(layoutBlocks);
        kimBabKing.getRestaurant(layoutBlocks);

        Slack.getInstance().send("https://hooks.slack.com/services/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",
                payload(p -> p
                        .text("슬랙에 메시지를 출력하였습니다.")
                        .blocks(layoutBlocks)
                )
        );
    }
}
