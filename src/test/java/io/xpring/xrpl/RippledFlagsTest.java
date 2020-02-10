package io.xpring.xrpl;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RippledFlagsTest {
    @Test
    public void testCheckContainsValue() {
        // GIVEN a set of flags that contains the tfPartialPayment flag.
        Integer flags = RippledFlags.TF_PARTIAL_PAYMENT.value | 1 | 4; // 1 and 4 are arbitrarily chosen numbers.

        // WHEN the presence of tfPartialPayment is checked THEN the flag is reported as present.
        assertTrue(RippledFlags.check(RippledFlags.TF_PARTIAL_PAYMENT, flags));
    }

    @Test
    public void testCheckDoesNotContainsValue() {
        // GIVEN a set of flags that does not contain the tfPartialPayment flag.
        Integer flags = 1 | 4; // 1 and 4 are arbitrarily chosen numbers.

        // WHEN the presence of tfPartialPayment is checked THEN the flag is reported as not present.
        assertFalse( RippledFlags.check(RippledFlags.TF_PARTIAL_PAYMENT, flags));
    }
}