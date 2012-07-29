package com.autentia.tutorial.websockets.messages;

import java.util.List;

public class ConnectionInfoMessage {

    private final ConnectionInfo connectionInfo;

    public ConnectionInfoMessage(String user, List<String> activeUsers) {
        this.connectionInfo = new ConnectionInfo(user, activeUsers);
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    class ConnectionInfo {

        private final String user;

        private final List<String> activeUsers;

        private ConnectionInfo(String user, List<String> activeUsers) {
            this.user = user;
            this.activeUsers = activeUsers;
        }

        public String getUser() {
            return user;
        }

        public List<String> getActiveUsers() {
            return activeUsers;
        }
    }

}
