package art.navastudio.mozahhemgir

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NumberNormalizerTest {
    @Test fun normalizesIranianInternationalForms() {
        assertEquals("09421234567", NumberNormalizer.toIranDomestic("+989421234567"))
        assertEquals("09421234567", NumberNormalizer.toIranDomestic("00989421234567"))
        assertEquals("09421234567", NumberNormalizer.toIranDomestic("989421234567"))
        assertEquals("09421234567", NumberNormalizer.toIranDomestic("9421234567"))
    }

    @Test fun preservesDomesticNumber() {
        assertEquals("09121234567", NumberNormalizer.toIranDomestic("09121234567"))
    }

    @Test fun rejectsEmptyValues() {
        assertNull(NumberNormalizer.toIranDomestic(null))
        assertNull(NumberNormalizer.toIranDomestic(""))
        assertNull(NumberNormalizer.normalizePrefix("abc"))
    }

    @Test fun prefixMustBeAtLeastFourDomesticDigits() {
        assertNull(NumberNormalizer.normalizePrefix("09"))
        assertEquals("0942", NumberNormalizer.normalizePrefix("+98942"))
    }
}
