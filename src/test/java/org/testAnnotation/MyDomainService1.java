package org.testAnnotation;

public class MyDomainService1 {
    private MyEntity1 myEntity1;

    public MyDomainService1(MyEntity1 myEntity1) {
        this.myEntity1 = myEntity1;
    }

    public Integer increase(int count) {
        return myEntity1.getNumber() + count;
    }
}
