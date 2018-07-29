package com.mycompany.app;

import com.mycompany.app.topo.Queue;
import com.mycompany.app.topo.Subscription;
import com.mycompany.app.topo.Topic;
import com.mycompany.app.topo.Topology;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class BeanRegistrationIntegrationTest {
    @Autowired
    private GenericWebApplicationContext context;

    @Test
    public void testSomething() {
        String configured = context.getBean("makeSomethingElseDependentOnMyService", String.class);

        assertEquals("got:ok", configured);

    }

    @Test
    public void testSimpleSubs() {
        Subscription simpleSubscription = context.getBean("simpleSubscription", Subscription.class);

        assertEquals("simple producer -> simple consumer", simpleSubscription.toString());

    }

    @Test
    public void testForkOneSide() {
        Subscription simpleSubscription = context.getBean("fork", Subscription.class);

        assertEquals("common producer -> consumer 2", simpleSubscription.describe());

    }

    @Autowired
    public List<Topic> topics;

    @Test
    public void testTopicsPicked() {
        assertThat(topics).extracting("name")
                .containsExactlyInAnyOrder("simple producer", "common producer");

    }

    @Autowired
    public List<Subscription> subs;

    @Test
    public void testSubsPicked() {
        assertThat(subs).extracting(Subscription::describe)
                .containsExactlyInAnyOrder("simple producer -> simple consumer", "common producer -> consumer 1", "common producer -> consumer 2");

    }

    @Autowired
    public List<Queue> qs;

    @Test
    public void testQsPicked() {
        assertThat(qs).extracting("name")
                .containsExactlyInAnyOrder("simple consumer", "consumer 1", "consumer 2");

    }

    @Autowired
    public Topology topology;

    @Test
    public void testTopology() {
        assertThat(topology.data)
                .containsExactlyInAnyOrder("simple producer -> simple consumer", "common producer -> consumer 1", "common producer -> consumer 2");

    }


}
