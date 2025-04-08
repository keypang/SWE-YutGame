package model;

import java.util.ArrayList;
import java.util.Random;

public class Yut {

    ArrayList<Integer> status = new ArrayList<>();

    double[] probability = {
            0.0625, // 백도
            0.25,   // 도
            0.375,  // 개
            0.25,   // 걸
            0.0625, // 윷
            0.0625  // 모
    };

    // 누적확률 방식으로 확률 계산
    public int yutThrowRandom() {
        double rand = Math.random(); // 0.0 ~ 1.0
        double cumulative = 0.0;

        for (int i = 0; i < probability.length; i++) {
            cumulative += probability[i];
            if (rand < cumulative) {
                status.add(i);
                return i;
            }
        }
        // 정밀도 문제 대비
        status.add(probability.length - 1);
        return probability.length - 1;
    }

    public int yutThrowFixed(){
        Random rand = new Random();
        int randomInt = rand.nextInt(6);
        status.add(randomInt);
        return randomInt;
    }
}
