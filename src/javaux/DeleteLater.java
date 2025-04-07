//
//
//
//
//
//            private void saveNewPasswordToFile(String encryptedEmail, String encryptedNewPassword) {
//            try {
//
//                File file = new File("user_data.txt");
//                File tempFile = new File("user_data_temp.txt");
//
//
//                BufferedReader reader = new BufferedReader(new FileReader(file));
//                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
//
//                String currentAnswer = securityAnswerTxt.getText().trim();
//                
//                String line;
//
//
//
//                while ((line = reader.readLine()) != null) {
//
//                    String[] parts = line.split(",");
//
//
//                    if (parts.length >= 3) {
//                        // Decrypt stored username and email to compare them in plaintext
//                        String decryptedStoredEmail = decryptData(parts[1]);
//                        String decryptedStoredAnswer = decryptData(parts[4]);
//
//                        if (decryptedStoredAnswer.equals(currentAnswer)) {
//                            // Replace with new encrypted values
//                            writer.write(parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + "," + encryptedNewPassword + "," + parts[5] + "," + parts[6]);
//                         
//                        } else {
//                            writer.write(line); 
//                        }
//                    } else {
//                        writer.write(line);
//                    }
//                    writer.newLine();
//                }
//
//                reader.close();
//                writer.close();
//
//                if (file.delete()) {
//                    tempFile.renameTo(file);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

///////////////////////////////////////EditPrifilePage.java/////////////////////////////////////////////////////

// 
//        private HashMap<String, String> loadUserData() {
//            HashMap<String, String> userData = new HashMap<>();
//            try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    String[] parts = line.split(",");
//
//                    if (parts.length >= 3) {
//                        String decryptedUsername = decryptData(parts[0]);
//                        String decryptedEmail = decryptData(parts[1]);
//
//                        userData.put(decryptedEmail + ":" + decryptedUsername, line);  
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return userData;
//        }
//
//
//
//

    // Save the updated username and email to the file
//        private void saveUpdatedDataToFile(String encryptedCurrentEmail, String encryptedNewEmail, 
//                                       String encryptedCurrentUsername, String encryptedNewUsername, 
//                                       String encryptedNewBirthday, String encryptedNewGender) {
//            try {
//                File file = new File("user_data.txt");
//                File tempFile = new File("user_data_temp.txt");
//                BufferedReader reader = new BufferedReader(new FileReader(file));
//                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
//
//                String currentUsername = currentUsernameLabel.getText().trim();  // Get current username from label
//                String currentEmail = currentEmailLabel.getText().trim();  // Get current email from label
//                
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    String[] parts = line.split(",");
//                    if (parts.length >= 3) {
//                        // Decrypt stored username and email to compare them in plaintext
//                        String decryptedStoredUsername = decryptData(parts[0]);
//                        String decryptedStoredEmail = decryptData(parts[1]);
//
//                        if (decryptedStoredUsername.equals(currentUsername) && decryptedStoredEmail.equals(currentEmail)) {
//                            // Replace with new encrypted values
//                            writer.write(encryptedNewUsername + "," + encryptedNewEmail + "," + parts[2] + "," + parts[3] + "," + parts[4] + "," + encryptedNewBirthday + "," + encryptedNewGender);
//                         
//                        } else {
//                            writer.write(line); 
//                        }
//                    } else {
//                        writer.write(line);
//                    }
//                    writer.newLine();
//                }
//
//                reader.close();
//                writer.close();
//
//                if (file.delete()) {
//                    tempFile.renameTo(file);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//

//public static String encryptData(String data) {
//        try {
//            // Generate a random IV
//            byte[] iv = new byte[16];
//            new SecureRandom().nextBytes(iv);
//            IvParameterSpec ivSpec = new IvParameterSpec(iv);
//            
//            // AES Key Spec
//            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
//            
//            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
//            
//            // Combine IV and encrypted data (IV must be known for decryption)
//            byte[] combined = new byte[iv.length + encryptedBytes.length];
//            System.arraycopy(iv, 0, combined, 0, iv.length);
//            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
//            
//            return Base64.getEncoder().encodeToString(combined);            
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    
//    // Decrypt method with IV
//    public static String decryptData(String encryptedData) {
//        try {
//            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
//
//            // Extract IV (first 16 bytes)
//            byte[] iv = new byte[16];
//            System.arraycopy(decodedBytes, 0, iv, 0, iv.length);
//            IvParameterSpec ivSpec = new IvParameterSpec(iv);
//
//            // Extract encrypted content 
//            byte[] encryptedBytes = new byte[decodedBytes.length - iv.length];
//            System.arraycopy(decodedBytes, iv.length, encryptedBytes, 0, encryptedBytes.length);
//
//            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
//
//            return new String (cipher.doFinal(encryptedBytes));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }