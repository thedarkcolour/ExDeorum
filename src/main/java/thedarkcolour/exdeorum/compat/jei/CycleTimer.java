/*
 * Ex Deorum
 * Copyright (c) 2024 thedarkcolour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package thedarkcolour.exdeorum.compat.jei;

import net.minecraft.client.gui.screens.Screen;

import java.util.List;

// Copy of JEI CycleTimer that doesn't force TypedIngredient and isn't in a non-API package
public class CycleTimer {
	// the amount of time in ms to display one thing before cycling to the next one
	private static final int CYCLE_TIME = 1000;
	private long startTime;
	private long drawTime;
	private long pausedDuration = 0;

	public CycleTimer(int offset) {
		long time = System.currentTimeMillis();
		this.startTime = time - ((long) offset * CYCLE_TIME);
		this.drawTime = time;
	}

	// Assumes list is not empty
	public <T> T getCycledItem(List<T> list) {
		long index = ((this.drawTime - this.startTime) / CYCLE_TIME) % list.size();
		return list.get(Math.toIntExact(index));
	}

	public void onDraw() {
		if (!Screen.hasShiftDown()) {
			if (pausedDuration > 0) {
				this.startTime += this.pausedDuration;
				this.pausedDuration = 0;
			}
			this.drawTime = System.currentTimeMillis();
		} else {
			this.pausedDuration = System.currentTimeMillis() - this.drawTime;
		}
	}
}