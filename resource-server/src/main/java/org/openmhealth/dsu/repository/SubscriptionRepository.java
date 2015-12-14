package org.openmhealth.dsu.repository;

import org.openmhealth.dsu.domain.DataPoint;
import org.openmhealth.dsu.domain.Subscription;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * A repository of subscriptions.
 *
 * Created by kkujovic on 4/12/15.
 */
public interface SubscriptionRepository extends Repository<Subscription, String> {
    boolean exists(String id);

    Optional<Subscription> findOne(String id);

    Iterable<Subscription> findByUserId(String userId);

    Iterable<Subscription> findByUserIdAndCallbackUrl(String userId, String callbackUrl);

    Subscription save(Subscription subscription);

    void delete(String id);

    Long deleteByIdAndUserId(String id, String userId);
}
