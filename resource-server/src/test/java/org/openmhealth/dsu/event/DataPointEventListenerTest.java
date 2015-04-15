package org.openmhealth.dsu.event;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.openmhealth.dsu.domain.Subscription;
import org.openmhealth.dsu.domain.SubscriptionBuilder;
import org.openmhealth.dsu.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by kkujovic on 4/14/15.
 */
public class DataPointEventListenerTest {

    private DataPointEventListener listener;
    private SubscriptionService subscriptionService;
    private RestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        subscriptionService = mock(SubscriptionService.class);
        restTemplate = mock(RestTemplate.class);
        listener = new DataPointEventListener();
        ReflectionTestUtils.setField(listener, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(listener, "subscriptionService", subscriptionService);
    }

    @Test
    public void testOnApplicationEvent() throws Exception {
        //sample data point event
        DataPointEvent evt = new DataPointEvent(this, "userid", "datapointid", DataPointEvent.DataPointEventType.CREATE, "2015-04-15T00:00:00.000Z");

        //sample subscription
        SubscriptionBuilder builder = new SubscriptionBuilder();
        Subscription subscription = builder.setCallbackUrl("http://localhost:8080").setUserId(evt.getUserId()).build();
        List<Subscription> subscriptions = Lists.newArrayList(subscription);

        //setup mocks
        when(subscriptionService.findByUserId(evt.getUserId())).thenReturn(subscriptions);
        ResponseEntity<Notification> entity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.postForEntity(any(String.class), any(Notification.class), (Class<Notification>) any(Class.class))).thenReturn(entity);

        listener.onApplicationEvent(evt);

        verify(subscriptionService, times(1)).findByUserId(evt.getUserId());
        verifyNoMoreInteractions(subscriptionService);

        verify(restTemplate, times(1)).postForEntity(any(String.class), any(Notification.class), (Class<Notification>) any(Class.class));
        verifyNoMoreInteractions(restTemplate);

    }
}