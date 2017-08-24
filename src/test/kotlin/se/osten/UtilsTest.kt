package se.osten

import org.junit.Test
import se.osten.utils.createGuid
import kotlin.test.assertEquals

class UtilsTest {

    @Test
    fun shouldCreateGuidWithFourDashes(){
        assertEquals(createGuid().count { v -> v == '-' }, 4, "Should have four dashes")
    }
}
