package latmod.lib.json;

import com.google.gson.JsonElement;

/**
 * Created by LatvianModder on 03.01.2016.
 */
public interface IJsonObject
{
	void setJson(JsonElement e);
	JsonElement getJson();
}