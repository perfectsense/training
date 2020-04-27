package bex.training.countdown;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.ImmutableMap;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.RoutingFilter;

@RoutingFilter.Path("/countdown-endpoint")
public class CoutdownApiStub extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(ObjectUtils.toJson(ImmutableMap.of(
                "shape", ThreadLocalRandom.current().nextDouble(Double.MIN_VALUE, 10.0)
        )));
    }
}
