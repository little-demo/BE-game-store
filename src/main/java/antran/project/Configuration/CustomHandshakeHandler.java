package antran.project.Configuration;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        URI uri = request.getURI();
        String query = uri.getQuery(); // Ex: "username=quochuy1"

        if (query != null && query.startsWith("username=")) {
            String username = query.split("=")[1];
            System.out.println("✅ WebSocket kết nối từ username: " + username);
            return () -> username;
        }
        return () -> "anonymous";
    }
}
