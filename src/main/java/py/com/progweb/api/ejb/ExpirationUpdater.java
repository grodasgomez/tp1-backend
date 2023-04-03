package py.com.progweb.api.ejb;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that updates the expiration date of the points of the clients
 */
@Stateless
public class ExpirationUpdater {
    private Logger logger = LoggerFactory.getLogger(ExpirationUpdater.class);

    @Inject
    private PointBagDAO pointBagDAO;

    @Schedule(second = "*/4", minute = "*", hour = "*")
    public void execute() {
        logger.info("Updating expired points");

        pointBagDAO.updateExpiredPoints();
    }
}