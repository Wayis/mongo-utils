package fr.wayis.framework.javaee.extensions.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import fr.wayis.framework.javaee.extensions.api.config.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.net.UnknownHostException;

/**
 * EJB singleton that handles the connection to MongoDB database.<br/>
 * All properties are searched into a mongodb.properties bundle file:
 * <ul>
 * <li>mongodb.host: The host of the mongodb server. (Default value: 'localhost').</li>
 * <li>mongodb.port: The port of the mongodb server. (Default value: '27017').</li>
 * <li>mongodb.dbname: The mongodb database to use. If the database does not exist, it will be created after the construction of this singleton EJB class.</li>
 * </ul>
 * <p/>
 * According to MongoDB Java driver documentation, there should be only one instance of {@link MongoClient} in a Web application.
 *
 * @see <a href="http://docs.mongodb.org/ecosystem/drivers/java-concurrency/#java-driver-concurrency">http://docs.mongodb.org/ecosystem/drivers/java-concurrency/#java-driver-concurrency</a>
 */
@Singleton
public class DBConnection {

    private DB database;

    /**
     * MongoDB server address.<br/>
     * Default value: 'localhost'.
     */
    @Inject
    @ConfigProperty(bundle = "mongodb", key = "mongodb.host", defaultValue = "localhost")
    private String host;
    /**
     * MongoDB port.<br/>
     * Default value: '27017'.
     */
    @Inject
    @ConfigProperty(bundle = "mongodb", key = "mongodb.port", defaultValue = "27017")
    private Integer port;
    /**
     * MongoDB database to use.<br/>
     * If the database does not exist, it will be created after the construction of this singleton EJB class
     */
    @Inject
    @ConfigProperty(bundle = "mongodb", key = "mongodb.dbname", mandatory = true)
    private String dbName;

    @PostConstruct
    public void afterCreate() throws UnknownHostException {
        MongoClient client = new MongoClient(this.host, this.port);
        this.database = client.getDB(this.dbName);
    }

    /**
     * Get a collection from mongodb database.
     *
     * @param name the collection name. If null, collection will be created to database.
     * @return the related collection
     */
    public DBCollection getCollection(final String name) {
        return this.database.getCollection(name);
    }
}