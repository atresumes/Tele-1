package com.sinch.android.rtc.internal.service.pubnub;

import com.sinch.gson.JsonArray;
import com.sinch.gson.JsonElement;
import com.sinch.gson.JsonParseException;
import com.sinch.gson.JsonParser;
import java.util.ArrayList;

public class PubNubResponse {
    private boolean isValid;
    private ArrayList<String> messages;
    private Long timeToken;

    public class Builder {
        private boolean isValid;
        private ArrayList<String> messages = new ArrayList();
        private Long timeToken;

        public Builder addMessage(String str) {
            this.messages.add(str);
            return this;
        }

        public PubNubResponse build() {
            return new PubNubResponse(this.timeToken, this.isValid, this.messages);
        }

        public Builder setIsValid(boolean z) {
            this.isValid = z;
            return this;
        }

        public Builder setTimeToken(Long l) {
            this.timeToken = l;
            return this;
        }
    }

    public PubNubResponse(Long l, boolean z, ArrayList<String> arrayList) {
        this.timeToken = l;
        this.isValid = z;
        this.messages = arrayList;
    }

    public static PubNubResponse parse(String str) {
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
            if (asJsonArray.size() != 2) {
                return builder.setIsValid(false).build();
            }
            builder.setTimeToken(Long.valueOf(asJsonArray.get(1).getAsLong()));
            JsonArray asJsonArray2 = asJsonArray.get(0).getAsJsonArray();
            for (int i = 0; i < asJsonArray2.size(); i++) {
                builder.addMessage(asJsonArray2.get(i).getAsString());
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

    public ArrayList<String> getMessages() {
        return this.messages;
    }

    public String[] getMessagesAsArray() {
        String[] strArr = new String[this.messages.size()];
        this.messages.toArray(strArr);
        return strArr;
    }

    public Long getTimeToken() {
        return this.timeToken;
    }

    public boolean isValid() {
        return this.isValid;
    }
}
