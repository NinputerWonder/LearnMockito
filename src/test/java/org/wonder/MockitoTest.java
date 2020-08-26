package org.wonder;

import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
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

    @Test
    public void testArgumentsByStub()
    {
        LinkedList mockedList = mock(LinkedList.class);
        //stubbing using built-in anyInt() argument matcher
        when(mockedList.get(anyInt())).thenReturn("element");
        //stubbing using custom matcher (let's say isValid() returns your own matcher implementation):
        when(mockedList.contains(argThat(isValid()))).thenReturn(true);

        assertEquals("element", mockedList.get(999));
        verify(mockedList).get(999);
        verify(mockedList).get(anyInt());

        assertEquals("element", mockedList.get(anyInt()));

        assertTrue(mockedList.contains("wonder"));
        assertFalse(mockedList.contains("element"));

        mockedList.add(123, "new element");
        verify(mockedList).add(anyInt(), eq("new element"));
    }

    @Test
    public void testVerifyExactNumberOfInvocation()
    {
        List mockedList = mock(List.class);
        mockedList.add("once");

        mockedList.add("twice");
        mockedList.add("twice");

        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        verify(mockedList).add("once"); //equal to: verify(mockedList, times(1)).add("once");
        verify(mockedList, times(1)).add("once");
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");
        verify(mockedList, times(6)).add(anyString());

        verify(mockedList, never()).add("never happened");

        verify(mockedList, atMost(1)).add("once");
        verify(mockedList, atLeastOnce()).add("twice");
        verify(mockedList, atLeast(2)).add("three times");
        verify(mockedList, atMost(100)).add("three times");
    }

    @Test
    public void testExceptionWhenMethodRetunsVoid()
    {
        List mockedList = mock(List.class);
        // when(mockedList.clear()).thenThrow(new RuntimeException("test")); //'void' type not allowed here
        doThrow(new RuntimeException("mocked exception")).when(mockedList).clear();

        try {
            mockedList.clear();
        } catch (RuntimeException ex){
            assertEquals("mocked exception", ex.getMessage());
        } finally {
            verify(mockedList).clear();
        }
    }

    private ValidElementInList isValid() {
        return new ValidElementInList();
    }

    class ValidElementInList extends ArgumentMatcher<List> {
        @Override
        public boolean matches(Object object) {
            return object.equals("wonder");
        }
        public String toString() {
            return "Only contains wonder";
        }
    }
}
