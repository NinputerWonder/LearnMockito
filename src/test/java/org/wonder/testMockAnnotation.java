package org.wonder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class testMockAnnotation {
    @Mock List<Integer> intList;

    @Test
    public void testIntList1() {
        when(intList.get(0)).thenReturn(1);
        assertEquals(new Integer(1), intList.get(0));
    }

    @Test
    public void testIntList2() {
        when(intList.get(0)).thenReturn(2);
        assertEquals(new Integer(2), intList.get(0));
    }

    @Test
    public void testIntList3() {
        assertEquals(null, intList.get(0));
    }
}
