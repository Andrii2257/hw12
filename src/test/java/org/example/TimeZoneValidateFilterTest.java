package org.example;

import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Test;

public class TimeZoneValidateFilterTest {
    @Test
    public void testValidTimezone() {
        TimeZoneValidateFilter filter = new TimeZoneValidateFilter();
        Assert.assertTrue(filter.isValidTimeZone("UTC+2"));
        Assert.assertTrue(filter.isValidTimeZone("UTC-5"));
        Assert.assertTrue(filter.isValidTimeZone("UTC+0"));
        Assert.assertTrue(filter.isValidTimeZone("GMT+2"));
        Assert.assertTrue(filter.isValidTimeZone("UT+3"));
    }

    @Test
    public void testInvalidTimezone() {
        TimeZoneValidateFilter filter = new TimeZoneValidateFilter();
        assertFalse(filter.isValidTimeZone("InvalidTimezone"));
        assertFalse(filter.isValidTimeZone("UTC+20"));
        assertFalse(filter.isValidTimeZone("GMT-25"));
        assertFalse(filter.isValidTimeZone("UT-30"));
    }
}
