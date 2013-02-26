package com.jimmyblaze.roverremote;

/* This source does not belong to me, it is under the new
 * BSD license located at http://opensource.org/licenses/BSD-3-Clause
 * 
 * The Google Code page for this open-source widget is at 
 * http://code.google.com/p/mobile-anarchy-widgets/
 */

public interface JoystickMovedListener {
	public void OnMoved(int pan, int tilt);
	public void OnReleased();
	public void OnReturnedToCenter();
}
