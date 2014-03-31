#MongoDB Java EE 6 utils
**_Maven Dependencies_**<br/>
```
<dependency>
    <groupId>com.github.wayis.framework</groupId>
    <artifactId>javaee6-extensions-mongodb</artifactId>
    <version>1.0.0</version>
</dependency>
```
## Version 1.0.0
**_DBConnection_**<br/>
EJB singleton that handles the connection to MongoDB database.<br/>
All properties are searched into a mongodb.properties bundle file:

* mongodb.host: The host of the mongodb server. (Default value: 'localhost').</li>
* mongodb.port: The port of the mongodb server. (Default value: '27017').</li>
* mongodb.dbname: The mongodb database to use. If the database does not exist, it will be created after the construction of this singleton EJB class.</li>

Example to use it in an API REST Resource with JAX-RS:

```java
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.github.wayis.framework.javaee.extensions.mongodb.DBConnection;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST API to manage users.<br/>
 * <ul>
 * <li><i>/users : GET</i> => Gets all users in JSON format.</li>
 * </ul>
 * Example of users in JSON format :<br/>
 * <pre>
 * {
 *      "lastname": "DOE",
 *      "firstname": "John"
 * }
 * </pre>
 */
@Stateless
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    /**
     * MongoDB connection provider.
     */
    @Inject
    private DBConnection dbConnection;
    /**
     * Users collection.
     */
    private DBCollection users;

    @PostConstruct
    public void init() {
        users = dbConnection.getCollection("users");
    }

    /**
     * Finds all users.
     *
     * @return List of DBObject represents users.
     */
    @GET
    public List<DBObject> findAll() {
        return users.find().toArray();
    }
}
```