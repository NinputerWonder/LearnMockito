package org.wonder;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockitoTest
{
    @Test
    public void testVerify()
    {
        List mockedList = mock(List.class);

        mockedList.add("one");
        mockedList.clear();

        verify(mockedList).add("one");
        verify(mockedList).clear();

        mockedList.add("two");
        mockedList.size();
        verify(mockedList).add("two");
        verify(mockedList).size();

        assertEquals(0, mockedList.size());
    }
}
