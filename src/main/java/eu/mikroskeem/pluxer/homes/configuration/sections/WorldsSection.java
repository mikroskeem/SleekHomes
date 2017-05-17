package eu.mikroskeem.pluxer.homes.configuration.sections;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
public class WorldsSection {
    @Setting(value = "world-groups", comment = "World groups")
    public Map<String, List<String>> groups = Collections.singletonMap("survival-group",
            Arrays.asList("surv", "surv_nether", "surv_the_end"));
}
