package com.myththewolf.MCCBukkit.lib;

import org.json.JSONObject;

public class SocketResult {
  private StatusType status;
  private String raw;

  public SocketResult(JSONObject res) {
    raw = res.toString();
    switch (res.getString("status")) {
      case "TIMEDOUT":
        status = StatusType.TIMED_OUT;
        break;
      case "BADREQUEST":
        status = StatusType.BAD_REQUEST;
        break;
      case "EMPTYRESPONSE":
        status = StatusType.EMPTY_RESPONSE;
        break;
      case "BADRESPONSE":
        status = StatusType.BAD_RESPONSE;
        break;
      case "OK":
        status = StatusType.SUCCESS;
        break;
    }
  }

  public StatusType getStatus() {
    return status;
  }

  public String getRawResult() {
    return this.raw;
  }
}
