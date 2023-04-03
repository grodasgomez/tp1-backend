package py.com.progweb.api.ejb;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that updates the expiration date of the points of the clients
 */
@Stateless
public class ExpirationUpdater {
    private Logger logger = LoggerFactory.getLogger(ExpirationUpdater.class);

    @Schedule(second = "*/4", minute = "*", hour = "*")
    public void execute() {
        logger.info("Hello World! from ExpirationUpdater");
        // TODO implement the logic to update the expiration date of the points of the clients
    }
}