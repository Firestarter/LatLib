package latmod.lib;

import com.google.gson.JsonElement;

/**
 * Created by LatvianModder on 03.01.2016.
 */
public interface IJsonObject
{
	public void setJson(JsonElement e);
	public JsonElement getJson();
}