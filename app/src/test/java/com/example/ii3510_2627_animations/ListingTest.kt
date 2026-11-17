package com.example.ii3510_2627_animations

import org.junit.Assert.*
import org.junit.Test

class ListingTest {
    @Test fun listingKeysAreUnique() {
        assertEquals(sampleListings.size, sampleListings.map { it.id }.toSet().size)
    }
    @Test fun filtersReturnOnlyTheirCategory() {
        listingCategories.drop(1).forEach { category ->
            val result = filterListings(category)
            assertTrue(result.isNotEmpty())
            assertTrue(result.all { it.category == category })
        }
        assertEquals(sampleListings, filterListings("All"))
    }
    @Test fun priceSortingPreservesFilteredItems() {
        val sorted = displayListings("Tech", true)
        assertEquals(filterListings("Tech").map { it.id }.toSet(), sorted.map { it.id }.toSet())
        assertEquals(sorted.map { it.price }.sorted(), sorted.map { it.price })
        assertEquals(sampleListings, displayListings("All", false))
    }
    @org.junit.Ignore("Enable after completing TODO 1a")
    @Test fun entranceTimingIsBounded() {
        assertEquals(0L, entranceDelay(0, false))
        assertEquals(70L, entranceDelay(1, false))
        assertEquals(350L, entranceDelay(999, false))
        assertEquals(0L, entranceDelay(999, true))
    }
}
