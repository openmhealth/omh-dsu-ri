/*
 * Copyright 2015 Open mHealth
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

package utils;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author Emerson Farrugia
 */
public class ValidationSummary {

    private AtomicInteger attempted = new AtomicInteger();
    private AtomicInteger succeeded = new AtomicInteger();
    private AtomicInteger failed = new AtomicInteger();

    public void incrementAttempted() {
        attempted.incrementAndGet();
    }

    public int getAttempted() {
        return attempted.get();
    }

    public void incrementSucceeded() {
        succeeded.incrementAndGet();
    }

    public int getSucceeded() {
        return succeeded.get();
    }

    public void incrementFailed() {
        failed.incrementAndGet();
    }

    public int getFailed() {
        return failed.get();
    }
}
