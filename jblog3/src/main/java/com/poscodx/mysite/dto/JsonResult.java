package com.poscodx.mysite.dto;

public class JsonResult {

  private String result; // "success" or "fail"
  private String message; // if fail, set
  private Object data; // if success, set

  // 파라미터가 뭐냐에 따라 success, fail인지
  private JsonResult(Object data) {
    result = "success";
    this.data = data;
  }

  private JsonResult(String message) {
    result = "fail";
    this.message = message;
  }

  public static JsonResult success(Object data) {
    return new JsonResult(data);
  }

  public static JsonResult fail(String message) {
    return new JsonResult(message);
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
