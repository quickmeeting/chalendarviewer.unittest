/**
    This file is part of ChalendarViewer.

    ChalendarViewer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ChalendarViewer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ChalendarViewer.  If not, see <http://www.gnu.org/licenses/>.    
*/

/**
 *
 */
package org.ch.chalendarviewer.service.test;

import android.test.AndroidTestCase;

import org.ch.chalendarviewer.service.UserManager;

import junit.framework.TestCase;


/**
 * @author Tom�s
 *
 */
public class UserManagerTest extends AndroidTestCase {
    
    UserManager instance;
    
    @Override
    protected void setUp() throws Exception {
        instance = UserManager.getInstance(getContext());
    }
    
    public void testGetInstance() {
        assertTrue(instance != null);
    }

}
