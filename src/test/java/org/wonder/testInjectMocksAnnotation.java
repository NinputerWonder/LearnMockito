package org.wonder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class testInjectMocksAnnotation {

    @Mock List<Integer> list;
    @Mock(name = "entity2") MyEntity1 mockedEntity2;
    @Mock(name = "entity1") MyEntity1 mockedEntity1;
    @InjectMocks InjectMocksByConstructor injectMocksByConstructor;
    @InjectMocks InjectMocksBySetter injectMocksBySetter;
    @InjectMocks InjectMocksByField injectMocksByField;

    @Test
    public void testInjectByCons(){
        when(list.get(0)).thenReturn(123);
        when(mockedEntity2.getNumber()).thenReturn(789);
        when(mockedEntity1.getNumber()).thenReturn(456);

        Integer integer = injectMocksByConstructor.getInts().get(0);
        assertEquals(123, integer.intValue());
        //问题：如果构造函数中有多个同类型的变量，可能会把某个inject进去，name参数无效

//        assertEquals(456, injectMocksByConstructor.getEntity1().getNumber().intValue());
//
//        assertEquals(789, injectMocksByConstructor.getEntity2().getNumber().intValue());
    }

    @Test
    public void testInjectBySetter(){
        when(list.get(0)).thenReturn(123);
        when(mockedEntity2.getNumber()).thenReturn(789);
        when(mockedEntity1.getNumber()).thenReturn(4567890);

        Integer integer = injectMocksBySetter.getInts().get(0);
        assertEquals(123, integer.intValue());
        assertEquals(4567890, injectMocksBySetter.getEntity1().getNumber().intValue());
        assertEquals(789, injectMocksBySetter.getEntity2().getNumber().intValue());
    }

    @Test
    public void testInjectByFiled(){
        when(list.get(0)).thenReturn(123);
        when(mockedEntity2.getNumber()).thenReturn(789);
        when(mockedEntity1.getNumber()).thenReturn(23333);

        Integer integer = injectMocksByField.getInts().get(0);
        assertEquals(123, integer.intValue());
        assertEquals(23333, injectMocksByField.getEntity1().getNumber().intValue());
        assertEquals(789, injectMocksByField.getEntity2().getNumber().intValue());
    }
}

class InjectMocksByConstructor {
    public void setInts(List<Integer> ints) {
        this.ints = ints;
    }

    public void setEntity1(MyEntity1 entity1) {
        this.entity1 = entity1;
    }

    public void setEntity2(MyEntity1 entity2) {
        this.entity2 = entity2;
    }

    public List<Integer> getInts() {
        return ints;
    }

    public MyEntity1 getEntity1() {
        return entity1;
    }

    public MyEntity1 getEntity2() {
        return entity2;
    }

    private List<Integer> ints;
    private MyEntity1 entity1;
    private MyEntity1 entity2;

    public InjectMocksByConstructor(List<Integer> ints, MyEntity1 entity1, MyEntity1 entity2) {
        this.ints = ints;
        this.entity1 = entity1;
        this.entity2 = entity2;
    }


}

class InjectMocksBySetter {
    public void setInts(List<Integer> ints) {
        this.ints = ints;
    }

    public void setEntity1(MyEntity1 entity1) {
        this.entity1 = entity1;
    }

    public void setEntity2(MyEntity1 entity2) {
        this.entity2 = entity2;
    }

    public List<Integer> getInts() {
        return ints;
    }

    public MyEntity1 getEntity1() {
        return entity1;
    }

    public MyEntity1 getEntity2() {
        return entity2;
    }

    private List<Integer> ints;
    private MyEntity1 entity1;
    private MyEntity1 entity2;

}

class InjectMocksByField {

    public List<Integer> getInts() {
        return ints;
    }

    public MyEntity1 getEntity1() {
        return entity1;
    }

    public MyEntity1 getEntity2() {
        return entity2;
    }

    private List<Integer> ints;
    private MyEntity1 entity1;
    private MyEntity1 entity2;

}

class MyApplicationService
{
    private MyDomainService1 myDomainService1;
    private MyDomainService2 myDomainService2;


}

class MyDomainService1
{

}

class MyDomainService2
{

}

class MyEntity1
{
    public Integer getNumber() {
        return number;
    }

    private Integer number;

    public MyEntity1(Integer number) {
        this.number = number;
    }
}

class MyEntity2
{

}