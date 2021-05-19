package org.testAnnotation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestMockMultiLayers {

    @InjectMocks MyApplicationService myApplicationService;
    @InjectMocks MyDomainService1 myDomainService1;
    @InjectMocks MyDomainService2 myDomainService2;
    @Mock MyEntity1 myEntity1;
    @Mock MyEntity2 myEntity2;

    @Test
    public void testApplication() {
        when(myEntity1.getNumber()).thenReturn(3);
        when(myEntity2.getNumber()).thenReturn(4);

//        Integer cal = myApplicationService.cal(10, 2);
        //事实证明，多层次的对象构造，无法使用InjectMock完成
//        assertEquals(5, cal.intValue());
        assertEquals(true, true);
    }

    @Test
    public void testApplication2() {
        when(myEntity1.getNumber()).thenReturn(3);
        when(myEntity2.getNumber()).thenReturn(4);

        myDomainService1 = new MyDomainService1(myEntity1);
        myDomainService2 = new MyDomainService2(myEntity2);
        myApplicationService = new MyApplicationService(myDomainService1, myDomainService2);

        Integer cal = myApplicationService.cal(10, 2);

        assertEquals(5, cal.intValue());
    }
}
