package model;

public class Yut {

  private final double[] probability = {
      0.0625, // 백도
      0.1875, // 도
      0.375,  // 개
      0.25,   // 걸
      0.0625, // 윷
      0.0625  // 모
  };

  private final YutResult[] results = {
      YutResult.백도,
      YutResult.도,
      YutResult.개,
      YutResult.걸,
      YutResult.윷,
      YutResult.모
  };

  // 랜덤으로 윷 결과 반환
  public YutResult yutThrowRandom() {
    double rand = Math.random(); // 0.0 ~ 1.0
    double cumulative = 0.0;

    for (int i = 0; i < probability.length; i++) {
      cumulative += probability[i];
      if (rand < cumulative) {
        return results[i];
      }
    }
    // 정밀도 문제 대비
    return results[results.length - 1];
  }

  // 고정된 결과 반환 (테스트용)
  public YutResult yutThrowFixed(String name) {
    return YutResult.valueOf(name);
  }
}