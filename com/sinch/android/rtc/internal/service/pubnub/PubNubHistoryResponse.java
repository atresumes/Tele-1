package com.sinch.android.rtc.internal.service.pubnub;

import com.sinch.gson.JsonArray;
import com.sinch.gson.JsonElement;
import com.sinch.gson.JsonObject;
import com.sinch.gson.JsonParseException;
import com.sinch.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;

public class PubNubHistoryResponse {
    private long end;
    private boolean isValid;
    private List<PubNubMessage> messages;
    private long start;

    public class Builder {
        private long end;
        private boolean isValid;
        private List<PubNubMessage> messages = new ArrayList();
        private long start;

        public Builder addMessage(String str, Long l) {
            this.messages.add(new PubNubMessage(str, l));
            return this;
        }

        public PubNubHistoryResponse build() {
            return new PubNubHistoryResponse(this.start, this.end, this.isValid, this.messages);
        }

        public Builder setEnd(Long l) {
            this.end = l.longValue();
            return this;
        }

        public Builder setIsValid(boolean z) {
            this.isValid = z;
            return this;
        }

        public Builder setStart(Long l) {
            this.start = l.longValue();
            return this;
        }
    }

    public PubNubHistoryResponse(long j, long j2, boolean z, List<PubNubMessage> list) {
        this.start = j;
        this.end = j2;
        this.isValid = z;
        this.messages = list;
    }

    public static PubNubHistoryResponse parse(String str) {
        Builder builder = new Builder();
        if (str == null) {
            return builder.setIsValid(false).build();
        }
        try {
            JsonElement parse = new JsonParser().parse(str);
            if (!parse.isJsonArray()) {
                return builder.setIsValid(false).build();
            }
            JsonArray asJsonArray = parse.getAsJsonArray();
            if (asJsonArray.size() < 3) {
                return builder.setIsValid(false).build();
            }
            builder.setStart(Long.valueOf(asJsonArray.get(1).getAsLong()));
            builder.setEnd(Long.valueOf(asJsonArray.get(2).getAsLong()));
            JsonArray asJsonArray2 = asJsonArray.get(0).getAsJsonArray();
            for (int i = 0; i < asJsonArray2.size(); i++) {
                JsonObject asJsonObject = asJsonArray2.get(i).getAsJsonObject();
                builder.addMessage(asJsonObject.get("message").getAsString(), Long.valueOf(asJsonObject.get("timetoken").getAsLong()));
            }
            return builder.setIsValid(true).build();
        } catch (JsonParseException e) {
            return builder.setIsValid(false).build();
        } catch (ClassCastException e2) {
            return builder.setIsValid(false).build();
        } catch (IllegalStateException e3) {
            return builder.setIsValid(false).build();
        } catch (NumberFormatException e4) {
            return builder.setIsValid(false).build();
        }
    }

    public String getEndTimeTokenAsString() {
        return String.valueOf(this.end);
    }

    public int getMessageCount() {
        return this.messages.size();
    }

    public List<PubNubMessage> getMessages() {
        return this.messages;
    }

    public String[] getMessagesAsArray() {
        String[] strArr = new String[this.messages.size()];
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = ((PubNubMessage) this.messages.get(i)).getMessage();
        }
        return strArr;
    }

    public String getStartTimeTokenAsString() {
        return String.valueOf(this.start);
    }

    public String[] getTimeTokensAsArray() {
        String[] strArr = new String[this.messages.size()];
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = String.valueOf(((PubNubMessage) this.messages.get(i)).getTimeToken());
        }
        return strArr;
    }

    public boolean isValid() {
        return this.isValid;
    }
}
