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

package org.openmhealth.dsu.validation;

import org.openmhealth.dsu.domain.DataPointSearchCriteria;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * @author Emerson Farrugia
 */
public class DataPointSearchCriteriaValidator
        implements ConstraintValidator<ValidDataPointSearchCriteria, DataPointSearchCriteria> {

    @Override
    public void initialize(ValidDataPointSearchCriteria constraintAnnotation) {
    }

    // TODO add constraint violations if valuable
    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean isValid(DataPointSearchCriteria value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        // TODO decide if any range queries are required and implement checks for reversed bounds
        // if (!value.getCreationTimestampRange().hasLowerBound()
        //         && !value.getCreationTimestampRange().hasUpperBound()
        //         && !value.getEffectiveTimestampRange().hasLowerBound()
        //         && !value.getEffectiveTimestampRange().hasUpperBound(
        //
        //     return false;
        // }

        return true;
    }
}