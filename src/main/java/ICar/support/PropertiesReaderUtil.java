package ICar.support;

import org.aeonbits.owner.ConfigCache;

public class PropertiesReaderUtil {
	
	public static IPropertiesReader getInstance() {
		return ConfigCache.getOrCreate(IPropertiesReader.class);
	}

}
