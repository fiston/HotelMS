package net.mklew.hotelms.inhouse.web.rest;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import net.mklew.hotelms.inhouse.web.dto.RateDto;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

/**
 * @author Marek Lewandowski <marek.m.lewandowski@gmail.com>
 * @since 11/25/12
 * time 1:26 PM
 */
@Provider
@Produces("application/json")
public class JAXBJSONContextResolver implements ContextResolver<JAXBContext>
{
    private JAXBContext context;
    public JAXBJSONContextResolver() throws Exception {
        JSONConfiguration.MappedBuilder b = JSONConfiguration.mapped();
        b.rootUnwrapping(true);
        b.arrays("ratedto");
        context = new JSONJAXBContext(b.build(), RateDto.class);
    }
    @Override
    public JAXBContext getContext(Class<?> objectType) {
        return context;
    }
}