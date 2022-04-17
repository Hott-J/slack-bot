package tmax.fintech.slackbot.domain.meal;

import com.slack.api.model.block.element.ImageElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;
import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.Blocks.context;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.*;

@Component
public class Joyce {

    @Value(value = "${emoji.locationMarker}")
    String emojiLocationMarker;

    @Value(value = "${restaurant.joyceMap}")
    String map;

    @Value(value = "${restaurant.joyceMenu}")
    String menu;

    @Value(value = "${restaurant.joycePhoneNum}")
    String phoneNum;

    @Value(value = "${restaurant.joyceImage}")
    String image;

    @Value(value = "${restaurant.joyceAddress}")
    String address;

    public void getRestaurant(List layoutBlocks) throws IOException {
        layoutBlocks.add(divider());
        layoutBlocks.add(
                section(sections -> sections
                        .text(markdownText(
                                        "*<" + map + "|[ 조이스 키친 ]>*\n" +
                                        "이탈리안 푸드\n\n"))
                        .accessory(ImageElement.builder()
                                .imageUrl(image)
                                .altText("image")
                                .build())
                )
        );
        layoutBlocks.add(
                context(contexts -> contexts
                        .elements(asContextElements(
                                imageElement(image -> image.imageUrl(emojiLocationMarker)
                                        .altText("emojiLocationMarker")
                                ), plainText(p -> p.emoji(true).text(address))
                        ))));
        layoutBlocks.add(divider());
        layoutBlocks.add(
                actions(actions -> actions
                        .elements(asElements(
                                button(b -> b.text(plainText(pt -> pt.emoji(true).text("좋아요")))
                                        .value("/api/v1/meal/approve")
                                        .style("primary")
                                        .actionId("action_approve")
                                ),
                                button(b -> b.text(plainText(pt -> pt.emoji(true).text("싫어요")))
                                        .value("/api/v1/meal/reject")
                                        .style("danger")
                                        .actionId("action_reject")
                                )
                        ))
                )
        );
    }
}
