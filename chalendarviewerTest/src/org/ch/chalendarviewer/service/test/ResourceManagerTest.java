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

package org.ch.chalendarviewer.service.test;

import android.test.AndroidTestCase;
import android.util.Log;

import org.ch.chalendarviewer.objects.CalendarResource;
import org.ch.chalendarviewer.objects.Event;
import org.ch.chalendarviewer.service.ResourceManager;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * ResourceManager testing class
 * @author vitor
 */
public class ResourceManagerTest extends AndroidTestCase {

    /** instance to test */
    private ResourceManager instance;
    
    /** Log tag */
    static private final String TAG = ResourceManagerTest.class.toString();
    
    @Override
    protected void setUp() throws Exception {
        instance = ResourceManager.getInstance(getContext());
    }
    
    /**
     * Test getting an instance
     */
    public void testGetInstance() {
        assertTrue(instance != null);
    }
    
    /**
     * Test methods used by UI
     */
    public void testGetActiveResources(){
        for(CalendarResource resource : instance.getActiveResources()){
            Log.d(TAG, "Resource testing : " + resource.getId() + "/" + resource.getTitle());
        }
    }
    
    /**
     * Test methods used by UI
     */
    public void testGetEventsFromResources(){
        Calendar begin = Calendar.getInstance();
        Calendar end = new GregorianCalendar();
        end.add(Calendar.DAY_OF_MONTH, 200);
        
        List<?extends Event> events = instance.getEvents("1", begin, end);
        for (Event event : events) {
            Log.d(TAG, "Event: " + event.getId() + " / " + event.getTitle()); 
        }
        Log.d(TAG, "No more events");
    }
    
    
    /**
     * Test methods used by UI
     */
    public void testPublishEvent(){
        Calendar begin = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        begin.add(Calendar.DAY_OF_YEAR,1);
        end.add(Calendar.DAY_OF_YEAR,1);
        end.add(Calendar.HOUR, 1);
        
        Event ev = new Event();
        ev.setBegin(begin);
        ev.setEnd(end);
        ev.setTitle("SIII");
        ev.setDetails("Evento in situ");
        
        instance.createEvent("1", ev);
        
        Log.d(TAG, "End of publish event");
    }
    
}
