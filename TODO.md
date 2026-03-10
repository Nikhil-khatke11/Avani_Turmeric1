# TODO List for Fixing Cart Backend Issues

## 1. Standardize Database USER in ViewCartServlet
- Change USER from "AVANI_DB" to "avani_db" to match other servlets. ✅ Done

## 2. Update ViewCartServlet to Handle Multiple Cart Items
- Modify JSON output to return an array of cart items instead of a single object. ✅ Done

## 3. Standardize Database USER in PlaceOrderServlet
- Change DB_USER from "AVANI_DB" to "avani_db" to match other servlets. ✅ Done

## 4. Update PlaceOrderServlet to Handle Multiple Cart Items
- Modify order placement logic to process all items in the cart, not just one. ✅ Done

## 5. Test the Changes
- Verify that adding to cart and viewing cart works correctly.
