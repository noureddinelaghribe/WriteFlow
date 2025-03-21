package com.noureddine.WriteFlow.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.noureddine.WriteFlow.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Set;

/**
 * A secure wrapper for SharedPreferences that encrypts both keys and values.
 * Uses the AndroidX Security library to provide encryption.
 */
public class EncryptedPrefsManager {
    private static final String TAG = "EncryptedPrefsManager";
    private static final String DEFAULT_PREFS_NAME = "encrypted_prefs";
    private static final String CURRENT_USER_KEY = "CURRENT_USER";
    private final SharedPreferences encryptedPrefs;
    private static EncryptedPrefsManager instance;

    /**
     * Private constructor that initializes encrypted shared preferences.
     * @param context Application context
     * @param prefsName Name for the preferences file
     */
    private EncryptedPrefsManager(Context context, String prefsName) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            encryptedPrefs = EncryptedSharedPreferences.create(
                    context,
                    prefsName,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            Log.e(TAG, "Failed to initialize EncryptedSharedPreferences", e);
            throw new RuntimeException("Failed to initialize EncryptedSharedPreferences", e);
        }
    }

    /**
     * Gets singleton instance with default preferences name.
     * @param context Application context
     * @return Instance of EncryptedPrefsManager
     */
    public static synchronized EncryptedPrefsManager getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new EncryptedPrefsManager(context.getApplicationContext(), DEFAULT_PREFS_NAME);
        }
        return instance;
    }

    /**
     * Gets singleton instance with custom preferences name.
     * @param context Application context
     * @param prefsName Name for the preferences file
     * @return Instance of EncryptedPrefsManager
     */
    public static synchronized EncryptedPrefsManager getInstance(@NonNull Context context, String prefsName) {
        if (instance == null) {
            instance = new EncryptedPrefsManager(context.getApplicationContext(), prefsName);
        }
        return instance;
    }


    public void saveUser(User user) {
        if (user == null) {
            Log.e(TAG, "Cannot save null user");
            return;
        }
        try {
            JSONObject userJson = new JSONObject();
            userJson.put("name", user.getName());
            userJson.put("email", user.getEmail());
            userJson.put("uid", user.getUid());
            userJson.put("membership", user.getMembership());
            userJson.put("endSubscription", user.getEndSubscription());
            userJson.put("wordPremium", user.getWordPremium());
            userJson.put("wordProcessing", user.getWordProcessing());

            // Save user data using the user's uid as the key.
            encryptedPrefs.edit().putString(user.getUid(), userJson.toString()).apply();

            // Also store the current user's uid under a constant key.
            encryptedPrefs.edit().putString(CURRENT_USER_KEY, user.getUid()).apply();

            Log.d(TAG, "User saved successfully: " + user.getUid());
        } catch (JSONException e) {
            Log.e(TAG, "Error serializing user data", e);
        }
    }

    public User getUser() {
        // Retrieve the uid of the current user from preferences.
        String currentUid = encryptedPrefs.getString(CURRENT_USER_KEY, null);
        if (currentUid == null) {
            Log.e(TAG, "No current user uid found");
            return null;
        }

        // Retrieve the user data using the current uid.
        String userString = encryptedPrefs.getString(currentUid, null);
        if (userString == null || userString.isEmpty()) {
            Log.e(TAG, "No user data found for uid: " + currentUid);
            return null;
        }
        try {
            JSONObject userJson = new JSONObject(userString);
            return new User(
                    userJson.getString("name"),
                    userJson.getString("email"),
                    userJson.getString("uid"),
                    userJson.getString("membership"),
                    userJson.getLong("endSubscription"),
                    userJson.getLong("wordPremium"),
                    userJson.getLong("wordProcessing")
            );
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse user data", e);
            return null;
        }
    }






    /**
     * Saves a string value to encrypted preferences.
     * @param key The key to store the value under
     * @param value The string value to store
     */
    public void saveString(String key, String value) {
        encryptedPrefs.edit().putString(key, value).apply();
    }

    /**
     * Saves an integer value to encrypted preferences.
     * @param key The key to store the value under
     * @param value The integer value to store
     */
    public void saveInt(String key, int value) {
        encryptedPrefs.edit().putInt(key, value).apply();
    }

    /**
     * Saves a boolean value to encrypted preferences.
     * @param key The key to store the value under
     * @param value The boolean value to store
     */
    public void saveBoolean(String key, boolean value) {
        encryptedPrefs.edit().putBoolean(key, value).apply();
    }

    /**
     * Saves a long value to encrypted preferences.
     * @param key The key to store the value under
     * @param value The long value to store
     */
    public void saveLong(String key, long value) {
        encryptedPrefs.edit().putLong(key, value).apply();
    }

    /**
     * Saves a float value to encrypted preferences.
     * @param key The key to store the value under
     * @param value The float value to store
     */
    public void saveFloat(String key, float value) {
        encryptedPrefs.edit().putFloat(key, value).apply();
    }

    /**
     * Saves a string set value to encrypted preferences.
     * @param key The key to store the value under
     * @param values The string set to store
     */
    public void saveStringSet(String key, Set<String> values) {
        encryptedPrefs.edit().putStringSet(key, values).apply();
    }

    /**
     * Saves multiple values at once to encrypted preferences.
     * @param keys Array of keys to store values under
     * @param values Array of values to store (must match keys array length)
     * @throws IllegalArgumentException if arrays have different lengths or unsupported value type
     */
    public void saveMultipleValues(String[] keys, Object[] values) {
        if (keys.length != values.length) {
            throw new IllegalArgumentException("Keys and values arrays must have the same length");
        }

        SharedPreferences.Editor editor = encryptedPrefs.edit();
        for (int i = 0; i < keys.length; i++) {
            if (values[i] instanceof String) {
                editor.putString(keys[i], (String) values[i]);
            } else if (values[i] instanceof Integer) {
                editor.putInt(keys[i], (Integer) values[i]);
            } else if (values[i] instanceof Boolean) {
                editor.putBoolean(keys[i], (Boolean) values[i]);
            } else if (values[i] instanceof Long) {
                editor.putLong(keys[i], (Long) values[i]);
            } else if (values[i] instanceof Float) {
                editor.putFloat(keys[i], (Float) values[i]);
            } else if (values[i] instanceof Set<?>) {
                try {
                    @SuppressWarnings("unchecked")
                    Set<String> stringSet = (Set<String>) values[i];
                    editor.putStringSet(keys[i], stringSet);
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException("Set must contain only String values", e);
                }
            } else {
                throw new IllegalArgumentException("Unsupported value type: " + values[i].getClass().getName());
            }
        }
        editor.apply();
    }

    /**
     * Saves values immediately using commit() instead of apply().
     * @param key The key to store the value under
     * @param value The value to store
     * @return True if successful, false otherwise
     */
    public boolean saveImmediately(String key, Object value) {
        SharedPreferences.Editor editor = encryptedPrefs.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Set<?>) {
            try {
                @SuppressWarnings("unchecked")
                Set<String> stringSet = (Set<String>) value;
                editor.putStringSet(key, stringSet);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Set must contain only String values", e);
            }
        } else {
            throw new IllegalArgumentException("Unsupported value type: " + value.getClass().getName());
        }
        return editor.commit();
    }

    /**
     * Retrieves a string value from encrypted preferences.
     * @param key The key to retrieve the value for
     * @param defaultValue Value to return if key doesn't exist
     * @return The stored string value or defaultValue if not found
     */
    public String getString(String key, String defaultValue) {
        return encryptedPrefs.getString(key, defaultValue);
    }

    /**
     * Retrieves an integer value from encrypted preferences.
     * @param key The key to retrieve the value for
     * @param defaultValue Value to return if key doesn't exist
     * @return The stored integer value or defaultValue if not found
     */
    public int getInt(String key, int defaultValue) {
        return encryptedPrefs.getInt(key, defaultValue);
    }

    /**
     * Retrieves a boolean value from encrypted preferences.
     * @param key The key to retrieve the value for
     * @param defaultValue Value to return if key doesn't exist
     * @return The stored boolean value or defaultValue if not found
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return encryptedPrefs.getBoolean(key, defaultValue);
    }

    /**
     * Retrieves a long value from encrypted preferences.
     * @param key The key to retrieve the value for
     * @param defaultValue Value to return if key doesn't exist
     * @return The stored long value or defaultValue if not found
     */
    public long getLong(String key, long defaultValue) {
        return encryptedPrefs.getLong(key, defaultValue);
    }

    /**
     * Retrieves a float value from encrypted preferences.
     * @param key The key to retrieve the value for
     * @param defaultValue Value to return if key doesn't exist
     * @return The stored float value or defaultValue if not found
     */
    public float getFloat(String key, float defaultValue) {
        return encryptedPrefs.getFloat(key, defaultValue);
    }

    /**
     * Retrieves a string set from encrypted preferences.
     * @param key The key to retrieve the value for
     * @param defaultValue Value to return if key doesn't exist
     * @return The stored string set or defaultValue if not found
     */
    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return encryptedPrefs.getStringSet(key, defaultValue);
    }

    /**
     * Retrieves all preferences as a Map.
     * @return Map of all key-value pairs in preferences
     */
    public Map<String, ?> getAll() {
        return encryptedPrefs.getAll();
    }

    /**
     * Removes a specific value from preferences.
     * @param key The key to remove
     */
    public void remove(String key) {
        encryptedPrefs.edit().remove(key).apply();
    }

    /**
     * Removes multiple keys at once.
     * @param keys Array of keys to remove
     */
    public void removeMultiple(String[] keys) {
        SharedPreferences.Editor editor = encryptedPrefs.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }

    /**
     * Clears all values from preferences.
     */
    public void clearAll() {
        encryptedPrefs.edit().clear().apply();
    }

    /**
     * Checks if a key exists in preferences.
     * @param key The key to check
     * @return True if the key exists, false otherwise
     */
    public boolean contains(String key) {
        return encryptedPrefs.contains(key);
    }

    /**
     * Registers a listener to be notified of preference changes.
     * @param listener The listener to register
     */
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        encryptedPrefs.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Unregisters a previously registered listener.
     * @param listener The listener to unregister
     */
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        encryptedPrefs.unregisterOnSharedPreferenceChangeListener(listener);
    }
}