package es.udc.ws.app.socialnetwork.my;

import net.sf.json.JSONObject;

class MySocialNetworkError {
    private String details;
    private String error;
    private String message;
    private String path;
    private long status;
    private long timestamp;

    public MySocialNetworkError(JSONObject json) {
        try {
            details = json.getString("details");
        } catch (Exception e) {
            details = "";
        }

        error = json.getString("error");
        message = json.getString("message");
        path = json.getString("path");
        status = json.getLong("status");

        try {
            timestamp = json.getLong("timestamp");
        } catch (Exception e) {
            timestamp = 0;
        }
    }

    public String getDetails() {
        return details;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public long getStatus() {
        return status;
    }

    public long getTimestamp() {
        return timestamp;
    }
}