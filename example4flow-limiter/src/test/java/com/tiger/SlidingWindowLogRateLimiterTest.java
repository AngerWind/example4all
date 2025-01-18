package com.tiger;

import java.time.Clock;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/12
 * @description
 */
public class SlidingWindowLogRateLimiterTest {

    @Test
    void tryAcquire_burstyTraffic_acceptsAllRequestsWithinRateLimitThresholds() {
        Clock clock = mock(Clock.class);
        when(clock.millis()).thenReturn(0L, 999L, 1000L,
                1001L, 1002L, 1999L, 2000L);

        SlidingWindowLogRateLimiter limiter
                = new SlidingWindowLogRateLimiter(2, 1000, clock);

        // 0 seconds passed
        assertTrue(limiter.tryAcquire(),
                "request 1 at timestamp=0 must pass");
        assertTrue(limiter.tryAcquire(),
                "request 2 at timestamp=999 must pass");

        // 1 second passed
        assertFalse(limiter.tryAcquire(),
                "request 3 at timestamp=1000 must not be tryAcquire");
        assertTrue(limiter.tryAcquire(),
                "request 4 at timestamp=1001 must pass, because request 1" +
                        " at timestamp=0 is outside the current sliding window [1; 1001]");
        assertFalse(limiter.tryAcquire(),
                "request 5 at timestamp=1002 must not be tryAcquire");
        assertFalse(limiter.tryAcquire(),
                "request 6 at timestamp=1999 must not be tryAcquire");

        // 2 seconds passed
        assertTrue(limiter.tryAcquire(),
                "request 7 at timestamp=2000 must pass, because request 2" +
                        " at timestamp=999 is outside the current sliding window [1000; 2000]");
    }
}