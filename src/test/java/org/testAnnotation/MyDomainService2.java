package org.testAnnotation;

public class MyDomainService2 {

    private MyEntity2 myEntity2;

    public MyDomainService2(MyEntity2 myEntity2) {
        this.myEntity2 = myEntity2;
    }

    public Integer multi(int times) {
        return myEntity2.getNumber()*times;
    }
}
