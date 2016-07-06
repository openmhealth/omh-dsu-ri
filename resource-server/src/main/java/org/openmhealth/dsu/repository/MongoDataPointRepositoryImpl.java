/*
 * Copyright 2016 Open mHealth
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

import com.github.rutledgepaulv.qbuilders.builders.GeneralQueryBuilder;
import com.github.rutledgepaulv.qbuilders.conditions.Condition;
import com.github.rutledgepaulv.qbuilders.structures.FieldPath;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.rqe.pipes.DefaultArgumentConversionPipe;
import com.github.rutledgepaulv.rqe.pipes.QueryConversionPipeline;
import com.github.rutledgepaulv.rqe.resolvers.MongoPersistentEntityFieldTypeResolver;
import com.google.common.collect.Range;
import org.openmhealth.dsu.domain.DataPointSearchCriteria;
import org.openmhealth.schema.domain.omh.DataPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Nullable;
import java.time.OffsetDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.BoundType.CLOSED;
import static org.springframework.data.mongodb.core.query.Criteria.where;


/**
 * @author Emerson Farrugia
 */
public class MongoDataPointRepositoryImpl implements DataPointSearchRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;


    private QueryConversionPipeline pipeline = QueryConversionPipeline.builder()
            .useNonDefaultArgumentConversionPipe(DefaultArgumentConversionPipe
                    .builder()
                    .useNonDefaultFieldResolver(new MongoPersistentEntityFieldTypeResolver() {
                        @Override
                        public Class<?> apply(FieldPath path, Class<?> root) {
                            return Object.class;
                        }
                    })
                    .build()
            ).build();



    // if a data point is filtered by its data and not just its header, these queries will need to be written using
    // the MongoDB Java driver instead of Spring Data MongoDB, since there is no mapping information to work against
    @Override
    public Iterable<DataPoint> findBySearchCriteria(String queryFilter, DataPointSearchCriteria searchCriteria, @Nullable Integer offset,
                                                    @Nullable Integer limit) {


        checkNotNull(searchCriteria);
        checkArgument(offset == null || offset >= 0);
        checkArgument(limit == null || limit >= 0);

        Query query = newQuery(queryFilter, searchCriteria);

        if (offset != null) {
            query.skip(offset);
        }

        if (limit != null) {
            query.limit(limit);
        }

        return mongoOperations.find(query, DataPoint.class);
    }

    private void maybeAddFilter(String queryFilter, Query query) {

        if (queryFilter != null) {
            Condition<GeneralQueryBuilder> condition = pipeline.apply(queryFilter.replace("&&", ";").replace("||", ","), DataPoint.class);
            query.addCriteria(condition.query(new MongoVisitor()));
        }

    }

    private Query newQuery(String queryFilter, DataPointSearchCriteria searchCriteria) {

        Query query = new Query();
        maybeAddFilter(queryFilter, query);

        query.addCriteria(where("header.user_id").is(searchCriteria.getUserId()));
        query.addCriteria(where("header.schema_id.namespace").is(searchCriteria.getSchemaNamespace()));
        query.addCriteria(where("header.schema_id.name").is(searchCriteria.getSchemaName()));

        searchCriteria.getSchemaVersion().ifPresent(schemaVersion -> {

            query.addCriteria(where("header.schema_id.version.major").is(schemaVersion.getMajor()));
            query.addCriteria(where("header.schema_id.version.minor").is(schemaVersion.getMinor()));

            if (schemaVersion.getQualifier().isPresent()) {
                query.addCriteria(where("header.schema_id.version.qualifier").is(schemaVersion.getQualifier().get()));
            }
            else {
                query.addCriteria(where("header.schema_id.version.qualifier").exists(false));
            }
        });

        addCreationTimestampCriteria(query, searchCriteria.getCreationTimestampRange());

        return query;
    }

    void addCreationTimestampCriteria(Query query, Range<OffsetDateTime> timestampRange) {

        if (timestampRange.hasLowerBound() || timestampRange.hasUpperBound()) {

            Criteria timestampCriteria = where("header.creation_date_time");

            if (timestampRange.hasLowerBound()) {
                if (timestampRange.lowerBoundType() == CLOSED) {
                    timestampCriteria = timestampCriteria.gte(timestampRange.lowerEndpoint());
                }
                else {
                    timestampCriteria = timestampCriteria.gt(timestampRange.lowerEndpoint());
                }
            }

            if (timestampRange.hasUpperBound()) {
                if (timestampRange.upperBoundType() == CLOSED) {
                    timestampCriteria = timestampCriteria.lte(timestampRange.upperEndpoint());
                }
                else {
                    timestampCriteria = timestampCriteria.lt(timestampRange.upperEndpoint());
                }
            }

            query.addCriteria(timestampCriteria);
        }
    }
}