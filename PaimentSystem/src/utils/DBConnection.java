// PaimentSystem/src/utils/DBConnection.java
package utils;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {
    private static String url;
    private static String user;
    private static String password;

    static {
        Properties props = new Properties();
        String resourceName = "db.properties";
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL locatedAt = cl.getResource(resourceName);

        try (InputStream in = cl.getResourceAsStream(resourceName)) {
            if (in == null) {
                String where = (locatedAt == null) ? "(introuvable sur le classpath)" : locatedAt.toString();
                throw new IllegalStateException("Fichier 'db.properties' introuvable sur le classpath. Localisation: " + where);
            }
            props.load(in);
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");

            System.out.println("[DBConnection] db.properties chargé depuis: " + locatedAt);
            System.out.println("[DBConnection] db.url=" + url);
            System.out.println("[DBConnection] db.user=" + user);

            if (url == null || user == null) {
                throw new IllegalStateException("Propriétés db.url et db.user obligatoires.");
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Erreur lecture 'db.properties': " + e.getMessage());
        }
    }

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
