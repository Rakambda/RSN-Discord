package fr.rakambda.rsndiscord.spring.api.trakt.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RenewTokenRequest{
	@JsonProperty("client_id")
	private String clientId;
	@JsonProperty("client_secret")
	private String clientSecret;
	@JsonProperty("grant_type")
	private String grantType;
	@JsonProperty("redirect_uri")
	private String redirectUri;
	@JsonProperty("refresh_token")
	private String refreshToken;
}
