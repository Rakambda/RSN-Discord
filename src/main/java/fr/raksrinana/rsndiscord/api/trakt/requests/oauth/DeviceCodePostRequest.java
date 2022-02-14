package fr.raksrinana.rsndiscord.api.trakt.requests.oauth;

import fr.raksrinana.rsndiscord.api.trakt.TraktApi;
import fr.raksrinana.rsndiscord.api.trakt.model.auth.DeviceCode;
import fr.raksrinana.rsndiscord.api.trakt.requests.ITraktPostRequest;
import kong.unirest.core.GenericType;
import kong.unirest.core.RequestBodyEntity;
import kong.unirest.core.Unirest;
import kong.unirest.core.json.JSONObject;
import org.jetbrains.annotations.NotNull;

public class DeviceCodePostRequest implements ITraktPostRequest<DeviceCode>{
	@Override
	@NotNull
	public GenericType<DeviceCode> getOutputType(){
		return new GenericType<>(){};
	}
	
	@Override
	@NotNull
	public RequestBodyEntity getRequest(){
		return Unirest.post(TraktApi.API_URL + "/oauth/device/code").body(getBody());
	}
	
	@NotNull
	public static JSONObject getBody(){
		var data = new JSONObject();
		data.put("client_id", TraktApi.getClientId());
		return data;
	}
}
