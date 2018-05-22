package fr.mrcraftcod.gunterdiscord.settings;

import fr.mrcraftcod.gunterdiscord.commands.SetConfigCommand;
import fr.mrcraftcod.gunterdiscord.utils.Actions;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import java.awt.*;
import java.io.InvalidClassException;
import java.util.LinkedList;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com)
 *
 * @author Thomas Couchoud
 * @since 2018-04-15
 */
public abstract class ValueConfiguration extends Configuration
{
	private Object lastValue = null;
	
	@Override
	public SetConfigCommand.ActionResult handleChange(MessageReceivedEvent event, SetConfigCommand.ChangeConfigType action, LinkedList<String> args)
	{
		if(action == SetConfigCommand.ChangeConfigType.SHOW)
		{
			EmbedBuilder builder = new EmbedBuilder();
			builder.setAuthor(event.getAuthor().getName(), null, event.getAuthor().getAvatarUrl());
			builder.setColor(Color.GREEN);
			builder.setTitle("Valeur de " + getName());
			builder.addField("", getObject(event.getGuild()).toString(), false);
			Actions.reply(event, builder.build());
			return SetConfigCommand.ActionResult.NONE;
		}
		if(args.size() < 1)
			return SetConfigCommand.ActionResult.ERROR;
		switch(action)
		{
			case SET:
				setValue(event.getGuild(), args.poll());
				return SetConfigCommand.ActionResult.OK;
		}
		return SetConfigCommand.ActionResult.OK;
	}
	
	/**
	 * Get the value as an integer.
	 *
	 * @param defaultValue The default value to return if none was found.
	 *
	 * @return The string.
	 *
	 * @throws IllegalArgumentException If this configuration isn't a value.
	 * @throws InvalidClassException    If this configuration isn't an integer.
	 */
	public int getInt(Guild guild, int defaultValue) throws InvalidClassException
	{
		Object value = lastValue == null ? getObject(guild) : lastValue;
		if(value == null)
		{
			setValue(guild, defaultValue);
			return defaultValue;
		}
		if(value instanceof Integer)
			return (Integer) value;
		throw new InvalidClassException("Config is not a long: " + value.getClass().getSimpleName());
	}
	
	/**
	 * Get the value as a long.
	 *
	 * @param defaultValue The default value to return if none was found.
	 *
	 * @return The string.
	 *
	 * @throws IllegalArgumentException If this configuration isn't a value.
	 * @throws InvalidClassException    If this configuration isn't a long.
	 */
	public long getLong(Guild guild, long defaultValue) throws InvalidClassException
	{
		Object value = lastValue == null ? getObject(guild) : lastValue;
		if(value == null)
		{
			setValue(guild, defaultValue);
			return defaultValue;
		}
		if(value instanceof Long)
			return (Long) value;
		throw new InvalidClassException("Config is not a long: " + value.getClass().getSimpleName());
	}
	
	@Override
	public ConfigType getType()
	{
		return ConfigType.VALUE;
	}
	
	/**
	 * Get the value as an object.
	 *
	 * @return The object or null if not found.
	 *
	 * @throws IllegalArgumentException If this configuration isn't a value.
	 */
	protected Object getObject(Guild guild) throws IllegalArgumentException
	{
		if(getType() != ConfigType.VALUE)
			throw new IllegalArgumentException("Not a value config");
		return Settings.getObject(guild, getName());
	}
	
	/**
	 * Set the value.
	 *
	 * @param value the value to set.
	 */
	public void setValue(Guild guild, Object value)
	{
		lastValue = value;
		Settings.setValue(guild, this, value);
	}
	
	/**
	 * Get the value as a string.
	 *
	 * @param defaultValue The default value to return if none was found.
	 *
	 * @return The string.
	 *
	 * @throws IllegalArgumentException If this configuration isn't a value.
	 * @throws InvalidClassException    If this configuration isn't a string.
	 */
	public String getString(Guild guild, String defaultValue) throws InvalidClassException, IllegalArgumentException
	{
		Object value = lastValue == null ? getObject(guild) : lastValue;
		if(value == null)
		{
			setValue(guild, defaultValue);
			return defaultValue;
		}
		if(value instanceof String)
			return (String) value;
		throw new InvalidClassException("Config is not a string: " + value.getClass().getSimpleName());
	}
	
	/**
	 * Get the value as a long.
	 *
	 * @return The long.
	 *
	 * @throws IllegalArgumentException If this configuration isn't a value.
	 * @throws InvalidClassException    If this configuration isn't a string.
	 * @throws NoValueDefinedException  If no value exists for this config.
	 */
	public long getLong(Guild guild) throws InvalidClassException, IllegalArgumentException, NoValueDefinedException
	{
		Object value = lastValue == null ? getObject(guild) : lastValue;
		if(value == null)
			throw new NoValueDefinedException(this);
		if(value instanceof Long)
			return (Long) value;
		throw new InvalidClassException("Config is not a long: " + value.getClass().getSimpleName());
	}
	
	/**
	 * Get the value as a string.
	 *
	 * @return The string.
	 *
	 * @throws IllegalArgumentException If this configuration isn't a value.
	 * @throws InvalidClassException    If this configuration isn't a string.
	 * @throws NoValueDefinedException  If no value exists for this config.
	 */
	public String getString(Guild guild) throws InvalidClassException, IllegalArgumentException, NoValueDefinedException
	{
		Object value = lastValue == null ? getObject(guild) : lastValue;
		if(value == null)
			throw new NoValueDefinedException(this);
		if(value instanceof String)
			return (String) value;
		throw new InvalidClassException("Config is not a string: " + value.getClass().getSimpleName());
	}
	
	@Override
	public boolean isActionAllowed(SetConfigCommand.ChangeConfigType action)
	{
		return action == SetConfigCommand.ChangeConfigType.SET || action == SetConfigCommand.ChangeConfigType.SHOW;
	}
}
