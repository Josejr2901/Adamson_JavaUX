/* 

    ✅ What You Need to Do
    1. Add a method to load login status from file:

    private void loadLockStatus() {
        File lockFile = new File("lock_status.txt");
        if (!lockFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(lockFile))) {
            String[] parts = reader.readLine().split(",");
            if (parts.length >= 4) {
                String savedUsername = parts[0];
                blockTime = Long.parseLong(parts[1]);
                BLOCK_DURATION = Long.parseLong(parts[2]);
                failedAttempts = Integer.parseInt(parts[3]);
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    Call loadLockStatus() during application startup or inside your constructor/class that initializes the login page.

-----------------------------------------------------------------------------------------------------------------------
    2. Update saveLockStatus() to always save status, even if not blocked:
    Change the function name (optional but clearer) and logic:

    private void saveLoginStatus(String username) {
        File lockFile = new File("lock_status.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(lockFile))) {
            writer.write(username + "," + blockTime + "," + BLOCK_DURATION + "," + failedAttempts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

------------------------------------------------------------------------------------------------------------------------
    3. Update failed attempts after each failed login:
    Inside the else if (!user.getPassword().equals(password)) block, add this line right after failedAttempts++:
    saveLoginStatus(username); // Save current attempt count immediately

    And replace this line:
    saveLockStatus(username, blockTime);

    With:
    saveLoginStatus(username);

-------------------------------------------------------------------------------------------------------------------------
    4. Reset failed attempts only after full block or successful login:
    After a successful login (else block at the bottom), update like this:

    failedAttempts = 0;
    blockTime = 0;
    BLOCK_DURATION = 60000;
    saveLoginStatus(username); // Reset in file as well

---------------------------------------------------------------------------------------------------------------------------
    5. Update isBlocked() to save reset status after unlock:
    Update isBlocked() like this:

    private boolean isBlocked() {
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
            long timeLeft = (blockTime + BLOCK_DURATION - System.currentTimeMillis()) / 1000;
            if (timeLeft > 0) {
                return true;
            } else {
                failedAttempts = 0;
                blockTime = 0;
                saveLoginStatus(usernameTxt.getText().trim()); // Reset in file
                return false;
            }
        }
        return false;
    }


*/