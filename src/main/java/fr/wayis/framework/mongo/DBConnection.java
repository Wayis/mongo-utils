package fr.wayis.framework.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import fr.wayis.framework.jee6.property.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.net.UnknownHostException;

/**
 * EJB that handles the connection to MongoDB database. According to MongoDB
 * Java driver documentation, there should be only one instance of
 * {@link MongoClient} in a Web application @see <a href=
 * "http://docs.mongodb.org/ecosystem/drivers/java-concurrency/#java-driver-concurrency"
 * >http://docs.mongodb.org/ecosystem/drivers/java-concurrency/#java-driver-
 * concurrency</a> If config property mongodb.dbname value is an un-existing
 * database, it will be created after the construction of this singleton EJB
 * class.
 */
@Singleton
public class DBConnection {

    private DB database;
    /**
     * MongoDB server address.
     */
    @Inject
    @ConfigProperty(key = "mongodb.host", defaultValue = "localhost")
    private String host;
    /**
     * MongoDB port.
     */
    @Inject
    @ConfigProperty(key = "mongodb.port", defaultValue = "27017")
    private Integer port;
    /**
     * MongoDB database to use.
     */
    @Inject
    @ConfigProperty(key = "mongodb.dbname", mandatory = true)
    private String dbName;

    @PostConstruct
    public void afterCreate() throws UnknownHostException {
        MongoClient client = new MongoClient(this.host, this.port);
        this.database = client.getDB(this.dbName);
    }

    /**
     * Get a collection from mongodb database.
     *
     * @param name
     *            the collection name. If null, collection will be created to
     *            database.
     * @return the related collection
     */
    public DBCollection getCollection(final String name) {
        return this.database.getCollection(name);
    }
}