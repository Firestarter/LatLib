package com.latmod.lib.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.latmod.lib.util.LMFileUtils;
import com.latmod.lib.util.LMStringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Type for Lists: new TypeToken<List<E>>() {}.getType()
 */
@ParametersAreNullableByDefault
public class LMJsonUtils
{
    public static final JsonDeserializationContext DESERIALIZATION_CONTEXT;
    public static final JsonSerializationContext SERIALIZATION_CONTEXT, PRETTY_SERIALIZATION_CONTEXT;
    public static final Gson GSON;
    public static final Gson GSON_PRETTY;

    static
    {
        GsonBuilder gb = new GsonBuilder();
        GSON = gb.create();
        gb.setPrettyPrinting();
        GSON_PRETTY = gb.create();

        DESERIALIZATION_CONTEXT = new JsonDeserializationContext()
        {
            @Override
            public <T> T deserialize(JsonElement json, Type typeOfT) throws JsonParseException
            {
                return GSON.fromJson(json, typeOfT);
            }
        };

        SERIALIZATION_CONTEXT = new JsonSerializationContext()
        {
            @Override
            public JsonElement serialize(Object src)
            {
                return GSON.toJsonTree(src);
            }

            @Override
            public JsonElement serialize(Object src, Type typeOfSrc)
            {
                return GSON.toJsonTree(src, typeOfSrc);
            }
        };

        PRETTY_SERIALIZATION_CONTEXT = new JsonSerializationContext()
        {
            @Override
            public JsonElement serialize(Object src)
            {
                return GSON_PRETTY.toJsonTree(src);
            }

            @Override
            public JsonElement serialize(Object src, Type typeOfSrc)
            {
                return GSON_PRETTY.toJsonTree(src, typeOfSrc);
            }
        };
    }

    @Nonnull
    public static String toJson(@Nonnull Gson gson, JsonElement e)
    {
        return gson.toJson(e == null ? JsonNull.INSTANCE : e);
    }

    public static boolean toJson(@Nonnull Gson gson, @Nonnull File f, JsonElement o)
    {
        try
        {
            String s = toJson(gson, o);
            LMFileUtils.save(f, s);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    @Nonnull
    public static String toJson(JsonElement o)
    {
        return toJson(GSON, o);
    }

    public static boolean toJson(@Nonnull File f, JsonElement o)
    {
        return toJson(GSON_PRETTY, f, o);
    }

    @Nonnull
    public static JsonElement fromJson(String json)
    {
        return (json == null || json.isEmpty()) ? JsonNull.INSTANCE : new JsonParser().parse(json);
    }

    @Nonnull
    public static JsonElement fromJson(Reader json)
    {
        return (json == null) ? JsonNull.INSTANCE : new JsonParser().parse(json);
    }

    @Nonnull
    public static JsonElement fromJson(File json)
    {
        try
        {
            if(json == null || !json.exists())
            {
                return JsonNull.INSTANCE;
            }
            BufferedReader reader = new BufferedReader(new FileReader(json));
            JsonElement e = fromJson(reader);
            reader.close();
            return e;
        }
        catch(Exception ex)
        {
            return JsonNull.INSTANCE;
        }
    }

    // -- //

    @Nonnull
    public static JsonElement toIntArray(int... ai)
    {
        if(ai == null)
        {
            return JsonNull.INSTANCE;
        }

        JsonArray a = new JsonArray();
        if(ai.length == 0)
        {
            return a;
        }

        for(int anAi : ai)
        {
            a.add(new JsonPrimitive(anAi));
        }

        return a;
    }

    @Nullable
    public static int[] fromIntArray(JsonElement e)
    {
        if(e == null || e.isJsonNull())
        {
            return null;
        }

        if(e.isJsonArray())
        {
            JsonArray a = e.getAsJsonArray();
            int[] ai = new int[a.size()];
            if(ai.length == 0)
            {
                return ai;
            }
            for(int i = 0; i < ai.length; i++)
            {
                ai[i] = a.get(i).getAsInt();
            }
            return ai;
        }

        return new int[] {e.getAsInt()};
    }

    @Nonnull
    public static JsonElement toNumberArray(Number[] ai)
    {
        if(ai == null)
        {
            return JsonNull.INSTANCE;
        }

        JsonArray a = new JsonArray();
        if(ai.length == 0)
        {
            return a;
        }

        for(Number anAi : ai)
        {
            a.add(new JsonPrimitive(anAi));
        }

        return a;
    }

    @Nullable
    public static Number[] fromNumberArray(JsonElement e)
    {
        if(e == null || e.isJsonNull())
        {
            return null;
        }

        if(e.isJsonArray())
        {
            JsonArray a = e.getAsJsonArray();
            Number[] ai = new Number[a.size()];
            if(ai.length == 0)
            {
                return ai;
            }
            for(int i = 0; i < ai.length; i++)
            {
                ai[i] = a.get(i).getAsNumber();
            }
            return ai;
        }

        return new Number[] {e.getAsNumber()};
    }

    @Nonnull
    public static JsonElement toStringArray(String... ai)
    {
        if(ai == null)
        {
            return JsonNull.INSTANCE;
        }

        JsonArray a = new JsonArray();
        if(ai.length == 0)
        {
            return a;
        }

        for(String anAi : ai)
        {
            a.add(new JsonPrimitive(anAi));
        }

        return a;
    }

    @Nullable
    public static String[] fromStringArray(JsonElement e)
    {
        if(e == null || e.isJsonNull() || !e.isJsonArray())
        {
            return null;
        }
        JsonArray a = e.getAsJsonArray();
        String[] ai = new String[a.size()];
        if(ai.length == 0)
        {
            return ai;
        }
        for(int i = 0; i < ai.length; i++)
        {
            ai[i] = a.get(i).getAsString();
        }
        return ai;
    }

    @Nonnull
    public static List<JsonElement> deserializeText(List<String> text)
    {
        List<JsonElement> elements = new ArrayList<>();

        if(text == null || text.isEmpty())
        {
            return elements;
        }

        StringBuilder sb = new StringBuilder();
        int inc = 0;

        for(String s : text)
        {
            s = LMStringUtils.trimAllWhitespace(s);

            System.out.println(s);

            if(s.isEmpty())
            {
                elements.add(JsonNull.INSTANCE);
            }
            else
            {
                if(inc > 0 || s.startsWith("{"))
                {
                    for(int i = 0; i < s.length(); i++)
                    {
                        char c = s.charAt(i);
                        if(c == '{')
                        {
                            inc++;
                        }
                        else if(c == '}')
                        {
                            inc--;
                        }
                        sb.append(c);

                        if(inc == 0)
                        {
                            System.out.println(":: " + sb);
                            elements.add(fromJson(sb.toString()));
                            sb.setLength(0);
                        }
                    }
                }
                else
                {
                    elements.add(new JsonPrimitive(s));
                }
            }
        }

        return elements;
    }
}