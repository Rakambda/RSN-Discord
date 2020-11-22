package fr.raksrinana.rsndiscord.modules.settings.guild;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.raksrinana.rsndiscord.modules.settings.ICompositeConfiguration;
import fr.raksrinana.rsndiscord.modules.settings.types.RoleConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static java.util.Optional.ofNullable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class RandomKickConfiguration implements ICompositeConfiguration {
    @JsonProperty("randomKickRolesPing")
    @Getter
    @Setter
    private Set<RoleConfiguration> randomKickRolesPing = new HashSet<>();
    @JsonProperty("kickedRole")
    @Setter
    private RoleConfiguration kickedRole;
    @JsonProperty("kickableRoles")
    @Getter
    @Setter
    private Set<RoleConfiguration> kickableRoles = new HashSet<>();
    @JsonProperty("kickRoleProbability")
    @Getter
    @Setter
    private double kickRoleProbability = 0.25;

    public Optional<RoleConfiguration> getKickedRole() {
        return ofNullable(kickedRole);
    }
}
