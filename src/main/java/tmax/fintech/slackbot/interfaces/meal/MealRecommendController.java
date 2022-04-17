package tmax.fintech.slackbot.interfaces.meal;

import com.slack.api.Slack;
import com.slack.api.app_backend.interactive_components.ActionResponseSender;
import com.slack.api.app_backend.interactive_components.payload.BlockActionPayload;
import com.slack.api.app_backend.interactive_components.response.ActionResponse;
import com.slack.api.model.Message;
import com.slack.api.util.json.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tmax.fintech.slackbot.config.Restaurant;
import tmax.fintech.slackbot.domain.meal.Lunch;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MealRecommendController {

    private final MealRecommendService mealRecommendService;
    private final ShowAllMenuService showAllMenuService;

    private static int reject_cnt = 0;
    private static int approve_cnt = 0;

    @Value(value = "${slack.channel.stage}")
    String stageChannel;

    @GetMapping("/api/v1/meal")
    public void getMeal() {
        mealRecommendService.getMenu();
    }

    @PostMapping("/api/v1/meal")
    public Lunch setMeal() {
        return new Lunch("in_channel", Restaurant.getAllRestaurants());
    }

    @PostMapping("/api/v1/menu")
    public void getAllMenu() throws IOException {
        showAllMenuService.getAllMenu();
    }

    @PostMapping("/api/v1/recommend/menu")
    public void getRecommendMenu() throws IOException {
        mealRecommendService.getRecommendMenu();
    }

    @RequestMapping(value = "/api/v1/meal/approve", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void approve(@RequestParam String payload) throws IOException {
        // Json String -> BlockActionPayload 변경
        BlockActionPayload blockActionPayload =
                GsonFactory.createSnakeCase()
                        .fromJson(payload, BlockActionPayload.class);

// Block 수정
        blockActionPayload.getMessage().getBlocks().remove(0);

        List<BlockActionPayload.Action> actions = blockActionPayload.getActions();


        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(0).getActionId().equals("action_reject") && reject_cnt < 2 && approve_cnt <= 2) {
                reject_cnt++;
                System.out.println("a = " + reject_cnt);
                String m = "추천메뉴를 *승인* " + approve_cnt + " *거부* " + reject_cnt + "하였습니다.";
                blockActionPayload.getMessage().getBlocks().add(0,
                        section(section ->
                                section.text(markdownText(m))
                        )
                );
            } else if (actions.get(0).getActionId().equals("action_reject") && reject_cnt >= 2 && approve_cnt <= 2) {
                reject_cnt++;
                mealRecommendService.getRecommendMenu();
                reject_cnt = 0;
                approve_cnt = 0;
            }

            if (actions.get(0).getActionId().equals("action_approve") && approve_cnt < 2 && reject_cnt <= 2) {
                approve_cnt++;
                System.out.println("a = " + approve_cnt);
                String m = "추천메뉴를 *승인* " + approve_cnt + " *거부* " + reject_cnt + "하였습니다.";
                blockActionPayload.getMessage().getBlocks().add(0,
                        section(section ->
                                section.text(markdownText(m))
                        )
                );
            } else if (actions.get(0).getActionId().equals("action_approve") && approve_cnt >= 2 && reject_cnt <= 2) {
                approve_cnt++;
                System.out.println(blockActionPayload.getActions());
                System.out.println(blockActionPayload.getMessage());
                ActionResponse response =
                        ActionResponse.builder()
                                .replaceOriginal(false)
                                .text("성공")
                                .build();
                Slack slack = Slack.getInstance();
                ActionResponseSender sender = new ActionResponseSender(slack);
                sender.send(blockActionPayload.getResponseUrl(), response);
                return;
            }
        }

//        blockActionPayload.getActions().forEach(action -> {
//
//
//            if (action.getActionId().equals("action_reject")) {
//                blockActionPayload.getMessage().getBlocks().add(0,
//                        section(section ->
//                                section.text(markdownText("추천메뉴를 *거부* 하였습니다."))
//                        )
//                );
//                try {
//                    mealRecommendService.getRecommendMenu();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                blockActionPayload.getMessage().getBlocks().add(0,
//                        section(section ->
//                                section.text(markdownText("추천메뉴를 *승인* " + approve_cnt +"하였습니다."))
//                        )
//                );
//            }
//        });
        ActionResponse response =
                ActionResponse.builder()
                        .replaceOriginal(true)
                        .blocks(blockActionPayload.getMessage().getBlocks())
                        .build();
        Slack slack = Slack.getInstance();
        ActionResponseSender sender = new ActionResponseSender(slack);
        sender.send(blockActionPayload.getResponseUrl(), response);
    }
}
