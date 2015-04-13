package org.openmhealth.dsu.service;

import org.openmhealth.dsu.domain.Subscription;
import org.openmhealth.dsu.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by kkujovic on 4/12/15.
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Optional<Subscription> findOne(String id) {

        checkNotNull(id);
        checkArgument(!id.isEmpty());

        return subscriptionRepository.findOne(id);
    }

    @Override
    public Iterable<Subscription> findByUserId(String userId) {


        checkNotNull(userId);
        checkArgument(!userId.isEmpty());

        return subscriptionRepository.findByUserId(userId);

    }

    @Override
    public Subscription save(Subscription subscription) {

        checkNotNull(subscription);

        return subscriptionRepository.save(subscription);

    }

    @Override
    public Iterable<Subscription> findByUserIdAndCallbackUrl(String userId, String callbackUrl) {
        return subscriptionRepository.findByUserIdAndCallbackUrl(userId, callbackUrl);
    }

    @Override
    public void delete(String id) {
        subscriptionRepository.delete(id);
    }

    @Override
    public Long deleteByIdAndUserId(String id, String endUserId) {
        return subscriptionRepository.deleteByIdAndUserId(id, endUserId);
    }
}
