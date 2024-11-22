package ICar.support;


import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.testng.Assert;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;


public class DataFaker {
	
	/**
	 * To generate and put unique dummy emailIDs
	 * @return String
	 * @throws Exception
	 */
	public static String FakeEmailID() throws Exception {
		String email = "";
		try {
			// CASE 1 - Create random mail ID
			FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());
			email = fakeValuesService.bothify("????##@gmail.com");
			Matcher emailMatcher = Pattern.compile("\\w{4}\\d{2}@gmail.com").matcher(email);
			if (!emailMatcher.find()) {
				Assert.fail("Generated email is not matching the pattern");
			}

		} catch (Exception e) {
			Log.exception(e);
		} // End catch
		return email;
	}

	/**
	 * To generate and put random alphanumeric string.
	 * @return String
	 * @throws Exception
	 */
	public static String FakeTextInputAlphanumeric() throws Exception {
		String alphaNumericString = "";
		try {

			FakeValuesService fakeValuesService1 = new FakeValuesService(new Locale("en-GB"), new RandomService());
			alphaNumericString = fakeValuesService1.regexify("[a-z1-9]{10}");
			Matcher alphaNumericMatcher = Pattern.compile("[a-z1-9]{10}").matcher(alphaNumericString);
			if (!alphaNumericMatcher.find()) {
				Assert.fail("Generated string is not matching the pattern");
			}

		} catch (Exception e) {
			Log.exception(e);
		} // End catch
		return alphaNumericString;

	}

	/**
	 * To generate and put random address 
	 * @return ArrayList<String>
	 * @throws Exception
	 */
	public static ArrayList<String> FakeAddressInput() throws Exception {
		ArrayList<String> address = new ArrayList<String>();
		try {

			// Random address
			Faker faker = new Faker();

			String streetName = faker.address().streetName();
			String number = faker.address().buildingNumber();
			String city = faker.address().city();
			String country = faker.address().country();
			//
			address.add(streetName);
			address.add(number);
			address.add(city);
			address.add(country);

		} catch (Exception e) {
			 Log.exception(e);
		} // End catch

		return address;

	}

	/**
	 * To generate and put region code / Locale 
	 * @param region :
	 * 					Enter region name among (uk/us)
	 * @return String
	 * @throws Exception
	 */
	public static String FakeRegionCodeLocale(String region) throws Exception {
		// Faker - local
		String regionCode = "";
		try {

			if (region.equalsIgnoreCase("UK")) {
				Faker ukFaker = new Faker(new Locale("en-GB"));
				regionCode = ukFaker.address().zipCode();
				
				Pattern ukPattern = Pattern.compile("([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|"
						+ "(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y]"
						+ "[0-9]?[A-Za-z]))))\\s?[0-9][A-Za-z]{2})");
				Matcher ukMatcher = ukPattern.matcher(ukFaker.address().zipCode());
				if (!ukMatcher.find()) {
					Assert.fail("Generated email is not matching the pattern");
				}
			}

			else {
				Faker usFaker = new Faker(new Locale("en-US"));
				regionCode = usFaker.address().zipCode();
				
				Matcher usMatcher = Pattern.compile("^\\d{5}(?:[-\\s]\\d{4})?$").matcher(usFaker.address().zipCode());
				if (!usMatcher.find()) {
					Assert.fail("Generated email is not matching the pattern");
				}
			}

		} catch (Exception e) {
			 Log.exception(e);
		} // End catch

		return regionCode;

	}

	/**
	 * To generate and put random phone number
	 * @return String
	 * @throws Exception
	 */
	public static String FakePhoneNumber() throws Exception {
		String phoneNumber = new String();
		try {
			Faker faker = new Faker(new Locale("en-IND"));
			String fakerphoneNumber = faker.phoneNumber().cellPhone();
			phoneNumber = fakerphoneNumber.replace("-", "");
			

			
		} catch (Exception e) {
			 Log.exception(e);
		} // End catch

		return phoneNumber;

	}


}
