package fr.rakambda.rsndiscord.spring.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
@EnableRabbit
public class AmqpConfiguration{
	private final AmqpNameProvider amqpNameProvider;
	
	@Autowired
	public AmqpConfiguration(AmqpNameProvider amqpNameProvider){
		this.amqpNameProvider = amqpNameProvider;
	}
	
	@Bean
	@Qualifier("delayQueue")
	public Queue delayQueue(){
		return new Queue(amqpNameProvider.getQueueDelayName(), true);
	}
	
	@Bean
	@Qualifier("delayExchange")
	public CustomExchange delayExchange(){
		var args = Map.<String, Object> of("x-delayed-type", "direct");
		return new CustomExchange(amqpNameProvider.getExchangeDelayName(), "x-delayed-message", true, false, args);
	}
	
	@Bean
	public Binding delayBinding(@Qualifier("delayQueue") Queue testeQueue, @Qualifier("delayExchange") Exchange exchange){
		return BindingBuilder.bind(testeQueue).to(exchange).with(amqpNameProvider.getRoutingKeyDelay()).noargs();
	}
}
