# Problem 1: Database Transaction Rollback System

## Problem Statement

You are tasked with implementing a simplified database transaction system that supports rollback operations. The database stores key-value pairs organized in tables (rows), and you need to handle the following operations:

1. **UPDATE(table, key, value)** - Update a key-value pair in a table
2. **DELETE(table)** - Delete an entire table
3. **BEGIN()** - Start a new transaction (save current state)
4. **COMMIT()** - Commit the current transaction (remove savepoint)
5. **ROLLBACK()** - Rollback to the last savepoint

## Input Format

- First line contains `n` (1 ≤ n ≤ 10^5) - number of operations
- Next `n` lines contain operations in the format:
  - `UPDATE table_name key value`
  - `DELETE table_name`
  - `BEGIN`
  - `COMMIT`
  - `ROLLBACK`
  - `QUERY table_name key` - Query a value (for output)

## Output Format

For each `QUERY` operation, output the value if it exists, otherwise output `NULL`.

## Constraints

- Table names and keys are strings (max length 20)
- Values are integers (-10^9 ≤ value ≤ 10^9)
- At most 1000 nested transactions
- All strings contain only alphanumeric characters

## Sample Input

```
12
UPDATE user name alice
UPDATE user age 25
QUERY user name
BEGIN
UPDATE user age 30
DELETE user
QUERY user name
ROLLBACK
QUERY user name
QUERY user age
COMMIT
QUERY user name
```

## Sample Output

```
alice
NULL
alice
25
alice
```

## Explanation

1. Create user table with name="alice", age=25
2. Query user.name → "alice"
3. Begin transaction (save state)
4. Update user.age to 30, then delete user table
5. Query user.name → NULL (table deleted)
6. Rollback to saved state (user table restored)
7. Query user.name → "alice", user.age → 25
8. Commit transaction (remove savepoint)
9. Query user.name → "alice" (state preserved)

## Time Complexity Expected

O(n * m) where n is number of operations and m is average data size per operation.

## Tags

`Hash Map` `Stack` `State Management` `Design Patterns`
