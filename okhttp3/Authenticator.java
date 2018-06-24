package okhttp3;

import java.io.IOException;

public interface Authenticator {
    public static final Authenticator NONE = new C10821();

    static class C10821 implements Authenticator {
        C10821() {
        }

        public Request authenticate(Route route, Response response) {
            return null;
        }
    }

    Request authenticate(Route route, Response response) throws IOException;
}
