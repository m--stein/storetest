package org.storetest;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("test")
public class CacheAccess {

    private final static Logger logger = Logger.getLogger(CacheAccess.class.getName());

    @Resource(lookup = "java:jboss/infinispan/container/teststore")
    protected CacheContainer container;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response test() {

        Response.ResponseBuilder builder = Response.ok();

        try {
            Cache<String, Long> cache = container.getCache("test");
            Long incValue = cache.getOrDefault("value", new Long(0));

            logger.info(String.format("Value: %d", incValue));
            builder.entity(String.format("Value: %d", incValue));

            cache.put("value", new Long(incValue.longValue() + 1));
        }
        catch (Exception ex) {
            logger.log(Level.SEVERE, "Test failed ;-)", ex);
        }

        return builder.build();
    }
}
