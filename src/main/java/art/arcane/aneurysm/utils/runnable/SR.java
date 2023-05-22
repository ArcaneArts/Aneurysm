/*------------------------------------------------------------------------------
 -   Adapt is a Skill/Integration plugin  for Minecraft Bukkit Servers
 -   Copyright (c) 2022 Arcane Arts (Volmit Software)
 -
 -   This program is free software: you can redistribute it and/or modify
 -   it under the terms of the GNU General Public License as published by
 -   the Free Software Foundation, either version 3 of the License, or
 -   (at your option) any later version.
 -
 -   This program is distributed in the hope that it will be useful,
 -   but WITHOUT ANY WARRANTY; without even the implied warranty of
 -   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 -   GNU General Public License for more details.
 -
 -   You should have received a copy of the GNU General Public License
 -   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 -----------------------------------------------------------------------------*/

package art.arcane.aneurysm.utils.runnable;

import org.bukkit.plugin.Plugin;

public abstract class SR implements Runnable, CancellableTask {
    private int id = 0;

    public SR(Plugin p) {
        this(0, p);
    }

    public SR(int interval, Plugin p) {
        id = J.sr(this, interval, p);
    }

    @Override
    public void cancel() {
        J.csr(id);
    }

    public int getId() {
        return id;
    }
}
