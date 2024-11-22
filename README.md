# README #

This README would normally document whatever properties need to be setup in config.properties & TestNGSuite.xml to get your application up and running.

### How to execute test cases? ###

*** Local To Local ***

	* Desktop *
		** config.properties **
			runSauceFromLocal: false
			runDesktop: false
			runMobile: false
			runTablet: false
			runUserAgentDeviceTest: false
			realDeviceTest: false
	** TestNGSuite.xml **
			deviceHost : <ip address of hub>
			devicePort : <port mentioned in hub>
			browserName : <browserName-platform, ...>  # Ex: chrome-windows,safari-mac
	
	* Mobile *   ** User Agent **
		** config.properties **
			runSauceFromLocal: false
			runDesktop: false
			runMobile: false
			runTablet: false
			runUserAgentDeviceTest: true
			deviceName: <User-Agent device configuration name> # Ex: iphone6plus_safari_mobile
			realDeviceTest: false
		** TestNGSuite.xml **
			deviceHost : <ip address of hub>
			devicePort : <port mentioned in hub>
			browserName : <browserName-platform, ...>  # Ex: chrome-windows,safari-mac
	
	* Tablet *   ** User Agent **
		** config.properties **
			runSauceFromLocal: false
			runDesktop: false
			runMobile: false
			runTablet: false
			runUserAgentDeviceTest: true
			deviceName: <User-Agent device configuration name> # Ex: ipad4_safari_tablet
			realDeviceTest: false
		** TestNGSuite.xml **
			deviceHost : <ip address of hub>
			devicePort : <port mentioned in hub>
			browserName : <browserName-platform, ...>  # Ex: chrome-windows,safari-mac
			
*** Local To Sauce ***

	* Desktop *
		** config.properties **
			runSauceFromLocal: true
			runDesktop: true
			browserName: <Browser Name>				# Ex: chrome, firefox, ie, edge, safari
			browserVersion: <Browser Version>		# Ex: 62, 48, 11, 10, 10
			platform: <Platform name with version>	# Ex: Windows7, Windows10, macOS 10.12
			runMobile: false
			runTablet: false
			runUserAgentDeviceTest: false
			realDeviceTest: false
	
	* Mobile *   ** Simulator **
		** config.properties **
			runSauceFromLocal: true
			runDesktop: false
			runMobile: true
			mobileBrowserName: <Browser Name>			# Ex: Chrome, Safari
			mobilePlatformName: <Platform Name>			# Ex: Android / IOS
			mobileDeviceName: <Device Name>				# Ex: iPhone 6 Simulator
			mobilePlatformVersion: <Device OS Version>	# Ex: 10.3.3
			appiumVersion: <Appium Version>				# Ex: 1.6.3
			deviceOrientation: <Device Orientation>		# Ex: landscape / portrait
			runTablet: false
			runUserAgentDeviceTest: false
			realDeviceTest: false
			
	* Tablet *   ** Simulator **
		** config.properties **
			runSauceFromLocal: false
			runDesktop: false
			runMobile: false
			runTablet: true
			tabletBrowserName: <Browser Name>			# Ex: Chrome, Safari
			tabletPlatformName: <Platform Name>			# Ex: Android / IOS
			tabletDeviceName: <Device Name>				# Ex: iPad Air 2 Simulator
			tabletPlatformVersion: <Device OS Version>	# Ex: 10.3.3
			appiumVersion: <Appium Version>				# Ex: 1.6.3
			deviceOrientation: <Device Orientation>		# Ex: landscape / portrait
			runUserAgentDeviceTest: false
			realDeviceTest: false
			
*** Local To Appium Devices ***

	* Real-Devices through Remote WebDriver*
		runSauceFromLocal: false
		runDesktop: false
		runMobile: false
		runTablet: false
		runUserAgentDeviceTest: false
		realDeviceTest: true
		devplatform: <Device Platform> 			# Ex: Android / iOS
		version: <Device OS Version>			# Ex: 7.1.1 / 10.3.3
		appType: mobileweb
		browser: chrome
		udidList: <Device udid value>			# Ex: HKL3J8V3
		host: <Hub Host IP address>				# Ex: 172.24.212.64
		port: <Hub Host port id>				# Ex: 4445
		deviceType: <Device type>				# Ex: mobile / tablet
		mobileDeviceName: <Device Model Name>	# Ex: Lenovo_K8_Note / iPhone_my_phone