package com.challenge.tuiter.infraestructura.timeline.redis;

public enum ClaveDeTipo {
  TUIT {
    @Override
    public String con(String id) {
      return "tuit:" + id;
    }
  }, TIMELINE {
    @Override
    public String con(String id) {
      return "timeline:" + id;
    }
  };

  public abstract String con(String id);
}
