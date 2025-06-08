package net.malevy.edaorder.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@ConfigurationPropertiesBinding
public class UriConverter implements Converter<String, URI> {
    @Override
    public URI convert(String s) {
        if (!StringUtils.hasText(s)) return null;

        var uri = URI.create(s);
        if (! uri.isAbsolute()) {
            uri = UriComponentsBuilder
                    .fromUri(uri)
                    .scheme("http")
                    .build()
                    .toUri();
        }

        return uri;
    }
}
