package net.malevy.edaorder.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URI;

@Component
@ConfigurationPropertiesBinding
public class UriConverter implements Converter<String, URI> {
    @Override
    public URI convert(String s) {
        if (StringUtils.isEmpty(s)) return null;

        return URI.create(s);
    }
}
