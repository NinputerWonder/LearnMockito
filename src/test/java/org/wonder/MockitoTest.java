package org.wonder;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MockitoTest
{
    
    @Test
    public void testVerifyByMock()
    {
        //Once created, a mock will remember all interactions. 
        //Then you can selectively verify whatever interactions you are interested in.
        List mockedList = mock(List.class);

        mockedList.add("one");
        mockedList.clear();

        verify(mockedList).add("one");
        verify(mockedList).clear();

        mockedList.add("two");
        mockedList.size();
        verify(mockedList).add("two");
        verify(mockedList).size();

        //By default, for all methods that return a value, a mock will return either null,
        //a primitive/primitive wrapper value, or an empty collection, as appropriate
        assertEquals(0, mockedList.size());
        assertEquals(null, mockedList.get(1));
    }

    @Test
    public void testMockedValueByStub()
    {
        LinkedList mockedList = mock(LinkedList.class);
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException("This is mocked exception"));

        assertEquals("first", mockedList.get(0));
        verify(mockedList).get(0);

        //always return the stubbed value
        assertEquals("first", mockedList.get(0));

        try {
            mockedList.get(1);
        }
        catch (RuntimeException ex){
            assertEquals("This is mocked exception", ex.getMessage());
        }
        finally {
            verify(mockedList).get(1);
        }

        when(mockedList.get(0)).thenReturn("second"); //override the previous stubbed value
        assertEquals("second", mockedList.get(0));

        // when(mockedList.get(1)).thenReturn("second"); //runtime exception: This is mocked exception

        //the none stubbed value is default
        assertEquals(null, mockedList.get(999));
    }
}
