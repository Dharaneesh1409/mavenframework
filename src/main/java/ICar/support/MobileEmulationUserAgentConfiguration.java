package ICar.support;

import java.util.HashMap;

/**
 * MobileEmulationUserAgentConfiguration Class to set the Mobile emulation
 * configuration on chrome browser
 * 
 * @see <a href=
 *      "http://www.webapps-online.com/online-tools/user-agent-strings/dv">User
 *      Agent String</a> for more info on the available user agent string.
 * 
 * @see <a href=
 *      "https://sites.google.com/a/chromium.org/chromedriver/mobile-emulation">
 *      Mobile emulation on chrome</a> for more info on chrome mobile emulation.
 * 
 */
@SuppressWarnings("serial")
public class MobileEmulationUserAgentConfiguration {

    public static final String APPLE_IPHONE_IOS8 = "iphone6plus_ios8_mobile";
    public static final String APPLE_IPAD_IOS9 = "ipad4_ios9_tablet";
    public static final String ANDROID_GALAXY_S5 = "android_galaxy_s5_mobile";
    public static final String ANDROID_GALAXY_S8 = "android_galaxy_s8_mobile";
    public static final String ANDROID_GALAXY_S9 = "android_galaxy_s9_mobile";
    public static final String ANDROID_GALAXY_S10 = "android_galaxy_s10_mobile";

    /**
     * To storing the android iphone devices configurations
     */
    private final HashMap<String, String> apple_iphone6plus_ios8 = new HashMap<String, String>() {
        {
            put("userAgent",
                    "Mozilla/6.0 (iPhone; CPU iPhone OS 8_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/8.0 Mobile/10A5376e Safari/8536.25");
            put("width", "414");
            put("height", "736");
            put("pixelRatio", "2");
        }
    };

    /**
     * To storing the apple ipad devices configurations
     */
    private final HashMap<String, String> apple_ipad4_ios9 = new HashMap<String, String>() {
        {
            put("userAgent",
                    "Mozilla/5.0 (iPad; CPU OS 9_3_3 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13G34 Safari/601.1");
            put("width", "768");
            put("height", "1024");
            put("pixelRatio", "2");
        }
    };

    /**
     * To storing the android devices configurations
     */
    private final HashMap<String, String> android_galaxy_s5 = new HashMap<String, String>() {
        {
            put("userAgent",
                    "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36");
            put("width", "360");
            put("height", "640");
            put("pixelRatio", "3");
        }
    };
    
    /**
     * To storing the android devices configurations
     */
    private final HashMap<String, String> android_galaxy_s8 = new HashMap<String, String>() {
        {
            put("userAgent",
                    "Mozilla/5.0 (Linux; Android 7.0; SAMSUNG SM-G950F Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/5.2 Chrome/51.0.2704.106 Mobile Safari/537.36");
            put("width", "360");
            put("height", "740");
            put("pixelRatio", "4");
        }
    };
    
    /**
     * To storing the android devices configurations
     */
    private final HashMap<String, String> android_galaxy_s9 = new HashMap<String, String>() {
        {
            put("userAgent",
                    "Mozilla/5.0 (Linux; Android 8.0.0; SM-G950F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.80 Mobile Safari/537.36");
            put("width", "360");
            put("height", "740");
            put("pixelRatio", "4");
        }
    };
    
    /**
     * To storing the android devices configurations
     */
    private final HashMap<String, String> android_galaxy_s10 = new HashMap<String, String>() {
        {
            put("userAgent",
                    "Mozilla/5.0 (Linux; Android 9; SM-G950F Build/PPR1.180610.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.157 Mobile Safari/537.36");
            put("width", "360");
            put("height", "740");
            put("pixelRatio", "4");
        }
    };

    /**
     * To storing the all the devices configurations
     *
     * @return userAgentData - have mobile emulation configuration data(user agent,
     *         width, height and pixelRatio)
     */
    public HashMap<String, HashMap<String, String>> setUserAgentConfigurationValue() {
        HashMap<String, HashMap<String, String>> userAgentData = new HashMap<String, HashMap<String, String>>();
        userAgentData.put(APPLE_IPHONE_IOS8, apple_iphone6plus_ios8);
        userAgentData.put(APPLE_IPAD_IOS9, apple_ipad4_ios9);
        userAgentData.put(ANDROID_GALAXY_S5, android_galaxy_s5);
        userAgentData.put(ANDROID_GALAXY_S8, android_galaxy_s8);
        userAgentData.put(ANDROID_GALAXY_S9, android_galaxy_s9);
        userAgentData.put(ANDROID_GALAXY_S10, android_galaxy_s10);
        return userAgentData;
    }

    /**
     * To get the user agent string from device name
     * 
     * @param deviceName
     *            - device name which going to perform a mobile emulation on chrome
     * @return dataToBeReturned- user Agent
     */
    public String getUserAgent(String deviceName) {
        String dataToBeReturned = null;
        HashMap<String, HashMap<String, String>> getUserAgent = setUserAgentConfigurationValue();
        dataToBeReturned = hasDeviceName(deviceName) ? (String) getUserAgent.get(deviceName).get("userAgent") : null;
        return dataToBeReturned;
    }

    /**
     * To get the device width string from device name
     * 
     * @param deviceName
     *            - device name which going to perform a mobile emulation on chrome
     * @return dataToBeReturned- device Width
     */
    public String getDeviceWidth(String deviceName) {
        String dataToBeReturned = null;
        HashMap<String, HashMap<String, String>> getDeviceWidth = setUserAgentConfigurationValue();
        dataToBeReturned = hasDeviceName(deviceName) ? (String) getDeviceWidth.get(deviceName).get("width") : null;
        return dataToBeReturned;
    }

    /**
     * To get the device height string from device name
     * 
     * @param deviceName
     *            - device name which going to perform a mobile emulation on chrome
     * @return dataToBeReturned- device height
     */
    public String getDeviceHeight(String deviceName) {
        String dataToBeReturned = null;
        HashMap<String, HashMap<String, String>> getDeviceHeight = setUserAgentConfigurationValue();
        dataToBeReturned = hasDeviceName(deviceName) ? (String) getDeviceHeight.get(deviceName).get("height") : null;
        return dataToBeReturned;
    }

    /**
     * To get the device pixel ratio string from device name
     * 
     * @param deviceName
     *            - device name which going to perform a mobile emulation on chrome
     * @return dataToBeReturned - device pixel ratio
     */
    public String getDevicePixelRatio(String deviceName) {
        String dataToBeReturned = null;
        HashMap<String, HashMap<String, String>> getDevicePixelRatio = setUserAgentConfigurationValue();
        dataToBeReturned = hasDeviceName(deviceName) ? (String) getDevicePixelRatio.get(deviceName).get("pixelRatio")
                : null;
        return dataToBeReturned;
    }

    /**
     * To check the device name present in the set up key hash map
     * 
     * @param deviceName
     *            - device name which going to perform a mobile emulation on chrome
     * @return boolean value - if device name in the set up key will return true,
     *         otherwise false
     */
    private boolean hasDeviceName(String deviceName) {
        HashMap<String, HashMap<String, String>> hasDeviceName = setUserAgentConfigurationValue();
        return hasDeviceName.containsKey(deviceName);
    }

    /**
     * To get the device name from mobile emulation attributes
     * 
     * @param userAgent
     *            - mapped user agent string with device name
     * @param pixelRatio
     *            - mapped pixel ratio with device name
     * @param width
     *            - mapped width with device name
     * @param height
     *            - mapped height with device name
     * @return dataToBeReturned - device name mapped with user agent, pixel ratio,
     *         width and height
     */
    public String getDeviceNameFromMobileEmulation(String userAgent, String pixelRatio, String width, String height) {
        String dataToBeReturned = null;
        boolean found = false;
        HashMap<String, HashMap<String, String>> getDeviceData = setUserAgentConfigurationValue();
        for (Object usKey : getDeviceData.keySet()) {
            if (getDeviceData.get(usKey).get("userAgent").equals(userAgent)
                    && getDeviceData.get(usKey).get("pixelRatio").equals(pixelRatio)
                    && getDeviceData.get(usKey).get("width").equals(width)
                    && getDeviceData.get(usKey).get("height").equals(height)) {
                dataToBeReturned = (String) usKey;
                found = true;
            }
        }
        if (!found) {
            dataToBeReturned = null;
        }
        return dataToBeReturned;
    }

} // MobileEmulationUserAgentConfiguration
