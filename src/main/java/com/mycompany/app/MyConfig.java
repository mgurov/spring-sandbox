package com.mycompany.app;

import com.mycompany.app.topo.Queue;
import com.mycompany.app.topo.Subscription;
import com.mycompany.app.topo.Topic;
import com.mycompany.app.topo.Topology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class MyConfig {
    @Autowired
    private GenericWebApplicationContext context;

    @Bean
    public String makeMyService() {
        context.registerBean(MyService.class, () -> new MyService("ok"));
        return "ok";
    }

    @Bean
    public String makeSomethingElseDependentOnMyService(MyService myService) {
        return "got:" + myService.getValue();
    }

    @Bean
    public Subscription simpleSubscription() {
        Topic topic = new Topic("simple producer");
        context.registerBean("simpleProducer", Topic.class, () -> topic);
        Queue queue = new Queue("simple consumer");
        context.registerBean(queue.name, Queue.class, () -> queue);
        return new Subscription(topic, queue);
    }

    @PostConstruct
    public void fork() {
        Topic topic = new Topic("common producer");
        context.registerBean("commonProducer", Topic.class, () -> topic);

        Queue queue1 = new Queue("consumer 1");
        context.registerBean(queue1.name, Queue.class, () -> queue1);
        Subscription subs1 = new Subscription(topic, queue1);
        context.registerBean("f1", Subscription.class, () -> subs1);

        Queue queue2 = new Queue("consumer 2");
        context.registerBean(queue2.name, Queue.class, () -> queue2);
        Subscription subs2 = new Subscription(topic, queue2);
        context.registerBean("f2", Subscription.class, () -> subs2);
    }

    @Bean
    public Topology topology(List<Subscription> subs) {
        return new Topology(subs.stream().map(Subscription::describe).collect(Collectors.toList()));
    }


}
