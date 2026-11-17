package com.example.ii3510_2627_animations

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class DemoTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    private fun awaitListings() {
        compose.waitUntil(5_000) {
            compose.onAllNodesWithTag("listing-1").fetchSemanticsNodes().isNotEmpty()
        }
        compose.mainClock.advanceTimeBy(1_000)
        compose.waitForIdle()
    }

    @Test fun filterDetailsAndBackKeepCategory() {
        awaitListings()
        compose.onNodeWithTag("category-Books").performClick()
        compose.mainClock.advanceTimeBy(600)
        compose.onNodeWithTag("listing-1").assertIsDisplayed().performClick()
        compose.mainClock.advanceTimeBy(600)
        compose.onNodeWithTag("details").assertIsDisplayed()
        compose.onNodeWithText("Listed by Alex").assertExists()
        compose.onNodeWithTag("back").performClick()
        compose.mainClock.advanceTimeBy(600)
        compose.onNodeWithTag("category-Books").assertIsSelected()
        compose.onNodeWithTag("listing-1").assertIsDisplayed()
        compose.onNodeWithTag("listing-2").assertDoesNotExist()
    }

    @Test fun favouritesPersistBetweenListAndDetailsAndPickupExpands() {
        awaitListings()
        compose.onNodeWithTag("favourite-1").performClick().assertIsOn()
        compose.onNodeWithTag("listing-1").performClick()
        compose.mainClock.advanceTimeBy(600)
        compose.onNodeWithTag("favourite-1").assertIsOn().performClick().assertIsOff()
        compose.onNodeWithTag("pickup-toggle").performScrollTo().performClick()
        compose.mainClock.advanceTimeBy(600)
        compose.onNodeWithTag("pickup-info").assertExists()
        compose.onNodeWithTag("pickup-toggle").performClick()
        compose.mainClock.advanceTimeBy(600)
        compose.onNodeWithTag("pickup-info").assertDoesNotExist()
        compose.onNodeWithTag("back").performScrollTo().performClick()
        compose.mainClock.advanceTimeBy(600)
        compose.onNodeWithTag("favourite-1").assertIsOff()
    }

    @Test fun sortMovesCheapestListingToTopAndTogglesBack() {
        awaitListings()
        compose.onNodeWithTag("sort").performClick()
        compose.mainClock.advanceTimeBy(600)
        compose.onNodeWithTag("listing-10").assertIsDisplayed()
        compose.onNodeWithTag("sort").performClick()
        compose.mainClock.advanceTimeBy(600)
        compose.onNodeWithTag("listing-1").assertIsDisplayed()
    }

    @Test fun replayWorksWithReducedMotion() {
        awaitListings()
        compose.onNodeWithTag("reduced-motion").performClick()
        compose.onNodeWithTag("reduced-motion").assertIsOn()
        compose.onNodeWithTag("replay").performClick()
        awaitListings()
        compose.onNodeWithTag("listing-1").assertIsEnabled().performClick()
        compose.onNodeWithTag("details").assertIsDisplayed()
        compose.onNodeWithTag("back").performClick()
        compose.onNodeWithTag("listing-1").assertIsDisplayed()
    }
}
