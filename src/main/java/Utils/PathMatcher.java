package Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import java.util.Map;

public class PathMatcher extends AntPathMatcher {
    private final Logger LOGGER = LoggerFactory.getLogger(PathMatcher.class);

    @Override
    protected boolean doMatch(String pattern, String path, boolean fullMatch, Map<String, String> uriTemplateVariables) {
        LOGGER.info(pattern + " -- " + path);
        return super.doMatch(pattern.toLowerCase(), path.toLowerCase(), fullMatch, uriTemplateVariables);
    }
}
