package org.wonder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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

    @Test
    public void testVerificationInOrder()
    {
        List singleMock = mock(List.class);
        singleMock.add("was added first");
        singleMock.add("was added second");

        InOrder inOrder = inOrder(singleMock);
        //the following verifications can not be exchanged. but if there's only the second, it's passed
        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");

        List firstMock = mock(List.class);
        List secondMock = mock(List.class);

        firstMock.add("was called first");
        secondMock.add("was called second");

        InOrder inOrder2 = inOrder(firstMock , secondMock);
        //the following verifications can not be exchanged. but if there's only the second, it's passed
        inOrder2.verify(firstMock).add("was called first");
        inOrder2.verify(secondMock).add("was called second");
    }

    @Test
    public void testNoInteractionOnMock()
    {
        List firstMock = mock(List.class);
        List secondMock = mock(List.class);

        firstMock.add("element");

        verify(firstMock).add("element");
        verifyZeroInteractions(secondMock);
    }

    @Test
    public void testNoMoreInteractionsOnMock()
    {
        List mockedList = mock(List.class);

        mockedList.add("first");
        mockedList.clear();

        verify(mockedList).add("first");
        verify(mockedList).clear();
        verifyNoMoreInteractions(mockedList);

        mockedList.add("second");
        verify(mockedList, times(2)).add(anyString());//because called by first and second
        verifyNoMoreInteractions(mockedList);// that means all interactions are verified
    }

    @Mock List shortHandCreationMockedList;  //must be declared as a field
    @Test
    public void testShortHandCreationForMocks()
    {
        shortHandCreationMockedList.add("element");
        verify(shortHandCreationMockedList).add(anyString());
    }

    @Test
    public void testStubbingConsecutiveCalls()
    {
        List mockedList = mock(List.class);
        when(mockedList.get(0))
                .thenReturn("0 first")
                .thenThrow(new RuntimeException("mocked exception"))
                .thenReturn("0 third");

        when(mockedList.get(1))
                .thenReturn("1 first", "1 second", "1 third");

        assertEquals("0 first", mockedList.get(0));

        try {
            mockedList.get(0);
        } catch (RuntimeException ex){
            assertEquals("mocked exception", ex.getMessage());
        }

        assertEquals("0 third", mockedList.get(0));
        verify(mockedList, times(3)).get(0);

        assertEquals("1 first", mockedList.get(1));
        assertEquals("1 second", mockedList.get(1));
        assertEquals("1 third", mockedList.get(1));

        verify(mockedList, times(6)).get(anyInt());
    }

    @Test
    public void testSpyOnRealObject()
    {
        LinkedList<String> list = new LinkedList<>();
        LinkedList<String> spy = spy(list);

        spy.add("one");
        spy.add("two");
        when(spy.size()).thenReturn(100);

        assertEquals("one", spy.get(0));
        assertEquals("two", spy.get(1));

        assertEquals(100, spy.size());

        // when(spy.get(2)).thenReturn("three"); //use doXXX function, why?
        doReturn("three").when(spy).get(2);
        assertEquals("three", spy.get(2));
    }

    @Test
    public void testCaptureArguments()
    {
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        List mockedList = mock(List.class);

        mockedList.add(new Person("Wonder", 30));

        verify(mockedList).add(argument.capture());
        assertEquals("Wonder", argument.getValue().getName());
        assertEquals(30, argument.getValue().getAge());
    }

    @Test
    public void testRealPartialMock()
    {
        final Person mockedPerson = mock(Person.class);
        when(mockedPerson.getName()).thenReturn("Wonder");
        when(mockedPerson.getAge()).thenReturn(30);
        when(mockedPerson.toString()).thenCallRealMethod();//when to use it???

        assertEquals("Wonder", mockedPerson.getName());
        assertEquals(30, mockedPerson.getAge());
        assertEquals("Person{name='null', age=0}", mockedPerson.toString());
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

    class Person{
        private final String name;
        private final int age;

        Person(String name , int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
