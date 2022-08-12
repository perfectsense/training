---
title: Debugging
parent: Meal Exercise
nav_order: 6
permalink: /meal-exercise/debugging.html
---

# Debugging

If you don’t see what you’re expecting and are unsure what’s the cause, a great tool for debugging is the same `?_renderer=json` technique we used earlier to test the backend without the frontend in place. This allows you to determine whether the issue is caused by a backend or frontend issue. If the json is correct, then the backend is doing its job and the issue is on the frontend; if the json is wrong then it’s a backend issue.
