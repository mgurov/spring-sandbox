package com.mycompany.app.topo;

public class Subscription {
    public final Topic topic;
    public final Queue queue;

    public Subscription(Topic topic, Queue queue) {
        this.topic = topic;
        this.queue = queue;
    }

    public String describe() {
        return topic.name +
                " -> " + queue.name;
    }
}
