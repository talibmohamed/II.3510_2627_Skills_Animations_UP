# Exercise: animated transitions and progressive listings

## Learning goals

Animate values from UI state, coordinate a bounded stagger, preserve item identity when lists change, and implement directional transitions between two screens.

## Task 1 - Progressive entrance (8-10 minutes)

In `Listing.kt`, finish `entranceDelay(index, reducedMotion)`. Expected start times for indices 0..7 are `0, 70, 140, 210, 280, 350, 350, 350` milliseconds. All start times must be zero with reduced motion. The existing effect takes differences between these start times; do not add the full cumulative delay at each iteration.

In `CampusDemo.kt`, animate `progress` from the current value to `1f` or `0f`, based on `visible`. Use 280 ms normally and 0 ms with reduced motion. The provided graphicsLayer already maps progress to opacity and upward movement.

Keep reveal state above LazyColumn. Scrolling and returning from details must not replay the entrance. Remove `@Ignore` from `entranceTimingIsBounded` and run the unit tests.

## Task 2 - List changes (5 minutes)

At TODO 2, animate item insertion, removal and placement. Keep `key = { it.id }`. Suggested durations: 180 ms fade-in, 300 ms movement, 160 ms fade-out. Use 0 ms for reduced motion.

Select Books, then All. Toggle Sort by price. Existing cards should keep their identity while moving to the correct positions.

## Task 3 - Screen transitions (7-10 minutes)

At TODO 3, combine a fade with a horizontal slide. Details should enter from the right. Returning to the list should reverse direction. Suggested durations: 240 ms fade-in, 300 ms slide-in, 160 ms fade-out, 240 ms slide-out. A short slide of one sixth of the container width is enough.

Use the AnimatedContent target state to choose the direction. Keep both incoming and outgoing content available during the transition. Respect reduced motion and keep the existing BackHandler.

## Task 4 - Expanding information (5 minutes)

At TODO 4, combine expandVertically with fadeIn and shrinkVertically with fadeOut. Use roughly 220-280 ms. The chevron rotation already works. With reduced motion, opening and closing must happen immediately.

## Acceptance checklist

- Replay shows a short load followed by a staggered entrance.
- Later items do not accumulate an unbounded delay.
- Filtering and sorting animate without changing which listing a card represents.
- Opening details and returning use opposite slide directions.
- Category selection, favourite state, and list position survive the round trip.
- Pickup details expand and collapse without clipping their text.
- Reduced motion makes all task animations instant. Simulated data loading remains.
- Rapid taps do not crash the app.

## Suggested assessment (10 points)

Progressive timing and opacity/translation: 3. Stable list animations: 2. Directional screen transitions: 3. Expanding content and reduced motion: 2.

## References and hints

- Animate a value: https://developer.android.com/develop/ui/compose/animation/value-based
- List keys and animateItem: https://developer.android.com/develop/ui/compose/lists
- AnimatedContent and AnimatedVisibility: https://developer.android.com/develop/ui/compose/animation/composables-modifiers

Look at FavouriteButton and the animated listing count for examples of state-driven animation. The completed solution is not included in this starter folder.
