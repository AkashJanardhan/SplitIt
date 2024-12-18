# SplitIt

SplitIt is a simple Android application (inspired by Splitwise) that helps you manage groups of people, their shared expenses, and how to settle up at the end. All data is stored locally using an SQLite database—no external servers or accounts required.

## Key Features

### Manage Groups
- **Create Groups:** Add new groups with custom names.
- **View Groups:** See all groups in a neatly formatted list with clear dividers and padding.
- **Delete Groups:** Remove entire groups (including their members and expenses) using a convenient delete icon.

### Add and Manage Members
- **Add Members:** Add members to any group, providing their name and optionally selecting a profile image from your device’s gallery.
- **Display Member Images:** Each member’s image is displayed next to their name in the group’s member list, making identification easy.

### Record Expenses
- **Add Expenses:** For each group, record expenses by specifying the amount, a description, and who paid for it (selected by member name, no manual ID entry required).
- **Clear Expense Info:** Expenses display both the amount and the payer’s name for transparency.
- **Neat Formatting:** The expenses list includes padding and separators for better readability.

### Settle Up
- **Fair Share Calculation:** The “Settle Up” page calculates each member’s share of the total expenses.
- **Minimal Transactions:** A smart simplifying algorithm determines the least number of payments needed to balance all debts, using transitive properties to minimize the number of transactions.
- **Who Owes Whom:** Instead of showing positive or negative balances, the page details who owes whom and how much, making it simple to settle accounts.

### Local-Only Data
- **Offline & Private:** All data (groups, members, expenses) is stored locally on your device’s SQLite database.
- **No Accounts Needed:** No external servers or accounts are required. Your data remains private and accessible offline.

## Getting Started
1. **Create a Group:** From the main screen, tap **“Add Group”**, enter a name, and confirm.
2. **Add Members:** In a group’s detail screen, tap **“Add Member”**, enter the member’s name, and optionally select an image from the gallery.
3. **Add Expenses:** Within a group, tap **“Add Expense”**, specify the amount, description, and choose the member who paid.
4. **View Balances & Settle Up:** Tap **“Settle Up”** to view suggested minimal transactions to clear all debts.

## Additional Notes
- **Schema Changes:** The database schema is managed by incrementing the database version and recreating tables as needed.  
- **Resetting Data:** Since it’s a local-only app, uninstalling or clearing app data resets all groups, members, and expenses.
- **Future Enhancements:** Potential future improvements include more robust error handling, a refined UX for image selection, and additional UI polish.

---
