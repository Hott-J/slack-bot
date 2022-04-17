package tmax.fintech.slackbot.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Restaurant {
    규돈("규돈"),
    김밥킹("김밥킹"),
    달인해장국("달인해장국"),
    동선참치("동선참치"),
    랄라샌드위치("랄라샌드위치"),
    맥도날드("맥도날드"),
    명가("명가"),
    별미("별미"),
    서브웨이("서브웨이"),
    어반("어반"),
    올라술고기("올라술고기"),
    올라회덮밥("올라회덮밥"),
    울엄마집밥("울엄마집밥"),
    유가네닭갈비("유가네닭갈비"),
    유메노키친("유메노키친"),
    조이스키친("조이스키친"),
    중화요리셰셰("중화요리 셰셰"),
    포메인("포메인"),
    포첸하우스("포첸하우스"),
    호프호프("호프호프"),
    히노아지("히노아지");

    private String text;

    public static String getAllRestaurants() {
        return 규돈.getText() + "\n" + 김밥킹.getText() + "\n" + 달인해장국.getText() + "\n" + 동선참치.getText() + "\n" + 랄라샌드위치.getText() +
                "\n" + 맥도날드.getText() + "\n" + 명가.getText() + "\n" + 별미.getText() + "\n" + 서브웨이.getText() + "\n" + 어반.getText() +
                "\n" + 올라술고기.getText() + "\n" + 올라회덮밥.getText() + "\n" + 울엄마집밥.getText() + "\n" + 유가네닭갈비.getText() +
                "\n" + 유메노키친.getText() + "\n" + 조이스키친.getText() + "\n" + 중화요리셰셰.getText() + "\n" + 포메인.getText() +
                "\n" + 포첸하우스.getText() + "\n" + 호프호프.getText() + "\n" + 히노아지.getText();
    }
}
