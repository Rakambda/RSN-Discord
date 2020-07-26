package fr.raksrinana.rsndiscord.utils.giphy.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@NoArgsConstructor
public class Meta{
	@JsonProperty("msg")
	private String message;
	@JsonProperty("status")
	private int status;
	@JsonProperty("response_id")
	private String responseId;
}
