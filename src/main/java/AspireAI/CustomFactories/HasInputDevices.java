package AspireAI.CustomFactories;

import AspireAI.CustomFactories.Keyboard;
import AspireAI.CustomFactories.Mouse;

public interface HasInputDevices {
	Keyboard getKeyboard();

	Mouse getMouse();
}
