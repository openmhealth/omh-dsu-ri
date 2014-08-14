/*
 * Copyright 2014 Open mHealth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openmhealth.dsu.repository;

import org.openmhealth.dsu.domain.DataPoint;
import org.openmhealth.dsu.domain.DataPointSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Nullable;

import static org.springframework.data.mongodb.core.query.Criteria.where;


/**
 * @author Emerson Farrugia
 */
public class MongoDataPointRepositoryImpl implements CustomDataPointRepository {

    @Autowired
    private MongoOperations mongoOperations;


    @Override
    public Iterable<DataPoint> findBySearchCriteria(DataPointSearchCriteria searchCriteria, @Nullable Integer offset,
            @Nullable Integer limit) {

        Query query = newQuery(searchCriteria);

        if (offset != null) {
            query.skip(offset);
        }

        if (limit != null) {
            query.limit(limit);
        }

        return mongoOperations.find(query, DataPoint.class);
    }

    // FIXME timestamp checks are kludged for now
    // TODO implement this using QueryDSL once its integration with Gradle stabilises
    private Query newQuery(DataPointSearchCriteria searchCriteria) {

        Query query = new Query();

        if (searchCriteria.getStartTimestamp().isPresent()) {
            query.addCriteria(where("tempTimestamp").gte(searchCriteria.getStartTimestamp().get()));
        }

        if (searchCriteria.getEndTimestamp().isPresent()) {
            query.addCriteria(where("tempTimestamp").lt(searchCriteria.getEndTimestamp().get()));
        }

        return query;
    }
}
